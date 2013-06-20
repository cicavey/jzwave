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

import java.util.List;

import net.rauros.jzwave.utils.ByteEnum;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public enum Function implements ByteEnum
{
	SERIAL_API_GET_INIT_DATA((byte)0x02), // get list of nodes bound to the
											// controller
	SERIAL_API_APPL_NODE_INFORMATION((byte)0x03),
	APPLICATION_COMMAND_HANDLER((byte)0x04),
	ZW_GET_CONTROLLER_CAPABILITIES((byte)0x05),
	SERIAL_API_SET_TIMEOUTS((byte)0x06),
	SERIAL_API_GET_CAPABILITIES((byte)0x07), // get controller caps - function
												// mapping
	SERIAL_API_SOFT_RESET((byte)0x08),
	ZW_SET_R_F_RECEIVE_MODE((byte)0x10),
	ZW_SET_SLEEP_MODE((byte)0x11),
	ZW_SEND_NODE_INFORMATION((byte)0x12),
	ZW_SEND_DATA((byte)0x13),
	ZW_SEND_DATA_MULTI((byte)0x14),
	ZW_GET_VERSION((byte)0x15),
	ZW_SEND_DATA_ABORT((byte)0x16),
	FUNC_ID_ZW_R_F_POWER_LEVEL_SET((byte)0x17),
	FUNC_ID_ZW_SEND_DATA_META((byte)0x18),
	FUNC_ID_ZW_GET_RANDOM((byte)0x1C),
	ZW_MEMORY_GET_ID((byte)0x20),
	MEMORY_GET_BYTE((byte)0x21),
	MEMORY_PUT_BYTE((byte)0x22),
	MEMORY_GET_BUFFER((byte)0x23),
	MEMORY_PUT_BUFFER((byte)0x24),
	CLOCK_SET((byte)0x30),
	CLOCK_GET((byte)0x31),
	CLOCK_COMPARE((byte)0x32),
	RTC_TIMER_CREATE((byte)0x33),
	RTC_TIMER_READ((byte)0x34),
	RTC_TIMER_DELETE((byte)0x35),
	RTC_TIMER_CALL((byte)0x36),
	ZW_SET_LEARN_NODE_STATE((byte)0x40),
	ZW_GET_NODE_PROTOCOL_INFO((byte)0x41), // query supported node classes
	ZW_SET_DEFAULT((byte)0x42), // setting default callback?
	ZW_NEW_CONTROLLER((byte)0x43),
	ZW_REPLICATION_COMMAND_COMPLETE((byte)0x44),
	ZW_REPLICATION_SEND_DATA((byte)0x45),
	ZW_ASSIGN_RETURN_ROUTE((byte)0x46),
	ZW_DELETE_RETURN_ROUTE((byte)0x47),
	ZW_REQUEST_NODE_NEIGHBOR_UPDATE((byte)0x48),
	ZW_APPLICATION_UPDATE((byte)0x49),
	ZW_ADD_NODE_TO_NETWORK((byte)0x4A),
	ZW_REMOVE_NODE_FROM_NETWORK((byte)0x4B),
	ZW_CREATE_NEW_PRIMARY((byte)0x4C),
	ZW_CONTROLLER_CHANGE((byte)0x4D),
	ZW_SET_LEARN_MODE((byte)0x50),
	ZW_ASSIGN_SUC_RETURN_ROUTE((byte)0x51),
	ZW_ENABLE_SUC((byte)0x52),
	ZW_REQUEST_NETWORK_UPDATE((byte)0x53),
	ZW_SET_SUC_NODE_ID((byte)0x54),
	ZW_DELETE_SUC_RETURN_ROUTE((byte)0x55),
	ZW_GET_SUC_NODE_ID((byte)0x56),
	ZW_SEND_SUC_ID((byte)0x57),
	ZW_REDISCOVERY_NEEDED((byte)0x59),
	FUNC_ID_ZW_REQUEST_NODE_NEIGHBOR_UPDATE_OPTIONS((byte)0x5A),
	ZW_REQUEST_NODE_INFO((byte)0x60),
	ZW_REMOVE_FAILED_NODE_ID((byte)0x61),
	ZW_IS_FAILED_NODE((byte)0x62),
	ZW_REPLACE_FAILED_NODE((byte)0x63),
	TIMER_START((byte)0x70),
	TIMER_RESTART((byte)0x71),
	TIMER_CANCEL((byte)0x72),
	TIMER_CALL((byte)0x73),
	GET_ROUTING_TABLE_LINE((byte)0x80),
	GET_T_X_COUNTER((byte)0x81),
	RESET_T_X_COUNTER((byte)0x82),
	STORE_NODE_INFO((byte)0x83),
	STORE_HOME_ID((byte)0x84),
	LOCK_ROUTE_RESPONSE((byte)0x90),
	ZW_SEND_DATA_ROUTE_DEMO((byte)0x91),
	SERIAL_API_TEST((byte)0x95),
	SERIAL_API_SLAVE_NODE_INFO((byte)0xA0),
	APPLICATION_SLAVE_COMMAND_HANDLER((byte)0xA1),
	ZW_SEND_SLAVE_NODE_INFO((byte)0xA2),
	ZW_SEND_SLAVE_DATA((byte)0xA3),
	ZW_SET_SLAVE_LEARN_MODE((byte)0xA4),
	ZW_GET_VIRTUAL_NODES((byte)0xA5),
	ZW_IS_VIRTUAL_NODE((byte)0xA6),
	ZW_GET_NEIGHBOR_COUNT((byte)0xBB),
	ZW_ARE_NODES_NEIGHBOURS((byte)0xBC),
	ZW_TYPE_LIBRARY((byte)0xBD),
	ZW_SET_PROMISCUOUS_MODE((byte)0xD0),
	FUNC_ID_PROMISCUOUS_APPLICATION_COMMAND_HANDLER((byte)0xD1);

	private byte byteValue;

	private Function(byte byteValue)
	{
		this.byteValue = byteValue;
	}

	public byte getByteValue()
	{
		return byteValue;
	}

	public static Function byByteValue(byte byteValue)
	{
		for(Function func : values())
		{
			if(func.getByteValue() == byteValue)
			{
				return func;
			}
		}
		return null;
	}

	public static List<Function> fromValues(List<Integer> values)
	{
		return Lists.newArrayList(Iterables.filter(Iterables.transform(values, mapper), Predicates.notNull()));

	}

	public static final com.google.common.base.Function<Integer, Function> mapper = new com.google.common.base.Function<Integer, Function>()
	{
		@Override
		public Function apply(Integer value)
		{
			return Function.byByteValue(value.byteValue());
		}
	};
}