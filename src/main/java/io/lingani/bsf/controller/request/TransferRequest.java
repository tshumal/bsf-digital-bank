package io.lingani.bsf.controller.request;

import io.lingani.bsf.util.Messages;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransferRequest {
    
    @NotNull(message="To Account is required")
    private Long toAccountId;

    @Positive(message= Messages.ACCT_TRAN_AMT_POSITIVE)
    private BigDecimal amount;

    /**
     * @return the toAccountId
     */
    public Long getToAccountId() {
        return toAccountId;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
