package hr.mlovrekov.command;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterCommand {
    @NotNull(message = "Url not provided")
    @Size(min = 3, message = "Invalid url provided")
    private String url;
    private int redirectType = 302;

    @AssertTrue(message = "Invalid redirect type provided")
    public boolean isValidRedirectType() {
        return redirectType == 301 || redirectType == 302;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }
}
