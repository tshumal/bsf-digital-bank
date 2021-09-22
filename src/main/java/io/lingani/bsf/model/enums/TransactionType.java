package io.lingani.bsf.model.enums;



public enum TransactionType {

	DEBIT("DEBIT"),
	CREDIT("CREDIT"),
	EITHER("EITHER");

	// default constructor
	private final String typeName;


	TransactionType(final String typeName) {
		this.typeName = typeName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return typeName;
	}

}
