package hr.mlovrekov.controller.account;

import hr.mlovrekov.command.CreateAccountCommand;
import hr.mlovrekov.dto.AccountCreationResponseDto;
import hr.mlovrekov.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new service account for given accountId
     * @param command - command object containing request parameters
     * @return
     */
    @RequestMapping(path = "/account", method = RequestMethod.POST)
    public ResponseEntity<AccountCreationResponseDto> account(@Valid @RequestBody CreateAccountCommand command) {

        AccountCreationResponseDto response = accountService.create(command.getAccountId());

        if(response.isSuccess()) {
            //Status should probably be 201 - created
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

}
