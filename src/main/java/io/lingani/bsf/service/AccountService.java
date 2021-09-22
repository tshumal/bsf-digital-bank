package io.lingani.bsf.service;

import io.lingani.bsf.controller.request.TransferRequest;
import io.lingani.bsf.exception.AccountNotFoundException;
import io.lingani.bsf.exception.RestBadRequestException;
import io.lingani.bsf.model.Account;
import io.lingani.bsf.model.Transaction;
import io.lingani.bsf.model.enums.TransactionType;
import io.lingani.bsf.repository.AccountRepository;
import io.lingani.bsf.repository.TransactionRepository;
import io.lingani.bsf.util.Constants;
import io.lingani.bsf.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);
	
	private final AccountRepository accountRepository;
	
	private final TransactionRepository accountTransactionRepository;

	@Autowired
	public AccountService(AccountRepository accountRepository, TransactionRepository accountTransactionRepository) {
		this.accountRepository = accountRepository;
		this.accountTransactionRepository = accountTransactionRepository;
	}

	/*
	 * Create a new account
	 */
	public Account createNewAccount (Account newAccount) {
				
		// Set Account Details
		newAccount.setCurrentBalance(newAccount.getOpeningBalance());

		// Check if Account opening date has been set, if not set it
		if (newAccount.getDateOpened() == null) {
			newAccount.setDateOpened(new Date());
		}
		
		// Create Account
		accountRepository.save(newAccount);

		// Set Initial Transaction
		Transaction accountTransaction = new Transaction();
		accountTransaction.setAmount(newAccount.getOpeningBalance());
		accountTransaction.setDescription(Constants.ACCT_TRAN_TYPE_DEPOSIT_CODE);
		accountTransaction.setTransactionDate(newAccount.getDateOpened());
		accountTransaction.setTransactionType(TransactionType.CREDIT);
		creditTransaction (newAccount, accountTransaction);
		
		LOG.debug("Create Account: New Account Created.");
		return newAccount;
	}

	/*
	 * Transfer amount between two accounts
	 *
	 * Accounts should be full objects. With that said, the objects are fetched to make sure.
	 *
	 * Transaction can be a partial object but must contain the transaction amount.
	 */
	public void transfer(Long id, TransferRequest transfer) {

		Account fromAccount = this.getAccountById(id);
		Account toAccount = this.getAccountById(transfer.getToAccountId());
		Transaction accountTransaction = new Transaction();
		accountTransaction.setAmount(transfer.getAmount());

		LOG.debug("Transfer Between Accounts:");

		// From Transaction
		fromAccount = this.getAccountById(fromAccount.getId());
		Transaction fromAt = new Transaction();
		fromAt.setAmount(accountTransaction.getAmount());
		fromAt.setTransactionDate(accountTransaction.getTransactionDate());
		fromAt.setDescription("Transfer to Account (" + toAccount.getAccountNumber() + ")");
		fromAt.setTransactionType(TransactionType.DEBIT);
		debitTransaction(fromAccount, fromAt);

		// To Transaction
		toAccount = this.getAccountById(toAccount.getId());
		Transaction toAt = new Transaction();
		toAt.setAmount(accountTransaction.getAmount());
		toAt.setTransactionDate(accountTransaction.getTransactionDate());
		toAt.setDescription("Transfer from Account (" + fromAccount.getAccountNumber() + ")");
		toAt.setTransactionType(TransactionType.CREDIT);
		creditTransaction(toAccount, toAt);

		LOG.debug("Transfer Between Accounts: Accounts Updated.");
	}

	/*
	 * Add a new transaction that will apply a credit to the account.
	 * 
	 * The Account passed in is expected to be a full Account object. With that said,
	 * the account object is fetched to make sure.
	 * 
	 * The AccountTransaction is expect to be a partial object. However,
	 * 		the Transaction should have the following values already defined 
	 * 		within the object.
	 * 
	 * 		- Amount of transaction
	 * 		- Transaction Type
	 * 		- Transaction Description
	 */
	public void creditTransaction(Account account, Transaction accountTransaction) {
		
		LOG.debug("Credit Transaction to Account:");
		
		account = this.getAccountById(account.getId());
		BigDecimal balance = account.getCurrentBalance();
		List<Transaction> atl = account.getAccountTransactionList();
		
		// if the list is null, then its the first transaction
		if (atl == null) {
			atl = new ArrayList<>();
		}
		else { // else we are adding another transaction to the list so calculate the new update
			balance = balance.add(accountTransaction.getAmount());
			account.setCurrentBalance(balance);
		}
		
		LOG.debug("Credit Transaction to Account: Current Number of Transactions: ->" + atl.size());
		
		// Check if the date was already set, if not set to current date time.
		if (accountTransaction.getTransactionDate() == null) {
			accountTransaction.setTransactionDate(new Date());
		}
		
		accountTransaction.setRunningBalance(balance);
		accountTransaction.setAccount(account);
		atl.add(accountTransaction);
		account.setAccountTransactionList(atl);
		
		// Update Account
		accountRepository.save(account);
		
		LOG.debug("Credit Transaction to Account: New Number of Transactions: ->" + atl.size());
		LOG.debug("Credit Transaction to Account: Account Updated.");
		
	}
	
	/*
	 * Add a new transaction that will apply a debit to the account.
	 * 
	 * The Account passed in is expected to be a full Account object. With that said
	 * the account object is fetched to make sure.
	 * 
	 * The Transaction is expect to be a partial object. However,
	 * 		the Transaction should have the following values already defined 
	 * 		within the object.
	 * 
	 * 		- Amount of transaction
	 * 		- Transaction Type
	 * 		- Transaction Description
	 */
	public void debitTransaction(Account account, Transaction accountTransaction) {
		
		LOG.debug("Debit Transaction from Account:");
		
		account = this.getAccountById(account.getId());
		
		List<Transaction> atl = account.getAccountTransactionList();
		
		BigDecimal balance = account.getCurrentBalance();
		BigDecimal amount = accountTransaction.getAmount();
		
		// Convert amount to a negative number since it is a withdraw
		BigDecimal negOne = new BigDecimal(-1);
		amount = amount.multiply(negOne);	
		balance = balance.add(amount);
		account.setCurrentBalance(balance);
		
		// Check if the date was already set, if not set to current date time.
		if (accountTransaction.getTransactionDate() == null) {
			accountTransaction.setTransactionDate(new Date());
		}
		
		accountTransaction.setRunningBalance(balance);
		accountTransaction.setAmount(amount);
		accountTransaction.setAccount(account);
		atl.add(accountTransaction);
		
		account.setAccountTransactionList(atl);
		
		// Update Account
		accountRepository.save(account);
		
		LOG.debug("Debit Transaction from Account: Account Updated.");
	}
	

	
	/*
	 * Get Account object by Id
	 */
	public Account getAccountById(Long id) {

		if (id < 0) {
			throw new RestBadRequestException(Messages.INVALID_OBJECT_ID);
		}

		Optional<Account> act = accountRepository.findById(id);

		if (!act.isPresent()) {
			throw new AccountNotFoundException(Messages.ACCT_NOT_FOUND + id);
		}
		return act.orElse(null);
	}
	
	public List<Account> getAllAccounts () {
		return accountRepository.findAll();
	}

	public List<Transaction> getLastTwoAccountTransactions (Account account) {
		return accountTransactionRepository.findTop2ByAccountOrderByTransactionDateDesc(account);
	}
}
