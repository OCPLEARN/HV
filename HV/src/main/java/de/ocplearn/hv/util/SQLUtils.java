package de.ocplearn.hv.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SQLUtils {
	
	
	public static String createSQLString (String TABLE_NAME_PREFIX, List<String> tableColumnNames) {
		
			Set<String> tmp = new HashSet<>( tableColumnNames );
			Iterator<String> it =  tmp.iterator();
			StringBuilder sbFull = new StringBuilder();
			while ( it.hasNext() ) {
				String col = it.next();
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