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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitSets
{
	/**
	 * Convert an array of bytes into bitset using little endian convention.
	 * 
	 * byte 0 = bits 0-7 byte 1 = bits 8-15 (bit 1 in byte => bit 8 of set)
	 * 
	 * <pre>
	 *       MSB                 LSB 
	 * byte0 07 06 05 04 03 02 01 00 
	 * byte1 15 14 13 12 11 10 09 08 
	 * byte2 23 22 21 20 19 18 17 16
	 * </pre>
	 * 
	 * @param bitmapBytes
	 * @return
	 */
	public static BitSet fromByteArray(byte bitmapBytes[], int offset, int length)
	{
		if(length < 0)
		{
			length = bitmapBytes.length - Math.abs(length) - offset;
		}

		BitSet bs = new BitSet(length * 8);

		for(int i = 0, bit = 0; i < length; i++)
		{
			byte cb = bitmapBytes[offset + i];
			for(int bitMask = 1; bitMask <= 128; bitMask <<= 1, bit++)
			{
				bs.set(bit, (cb & bitMask) == bitMask);
			}
		}

		return bs;
	}

	public static BitSet fromByteArray(byte bitmapBytes[])
	{
		return fromByteArray(bitmapBytes, 0, bitmapBytes.length);
	}

	public static List<Integer> getSetBits(BitSet bs)
	{
		return getSetBits(bs, 0);
	}

	public static List<Integer> getSetBits(BitSet bs, int countingBase)
	{
		List<Integer> setBits = new ArrayList<>();

		for(int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1))
		{
			setBits.add(Integer.valueOf(countingBase + i));
		}

		return setBits;
	}
}
