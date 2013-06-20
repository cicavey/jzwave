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

import net.rauros.jzwave.utils.ByteEnum;

public enum CommandClass implements ByteEnum
{
	NO_OPERATION((byte)0x00),
	BASIC((byte)0x20),
	CONTROLLER_REPLICATION((byte)0x21),
	APPLICATION_STATUS((byte)0x22),
	ZIP_SERVICES((byte)0x23),
	ZIP_SERVER((byte)0x24),
	SWITCH_BINARY((byte)0x25),
	SWITCH_MULTILEVEL((byte)0x26),
	SWITCH_MULTILEVEL_V2((byte)0x26),
	SWITCH_ALL((byte)0x27),
	SWITCH_TOGGLE_BINARY((byte)0x28),
	SWITCH_TOGGLE_MULTILEVEL((byte)0x29),
	CHIMNEY_FAN((byte)0x2A),
	SCENE_ACTIVATION((byte)0x2B),
	SCENE_ACTUATOR_CONF((byte)0x2C),
	SCENE_CONTROLLER_CONF((byte)0x2D),
	ZIP_CLIENT((byte)0x2E),
	ZIP_ADV_SERVICES((byte)0x2F),
	SENSOR_BINARY((byte)0x30),
	SENSOR_MULTILEVEL((byte)0x31),
	SENSOR_MULTILEVEL_V2((byte)0x31),
	METER((byte)0x32),
	ZIP_ADV_SERVER((byte)0x33),
	ZIP_ADV_CLIENT((byte)0x34),
	METER_PULSE((byte)0x35),
	METER_TBL_CONFIG((byte)0x3C),
	METER_TBL_MONITOR((byte)0x3D),
	METER_TBL_PUSH((byte)0x3E),
	THERMOSTAT_HEATING((byte)0x38),
	THERMOSTAT_MODE((byte)0x40),
	THERMOSTAT_OPERATING_STATE((byte)0x42),
	THERMOSTAT_SETPOINT((byte)0x43),
	THERMOSTAT_FAN_MODE((byte)0x44),
	THERMOSTAT_FAN_STATE((byte)0x45),
	CLIMATE_CONTROL_SCHEDULE((byte)0x46),
	THERMOSTAT_SETBACK((byte)0x47),
	DOOR_LOCK_LOGGING((byte)0x4C),
	SCHEDULE_ENTRY_LOCK((byte)0x4E),
	BASIC_WINDOW_COVERING((byte)0x50),
	MTP_WINDOW_COVERING((byte)0x51),
	MULTI_CHANNEL_V2((byte)0x60),
	MULTI_INSTANCE((byte)0x60),
	DOOR_LOCK((byte)0x62),
	USER_CODE((byte)0x63),
	CONFIGURATION((byte)0x70),
	CONFIGURATION_V2((byte)0x70),
	ALARM((byte)0x71),
	MANUFACTURER_SPECIFIC((byte)0x72),
	POWERLEVEL((byte)0x73),
	PROTECTION((byte)0x75),
	PROTECTION_V2((byte)0x75),
	LOCK((byte)0x76),
	NODE_NAMING((byte)0x77),
	FIRMWARE_UPDATE_MD((byte)0x7A),
	GROUPING_NAME((byte)0x7B),
	REMOTE_ASSOCIATION_ACTIVATE((byte)0x7C),
	REMOTE_ASSOCIATION((byte)0x7D),
	BATTERY((byte)0x80),
	CLOCK((byte)0x81),
	HAIL((byte)0x82),
	WAKE_UP((byte)0x84),
	WAKE_UP_V2((byte)0x84),
	ASSOCIATION((byte)0x85),
	ASSOCIATION_V2((byte)0x85),
	VERSION((byte)0x86),
	INDICATOR((byte)0x87),
	PROPRIETARY((byte)0x88),
	LANGUAGE((byte)0x89),
	TIME((byte)0x8A),
	TIME_PARAMETERS((byte)0x8B),
	GEOGRAPHIC_LOCATION((byte)0x8C),
	COMPOSITE((byte)0x8D),
	MULTI_CHANNEL_ASSOCIATION_V2((byte)0x8E),
	MULTI_INSTANCE_ASSOCIATION((byte)0x8E),
	MULTI_CMD((byte)0x8F),
	ENERGY_PRODUCTION((byte)0x90),
	MANUFACTURER_PROPRIETARY((byte)0x91),
	SCREEN_MD((byte)0x92),
	SCREEN_MD_V2((byte)0x92),
	SCREEN_ATTRIBUTES((byte)0x93),
	SCREEN_ATTRIBUTES_V2((byte)0x93),
	SIMPLE_AV_CONTROL((byte)0x94),
	AV_CONTENT_DIRECTORY_MD((byte)0x95),
	AV_RENDERER_STATUS((byte)0x96),
	AV_CONTENT_SEARCH_MD((byte)0x97),
	SECURITY((byte)0x98),
	AV_TAGGING_MD((byte)0x99),
	IP_CONFIGURATION((byte)0x9A),
	ASSOCIATION_COMMAND_CONFIGURATION((byte)0x9B),
	SENSOR_ALARM((byte)0x9C),
	SILENCE_ALARM((byte)0x9D),
	SENSOR_CONFIGURATION((byte)0x9E),
	MARK((byte)0xEF),
	NON_INTEROPERABLE((byte)0xF0);

	private byte byteValue;

	private CommandClass(byte byteValue)
	{
		this.byteValue = byteValue;
	}

	@Override
	public byte getByteValue()
	{
		return byteValue;
	}

	public static CommandClass byByteValue(byte byteValue)
	{
		for(CommandClass func : values())
		{
			if(func.getByteValue() == byteValue)
			{
				return func;
			}
		}
		return null;
	}
}
