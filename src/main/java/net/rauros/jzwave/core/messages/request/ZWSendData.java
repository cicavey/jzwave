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
package net.rauros.jzwave.core.messages.request;

import net.rauros.jzwave.core.CommandClass;
import net.rauros.jzwave.core.Function;
import net.rauros.jzwave.core.Message;
import net.rauros.jzwave.core.TransmitOption;
import net.rauros.jzwave.utils.ByteEnum;

public class ZWSendData extends Message
{
	protected int targetNodeId;
	private CommandClass commandClass;
	private ByteEnum command;

	public ZWSendData(int targetNodeId, CommandClass commandClass, ByteEnum command, Object... parameters)
	{
		super(MsgType.REQUEST, Function.ZW_SEND_DATA, targetNodeId, 2 + parameters.length, commandClass, command);

		for(Object parameter : parameters)
		{
			append(parameter);
		}
		
		append(TransmitOption.standard());

		addGeneratedCallback();

		// update the payload size after adding all sorts of random stuff
		buffer[5] = (byte)(dataSize - 7);
		
		this.targetNodeId = targetNodeId;
		this.commandClass = commandClass;
		this.command = command;
	}

	public String toReadableString()
	{
		return String.format("ZWSendData[len=%d, type=%s, func=%s, targetNode=%d, cmdCls=%s, cmd=%s, callback=%d]", buffer[1], getType(), getFunction(),
				targetNodeId, commandClass, command, getGeneratedCallbackId());
	}
}
