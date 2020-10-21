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
	public TablePageViewData() {}
	
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

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @param rowCount the rowCount to set
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the orderByDirection
	 */
	public String getOrderByDirection() {
		return orderByDirection;
	}

	/**
	 * @param orderByDirection the orderByDirection to set
	 */
	public void setOrderByDirection(String orderByDirection) {
		this.orderByDirection = orderByDirection;
	}	
	
	
	
}
