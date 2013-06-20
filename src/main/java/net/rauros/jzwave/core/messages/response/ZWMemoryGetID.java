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
package net.rauros.jzwave.core.messages.response;

import com.google.common.primitives.Ints;

import net.rauros.jzwave.core.Message;

public class ZWMemoryGetID extends ResponseMessage
{
	protected int homeID;
	protected byte nodeID;

	public ZWMemoryGetID(Message rawMessage)
	{
		super(rawMessage);

		byte[] payload = getPayload();
		homeID = Ints.fromByteArray(payload);
		nodeID = payload[4];
	}

	public int getHomeID()
	{
		return homeID;
	}

	public byte getNodeID()
	{
		return nodeID;
	}

	public String toReadableString()
	{
		return String.format("ZWMemoryGetID[len=%d, type=%s, func=%s, homeid=%08x, node=%d]", buffer[1], getType(), getFunction(), homeID, nodeID);
	}
}
