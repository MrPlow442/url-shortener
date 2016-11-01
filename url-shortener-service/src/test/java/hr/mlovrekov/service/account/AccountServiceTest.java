package hr.mlovrekov.service.account;

import hr.mlovrekov.TestUrlShortenerApplication;
import hr.mlovrekov.domain.Account;
import hr.mlovrekov.dto.AccountCreationResponseDto;
import hr.mlovrekov.dto.AccountDto;
import hr.mlovrekov.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestUrlShortenerApplication.class)
public class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Test
    public void test_when_getByAccountId_is_called_with_existing_accountId_then_accountDto_is_returned() {
        //given
        Account account = new Account();
        account.setAccountId("mlovrekov");

        given(this.accountRepository.findByAccountId("mlovrekov")).willReturn(Optional.of(account));
        //when
        Optional<AccountDto> accountDto = accountService.getByAccountId("mlovrekov");
        //then
        assertTrue(accountDto.isPresent());
        AccountDto actual = accountDto.get();
        assertEquals(account.getAccountId(), "mlovrekov");
    }

    @Test
    public void test_when_getByAccountId_is_called_with_non_existing_accountId_then_nothing_is_returned() {
        //given
        given(this.accountRepository.findByAccountId("unknown")).willReturn(Optional.empty());
        //when
        Optional<AccountDto> accountDto = accountService.getByAccountId("unknown");
        //then
        assertFalse(accountDto.isPresent());
    }

    @Test
    public void test_when_create_is_called_with_non_existing_accountId_then_a_new_account_is_created_and_success_accountCreationResponseDto_is_returned() {
        //given
        given(this.accountRepository.existsByAccountId("mlovrekov")).willReturn(false);
        given(this.accountRepository.save(any(Account.class))).willReturn(new Account());
        //when
        AccountCreationResponseDto accountCreationResponseDto = accountService.create("mlovrekov");
        //then
        then(accountRepository).should(times(1)).existsByAccountId("mlovrekov");
        then(accountRepository).should(times(1)).save(any(Account.class));
        assertNotNull(accountCreationResponseDto);
        assertEquals(accountCreationResponseDto.isSuccess(), true);
        assertEquals(accountCreationResponseDto.getDescription(), "Your account is opened");
        assertNotNull(accountCreationResponseDto.getPassword());
        assertEquals(accountCreationResponseDto.getPassword().length(), 8);
        assertTrue(accountCreationResponseDto.getPassword().matches("[A-Za-z0-9]{8}"));
    }

    @Test
    public void test_when_create_is_called_with_existing_accountId_then_failure_accountCreationResponseDto_is_returned() {
        //given
        given(this.accountRepository.existsByAccountId("mlovrekov")).willReturn(true);
        //when
        AccountCreationResponseDto accountCreationResponseDto = accountService.create("mlovrekov");
        //then
        then(accountRepository).should(times(1)).existsByAccountId("mlovrekov");
        assertNotNull(accountCreationResponseDto);
        assertEquals(accountCreationResponseDto.isSuccess(), false);
        assertEquals(accountCreationResponseDto.getDescription(), "Account with accountId 'mlovrekov' already exists");
        assertNull(accountCreationResponseDto.getPassword());
    }

}