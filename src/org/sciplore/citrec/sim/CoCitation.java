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

package org.sciplore.citrec.sim;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.sciplore.citrec.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calculate Co-Citation similarities
 * 
 * @author Mario Lipinski <a href="mailto:lipinski@sciplore.org">lipinski@sciplore.org</a>
 */
public class CoCitation {
	/**
	 * Calculate Co-Citation similarities
	 *
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		Logger logger = LoggerFactory.getLogger(CoCitation.class);
		Properties p = Helper.getProperties();

		
		// initialize database connections begin
		Class.forName(p.getProperty("db.driver"));
		Connection db = DriverManager.getConnection(p.getProperty("db.url"), p.getProperty("db.user"), p.getProperty("db.password"));
		// initialize database connections end
				
		Statement stmt = db.createStatement();
		// Statement to calculate all Co-Citations for one document
		PreparedStatement stmtcocit = db.prepareStatement("INSERT INTO sim_cocit " +
				"SELECT d1.pmcId, d2.pmcId, COUNT(DISTINCT r2.document) AS strength " +
				"FROM document d1 " +
				"LEFT JOIN refdoc_id ri1 " +
				"  ON ri1.type='pm' " +
				"  AND ri1.auxId=CAST(d1.pmId AS CHAR) " +
				"LEFT JOIN reference r1 " +
				"  ON r1.refDoc = ri1.docId " +
				"LEFT JOIN reference r2 " +
				"  ON r1.document=r2.document " +
				"  AND r1.refDoc != r2.refDoc " +
				"LEFT JOIN refdoc_id ri2 " +
				"  ON ri2.docId=r2.refDoc " +
				"  AND ri2.type='pm' " +
				"LEFT JOIN document d2 " +
				"  ON d2.pmId=ri2.auxId " +
				"WHERE d1.pmcId = ? " +
				"  AND d2.pmcId IS NOT NULL " +
				"GROUP BY r2.refDoc");
		
		stmt.execute("DROP TABLE IF EXISTS simi_cocit");
		stmt.execute("CREATE TABLE IF NOT EXISTS `sim_cocit` (" +
				"`document1` int(11) NOT NULL," +
				"`document2` int(11) NOT NULL," +
				"`value` double NOT NULL," +
				"KEY `document1` (`document1`)," +
				"KEY `document2` (`document2`)" +
				") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
		stmt.execute("ALTER TABLE `sim_cocit` DISABLE KEYS");
		ResultSet resdoc = stmt.executeQuery("SELECT pmcId FROM document");
		
		int cnt = 0;
		
	    while(resdoc.next()) { // iterate over all documents
	    	if(++cnt % 1000 == 0) {
	    		logger.info("{} documents processed.", cnt);
	    	}
			stmtcocit.setInt(1, resdoc.getInt("pmcId"));
			stmtcocit.executeUpdate(); // calculate all Co-Citations for one document and store to database
	    }
	    resdoc.close();
		stmt.execute("ALTER TABLE `sim_cocit` ENABLE KEYS");
	}
}
