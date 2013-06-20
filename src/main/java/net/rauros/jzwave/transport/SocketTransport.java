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
package net.rauros.jzwave.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketTransport implements Transport
{
	private Socket socket;

	public SocketTransport(String host, int port) throws UnknownHostException, IOException
	{
		socket = new Socket(host, port);
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return socket.getOutputStream();
	}

	@Override
	public void close() throws IOException
	{
		socket.getInputStream().close();
		socket.getOutputStream().close();
		socket.close();
	}

	public String toString()
	{
		return "SocketTransport[" + socket + "]";
	}
}
