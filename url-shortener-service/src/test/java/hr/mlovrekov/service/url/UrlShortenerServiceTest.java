package hr.mlovrekov.service.url;

import hr.mlovrekov.TestUrlShortenerApplication;
import hr.mlovrekov.domain.Account;
import hr.mlovrekov.domain.Url;
import hr.mlovrekov.dto.ShortUrlDto;
import hr.mlovrekov.dto.UrlDto;
import hr.mlovrekov.exception.EntityNotFoundException;
import hr.mlovrekov.repository.AccountRepository;
import hr.mlovrekov.repository.UrlRepository;
import hr.mlovrekov.service.url.token.TokenGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestUrlShortenerApplication.class)
public class UrlShortenerServiceTest {

    @InjectMocks
    UrlShortenerService urlShortenerService;

    @Mock
    TokenGeneratorService tokenGeneratorService;

    @Mock
    UrlRepository urlRepository;

    @Mock
    AccountRepository accountRepository;

    @Test
    public void test_when_register_is_called_with_valid_accountId_and_non_existent_token_is_generated_then_a_ShortUrlDto_is_returned_with_valid_shortUrl() {
        //given
        given(this.tokenGeneratorService.generate()).willReturn("ABCDEF");
        given(this.urlRepository.findByToken("ABCDEF")).willReturn(Optional.empty());
        given(this.urlRepository.save(any(Url.class))).willReturn(new Url());
        given(this.accountRepository.findByAccountId("mlovrekov")).willReturn(Optional.of(new Account()));
        //when
        ShortUrlDto dto = urlShortenerService.register("http://domain.com", "http://test.com", "mlovrekov", 302);
        //then
        then(tokenGeneratorService).should(times(1)).generate();
        then(urlRepository).should(times(1)).findByToken("ABCDEF");
        then(urlRepository).should(times(1)).save(any(Url.class));
        then(accountRepository).should(times(1)).findByAccountId("mlovrekov");
        assertNotNull(dto);
        assertEquals(dto.getShortUrl(), "http://domain.com/ABCDEF");
    }

    @Test
    public void test_when_register_is_called_with_valid_accountId_and_existent_token_is_generated_then_token_is_regenerated_and_a_ShortUrlDto_is_returned_with_valid_shortUrl() {
        //given
        given(this.tokenGeneratorService.generate())
                .willReturn("ABCDEF")
                .willReturn("GHIJKL");
        given(this.urlRepository.findByToken("ABCDEF")).willReturn(Optional.of(new Url()));
        given(this.urlRepository.findByToken("GHIJKL")).willReturn(Optional.empty());
        given(this.urlRepository.save(any(Url.class))).willReturn(new Url());
        given(this.accountRepository.findByAccountId("mlovrekov")).willReturn(Optional.of(new Account()));
        //when
        ShortUrlDto dto = urlShortenerService.register("http://domain.com", "http://test.com", "mlovrekov", 302);
        //then
        then(tokenGeneratorService).should(times(2)).generate();
        then(urlRepository).should(times(2)).findByToken(any(String.class));
        then(urlRepository).should(times(1)).save(any(Url.class));
        then(accountRepository).should(times(1)).findByAccountId("mlovrekov");
        assertNotNull(dto);
        assertEquals(dto.getShortUrl(), "http://domain.com/GHIJKL");
    }

    @Test(expected = EntityNotFoundException.class)
    public void test_when_register_is_called_with_invalid_accountID_and_non_existent_token_is_generated_then_an_exception_is_thrown() {
        //given
        given(this.tokenGeneratorService.generate()).willReturn("ABCDEF");
        given(this.urlRepository.findByToken("ABCDEF")).willReturn(Optional.empty());
        given(this.urlRepository.save(any(Url.class))).willReturn(new Url());
        given(this.accountRepository.findByAccountId("unknown")).willReturn(Optional.empty());
        //when
        urlShortenerService.register("http://domain.com", "http://test.com", "unknown", 302);
        //then
    }

    @Test
    public void test_when_getByToken_is_called_with_existing_token_then_associated_urlDto_is_returned() {
        //given
        Url url = new Url();
        url.setRedirectType(302);
        url.setToken("ABCDEF");
        url.setOriginalUrl("http://website.com");

        given(this.urlRepository.findByToken("ABCDEF")).willReturn(Optional.of(url));
        //when
        Optional<UrlDto> urlDto = urlShortenerService.getByToken("ABCDEF");
        //then
        then(urlRepository).should(times(1)).findByToken("ABCDEF");
        assertTrue(urlDto.isPresent());
        UrlDto actual = urlDto.get();
        assertEquals(actual.getToken(), "ABCDEF");
        assertEquals(actual.getOriginalUrl(), "http://website.com");
        assertEquals(actual.getRedirectType(), 302);
    }

    @Test
    public void test_when_getByToken_is_called_with_non_existing_token_then_nothing_is_returned() {
        //given
        given(this.urlRepository.findByToken("unknown")).willReturn(Optional.empty());
        //when
        Optional<UrlDto> urlDto = urlShortenerService.getByToken("unknown");
        //then
        then(urlRepository).should(times(1)).findByToken("unknown");
        assertFalse(urlDto.isPresent());
    }

    @Test
    public void test_when_incrementHitCount_is_called_with_existing_token_then_hit_count_is_incremented() {
        //given
        Url url = new Url();
        url.setRedirectType(302);
        url.setToken("ABCDEF");
        url.setOriginalUrl("http://website.com");
        url.setHitCount(1L);

        given(this.urlRepository.findByToken("ABCDEF")).willReturn(Optional.of(url));
        given(this.urlRepository.save(any(Url.class))).willReturn(url);
        //when
        urlShortenerService.incrementHitCount("ABCDEF");
        //then
        then(urlRepository).should(times(1)).findByToken("ABCDEF");
        assertEquals(url.getHitCount().longValue(), 2L);
    }

    @Test
    public void test_when_getStatisticsByAccountId_is_called_with_existing_accountId_then_statistics_map_is_returned() {
        //given
        Url url = new Url();
        url.setOriginalUrl("http://website.com");
        url.setHitCount(1L);

        Url url2 = new Url();
        url2.setOriginalUrl("http://otherwebsite.com");
        url2.setHitCount(4L);

        given(this.accountRepository.existsByAccountId("mlovrekov")).willReturn(true);
        given(this.urlRepository.findByAccountAccountId("mlovrekov")).willReturn(Arrays.asList(url, url2));
        //when
        Map<String, Long> statistics = urlShortenerService.getStatisticsByAccountId("mlovrekov");
        //then
        then(accountRepository).should(times(1)).existsByAccountId("mlovrekov");
        then(urlRepository).should(times(1)).findByAccountAccountId("mlovrekov");
        assertNotNull(statistics);
        assertEquals(statistics.size(), 2);
        assertTrue(statistics.containsKey("http://website.com"));
        assertTrue(statistics.containsKey("http://otherwebsite.com"));
        assertEquals(statistics.get("http://website.com").longValue(), 1L);
        assertEquals(statistics.get("http://otherwebsite.com").longValue(), 4L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void test_when_getStatisticsByAccountId_is_called_with_non_existing_accountId_then_exception_is_thrown() {
        //given
        given(this.accountRepository.existsByAccountId("unknown")).willReturn(false);
        //when
        urlShortenerService.getStatisticsByAccountId("unknown");
        //then
    }

}