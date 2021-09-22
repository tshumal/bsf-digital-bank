package io.lingani.bsf;

import io.lingani.bsf.model.Account;
import io.lingani.bsf.model.enums.AccountType;
import io.lingani.bsf.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void whenFindByAccountIdThenReturnAccount() {
        // given
        final Account account = new Account();
        account.setName("AccountOne");
        account.setAccountType(AccountType.CURRENT);
        account.setOpeningBalance(new BigDecimal(100));
        entityManager.persist(account);
        entityManager.flush();

        // when
        Optional<Account> found = accountRepository.findById(account.getId());

        // then
        assertThat(found.get().getName())
                .isEqualTo(account.getName());
    }
}
