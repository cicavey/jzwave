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

public interface ZWaveConstants
{
	public static final byte SOF = 0x01;
	public static final byte ACK = 0x06;
	public static final byte NAK = 0x15;
	public static final byte CAN = 0x18;
	
	public static byte NODE_BROADCAST = (byte)0xff;

	public static byte UPDATE_STATE_NODE_INFO_RECEIVED = (byte)0x84;
	public static byte UPDATE_STATE_NODE_INFO_REQ_FAILED = (byte)0x81;

	public static byte TRANSMIT_COMPLETE_OK = 0x00;
	public static byte TRANSMIT_COMPLETE_NO_ACK = 0x01;
	public static byte TRANSMIT_COMPLETE_FAIL = 0x02;
	public static byte TRANSMIT_COMPLETE_NOROUTE = 0x04;
}
