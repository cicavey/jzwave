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

import net.rauros.jzwave.utils.ByteEnum;

public enum GenericType implements ByteEnum
{
	//@formatter:off
	GENERIC_CONTROLLER((byte)0x01), 
	STATIC_CONTROLLER((byte)0x02), 
	AV_CONTROL_POINT((byte)0x03), 
	DISPLAY((byte)0x06), 
	GARAGE_DOOR((byte)0x07), 
	THERMOSTAT((byte)0x08), 
	WINDOW_COVERING((byte)0x09), 
	REPEATER_SLAVE((byte)0x0F),
	SWITCH_BINARY((byte)0x10), 
	SWITCH_MULTILEVEL((byte)0x11),
	SWITCH_REMOTE((byte)0x12), 
	SWITCH_TOGGLE((byte)0x13), 
	SENSOR_BINARY((byte)0x20), 
	SENSOR_MULTILEVEL((byte)0x21), 
	WATER_CONTROL((byte)0x22), 
	METER_PULSE((byte)0x30), 
	ENTRY_CONTROL((byte)0x40), 
	SEMI_INTEROPERABLE((byte)0x50), 
	NON_INTEROPERABLE((byte)0xFF);
	//@formatter:on

	private byte byteValue;

	private GenericType(byte byteValue)
	{
		this.byteValue = byteValue;
	}

	public byte getByteValue()
	{
		return byteValue;
	}
}