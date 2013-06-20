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
package net.rauros.jzwave.core;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * Immutable representation of a sensor value at a given instant
 */
public class SensorValue implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected SensorType type;
	protected int scale;
	protected BigDecimal value;
	protected DateTime lastUpdate = new DateTime();

	public SensorValue(SensorType type, int scale, BigDecimal value)
	{
		this.type = type;
		this.scale = scale;
		this.value = value;
	}

	public SensorType getType()
	{
		return type;
	}

	public int getScale()
	{
		return scale;
	}

	public BigDecimal getValue()
	{
		return value;
	}

	public DateTime getLastUpdate()
	{
		return lastUpdate;
	}

	public String toString()
	{
		return value.toPlainString() + type.units(scale);
	}
}
