package de.ocplearn.hv.dto;

public class TransactionDto {

	private int id;

	public TransactionDto(int id) {
		super();
		this.id = id;
	}

	public TransactionDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}
