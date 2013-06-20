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
package net.rauros.jzwave.core.messages;

import net.rauros.jzwave.core.Message;
import net.rauros.jzwave.core.Message.MsgType;
import net.rauros.jzwave.core.messages.response.SAPIGetCapabilities;
import net.rauros.jzwave.core.messages.response.ZWGetVersion;
import net.rauros.jzwave.core.messages.response.ZWMemoryGetID;
import net.rauros.jzwave.core.messages.response.ZWSendDataDelivery;

public class MessageFactory
{
	public static Message deriveResponseMessage(Message rawMessage)
	{
		if(rawMessage.getType() == MsgType.RESPONSE)
		{
			switch(rawMessage.getFunction())
			{
				case SERIAL_API_GET_CAPABILITIES:
					return new SAPIGetCapabilities(rawMessage);
				case ZW_MEMORY_GET_ID:
					return new ZWMemoryGetID(rawMessage);
				case ZW_GET_VERSION:
					return new ZWGetVersion(rawMessage);
				case ZW_SEND_DATA:
					return new ZWSendDataDelivery(rawMessage);
				default:
					break;
			}
		}

		return rawMessage;
	}
}
