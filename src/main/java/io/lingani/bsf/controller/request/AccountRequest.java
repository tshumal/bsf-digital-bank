package io.lingani.bsf.controller.request;

import io.lingani.bsf.model.enums.AccountType;
import io.lingani.bsf.util.Messages;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class AccountRequest {

    @Size(min = 1, max = 40)
    @NotEmpty(message= Messages.ACCT_NAME_REQ)
    private String accountName;

    @NotNull(message=Messages.ACCT_OPEN_DEPOSIT_REQ)
    @Positive(message=Messages.ACCT_TRAN_AMT_POSITIVE)
    private BigDecimal openingDeposit;

    @NotEmpty (message=Messages.ACCT_TYPE_REQ)
    private AccountType accountType;

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getOpeningDeposit() {
        return openingDeposit;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setOpeningDeposit(BigDecimal openingDeposit) {
        this.openingDeposit = openingDeposit;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}