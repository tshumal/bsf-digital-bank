package io.lingani.bsf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.lingani.bsf.model.enums.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id", nullable=false, updatable=false)
    private Long id;
    private String description;
    private BigDecimal amount;
    private BigDecimal runningBalance;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_number")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TransactionNumberSeq transactionNumber;


    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm")
    private Date transactionDate;

    @JsonIgnore
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    private TransactionType transactionType;

    /*
     * Constructor
     */
    public Transaction () {
        transactionNumber = new TransactionNumberSeq();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {


        this.amount = new BigDecimal(amount.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @return the transactionDate
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the type
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }


    /**
     * @return the runningBalance
     */
    public BigDecimal getRunningBalance() {
        return runningBalance;
    }

    /**
     * @param runningBalance the runningBalance to set
     */
    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = new BigDecimal(runningBalance.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @return the transactionNumber
     */
    public Long getTransactionNumber() {
        return transactionNumber.getId();
    }
}
