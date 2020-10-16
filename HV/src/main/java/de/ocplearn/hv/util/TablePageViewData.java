/**
 *  https://dev.mysql.com/doc/refman/8.0/en/select.html
 *  
 *  
 */
package de.ocplearn.hv.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides data to limit the view returned from a dao finder method
 *
 */
@Getter
@Setter
public class TablePageViewData {
	
	/* offset of the first row to return */
	private int offset;
	/* maximum numbers or rows to return */
	private int rowCount;
	/* specifies the sort column */
	private String orderBy;
	/* specifies the sort direction */
	private String orderByDirection;
	
	/**
	 * 
	 * */
	private TablePageViewData() {}
	
	/**
	 * @param offset
	 * @param rowCount
	 * @param orderBy
	 * @param orderByDirection
	 */
	private TablePageViewData(int offset, int rowCount, String orderBy, String orderByDirection) {
		super();
		this.offset = offset;
		this.rowCount = rowCount;
		this.orderBy = orderBy;
		this.orderByDirection = orderByDirection;
	}	
	
	
	
}
