package hr.mlovrekov.service.url;

import hr.mlovrekov.domain.Url;
import hr.mlovrekov.dto.ShortUrlDto;
import hr.mlovrekov.dto.UrlDto;
import hr.mlovrekov.exception.EntityNotFoundException;
import hr.mlovrekov.repository.AccountRepository;
import hr.mlovrekov.repository.UrlRepository;
import hr.mlovrekov.service.url.token.TokenGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UrlShortenerService {

    private final TokenGeneratorService tokenGeneratorService;
    private final UrlRepository urlRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UrlShortenerService(TokenGeneratorService tokenGeneratorService, UrlRepository urlRepository, AccountRepository accountRepository) {
        this.tokenGeneratorService = tokenGeneratorService;
        this.urlRepository = urlRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Generates a short url token and binds it to a given long url
     * @param requestRoot
     * @param urlToRegister
     * @param accountId
     * @param redirectType
     * @return short url which is [request root url]/[token]
     */
    public ShortUrlDto register(String requestRoot, String urlToRegister, String accountId, Integer redirectType) {
        redirectType = redirectType == null ? 302 : redirectType;

        String token;
        do {
            token = tokenGeneratorService.generate();
        } while (urlRepository.findByToken(token).isPresent());

        Url url = new Url();
        url.setOriginalUrl(urlToRegister);
        url.setToken(token);
        url.setRedirectType(redirectType);
        url.setAccount(accountRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account with accountId '" + accountId + "' does not exist"))
        );

        urlRepository.save(url);

        return new ShortUrlDto(String.format("%s/%s", requestRoot, token));
    }

    public Optional<UrlDto> getByToken(String token) {
        return urlRepository
                .findByToken(token)
                .map(t -> new UrlDto(t.getOriginalUrl(), t.getToken(), t.getRedirectType()));

    }

    public void incrementHitCount(String token) {
        urlRepository
                .findByToken(token)
                .ifPresent(url -> {
                    url.setHitCount(url.getHitCount() + 1);
                    urlRepository.save(url);
                });
    }

    /**
     * Retrieves hit statistics which consist of long url and hit count for that url
     * @param accountId
     * @return
     */
    public Map<String, Long> getStatisticsByAccountId(String accountId) {

        if(!accountRepository.existsByAccountId(accountId)) {
            throw new EntityNotFoundException("Account with accountId '" + accountId + "' does not exist");
        }

        return urlRepository.findByAccountAccountId(accountId)
                .stream()
                .collect(Collectors.toMap(Url::getOriginalUrl, Url::getHitCount));
    }
}
