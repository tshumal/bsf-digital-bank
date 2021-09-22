package io.lingani.bsf;

import io.lingani.bsf.controller.AccountController;
import io.lingani.bsf.controller.request.TransferRequest;
import io.lingani.bsf.model.Account;
import io.lingani.bsf.model.Transaction;
import io.lingani.bsf.model.enums.AccountType;
import io.lingani.bsf.repository.AccountRepository;
import io.lingani.bsf.repository.TransactionRepository;
import io.lingani.bsf.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountServiceConcurrentTests {

    private static final Logger LOG = LoggerFactory.getLogger(AccountServiceConcurrentTests.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldTransferFundsConcurrently_withOptimisticLockingHandling() throws InterruptedException {
        //given
        final Account fromAccount = new Account();
        fromAccount.setName("FromAccount");
        fromAccount.setAccountType(AccountType.CURRENT);
        fromAccount.setOpeningBalance(new BigDecimal(100.00));
        fromAccount.setCurrentBalance(new BigDecimal(100.00));

        final Account toAccount1 = new Account();
        toAccount1.setName("ToAccountOne");
        toAccount1.setAccountType(AccountType.CURRENT);
        toAccount1.setOpeningBalance(new BigDecimal(50.00));
        toAccount1.setCurrentBalance(new BigDecimal(50.00));

        final Account toAccount2 = new Account();
        toAccount2.setName("ToAccountTwo");
        toAccount2.setAccountType(AccountType.CURRENT);
        toAccount2.setOpeningBalance(new BigDecimal(50.00));
        toAccount2.setCurrentBalance(new BigDecimal(50.00));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount1);
        accountRepository.save(toAccount2);

        List<Account> accountList = accountRepository.findAll();
        LOG.info(accountList.toString());

        assertEquals(3, accountList.size());

        TransferRequest transferRequest1 = new TransferRequest();
        transferRequest1.setToAccountId(toAccount1.getId());
        transferRequest1.setAmount(new BigDecimal(20));

        TransferRequest transferRequest2 = new TransferRequest();
        transferRequest2.setToAccountId(toAccount2.getId());
        transferRequest2.setAmount(new BigDecimal(10));

//        TransferRequest transferRequest3 = new TransferRequest();
//        transferRequest3.setToAccountId(toAccount1.getId());
//        transferRequest3.setAmount(new BigDecimal(10));

        final List<TransferRequest> transferRequests = Arrays.asList(transferRequest1, transferRequest2);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(transferRequests.size());

        for (final TransferRequest transferRequest : transferRequests) {
            executor.execute(() -> accountService.transfer(fromAccount.getId(), transferRequest));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // then
        final Account postTransactionFromAccount   = accountRepository.findById(fromAccount.getId()).get();
        final Account postTransactionToAccount1   = accountRepository.findById(toAccount1.getId()).get();

        List<Account> postAccountList = accountRepository.findAll();
        LOG.info(postAccountList.toString());

        assertAll(
                () -> assertEquals(80.00, postTransactionFromAccount.getCurrentBalance().doubleValue()),
                () -> assertEquals(70.00, postTransactionToAccount1.getCurrentBalance().doubleValue())
        );
    }
}
