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

public class CommandFunction
{
	public enum BASIC implements ByteEnum
	{
		SET((byte)0x01), GET((byte)0x02), REPORT((byte)0x03);

		private byte byteValue;

		private BASIC(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static BASIC byByteValue(byte byteValue)
		{
			for(BASIC func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum CONFIGURATION implements ByteEnum
	{
		SET((byte)0x04), GET((byte)0x05), REPORT((byte)0x06);

		private byte byteValue;

		private CONFIGURATION(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static CONFIGURATION byByteValue(byte byteValue)
		{
			for(CONFIGURATION func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum MANUFACTURER_SPECIFIC implements ByteEnum
	{
		GET((byte)0x04), REPORT((byte)0x05);

		private byte byteValue;

		private MANUFACTURER_SPECIFIC(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static MANUFACTURER_SPECIFIC byByteValue(byte byteValue)
		{
			for(MANUFACTURER_SPECIFIC func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum SWITCH_BINARY implements ByteEnum
	{
		SET((byte)0x01), GET((byte)0x02), REPORT((byte)0x03);

		private byte byteValue;

		private SWITCH_BINARY(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static SWITCH_BINARY byByteValue(byte byteValue)
		{
			for(SWITCH_BINARY func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum SENSOR_BINARY implements ByteEnum
	{
		GET((byte)0x02), REPORT((byte)0x03);

		private byte byteValue;

		private SENSOR_BINARY(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static SENSOR_BINARY byByteValue(byte byteValue)
		{
			for(SENSOR_BINARY func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum SENSOR_MULTILEVEL implements ByteEnum
	{
		SUPPORTED_GET((byte)0x01), SUPPORTED_REPORT((byte)0x02), GET((byte)0x04), REPORT((byte)0x05);

		private byte byteValue;

		private SENSOR_MULTILEVEL(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static SENSOR_MULTILEVEL byByteValue(byte byteValue)
		{
			for(SENSOR_MULTILEVEL func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum VERSION implements ByteEnum
	{
		SET((byte)0x11), REPORT((byte)0x12), COMMAND_CLASS_GET((byte)0x13), COMMAND_CLASS_REPORT((byte)0x14);

		private byte byteValue;

		private VERSION(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static VERSION byByteValue(byte byteValue)
		{
			for(VERSION func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum POWERLEVEL implements ByteEnum
	{
		SET((byte)0x01), GET((byte)0x02), REPORT((byte)0x03), TEST_NODE_SET((byte)0x04), TEST_NODE_GET((byte)0x05), TEST_NODE_REPORT((byte)0x06);

		private byte byteValue;

		private POWERLEVEL(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static POWERLEVEL byByteValue(byte byteValue)
		{
			for(POWERLEVEL func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum BATTERY implements ByteEnum
	{
		GET((byte)0x02), REPORT((byte)0x03);

		private byte byteValue;

		private BATTERY(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static BATTERY byByteValue(byte byteValue)
		{
			for(BATTERY func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum WAKE_UP implements ByteEnum
	{
		INTERVAL_SET((byte)0x04), INTERVAL_GET((byte)0x05), INTERVAL_REPORT((byte)0x06), NOTIFICATION((byte)0x07), NO_MORE_INFO((byte)0x08);

		private byte byteValue;

		private WAKE_UP(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static WAKE_UP byByteValue(byte byteValue)
		{
			for(WAKE_UP func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}

	public enum ASSOCIATION implements ByteEnum
	{
		SET((byte)0x01), GET((byte)0x02), REPORT((byte)0x03), REMOVE((byte)0x04), GROUPINGS_GET((byte)0x05), GROUPINGS_REPORT((byte)0x06);

		private byte byteValue;

		private ASSOCIATION(byte byteValue)
		{
			this.byteValue = byteValue;
		}

		public byte getByteValue()
		{
			return byteValue;
		}

		public static ASSOCIATION byByteValue(byte byteValue)
		{
			for(ASSOCIATION func : values())
			{
				if(func.getByteValue() == byteValue)
				{
					return func;
				}
			}
			return null;
		}
	}
}
