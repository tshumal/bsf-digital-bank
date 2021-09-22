package io.lingani.bsf.model.enums;



public enum AccountType {

	SAVINGS("SAVINGS"),
	CURRENT("CURRENT");

	// default constructor
	private final String typeName;


	AccountType(final String typeName) {
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
