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

import net.rauros.jzwave.core.LibraryType;
import net.rauros.jzwave.core.Message;
import net.rauros.jzwave.utils.Enums;
import net.rauros.jzwave.utils.StringUtils;

public class ZWGetVersion extends ResponseMessage
{
	private String version;
	private LibraryType libType;

	public ZWGetVersion(Message rawMessage)
	{
		super(rawMessage);

		byte[] payload = getPayload();

		version = StringUtils.parseASCIINullTerm(payload, 0, 11);
		libType = Enums.byByteValue(LibraryType.class, payload[12]);
	}

	public String toReadableString()
	{
		return String.format("ZWGetVersion[len=%d, type=%s, func=%s, version=%s, lib=%s]", buffer[1], getType(), getFunction(), version, libType);
	}
}
