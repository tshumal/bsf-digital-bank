package io.lingani.bsf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.lingani.bsf.model.enums.AccountType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id", nullable=false, updatable=false)
    private Long id;
    private String name;

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_number")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AccountNumberSeq accountNumber;

    private BigDecimal currentBalance;
    private BigDecimal openingBalance;

    private AccountType accountType;

    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm")
    private Date dateOpened;

    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm")
    private Date dateClosed;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("transaction_date DESC")
    @JsonIgnore
    private List<Transaction> accountTransactionList;


    /*
     * Constructor
     */
    public Account () {
        accountNumber = new AccountNumberSeq();
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the accountNumber
     */
    public Long getAccountNumber() {
        return accountNumber.getId();
    }

    /**
     * @return the currentBalance
     */
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    /**
     * @param currentBalance the currentBalance to set
     */
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = new BigDecimal(currentBalance.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @return the openingBalance
     */
    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    /**
     * @param openingBalance the openingBalance to set
     */
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = new BigDecimal(openingBalance.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @return the accountType
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    /**
     * @return the dateOpened
     */
    public Date getDateOpened() {
        return dateOpened;
    }

    /**
     * @param dateOpened the dateOpened to set
     */
    public void setDateOpened(Date dateOpened) {
        this.dateOpened = dateOpened;
    }

    /**
     * @return the dateClosed
     */
    public Date getDateClosed() {
        return dateClosed;
    }

    /**
     * @param dateClosed the dateClosed to set
     */
    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    /**
     * @return the acountTransactionList
     */
    public List<Transaction> getAccountTransactionList() {
        return accountTransactionList;
    }

    /**
     * @param accountTransactionList the accountTransactionList to set
     */
    public void setAccountTransactionList(List<Transaction> accountTransactionList) {
        this.accountTransactionList = accountTransactionList;
    }

    public String toString() {

        String account = "\n\nAccount ***********************";

        account += "\nId:\t\t\t\t" 		+ this.getId();
        account += "\nName:\t\t\t" 		+ this.getName();
        account += "\nType:\t\t\t" 		+ this.getAccountType();
        account += "\nOpening:\t" 		+ this.getOpeningBalance();
        account += "\nCurrent:\t" 		+ this.getCurrentBalance();

        return account;

    }
}
