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

import java.io.Serializable;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.rauros.jzwave.config.Manufacturer;
import net.rauros.jzwave.config.Product;
import net.rauros.jzwave.utils.BitEnumSets;
import net.rauros.jzwave.utils.Enums;

import com.google.common.base.Objects;

public class Node implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected int nodeId;

	protected EnumSet<CommandClass> supportedCommandClasses = EnumSet.noneOf(CommandClass.class);
	protected EnumSet<CommandClass> controllableCommandClasses = EnumSet.noneOf(CommandClass.class);

	protected BitSet neighbors = new BitSet(256);

	protected boolean listening;
	protected boolean routing;

	protected int maxBaudRate;
	protected int version;
	protected EnumSet<Security> security;

	protected BasicType basicType;
	protected GenericType genericType;
	protected byte specificType;

	protected Manufacturer manufacturer;
	protected Product product;

	protected int batteryLevel;

	protected Map<SensorType, SensorValue> sensorValues = new HashMap<>();

	public Node(int nodeId, boolean listening, boolean routing, int maxBaudRate, int version, EnumSet<Security> security, BasicType basicType,
			GenericType genericType, byte specificType)
	{
		this.nodeId = nodeId;
		this.listening = listening;
		this.routing = routing;
		this.maxBaudRate = maxBaudRate;
		this.version = version;
		this.security = security;
		this.basicType = basicType;
		this.genericType = genericType;
		this.specificType = specificType;
	}

	public static Node from(int nodeId, byte data[])
	{
		return from(nodeId, data, 0);
	}

	//@formatter:off
	private static byte      listenMask = (byte)0b10000000;
	private static byte     routingMask = (byte)0b01000000;
	private static byte    baudRateMask = (byte)0b00111000;
	private static byte     versionMask = (byte)0b00000111;
	
	@SuppressWarnings("unused")
	private static byte   baudRate_unk1 = (byte)0b00001000;
	private static byte baudRate_40kbit = (byte)0b00010000;
	@SuppressWarnings("unused")
	private static byte   baudRate_unk2 = (byte)0b00100000;
	//@formatter:on

	public static Node from(int nodeId, byte data[], int offset)
	{
		boolean listening = (data[offset] & listenMask) != 0;
		boolean routing = (data[offset] & routingMask) != 0;

		int maxBaudRate = 9600;

		if((data[offset] & baudRateMask) == baudRate_40kbit)
		{
			maxBaudRate = 40000;
		}

		int version = (data[offset] & versionMask) + 1;

		EnumSet<Security> security = BitEnumSets.deconstructByte(Security.class, data[offset + 1]);

		// data[offset+2] == "Reserved"

		BasicType basicType = Enums.byByteValue(BasicType.class, data[offset + 3]);
		GenericType genericType = Enums.byByteValue(GenericType.class, data[offset + 4]);

		byte specificType = data[offset + 5];

		return new Node(nodeId, listening, routing, maxBaudRate, version, security, basicType, genericType, specificType);
	}

	public int getNodeId()
	{
		return nodeId;
	}

	public boolean isListening()
	{
		return listening;
	}

	public boolean isRouting()
	{
		return routing;
	}

	public int getMaxBaudRate()
	{
		return maxBaudRate;
	}

	public Manufacturer getManufacturer()
	{
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public int getVersion()
	{
		return version;
	}

	public EnumSet<Security> getSecurity()
	{
		return security;
	}

	public BasicType getBasicType()
	{
		return basicType;
	}

	public GenericType getGenericType()
	{
		return genericType;
	}

	public byte getSpecificType()
	{
		return specificType;
	}

	public int getBatteryLevel()
	{
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel)
	{
		this.batteryLevel = batteryLevel;
	}

	public void setSensorValue(SensorValue value)
	{
		sensorValues.put(value.getType(), value);
	}

	public SensorValue getSensorValue(SensorType type)
	{
		return sensorValues.get(type);
	}

	public Map<SensorType, SensorValue> getSensorValues()
	{
		return sensorValues;
	}

	public void addSupportedCommandClass(CommandClass cc)
	{
		supportedCommandClasses.add(cc);
	}

	public void addControllableCommandClass(CommandClass cc)
	{
		controllableCommandClasses.add(cc);
	}

	public String toString()
	{
		//@formatter:off
		return Objects.toStringHelper(Node.class)
				.add("nodeId", nodeId)
				.add("manufacturer", manufacturer)
				.add("product", product)
				.add("listening", listening)
				.add("routing", routing)
				.add("maxBaudRate", maxBaudRate)
				.add("version", version)
				.add("security", security)
				.add("basicType", basicType)
				.add("genericType", genericType)
				.add("specificType", specificType)
				.add("batteryLevel", batteryLevel)
				.add("sensors", sensorValues)
				.toString();
		//@formatter:on
	}
}
