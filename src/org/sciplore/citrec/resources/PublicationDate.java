/*
	CITREC - Evaluation Framework
    Copyright (C) 2015 SciPlore <team@sciplore.org>
    Copyright (C) 2015 Norman Meuschke <meuschke@sciplore.org>

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

package org.sciplore.citrec.resources;

/**
 * Resource class for publication dates.
 * 
 * @author Norman Meuschke <a href="mailto:meuschke@sciplore.org">meuschke@sciplore.org</a>
 */

public class PublicationDate implements Comparable<Object> {
	public int pubMonth;
	public int pubYear;
	
	public PublicationDate(int y, int m) {
		this.pubYear = y;
		this.pubMonth = m;
	}

	public int compareTo(Object comp) {
		if (comp.getClass().equals(this.getClass())) {
			if (this.pubYear > (((PublicationDate) comp).pubYear))
				return 1;
			else {
				if (this.pubYear < (((PublicationDate) comp).pubYear))
					return -1;
				else {
					if(this.pubMonth > (((PublicationDate) comp).pubMonth))
						return 1;
					else return -1;
				}
			}
		}
		else return -1;
	}
}
