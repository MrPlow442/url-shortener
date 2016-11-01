package hr.mlovrekov.dto;

public class ShortUrlDto {
    private final String shortUrl;

    public ShortUrlDto(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
