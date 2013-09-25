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

import java.net.URI;
import java.net.URISyntaxException;

public class TransportFactory
{
	private TransportFactory()
	{
	}

	public static final Transport create(String transportURIString) throws TransportException
	{
		try
		{
			return create(new URI(transportURIString));
		}
		catch(URISyntaxException e)
		{
			throw new TransportException("Unable to create URI for transport", e);
		}
	}

	public static final Transport create(URI transportURI) throws TransportException
	{
		Transport transport = null;

		if("socket".equalsIgnoreCase(transportURI.getScheme()))
		{
			try
			{
				transport = new SocketTransport(transportURI.getHost(), transportURI.getPort());
			}
			catch(Exception e)
			{
				throw new TransportException("Unable to create SocketTransport", e);
			}
		}
		else if("serial".equalsIgnoreCase(transportURI.getScheme()))
		{
			try
			{
                transport = new SerialTransport(transportURI.getPath());
			}
			catch(Exception e)
			{
				throw new TransportException("Unable to create SerialTransport", e);
			}
		}
		else
		{
			throw new TransportException("Unable to create transport for uri: " + transportURI);
		}

		return transport;
	}
}
