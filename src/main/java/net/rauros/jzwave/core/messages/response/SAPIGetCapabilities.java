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

import java.util.List;

import net.rauros.jzwave.core.Function;
import net.rauros.jzwave.core.Message;
import net.rauros.jzwave.utils.BitSets;

public class SAPIGetCapabilities extends ResponseMessage
{
	private byte majVer;
	private byte minVer;
	private byte manMajID;
	private byte manMinID;
	private byte manProdTypeMajID;
	private byte manProdTypeMinID;
	private byte manProdMajID;
	private byte manProdMinPID;
	private List<Function> supportedFunctions;

	public SAPIGetCapabilities(Message rawMessage)
	{
		super(rawMessage);

		byte[] payload = getPayload();

		majVer = payload[0];
		minVer = payload[1];

		manMajID = payload[2];
		manMinID = payload[3];

		manProdTypeMajID = payload[4];
		manProdTypeMinID = payload[5];

		manProdMajID = payload[6];
		manProdMinPID = payload[7];

		supportedFunctions = Function.fromValues(BitSets.getSetBits(BitSets.fromByteArray(payload, 8, 26)));
	}

	public String toReadableString()
	{
		return String.format(
				"SAPIGetCapabilities[len=%d, type=%s, func=%s, version=%d.%d, manVer=%02x.%02x, manProdTypeVer=%02x.%02x, manProD=%02x.%02x, functions=%s]",
				buffer[1], getType(), getFunction(), majVer, minVer, manMajID, manMinID, manProdTypeMajID, manProdTypeMinID, manProdMajID, manProdMinPID,
				supportedFunctions);
	}
}
