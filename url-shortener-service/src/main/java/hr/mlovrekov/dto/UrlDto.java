package hr.mlovrekov.dto;

public class UrlDto {
    private final String originalUrl;
    private final String token;
    private final int redirectType;

    public UrlDto(String originalUrl, String token, int redirectType) {
        this.originalUrl = originalUrl;
        this.token = token;
        this.redirectType = redirectType;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getToken() {
        return token;
    }

    public int getRedirectType() {
        return redirectType;
    }
}
