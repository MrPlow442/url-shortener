package hr.mlovrekov.dto;

public class AccountCreationResponseDto {
    private final boolean success;
    private final String description;
    private final String password;

    public AccountCreationResponseDto(boolean success, String description, String password) {
        this.success = success;
        this.description = description;
        this.password = password;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getDescription() {
        return description;
    }

    public String getPassword() {
        return password;
    }
}
