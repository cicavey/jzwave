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

import java.util.HashMap;
import java.util.Map;

public class Manufacturer
{
	protected String id;
	protected String name;
	protected Map<String, Product> products = new HashMap<String, Product>();

	public Manufacturer(String id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	public void addProduct(Product product)
	{
		products.put(product.getCompoundId(), product);
	}

	public Product getProduct(String type, String id)
	{
		return products.get(type + id);
	}

	public Product getProduct(byte data[], int offset)
	{
		String format = String.format("%02x%02x%02x%02x", data[offset], data[offset + 1], data[offset + 2], data[offset + 3]);
		return products.get(format);
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return name + " (" + id + ")";
	}
}
