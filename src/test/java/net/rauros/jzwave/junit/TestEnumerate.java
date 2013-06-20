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
package net.rauros.jzwave.junit;

import net.rauros.jzwave.ZWaveManager;
import net.rauros.jzwave.core.Node;
import net.rauros.jzwave.core.NodeListener;

public class TestEnumerate
{
	public static void main(String[] args)
	{
		if(args.length > 0)
		{
			ZWaveManager zwm = new ZWaveManager(args[0]);

			zwm.addNodeListener(new NodeListener()
			{
				@Override
				public void nodeChanged(ZWaveManager manager, Node node)
				{
					System.err.println(">>>>>> NODE CHANGED: " + node);
				}
			});

			zwm.open();
		}
		else
		{
			System.err.println("Insufficient arguments - must supply connection URI");
		}
	}
}
