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

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Uses rxtx to communicate.
 */
public class SerialTransport implements Transport
{
	private SerialPort commPort;

	public SerialTransport(String serialPortName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException
	{
		CommPortIdentifier usablePort = CommPortIdentifier.getPortIdentifier(serialPortName);
		commPort = (SerialPort)usablePort.open("SerialTransport", 0);
		commPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		commPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		commPort.setDTR(true);
		commPort.setRTS(true);
		commPort.setEndOfInputChar((byte)0x0D);
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return commPort.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return commPort.getOutputStream();
	}

	@Override
	public void close() throws IOException
	{
		commPort.getInputStream().close();
		commPort.getOutputStream().close();
		commPort.close();
	}
}
