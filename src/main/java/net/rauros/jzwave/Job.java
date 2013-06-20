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
package net.rauros.jzwave;

import net.rauros.jzwave.core.Function;
import net.rauros.jzwave.core.Message;

import com.google.common.util.concurrent.SettableFuture;

public class Job
{
	Message message = null;

	boolean sent = false;

	int sendCount = 0;

	boolean awaitingAck = true;
	boolean awaitingResponse = false;
	boolean awaitingCallback = false;

	int callbackId = 0;
	Function callbackType = null;

	SettableFuture<Boolean> future = SettableFuture.create();

	public Job(Message message)
	{
		this(message, false);
	}

	public Job(Message message, boolean requiresResponse)
	{
		this(message, requiresResponse, false);
	}

	public Job(Message message, boolean requiresResponse, boolean requiresCallback)
	{
		this.message = message;
		this.awaitingResponse = requiresResponse;
		setRequiresCallback(requiresCallback);
	}

	public void setRequiresCallback(boolean requiresCallback)
	{
		// TODO This belongs in Message
		if(requiresCallback)
		{
			message.addGeneratedCallback();

			awaitingCallback = true;
			callbackId = message.getGeneratedCallbackId();
			callbackType = message.getFunction();
		}
		else
		{
			message.removeGeneratedCallback();
			awaitingCallback = false;
			callbackId = 0;
			callbackType = null;
		}
	}

	public String toString()
	{
		return "Job[sent = " + sent + ", sendCount = " + sendCount + ", awaitingAck = " + awaitingAck + ", awaitingResponse = " + awaitingResponse
				+ ", callbackid = " + callbackId + ", callbacktype = " + callbackType + "\n\t" + message + "]";
	}

}
