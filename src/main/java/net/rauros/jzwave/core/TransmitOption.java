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

import java.util.EnumSet;

import net.rauros.jzwave.utils.BitEnumSets;
import net.rauros.jzwave.utils.BitField;
import net.rauros.jzwave.utils.ByteEnum;

public enum TransmitOption implements ByteEnum, BitField
{
	//@formatter:off
	
	          ACK((byte)0b00000001),
	    LOW_POWER((byte)0b00000010),
	   AUTO_ROUTE((byte)0b00000100),
	  FORCE_ROUTE((byte)0b00001000),
	     NO_ROUTE((byte)0b00010000),
	NO_RETRANSMIT((byte)0b01000000);
	          
	//@formatter:on

	private byte byteValue;

	private TransmitOption(byte byteValue)
	{
		this.byteValue = byteValue;
	}

	@Override
	public int getBitValue()
	{
		return byteValue;
	}

	public byte getByteValue()
	{
		return byteValue;
	}

	/**
	 * A typical combination of options, [ACK, AUTO_ROUTE]
	 * 
	 * @return
	 */
	public static byte standard()
	{
		return BitEnumSets.constructByte(EnumSet.of(TransmitOption.ACK, TransmitOption.AUTO_ROUTE));
	}
}
