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

public enum SensorType
{
	//@formatter:off
	TEMPERATURE("C", "F"),
	GENERAL("", "%"),
	LUMINANCE("%", "lux"),
	POWER("BTU/h", "W"),
	RELATIVE_HUMIDITY("%"),
	VELOCITY("mph", "m/s"),
	DIRECTION,
	ATOMSPHERIC_PRESSURE("inHg", "kPa"),
	BAROMETRIC_PRESSURE("inHg", "kPa"),
	SOLAR_RADITION("W/m^2"),
	DEW_POINT("F", "C"),
	RAIN_RATE("in/h", "mm/h"),
	TIDE_LEVEL("ft", "m"),
	WEIGHT("lb", "kg"),
	VOLTAGE("mV", "V"),
	CURRENT("mA", "A"),
	CO2("ppm"),
	AIR_FLOW("cfm", "m^3/h"),
	TANK_CAPACITY("l", "cbm", "gal"),
	DISTANCE("m", "cm", "ft"),
	ANGLE_POSITION("%", "deg N", "deg S"),
	ROTATION("rpm", "hz"),
	WATER_TEMPERATURE("C", "F"),
	SOIL_TEMPERATURE("C", "F"),
	SEISMIC_INTENSITY("mercalli", "EU macroseuismic", "liedu", "shindo"),
	SEISMIC_MAGNITUDE("local", "moment", "surface wave", "body wave"),
	ULTRAVIOLET,
	ELECTRICAL_RESISTIVITY("ohm"),
	ELECTRICAL_CONDUCTIVITY("siemens/m"),
	LOUDNESS("db", "dBA"),
	MOISTURE("%", "content", "k ohms", "water activity");
	//@formatter:on

	private String[] units;

	private SensorType(String... units)
	{
		this.units = units;
	}

	public String units(int index)
	{
		if(units != null && units.length > 0)
		{
			if(index >= units.length)
			{
				return units[0];
			}
			else
			{
				return units[index];
			}
		}

		return "";
	}

	public static SensorType byId(int id)
	{
		// sensor type is one based according to zwave
		return SensorType.values()[id - 1];
	}
}
