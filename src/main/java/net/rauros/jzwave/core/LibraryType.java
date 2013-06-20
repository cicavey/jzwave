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

public enum LibraryType implements ByteEnum
{
	CONTROLLER_STATIC((byte) 0x01),
	CONTROLLER((byte) 0x02),
	CONTROLLER_BRIDGE((byte) 0x07),
	SLAVE_ENHANCED((byte) 0x03),
	SLAVE_ROUTING((byte) 0xFF), // correct this value !!!!!!!!!!!!!!!!!!!!!!!!!!
	SLAVE((byte) 0x04),
	INSTALLER((byte) 0x05),
	NO_INTELLIGENT_LIFE((byte) 0x06);

	private byte byteValue;

	private LibraryType(byte byteValue)
	{
		this.byteValue = byteValue;
	}

	public byte getByteValue()
	{
		return byteValue;
	}
}
