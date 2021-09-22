package io.lingani.bsf.repository;


import io.lingani.bsf.model.Account;
import io.lingani.bsf.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	public List<Transaction> findAllByAccount (Account account);
	public Transaction findTopByAccountOrderByTransactionDateDesc (Account account);
	public List<Transaction> findTop2ByAccountOrderByTransactionDateDesc (Account account);
	
	public List<Transaction> findAllByAccountAndAmountGreaterThan (Account account, BigDecimal amount);
	public List<Transaction> findAllByAccountAndAmountLessThan (Account account, BigDecimal amount);
	
	public List<Transaction> findAllByAccountAndAmountGreaterThanAndTransactionDateAfter (Account account, BigDecimal amount, Date date);
	public List<Transaction> findAllByAccountAndAmountLessThanAndTransactionDateAfter (Account account, BigDecimal amount, Date date);

}
