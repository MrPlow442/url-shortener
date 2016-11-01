package hr.mlovrekov.service.security;

import hr.mlovrekov.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository
                .findByAccountId(username)
                .map(a -> new User(a.getAccountId(), a.getPassword(), true, true, true, true, AuthorityUtils.createAuthorityList(a.getRole().name())))
                .orElseThrow(() -> new UsernameNotFoundException("Account with accountId '" + username + "' does not exist"));
    }

}
