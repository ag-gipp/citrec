/*
	CITREC - Evaluation Framework
    Copyright (C) 2015 SciPlore <team@sciplore.org>
    Copyright (C) 2015 Mario Lipinski <lipinski@sciplore.org>

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

/**
 * 
 */
package org.sciplore.citrec;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Helper class that initializes properties according to a configuration file.
 *
 * @author Mario Lipinski <a href="mailto:lipinski@sciplore.org">lipinski@sciplore.org</a>
 */
public class Helper {
	private static Properties p;
	
	/**
	 * Get the properties for CITREC. 
	 *
	 * @return the properties
	 * @throws IOException on I/O error
	 */
	public static Properties getProperties() throws IOException {
		if (p == null) {
			// load Properties begin
			p = new Properties();
	
			p.load(Helper.class.getClassLoader().getResourceAsStream("CITREC.default.properties"));
			
			File propFile = new File(new File(Helper.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + File.separator + "CITREC.properties");
			if (propFile.canRead()) {
				p = new Properties(p);
				p.load(new FileReader(propFile));
			} else {
				propFile = new File(new File(Helper.class.getProtectionDomain().getCodeSource().getLocation().getPath()) + File.separator + "CITREC.properties");
				if (propFile.canRead()) {
					p = new Properties(p);
					p.load(new FileReader(propFile));
				}			
			}
			// load Properties end
		}
		
		return p;
	}
}
