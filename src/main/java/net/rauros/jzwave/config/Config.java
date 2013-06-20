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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config
{
	private static final Logger log = LoggerFactory.getLogger(Config.class);

	public static Manufacturers load(String configDir)
	{
		return load(configDir != null ? new File(configDir) : null);
	}

	public static Manufacturers load()
	{
		return load((File)null);
	}

	@SuppressWarnings("unchecked")
	public static Manufacturers load(File configDir)
	{
		if(configDir == null || !configDir.exists())
		{
			configDir = new File("config");
		}

		Manufacturers manufacturers = new Manufacturers();

		int manufacturersCount = 0;
		int productCount = 0;

		try
		{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(new File(configDir, "manufacturer_specific.xml"));
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			Manufacturer manufacturer = null;

			while(eventReader.hasNext())
			{
				XMLEvent event = eventReader.nextEvent();
				if(event.isStartElement())
				{
					StartElement startElt = event.asStartElement();
					if(startElt.getName().getLocalPart().equals("Manufacturer"))
					{
						String id = null;
						String name = null;

						Iterator<Attribute> attributes = startElt.getAttributes();
						while(attributes.hasNext())
						{
							Attribute next = attributes.next();
							if(next.getName().toString().equals("id"))
							{
								id = next.getValue();
							}
							else if(next.getName().toString().equals("name"))
							{
								name = next.getValue();
							}
						}

						manufacturer = new Manufacturer(id, name);
						manufacturers.addManufacturer(manufacturer);
						manufacturersCount++;
					}
					else if(startElt.getName().getLocalPart().equals("Product"))
					{
						String id = null;
						String type = null;
						String name = null;
						String config = null;

						Iterator<Attribute> attributes = startElt.getAttributes();
						while(attributes.hasNext())
						{
							Attribute next = attributes.next();
							String attrName = next.getName().toString();

							if(attrName.equals("id"))
							{
								id = next.getValue();
							}
							else if(attrName.equals("name"))
							{
								name = next.getValue();
							}
							else if(attrName.equals("type"))
							{
								type = next.getValue();
							}
							else if(attrName.equals("config"))
							{
								config = next.getValue();
							}
						}

						if(config != null && !config.trim().isEmpty())
						{
						}

						Product product = new Product(id, type, name, config);
						manufacturer.addProduct(product);
						productCount++;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		log.debug("Loaded {} manufacturers, {} products", manufacturersCount, productCount);

		return manufacturers;
	}
}
