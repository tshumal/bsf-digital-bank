package io.lingani.bsf.controller;

import io.lingani.bsf.controller.request.AccountRequest;
import io.lingani.bsf.controller.request.TransferRequest;
import io.lingani.bsf.exception.RestNotAcceptableException;
import io.lingani.bsf.model.Account;
import io.lingani.bsf.model.Transaction;
import io.lingani.bsf.service.AccountService;
import io.lingani.bsf.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Validated
@RestController
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/api/v1/account")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/api/v1/account/{id}")
    public ResponseEntity<?> getAccount(@PathVariable("id") Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/api/v1/account")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest account) {

        Account newAccount = new Account();

        // if we meet the minimum balance requirement, then open account
        if (account.getOpeningDeposit().compareTo(new BigDecimal(Constants.ACCT_MIN_BALANCE)) >= 0) {

            newAccount.setName(account.getAccountName());
            newAccount.setOpeningBalance(account.getOpeningDeposit());
            newAccount.setAccountType(account.getAccountType());
            accountService.createNewAccount(newAccount);

        } else {
            throw new RestNotAcceptableException("The initial deposit ($"
                    + account.getOpeningDeposit()
                    + ") entered does not meet the minimum amount ($"
                    + Constants.ACCT_MIN_BALANCE
                    + ") required. Please enter a valid deposit amount.");
        }
        LOG.info("Account created with details: " + newAccount.toString());
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/api/v1/account/{id}/transfer")
    public ResponseEntity<?> transferFunds(@PathVariable("id") Long id,
                                           @RequestBody @Valid TransferRequest transfer){

        Account fromAccount = accountService.getAccountById(id);
        Account toAccount = accountService.getAccountById(transfer.getToAccountId());
        Transaction transaction = new Transaction();
        transaction.setAmount(transfer.getAmount());

        //accountService.transfer(fromAccount, toAccount, transaction);
        accountService.transfer(id, transfer);

        LOG.info("Funds of amount: " + transfer.getAmount() + " transferred from account :" + fromAccount.getAccountNumber() + " to account: " + toAccount.getAccountNumber());
        return ResponseEntity.ok(accountService.getLastTwoAccountTransactions(fromAccount));
    }
}