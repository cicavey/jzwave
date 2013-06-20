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
package net.rauros.jzwave.utils;

import java.nio.charset.Charset;

public class StringUtils
{
	/**
	 * Parse at most length bytes from a byte array into a ASCII String,
	 * starting at offset. If a null character is found before length then
	 * parsing will halt.
	 * 
	 * @param rawDatap
	 * @param offset
	 * @param length
	 * @return
	 */
	public static String parseASCIINullTerm(byte rawDatap[], int offset, int length)
	{
		int searchLength = 0;
		while(searchLength < length && rawDatap[offset + searchLength] != 0)
			searchLength++;
		return new String(rawDatap, offset, searchLength, Charset.forName("US-ASCII"));
	}

	public static String toHexString(byte data[])
	{
		return toHexString(data, 0, data.length);
	}

	public static String toHexString(byte data[], int offset, int length)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++)
		{
			sb.append(Integer.toHexString((data[offset + i] & 0xF0) >> 4));
			sb.append(Integer.toHexString(data[offset + i] & 0x0F));
			if(i < length)
			{
				sb.append(" ");
			}
		}
		return sb.toString();
	}

}
