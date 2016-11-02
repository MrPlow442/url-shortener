package hr.mlovrekov.controller.url;

import hr.mlovrekov.command.RegisterCommand;
import hr.mlovrekov.dto.ShortUrlDto;
import hr.mlovrekov.dto.UrlDto;
import hr.mlovrekov.service.url.UrlShortenerService;
import hr.mlovrekov.util.url.URLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    /**
     * Generates and registers a short url for a given long url
     * @param command - command object containing request parameters
     * @param principal
     * @param httpRequest
     * @return
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ShortUrlDto register(@Valid @RequestBody RegisterCommand command, Principal principal, HttpServletRequest httpRequest) {
        return urlShortenerService.register(URLUtils.extractRootFromURL(httpRequest.getRequestURL().toString()) + httpRequest.getContextPath(),
                command.getUrl(),
                principal.getName(),
                command.getRedirectType());
    }

    /**
     * Redirects user to long url registered to given token. If given token does not exist, not found status is returned
     * @param token
     * @param response
     * @throws IOException
     */
    @RequestMapping(path = "/{token:[A-Za-z0-9]{6,12}}", method = RequestMethod.GET)
    public void redirect(@PathVariable String token, HttpServletResponse response) throws IOException {
        Optional<UrlDto> urlOptional = urlShortenerService.getByToken(token);

        if(!urlOptional.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        UrlDto urlDto = urlOptional.get();
        urlShortenerService.incrementHitCount(urlDto.getToken());
        response.setHeader("Location", urlDto.getOriginalUrl());
        response.setStatus(urlDto.getRedirectType());
    }

    /**
     * Returns hit statistics for each url registered by provided account
     * @param accountId
     * @return
     */
    @RequestMapping(path = "/statistic/{accountId}", method = RequestMethod.GET)
    public Map<String, Long> getStatistic(@PathVariable String accountId) {
        return urlShortenerService.getStatisticsByAccountId(accountId);
    }

}
