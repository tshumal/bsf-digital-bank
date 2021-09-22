package io.lingani.bsf.util;


import io.lingani.bsf.model.enums.TransactionType;

public class Messages {

	// Common
	public static final String INVALID_OBJECT_ID = "Invalid request format. Object Id should be a positive integer.";
	public static final String OBJECT_NOT_FOUND = "Object Not Found. Unable to locate object with id ";


	// Account
	public static final String ACCT_NOT_FOUND = "Account Not Found. Unable to locate account with id ";

	public static final String ACCT_NAME_REQ = "Account Name is required.";
	public static final String ACCT_TYPE_REQ = "Account Type Code is required.";
	public static final String ACCT_TYPE_FORMAT = "Account Type Code must either be '"
			+ Constants.ACCT_CHK_STD_CODE + "' or '"
			+ Constants.ACCT_CHK_INT_CODE + "' for checking or '"
			+ Constants.ACCT_SAV_STD_CODE + "' or '"
			+ Constants.ACCT_SAV_MMA_CODE + "' for savings.";

	public static final String ACCT_OPEN_DEPOSIT_REQ = "Opening Deposit is required.";
	public static final String ACCT_TRAN_AMT_POSITIVE = "Transaction amount must be a positive number.";
	public static final String ACCT_TRAN_DESC_REQ = "Transaction Descrption is required.";
	public static final String ACCT_TRAN_TYPE_CODE_REQ = "Transaction Type Code is required.";
	public static final String ACCT_TRAN_AMT_REQ = "Transaction Amount is required.";
	public static final String ACCT_TRAN_TYPE_FORMAT = "Transaction Type must be one of "
			+ Constants.ACCT_TRAN_TYPE_ATM_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_CHARGE_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_CHECK_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_CHECK_FEE_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_DEBIT_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_DEPOSIT_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_DIRECT_DEP_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_DIV_CREDIT_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_EFT_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_FEE_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_INT_INCOME_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_LATE_FEE_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_OVERDRAFT_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_OVERDRAFT_FEE_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_PAYMENT_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_POS_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_REFUND_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_WITHDRAWL_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_XFER_CODE + ", "
			+ Constants.ACCT_TRAN_TYPE_XFER_FEE_CODE;

	public static final String ACCT_TRAN_ACTION = "Transaction Action must either be '"
			+ TransactionType.CREDIT + "' or '"
			+ TransactionType.CREDIT + "' and is required when the "
			+ "Transaction Type Category is '"
			+ TransactionType.EITHER + "'";
}