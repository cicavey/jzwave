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
package net.rauros.jzwave.core;

import java.util.Arrays;

import net.rauros.jzwave.utils.ByteEnum;
import net.rauros.jzwave.utils.Enums;
import net.rauros.jzwave.utils.StringUtils;

public class Message
{
	public static final Message ACK = new Message(ZWaveConstants.ACK);
	public static final Message NAK = new Message(ZWaveConstants.NAK);
	public static final Message CAN = new Message(ZWaveConstants.CAN);

	public enum MsgType implements ByteEnum
	{
		REQUEST((byte)0x00), RESPONSE((byte)0x01);

		private byte byteValue;

		private MsgType(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}
	}

	private static int callbackCounter = 1;

	/**
	 * Generate a Job-wide callbackId using a rotating 8bit counter, never using
	 * zero only [1,255]
	 * 
	 * @return [1,255]
	 */
	private static synchronized int generateCallbackId()
	{
		if(callbackCounter > 255)
			callbackCounter = 1;
		return callbackCounter++;
	}

	// OpenZWave pre-allocates at buffer of 256 ... max message length, likely
	// since datalength is 8bit
	// perhaps 258 since SOF + 1 byte data length + possible 256 = 258
	protected int dataSize = 0;
	protected int size = 0;
	protected byte buffer[] = new byte[258];
	protected boolean requiresChecksum = true;

	protected int generatedCallbackId = 0;

	public Message(MsgType msgType, Function func, Object... parameters)
	{
		buffer[0] = ZWaveConstants.SOF;
		buffer[1] = 0x00; // length
		buffer[2] = msgType.getByteValue();
		buffer[3] = func.getByteValue();

		dataSize = 3;
		size = dataSize + 1;

		if(parameters != null)
		{
			for(Object parameter : parameters)
			{
				append(parameter);
			}
		}
	}

	public Message(byte... parameters)
	{
		requiresChecksum = false;
		System.arraycopy(parameters, 0, buffer, 0, parameters.length);
		dataSize = parameters.length;
		size = dataSize;
	}

	public void append(Object parameter)
	{
		if(parameter == null)
			return;

		if(parameter instanceof byte[])
		{
			byte byteArrarParameter[] = (byte[])parameter;
			for(int i = 0; i < byteArrarParameter.length; i++)
			{
				buffer[++dataSize] = byteArrarParameter[i];
				size++;
			}
		}
		else if(parameter instanceof Number)
		{
			buffer[++dataSize] = ((Number)parameter).byteValue();
			size++;
		}
		else if(parameter instanceof ByteEnum)
		{
			buffer[++dataSize] = ((ByteEnum)parameter).getByteValue();
			size++;
		}
		else if(parameter instanceof Boolean)
		{
			buffer[++dataSize] = ((Boolean)parameter).booleanValue() ? (byte)0xFF : (byte)0x00;
			size++;
		}
	}

	public void remove()
	{
		buffer[dataSize] = 0;
		dataSize--;
		size--;
	}

	public void addGeneratedCallback()
	{
		if(generatedCallbackId == 0)
		{
			generatedCallbackId = generateCallbackId();
			append(generatedCallbackId);
		}
	}

	public void removeGeneratedCallback()
	{
		if(generatedCallbackId != 0)
		{
			remove();
			generatedCallbackId = 0;
		}
	}

	public int getGeneratedCallbackId()
	{
		return generatedCallbackId;
	}

	public MsgType getType()
	{
		return Enums.byByteValue(MsgType.class, buffer[2]);
	}

	public byte getTarget()
	{
		return buffer[4];
	}

	public byte getCallbackId()
	{
		return buffer[4];
	}

	public byte[] getPayload()
	{
		return Arrays.copyOfRange(buffer, 4, buffer.length - 1);
	}

	public Function getFunction()
	{
		return Function.byByteValue(buffer[3]);
	}

	public static byte calculateChecksum(byte message[], int offset, int length)
	{
		byte startValue = (byte)0xFF;
		for(int i = 0; i < length; i++)
			startValue ^= message[offset + i];
		return startValue;
	}

	protected void calculateChecksum(byte message[])
	{
		message[size] = calculateChecksum(message, 1, dataSize);
	}

	public byte[] asByteArray()
	{
		updateLengthAndChecksum();
		return buffer;
	}

	public int getLength()
	{
		return size;
	}

	public int size()
	{
		return getLength();
	}

	protected void updateLengthAndChecksum()
	{
		if(requiresChecksum)
		{
			buffer[1] = (byte)dataSize;
			calculateChecksum(buffer);
		}
	}

	public String toReadableString()
	{
		return String.format("Message[len=%d, type=%s, func=%s]", buffer[1], getType(), getFunction());
	}

	public String toString()
	{
		return toHexString();
	}

	public String toHexString()
	{
		updateLengthAndChecksum();
		return StringUtils.toHexString(buffer, 0, getLength());
	}
}