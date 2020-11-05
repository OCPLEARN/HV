package de.ocplearn.hv.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Static helper methods in SQL Daos
 * */
public class SQLUtils {
	
	/**
	 * Build sql String for given sql column list. Will create result table, 
	 * where column names are prefixed with table name.
	 *
	 * @param TABLE_NAME_PREFIX 
	 * @param List of column names to convert
	 * @param List of column names (startsWith()) to exclude from conversion
	 * */
	public static String createSQLString (String TABLE_NAME_PREFIX, List<String> tableColumnNames, List<String> excludedColumns) {
		
			Set<String> tmp = new HashSet<>( tableColumnNames );
			Iterator<String> it =  tmp.iterator();
			StringBuilder sbFull = new StringBuilder();
			colLoop: while ( it.hasNext() ) {
				
				String col = it.next();
				
				for ( String s : excludedColumns ) {
					if ( col.startsWith(s) ) {
						sbFull.append(col).append(", ");
						continue colLoop;
					};
				}			
				
				StringBuilder sb = new StringBuilder();
				// replace 
				// SELECT * 
				// by
				// SELECT TABLE_NAME_PREFIX.tableColumName AS 'TABLE_NAME_PREFIX.ColumName'

				sb
				.append(TABLE_NAME_PREFIX).append('.').append(col)
				.append(" AS '")
				.append(TABLE_NAME_PREFIX).append('.').append(col)
				.append("', ");
				sbFull.append(sb.toString());	
				
			}
			return sbFull.delete( sbFull.length() - 2, sbFull.length() ).toString();
			
	}
}