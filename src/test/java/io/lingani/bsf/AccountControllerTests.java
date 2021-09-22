package io.lingani.bsf;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lingani.bsf.controller.request.AccountRequest;
import io.lingani.bsf.controller.request.TransferRequest;
import io.lingani.bsf.model.Account;
import io.lingani.bsf.model.Transaction;
import io.lingani.bsf.model.enums.AccountType;
import io.lingani.bsf.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class AccountControllerTests {

    private static final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetAccountById() throws Exception {

        //given
        final Account account = new Account();
        account.setName("AccountOne");
        account.setAccountType(AccountType.CURRENT);
        account.setOpeningBalance(new BigDecimal(100));
        account.setId(1L);

        given(accountService.getAccountById(account.getId())).willReturn(account);

        //get account
        mockMvc.perform(get("/api/v1/account/" + account.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllAccounts() throws Exception {

        //given
        final Account account = new Account();
        account.setName("AccountOne");
        account.setAccountType(AccountType.CURRENT);
        account.setOpeningBalance(new BigDecimal(30));
        account.setCurrentBalance(new BigDecimal(30));
        account.setId(1L);
        List<Account> accounts = Arrays.asList(account);

        given(accountService.getAllAccounts()).willReturn(accounts);

        //get accounts
        mockMvc.perform(get("/api/v1/account"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"AccountOne\",\"accountNumber\":null,\"currentBalance\":30.00,\"openingBalance\":30.00,\"accountType\":\"CURRENT\",\"dateOpened\":null,\"dateClosed\":null}]"));
    }

    @Test
    public void shouldCreateNewAccount() throws Exception {

        final AccountRequest account = new AccountRequest();
        account.setAccountName("AccountOne");
        account.setAccountType(AccountType.CURRENT);
        account.setOpeningDeposit(new BigDecimal(100.00));

        String json = mapper.writeValueAsString(account);

        mockMvc.perform(post("/api/v1/account").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name").value(account.getAccountName()))
                .andExpect(jsonPath("$.openingBalance").value(account.getOpeningDeposit().longValue()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailOnValidationTryingToCreateNewAccountWithNegativeOpeningDeposit() throws Exception {

        final AccountRequest account = new AccountRequest();
        account.setAccountName("test");
        account.setOpeningDeposit(new BigDecimal(-100.00));

        String json = mapper.writeValueAsString(account);

        mockMvc.perform(post("/api/v1/account").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotAcceptable());
    }

//    @Test
//    public void shouldFailOnValidationTryingToGetAccountNotExist() throws Exception {
//
//        mockMvc.perform(get("/api/v1/account/" + 6L))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isBadRequest());
//    }

    @Test
    public void shouldTransferFunds() throws Exception {

        //given
        final Account fromAccount = new Account();
        fromAccount.setName("AccountOne");
        fromAccount.setAccountType(AccountType.CURRENT);
        fromAccount.setOpeningBalance(new BigDecimal(100.00));
        fromAccount.setId(1L);

        final Account toAccount = new Account();
        toAccount.setName("AccountTwo");
        toAccount.setAccountType(AccountType.CURRENT);
        toAccount.setOpeningBalance(new BigDecimal(50.00));
        toAccount.setId(2L);

        given(accountService.getAccountById(fromAccount.getId())).willReturn(fromAccount);
        given(accountService.getAccountById(toAccount.getId())).willReturn(toAccount);

        Assertions.assertNotNull(toAccount, "The saved account should not be null");

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setToAccountId(toAccount.getId());
        transferRequest.setAmount(new BigDecimal(20));

        Transaction transaction = new Transaction();
        transaction.setAmount(transferRequest.getAmount());

        List<Transaction> transactions = Arrays.asList(transaction);
        given(accountService.getLastTwoAccountTransactions(fromAccount)).willReturn(transactions);

        String json = mapper.writeValueAsString(transferRequest);

        //transfer
        mockMvc.perform(post("/api/v1/account/" + fromAccount.getId() + "/transfer").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.[0].amount").value(20.00))
                .andExpect(status().isOk());
    }
}
