package hr.mlovrekov.service.security;

import hr.mlovrekov.TestUrlShortenerApplication;
import hr.mlovrekov.domain.Account;
import hr.mlovrekov.domain.Role;
import hr.mlovrekov.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestUrlShortenerApplication.class)
public class AccountUserDetailsServiceTest {

    @InjectMocks
    AccountUserDetailsService accountUserDetailsService;

    @Mock
    AccountRepository accountRepository;

    @Test
    public void test_when_loadUserByUsername_is_called_with_existing_username_then_UserDetails_instance_is_created_and_returned() {
        //given
        Account account = new Account();
        account.setAccountId("mlovrekov");
        account.setPassword("password");
        account.setRole(Role.USER);

        given(this.accountRepository.findByAccountId("mlovrekov")).willReturn(Optional.of(account));
        //when
        UserDetails userDetails = accountUserDetailsService.loadUserByUsername("mlovrekov");
        //then
        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), "mlovrekov");
        assertNotNull(userDetails.getPassword());
        assertEquals(userDetails.getAuthorities().size(), 1);
        assertEquals(userDetails.getAuthorities().iterator().next().getAuthority(), Role.USER.name());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void test_when_loadUserByUsername_is_called_with_non_existing_username_then_exception_is_thrown() {
        //given
        given(this.accountRepository.findByAccountId("unknown")).willReturn(Optional.empty());
        //when
        accountUserDetailsService.loadUserByUsername("unknown");
        //then
    }
}