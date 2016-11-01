package hr.mlovrekov.repository;

import hr.mlovrekov.TestUrlShortenerApplication;
import hr.mlovrekov.domain.Account;
import hr.mlovrekov.domain.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestUrlShortenerApplication.class)
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void test_when_findByAccountId_is_called_with_existing_accountId_then_account_is_returned() {
        //when
        Optional<Account> account = accountRepository.findByAccountId("mlovrekov");
        //then
        assertTrue(account.isPresent());
        Account actual = account.get();
        assertEquals(actual.getId().longValue(), 1L);
        assertEquals(actual.getAccountId(), "mlovrekov");
        assertEquals(actual.getPassword(), "$2a$10$4wUDRII5yHHNwY5BB4/Ct.GEIB1jXYoFYoBiRD12Zcy2PZBRxfMNy");
        assertEquals(actual.getRole(), Role.USER);
    }

    @Test
    public void test_when_findByAccountId_is_called_with_non_existing_accountId_then_nothing_is_returned() {
        //when
        Optional<Account> account = accountRepository.findByAccountId(null);
        //then
        assertFalse(account.isPresent());
    }

    @Test
    public void test_when_existsByAccountId_is_called_with_existing_accountId_then_true_is_returned() {
        //when
        boolean existsByAccountId = accountRepository.existsByAccountId("mlovrekov");
        //then
        assertTrue(existsByAccountId);
    }

    @Test
    public void test_when_existsByAccountId_is_called_with_non_existing_accountId_then_false_is_returned() {
        //when
        boolean existsByAccountId = accountRepository.existsByAccountId(null);
        //then
        assertFalse(existsByAccountId);
    }

}