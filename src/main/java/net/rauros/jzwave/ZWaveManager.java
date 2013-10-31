/**
 * Copyright 2012-2013 Chris Cavey <chris-jzwave@rauros.net>
 *
 * SOFTWARE NOTICE AND LICENSE
 *
 * This file is part of jzwave.
 *
 * jzwave is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jzwave is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jzwave.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.rauros.jzwave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

import net.rauros.jzwave.config.Config;
import net.rauros.jzwave.config.Manufacturers;
import net.rauros.jzwave.core.CommandClass;
import net.rauros.jzwave.core.CommandFunction;
import net.rauros.jzwave.core.CommandFunction.SENSOR_MULTILEVEL;
import net.rauros.jzwave.core.ControllerCapabilities;
import net.rauros.jzwave.core.Function;
import net.rauros.jzwave.core.LibraryType;
import net.rauros.jzwave.core.Message;
import net.rauros.jzwave.core.Message.MsgType;
import net.rauros.jzwave.core.Node;
import net.rauros.jzwave.core.SensorType;
import net.rauros.jzwave.core.SensorValue;
import net.rauros.jzwave.core.ZWaveConstants;
import net.rauros.jzwave.core.messages.MessageFactory;
import net.rauros.jzwave.core.messages.request.ZWSendData;
import net.rauros.jzwave.core.messages.response.ZWMemoryGetID;
import net.rauros.jzwave.transport.Transport;
import net.rauros.jzwave.transport.TransportFactory;
import net.rauros.jzwave.utils.BitEnumSets;
import net.rauros.jzwave.utils.BitSets;
import net.rauros.jzwave.utils.Enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;

public class ZWaveManager
{
	private static final Logger log = LoggerFactory.getLogger(ZWaveManager.class);

	private BlockingDeque<Job> jobQueue = new LinkedBlockingDeque<>();
	private Manufacturers manufacturers;
	private Thread ioThread;
	private ReadLoop ioRunnable;

	private List<Integer> reportedNodeIds = new ArrayList<>();
	private Map<Integer, Node> nodes = new LinkedHashMap<>();
	private Transport transport;

	private int controllerId = -1;

	private EventBus eventBus = new EventBus(this.getClass().getName());

	/**
	 * Create a new ZWaveManager using device identified by given String(URI). See {@link net.rauros.jzwave.transport.TransportFactory}
	 * 
	 * @param transportURIString
	 *            device URI string
	 */
	public ZWaveManager(String transportURIString)
	{
		this(TransportFactory.create(transportURIString));
	}

	/**
	 * Create a new ZWaveManager using device identified by given String(URI). See {@link net.rauros.jzwave.transport.TransportFactory}
	 * 
	 * @param transportURI
	 *            device URI
	 */
	public ZWaveManager(URI transportURI)
	{
		this(TransportFactory.create(transportURI));
	}

	/**
	 * Create a new ZWaveManager using device transport See {@link net.rauros.jzwave.transport.TransportFactory}
	 * 
	 * @param transport
	 *            transport instance
	 */
	public ZWaveManager(Transport transport)
	{
		this.transport = transport;
		this.manufacturers = Config.load();
	}

	public synchronized void open()
	{
		stop();

		jobQueue.clear();
		reportedNodeIds.clear();
		nodes.clear();

		ioThread = new Thread(ioRunnable = new ReadLoop(), "ZWaveIO");
		ioThread.start();

		bootstrap();
	}

	/**
	 * Execute several important commands to get basic capabilities of the ZWave network
	 */
	protected void bootstrap()
	{
		prepareJobAndWait(new Message(MsgType.REQUEST, Function.ZW_MEMORY_GET_ID));
		prepareJobAndWait(new Message(MsgType.REQUEST, Function.SERIAL_API_GET_CAPABILITIES));
		prepareJobAndWait(new Message(MsgType.REQUEST, Function.ZW_GET_VERSION));
		prepareJobAndWait(new Message(MsgType.REQUEST, Function.ZW_GET_CONTROLLER_CAPABILITIES));
		prepareJobAndWait(new Message(MsgType.REQUEST, Function.ZW_GET_SUC_NODE_ID));
		prepareJobAndWait(new Message(MsgType.REQUEST, Function.SERIAL_API_GET_INIT_DATA));

		// Used the reported ids to query for actual nodes with details
		for (Integer reportedNodeId : reportedNodeIds)
		{
			prepareJobAndWait(new Message(MsgType.REQUEST, Function.ZW_GET_NODE_PROTOCOL_INFO, reportedNodeId));
			if (reportedNodeId != controllerId)
			{
				prepareJobAndWait(new Message(MsgType.REQUEST, Function.ZW_REQUEST_NODE_INFO, reportedNodeId));
			}
		}

		for (Integer reportedNodeId : reportedNodeIds)
		{
			if (reportedNodeId != controllerId)
			{
				prepareJob(new ZWSendData(reportedNodeId, CommandClass.MANUFACTURER_SPECIFIC, CommandFunction.MANUFACTURER_SPECIFIC.GET), true, true);
			}
		}

		// prepareJob(new ZWSendData(5, CommandClass.MANUFACTURER_SPECIFIC,
		// CommandFunction.MANUFACTURER_SPECIFIC.GET), true, true);
		// prepareJob(new ZWSendData(2, CommandClass.VERSION,
		// CommandFunction.VERSION.COMMAND_CLASS_GET), true, true);

		// prepareJob(new ZWSendData(5, CommandClass.VERSION,
		// CommandFunction.VERSION.COMMAND_CLASS_GET), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.WAKE_UP,
		// CommandFunction.WAKE_UP.INTERVAL_GET), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.ASSOCIATION,
		// CommandFunction.ASSOCIATION.SET, 0, 1), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.ASSOCIATION,
		// CommandFunction.ASSOCIATION.GET), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.ASSOCIATION,
		// CommandFunction.ASSOCIATION.GROUPINGS_GET), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.BATTERY,
		// CommandFunction.BATTERY.GET), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.SENSOR_BINARY,
		// CommandFunction.SENSOR_BINARY.GET), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.SENSOR_MULTILEVEL,
		// CommandFunction.SENSOR_MULTILEVEL.GET), true, true);

		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 3), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 4), true, true);

		// this item is a bitmask for what is reported to association group 1
		// (1110 0001) - LSB = battery
		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.SET, 101, 4, explode(225)), true,
		// true);

		// report interval in seconds for sensors to association group 1
		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.SET, 111, 4, explode(15)), true, true);

		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 111), true, true);

		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 101), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 102), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 103), true, true);

		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 111), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 112), true, true);
		// prepareJob(new ZWSendData(5, CommandClass.CONFIGURATION,
		// CommandFunction.CONFIGURATION.GET, 113), true, true);

		// for(Integer reportedNodeId : reportedNodeIds)
		// {
		// if(reportedNodeId == 1)
		// continue;
		// prepareJob(new ZWSendData(reportedNodeId,
		// CommandClass.MANUFACTURER_SPECIFIC,
		// CommandFunction.MANUFACTURER_SPECIFIC.GET), true, true);
		// }
	}

	protected byte[] explode(int value)
	{
		return new byte[] { (byte) (value >> 24 & 0xFF), (byte) (value >> 16 & 0xFF), (byte) (value >> 8 & 0xFF), (byte) (value >> 0 & 0xFF) };
	}

	public synchronized void stop()
	{
		if (ioRunnable != null && ioThread != null)
		{
			ioRunnable.setRunning(false);
			try
			{
				ioThread.join();

				ioThread = null;
				ioRunnable = null;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	class ReadLoop implements Runnable
	{
		boolean running = true;

		protected void resendNAck()
		{
			List<Job> jobsAwaitingAck = getJobsAwaitingAck();
			if (jobsAwaitingAck.size() > 1)
			{
				log.error("Multiple jobs ({}) awaiting ACK", jobsAwaitingAck.size());
			}
			else if (jobsAwaitingAck.size() == 1)
			{
				jobsAwaitingAck.get(0).sent = false;
			}
			else
			{
				log.error("No jobs currently awaiting ACK");
			}
		}

		public void setRunning(boolean running)
		{
			this.running = running;
		}

		@Override
		public void run()
		{
			InputStream is = null;
			OutputStream os = null;

			try
			{
				is = transport.getInputStream();
				os = transport.getOutputStream();

				while (running)
				{
					if (is.available() > 0)
					{
						int first = is.read();
						switch (first)
						{
							case ZWaveConstants.ACK:

								log.debug("R: {}", Message.ACK);

								Job ackJob = null;

								for (Job job : jobQueue)
								{
									if (job.sent && job.awaitingAck)
									{
										ackJob = job;
										break;
									}
								}

								if (ackJob != null)
								{
									if (!ackJob.awaitingResponse && !ackJob.awaitingCallback)
									{
										removeJob(ackJob);
									}
									else
									{
										ackJob.awaitingAck = false;
									}
								}

								break;
							case ZWaveConstants.SOF:

								if (anyJobsAwaitingAck())
								{
									resendNAck();
								}

								int length = is.read();

								byte buffer[] = new byte[length + 2];
								buffer[0] = ZWaveConstants.SOF;
								buffer[1] = (byte) length;

								// Read "length" bytes from the input, handling
								// the case when it
								// all doesn't come in a nice tidy chunk
								int totalRead = 0;
								do
								{
									totalRead += is.read(buffer, 2 + totalRead, length - totalRead);
								}
								while (totalRead < length);

								// // Verify checksum
								byte calculatedChecksum = Message.calculateChecksum(buffer, 1, buffer.length - 2);
								byte receivedChecksum = buffer[buffer.length - 1];

								if (calculatedChecksum != receivedChecksum)
								{
									// Checksum mismatch
									System.err.printf("Checksum mismatch! calc(%02x) != recv(%02x)\n", calculatedChecksum, receivedChecksum);
									// Immediately ask for resend via NAK
									os.write(ZWaveConstants.NAK);
									System.err.println("W: " + Message.NAK);
									break;
								}

								// Package up the raw bytes into the same
								// Message object used to
								// send messages
								Message message = new Message(buffer);

								message = MessageFactory.deriveResponseMessage(message);

								log.debug("R: {}", message);
								log.debug("   {}", message.toReadableString());

								os.write(ZWaveConstants.ACK);
								log.debug("W: {}", Message.ACK);

								decodeMessage(message);

								break;
							case ZWaveConstants.CAN:
							case ZWaveConstants.NAK:
								resendNAck();
								break;
						}
					}
					else
					{
						Job nextJob = null;

						for (Job job : jobQueue)
						{
							if (job.sent)
							{
								if (job.awaitingAck || job.awaitingResponse || job.awaitingCallback)
								{
									break;
								}
							}
							else
							{
								nextJob = job;
								break;
							}
						}

						if (nextJob != null)
						{
							nextJob.sent = true;
							nextJob.sendCount++;
							os.write(nextJob.message.asByteArray(), 0, nextJob.message.getLength() + 1);

							log.debug("W: {}", nextJob.message);
							log.debug("   {}", nextJob.message.toReadableString());
						}
					}

					try
					{
						Thread.sleep(75);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

				transport.close();
			}
			catch (IOException e1)
			{
				running = false;
				e1.printStackTrace();
			}
		}
	}

	protected void removeJob(Job job)
	{
		jobQueue.remove(job);
		job.future.set(Boolean.TRUE);
	}

	protected void prepareJobAndWait(Message message)
	{
		ListenableFuture<Boolean> future = prepareJob(message, true, false);
		try
		{
			future.get();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
	}

	protected ListenableFuture<Boolean> prepareJob(Message message)
	{
		return prepareJob(message, true, false);
	}

	protected ListenableFuture<Boolean> prepareJob(Message message, boolean response)
	{
		return prepareJob(message, response, false);
	}

	/**
	 * Prepare a job with the given parameters and enqueue the job(message) for transmission
	 * 
	 * @param message
	 * @param response
	 * @param callback
	 */
	protected ListenableFuture<Boolean> prepareJob(Message message, boolean response, boolean callback)
	{
		Job job = new Job(message, response, callback);
		jobQueue.add(job);
		return job.future;
	}

	protected List<Job> getJobsAwaitingCallback(final Function callbackType, final int callbackId)
	{
		return Lists.newArrayList(Iterables.filter(jobQueue, new Predicate<Job>()
		{
			@Override
			public boolean apply(Job job)
			{
				return job.sent && !job.awaitingAck && !job.awaitingResponse && job.callbackId == callbackId && job.callbackType == callbackType;
			}
		}));
	}

	protected List<Job> getJobsAwaitingAck()
	{
		return Lists.newArrayList(Iterables.filter(jobQueue, new Predicate<Job>()
		{
			@Override
			public boolean apply(Job job)
			{
				return job.sent && job.awaitingAck;
			}
		}));
	}

	protected boolean anyJobsAwaitingAck()
	{
		for (Job job : jobQueue)
			if (job.sent && job.awaitingAck)
				return true;
		return false;
	}

	protected void decodeMessage(Message inputData)
	{
		switch (inputData.getType())
		{
			case REQUEST:
				handleRequest(inputData);
				break;
			case RESPONSE:
				handleResponse(inputData);
				break;
		}
	}

	protected void handleRequest(Message request)
	{
		List<Job> jobsAwaitingCallback = getJobsAwaitingCallback(request.getFunction(), request.getCallbackId());

		Job callbackJob = null;

		if (jobsAwaitingCallback.size() > 1)
		{
			log.error("Multiple jobs ({}) waiting for the same callback!", jobsAwaitingCallback.size());
		}
		else if (jobsAwaitingCallback.size() == 1)
		{
			callbackJob = jobsAwaitingCallback.get(0);
		}

		boolean doRemoveJob = false;

		switch (request.getFunction())
		{
			case ZW_SEND_DATA:
			{
				doRemoveJob = true;

				byte[] payload = request.getPayload();

				if (payload[1] == 1)
				{
					System.err.printf("ZW_SEND response, not delivered, with callback %02x\n", payload[0]);
				}
				else if (payload[1] == 0)
				{
					System.err.printf("ZW_SEND response with callback %02x\n", payload[0]);
				}
				else
				{
					System.err.printf("ZW_SEND response invalid with callback %02x\n", payload[0]);
				}

				break;
			}
			case ZW_APPLICATION_UPDATE:
			{
				byte[] payload = request.getPayload();

				if (payload[0] == ZWaveConstants.UPDATE_STATE_NODE_INFO_RECEIVED)
				{
					int nodeId = unsignedByte(payload[1]);
					Node node = nodes.get(nodeId);

					log.debug("UPDATE_STATE_NODE_INFO_RECEIVED for node {}", nodeId);

					boolean mark = false;
					int length = payload[2] - 3; // offset
					for (int i = 0; i < length; i++)
					{
						CommandClass cc = CommandClass.byByteValue(payload[6 + i]);

						// Classes that come after "MARK" are classes that can
						// be controlled by this device
						if (cc == CommandClass.MARK)
						{
							mark = true;
							log.debug(" Begin controllable classes:");
						}
						else
						{
							log.debug(" {}", cc);

							if (!mark)
							{
								node.addSupportedCommandClass(cc);
							}
							else
							{
								// add controllable command class
							}
						}
					}
				}

				break;
			}
			case APPLICATION_COMMAND_HANDLER:
			{
				byte[] payload = request.getPayload();

				int nodeId = unsignedByte(payload[1]);
				Node node = nodes.get(nodeId);

				// what are 0,1,2?
				// 00 03 03 25 03 00 d4
				// 0 == ? // instance?
				// 1 == NODE
				// 2 == LENGTH
				// 3 == COMMAND_CLASS
				// 4 == COMMAND_CLASS_FUNCTION

				CommandClass cc = CommandClass.byByteValue(payload[3]);
				log.info("Received application command class, {}", cc);

				if (cc == null)
				{
					break;
				}

				switch (cc)
				{
					case BASIC:
					{
						switch (CommandFunction.BASIC.byByteValue(payload[4]))
						{
						// R: 01 09 00 04 00 05 03 80 03 64 13
							case REPORT:
							{
								System.err.printf("Received BASIC report from node %d: %d\n", nodeId, payload[5]);
								break;
							}
							case SET:
							{
								System.err.printf("Received BASIC set from node %d: %d\n", nodeId, payload[5]);
								break;
							}
						}
						break;
					}
					case SWITCH_BINARY:
					{
						switch (CommandFunction.BASIC.byByteValue(payload[4]))
						{
							case REPORT:
							{
								boolean switch_on = unsignedByte(payload[5]) > 0;

								log.debug("SWITCH_BINARY report for node {}, switch is {}", nodeId, switch_on ? "on" : "off");

								break;
							}
						}
						break;
					}
					case VERSION:
					{
						switch (CommandFunction.VERSION.byByteValue(payload[4]))
						{
							case REPORT:
							{
								System.err.printf("Version Report for Node %d: Library Type: %d, ProtoVer: %d.%d, AppVer: %d.%d\n", nodeId, payload[5],
										payload[6], payload[7], payload[8], payload[9]);
								break;
							}
							case COMMAND_CLASS_REPORT:
							{
								int commandClass = unsignedByte(payload[5]);
								int commandClassVersion = unsignedByte(payload[5]);

								System.err.printf("Version Command Class Report for Node %d: CommandClass=%s, Version=%d\n", nodeId,
										CommandClass.byByteValue(payload[5]), commandClassVersion);

								break;
							}
							default:

								break;
						}
						break;
					}
					case MANUFACTURER_SPECIFIC:
					{
						switch (CommandFunction.MANUFACTURER_SPECIFIC.byByteValue(payload[4]))
						{
							case GET:
							{
								log.error("MANUFACTURER_SPECIFIC GET request ... unsure how to handle");
								break;
							}
							case REPORT:
							{
								node.setManufacturer(manufacturers.getManufacturer(payload, 5));
								node.setProduct(manufacturers.getProduct(payload, 5));

								System.err.println(node);
								log.debug(String.format("Version Report for Node %d: Manuf: %02x.%02x, ProtoVer: %02x.%02x, AppVer: %02x.%02x", payload[1],
										payload[5], payload[6], payload[7], payload[8], payload[9], payload[10]));
								break;
							}
						}
						break;
					}
					case BATTERY:
					{
						switch (CommandFunction.BATTERY.byByteValue(payload[4]))
						{
							case GET:
							{
								System.err.println("Someone asking ME about BATTERY?!?");
								break;
							}

							case REPORT:
							{
								byte level = payload[5];

								// Devices send 0xFF for low battery warning
								if (level == (byte) 0xff)
								{
									level = 0;
								}

								// mask off unsigned to get the proper value
								int batteryLevel = unsignedByte(level);

								if (node != null)
								{
									node.setBatteryLevel(batteryLevel);
								}

								log.debug("Battery level for node {} is {}", nodeId, batteryLevel);

								break;
							}

						}
						break;
					}
					case ASSOCIATION:
					{
						switch (CommandFunction.ASSOCIATION.byByteValue(payload[4]))
						{
							case GROUPINGS_REPORT:
							{
								int numGroups = unsignedByte(payload[5]);

								System.err.printf("Received associations for node %d, group count = %d\n", nodeId, numGroups);

								break;
							}

							case REPORT:
							{
								int groupIndex = unsignedByte(payload[5]);
								int maxAssociations = unsignedByte(payload[6]);
								int numReportsToFollow = unsignedByte(payload[7]);

								System.err.printf("Received associations for node %d: group index = %d, max = %d, numReports = %d\n", nodeId, groupIndex,
										maxAssociations, numReportsToFollow);

								break;
							}
						}
						break;
					}

					case WAKE_UP:
					{
						switch (CommandFunction.WAKE_UP.byByteValue(payload[4]))
						{
							case INTERVAL_REPORT:
							{
								int interval = (payload[5] & 0xff) << 16;
								interval |= (payload[6] & 0xff) << 8;
								interval |= (payload[7] & 0xff);

								int targetNodeId = unsignedByte(payload[8]);

								System.err.printf("Wakeup interval report from node %d: interval = %d, target node = %d\n", nodeId, interval, targetNodeId);

								// if(targetNodeId != controllerId)
								// {
								int wi = 60;
								prepareJob(new ZWSendData(nodeId, CommandClass.WAKE_UP, CommandFunction.WAKE_UP.INTERVAL_SET, (byte) ((wi >> 16) & 0xff),
										(byte) ((wi >> 8) & 0xff), (byte) ((wi) & 0xff), (byte) controllerId), true, true);
								// }

								break;
							}
							case NOTIFICATION:
							{
								System.err.printf("Node %d WOKE UP!\n", nodeId);
								break;
							}
						}
						break;
					}

					case CONFIGURATION:
					{
						switch (CommandFunction.CONFIGURATION.byByteValue(payload[4]))
						{
							case REPORT:
							{
								int paramter = unsignedByte(payload[5]);
								int size = payload[6] & 0x7;

								int value = 0;
								for (int i = 0; i < size; i++)
								{
									value <<= 8;
									value |= unsignedByte(payload[7 + i]);
								}

								System.err.printf("Parameter = %d, size = %d, value = %d\n", paramter, size, value);

								break;
							}
						}
						break;
					}

					case SENSOR_BINARY:
					{
						switch (CommandFunction.SENSOR_BINARY.byByteValue(payload[4]))
						{
							case REPORT:
							{
								System.err.printf("SensorBinary report for node %d: value = %d\n", nodeId, payload[5]);
								break;
							}
						}
						break;
					}

					case SENSOR_MULTILEVEL:
					{
						SENSOR_MULTILEVEL subfunc = CommandFunction.SENSOR_MULTILEVEL.byByteValue(payload[4]);
						if (subfunc == null)
						{
							System.err.println(">>>>>>>>>>>>>>>>>>>>>>> " + payload[4]);
							break;
						}
						switch (subfunc)
						{
							case REPORT:
							{
								SensorType type = SensorType.byId(unsignedByte(payload[5]));

								// 6 == 0

								int size = payload[6] & 0x07;
								int precision = (payload[6] & 0xe0) >> 5;
								int scale = (payload[6] & 0x18) >> 3;

								int tempValue = 0;
								for (int i = 0; i < size; i++)
								{
									tempValue <<= 8;
									tempValue |= unsignedByte(payload[7 + i]);
								}
								if ((payload[7] & 0x80) != 0 && size < 4)
								{
									tempValue = -tempValue;
								}

								BigDecimal number = BigDecimal.valueOf(tempValue, precision);

								log.debug(String.format("Got report from node %d, sensor %s, size %d, precision %d, scale %d - value = %s%s", nodeId, type,
										size, precision, scale, number, type.units(scale)));

								if (node != null)
								{
									node.setSensorValue(new SensorValue(type, scale, number));
								}
								else
								{
									log.error("Received sensor report for undefined node, {}", nodeId);
								}

								break;
							}
						}
						break;
					}
				}

				break;
			}
		}

		if (doRemoveJob && callbackJob != null)
		{
			removeJob(callbackJob);
		}
	}

	public EventBus getEventBus()
	{
		return eventBus;
	}

	protected int unsignedByte(byte b)
	{
		return b & 0xFF;
	}

	protected void handleResponse(Message response)
	{
		Job sourceJob = null;
		for (Job job : jobQueue)
		{
			if (job.awaitingResponse)
			{
				sourceJob = job;
				break;
			}
		}

		switch (response.getFunction())
		{
			case APPLICATION_COMMAND_HANDLER:
			{
				System.err.println("MEH?");
				break;
			}
			case ZW_SEND_DATA:
			{
				break;
			}
			case ZW_GET_NEIGHBOR_COUNT:
			{
				byte[] payload = response.getPayload();

				System.err.println("Neighbor Count: " + payload[0]);

				break;
			}
			case ZW_GET_CONTROLLER_CAPABILITIES:
			{
				byte[] payload = response.getPayload();

				System.err.println("CC => " + BitEnumSets.deconstruct(ControllerCapabilities.class, payload[0]));

				break;
			}
			case ZW_TYPE_LIBRARY:
			{
				byte[] payload = response.getPayload();
				LibraryType libType = Enums.byByteValue(LibraryType.class, payload[0]);
				System.err.println("Library Type: " + libType);
				break;
			}
			case ZW_GET_VERSION:
			{
				break;
			}

			case ZW_GET_SUC_NODE_ID:
			{
				byte[] payload = response.getPayload();

				if (payload[0] > 0)
				{
					System.err.printf("SUC ID = %d\n", payload[0]);
				}
				else
				{
					System.err.println("No SUC");
				}

				break;
			}

			case ZW_MEMORY_GET_ID:
			{
				ZWMemoryGetID memMsg = (ZWMemoryGetID) response;
				controllerId = memMsg.getNodeID();
				break;
			}

			case SERIAL_API_GET_CAPABILITIES:
			{
				break;
			}

			case SERIAL_API_GET_INIT_DATA:
			{
				byte[] payload = response.getPayload();

				// payload[0] == version
				// payload[1] == caps
				// payload[2] == MAGIC_LEN, 29
				// payload[2 + 29] == z-wave chip version OR

				byte version = payload[0];

				boolean slave_api = (payload[1] & 0b00000001) != 0;
				boolean controller_api = !slave_api;

				boolean timer = (payload[1] & 0b00000010) != 0;

				boolean sec_controller = (payload[1] & 0b00000100) != 0;
				boolean pri_controller = !sec_controller;

				// mask off the reserved bits
				byte reserved_bits = (byte) (payload[1] & 0b11111000);

				byte chipMaj = 0, chipMin = 0;

				if (payload[2] == 29)
				{
					reportedNodeIds.addAll(BitSets.getSetBits(BitSets.fromByteArray(payload, 3, 29), 1));
					chipMaj = payload[3 + 29];
					chipMin = payload[3 + 29 + 1];
				}
				else
				{
					chipMaj = payload[3];
					chipMin = payload[3 + 1];
				}

				System.err.printf("Version: %02x, Z-Wave Chip: ZW%02d%02d\n", version, chipMaj, chipMin);
				System.err.println("Caps:");
				System.err.println("  Slave API: " + slave_api);
				System.err.println("  Ctrlr API: " + controller_api);
				System.err.println(" Timer Supp: " + timer);
				System.err.println(" Prim. Ctrl: " + pri_controller);
				System.err.println("  Sec. Ctrl: " + sec_controller);
				System.err.printf("   Reserved: %s\n", Integer.toBinaryString(reserved_bits));
				System.err.println("      Nodes: " + reportedNodeIds);

				break;
			}

			case ZW_GET_NODE_PROTOCOL_INFO:
			{
				byte[] payload = response.getPayload();

				if (payload[4] != 0)
				{
					int nodeId = sourceJob.message.getTarget();

					Node node = Node.from(nodeId, payload);
					node.setEventBus(eventBus);

					nodes.put(node.getNodeId(), node);

					eventBus.post(new NodeAddedEvent(node));
				}
				else
				{
					// System.err.println("Invalid node: " + i);
				}
				break;
			}

			case ZW_REQUEST_NODE_INFO:
			{
				byte[] payload = response.getPayload();

				if (payload[0] != 0)
				{
					log.debug("ZW_REQUEST_NODE_INFO successful to node {}", response.getTarget());
				}
				else
				{
					log.debug("ZW_REQUEST_NODE_INFO failed to node {}", response.getTarget());
				}

				break;
			}

			default:
				log.error("UNHANDLED RESPONSE TO {}", response.getFunction());
				break;
		}

		sourceJob.awaitingResponse = false;

		// if no callback needed
		if (!sourceJob.awaitingCallback)
		{
			removeJob(sourceJob);
		}
	}
}
