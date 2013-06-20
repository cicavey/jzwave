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

public class Manufacturers
{
	protected Map<String, Manufacturer> manufacturers = new HashMap<String, Manufacturer>();

	public Manufacturer getManufacturer(String id)
	{
		return manufacturers.get(id);
	}

	public Manufacturer getManufacturer(byte msb, byte lsb)
	{
		return getManufacturer(String.format("%02x%02x", msb, lsb));
	}

	public Manufacturer getManufacturer(byte data[], int offset)
	{
		return getManufacturer(String.format("%02x%02x", data[offset], data[offset + 1]));
	}

	public Product getProduct(String manufacturerId, String productId, String productType)
	{
		Manufacturer manufacturer = getManufacturer(manufacturerId);
		return manufacturer.getProduct(productId, productType);
	}

	public Product getProduct(byte data[], int offset)
	{
		Manufacturer manufacturer = getManufacturer(data, offset);
		return manufacturer.getProduct(data, offset + 2);
	}

	public void addManufacturer(Manufacturer manufacturer)
	{
		manufacturers.put(manufacturer.getId(), manufacturer);
	}

	public int size()
	{
		return manufacturers.size();
	}
}
