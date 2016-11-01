package hr.mlovrekov.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateAccountCommand {
    @NotNull(message = "AccountId not provided")
    @Size(min = 1, message = "Invalid accountId provided")
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
