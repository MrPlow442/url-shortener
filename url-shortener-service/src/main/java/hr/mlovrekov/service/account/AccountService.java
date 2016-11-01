package hr.mlovrekov.service.account;

import hr.mlovrekov.domain.Account;
import hr.mlovrekov.dto.AccountCreationResponseDto;
import hr.mlovrekov.dto.AccountDto;
import hr.mlovrekov.repository.AccountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<AccountDto> getByAccountId(String accountId) {
        return accountRepository.findByAccountId(accountId)
                .map(a -> new AccountDto(a.getAccountId()));
    }

    public AccountCreationResponseDto create(String accountId) {
        if(accountRepository.existsByAccountId(accountId)) {
            return new AccountCreationResponseDto(false, "Account with accountId '" + accountId + "' already exists", null);
        }

        String password = RandomStringUtils.random(8, true, true);

        Account account = new Account();
        account.setAccountId(accountId);
        account.setPassword(password);

        accountRepository.save(account);
        return new AccountCreationResponseDto(true, "Your account is opened", password);
    }

}
