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


public enum ControllerCapabilities
{
	//@formatter:off
	
             IS_SECONDARY((byte)0b00000001),
         ON_OTHER_NETWORK((byte)0b00000010),
    NODEID_SERVER_PRESENT((byte)0b00000100),
          IS_REAL_PRIMARY((byte)0b00001000),
                   IS_SUC((byte)0b00010000);
    
	//@formatter:on

	private byte byteValue;

	private ControllerCapabilities(byte byteValue)
	{
		this.byteValue = byteValue;
	}

	public byte getByteValue()
	{
		return byteValue;
	}
}
