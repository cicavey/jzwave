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
package net.rauros.jzwave.config;

public class Product
{
	protected String id;
	protected String type;
	protected String compoundId;
	protected String name;
	protected String config;

	public Product(String id, String type, String name, String config)
	{
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.config = config;
		this.compoundId = type + id;
	}

	public String getCompoundId()
	{
		return compoundId;
	}

	@Override
	public String toString()
	{
		return name + " (" + compoundId + ")";
	}

}
