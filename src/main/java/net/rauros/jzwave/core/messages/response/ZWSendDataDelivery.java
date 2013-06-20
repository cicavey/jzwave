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

import net.rauros.jzwave.core.Message;

public class ZWSendDataDelivery extends ResponseMessage
{
	private byte sendDataResponse;

	public ZWSendDataDelivery(Message rawMessage)
	{
		super(rawMessage);
		byte[] payload = getPayload();
		sendDataResponse = payload[0];
	}

	public String toReadableString()
	{
		return String.format("ZWSendDataDelivery[len=%d, type=%s, func=%s, success=%s, code=%d]", buffer[1], getType(), getFunction(), sendDataResponse == 1,
				sendDataResponse);
	}
}
