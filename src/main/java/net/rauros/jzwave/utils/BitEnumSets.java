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

import java.util.EnumSet;

import net.rauros.jzwave.core.Security;
import net.rauros.jzwave.core.TransmitOption;

public class BitEnumSets
{
	private BitEnumSets()
	{
	}

	public static <E extends Enum<E>> EnumSet<E> deconstruct(Class<E> enumClass, int value, int startBit, int endBit)
	{
		EnumSet<E> set = EnumSet.noneOf(enumClass);

		if(BitField.class.isAssignableFrom(enumClass))
		{
			for(E enumConstant : enumClass.getEnumConstants())
			{
				if((value & ((BitField) enumConstant).getBitValue()) != 0)
				{
					set.add(enumConstant);
				}
			}
		}
		else
		{
			for(int i = startBit; i < endBit; i++)
			{
				if(((1 << i) & value) != 0)
				{
					set.add(enumClass.getEnumConstants()[i - startBit]);
				}
			}
		}

		return set;
	}

	public static <E extends Enum<E>> EnumSet<E> deconstruct(Class<E> enumClass, int value)
	{
		return deconstruct(enumClass, value, 0, 32);
	}

	public static <E extends Enum<E>> EnumSet<E> deconstructByte(Class<E> enumClass, int value)
	{
		return deconstruct(enumClass, value, 0, 8);
	}

	/**
	 * Combine an enum set into a single value with each bit representing an enum.
	 * Each enum corresponds to a bit by the following formula.
	 * 
	 * <pre>
	 * for each ordinal in the set:
	 *     combinedValue |= (1 << enumConstant.ordinal())
	 * </pre>
	 * 
	 * @see EnumSet
	 * @see ByteEnum
	 * @param es
	 * @return
	 */
	public static <E extends Enum<E>> int construct(EnumSet<E> es)
	{
		int combined = 0;
		for(E value : es)
		{
			if(value instanceof BitField)
			{
				combined |= ((BitField) value).getBitValue();
			}
			else
			{
				combined |= (1 << value.ordinal());
			}
		}
		return combined;
	}

	/**
	 * Combine an enum set into a single value, only returning the low byte
	 * 
	 * @param es
	 * @return
	 */
	public static <E extends Enum<E>> byte constructByte(EnumSet<E> es)
	{
		return (byte) (construct(es) & 0xff);
	}

	public static void main(String args[])
	{
		int c = construct(EnumSet.of(Security.SECURITY, Security.OPTIONAL_FUNCTIONALITY));
		System.err.println(c + " -> " + deconstruct(Security.class, c));

		c = construct(EnumSet.of(TransmitOption.ACK, TransmitOption.NO_RETRANSMIT));
		System.err.println(c + " -> " + deconstruct(TransmitOption.class, c));

	}

}
