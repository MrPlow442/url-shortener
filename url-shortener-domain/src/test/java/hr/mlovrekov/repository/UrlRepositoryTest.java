package hr.mlovrekov.repository;

import hr.mlovrekov.TestUrlShortenerApplication;
import hr.mlovrekov.domain.Url;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestUrlShortenerApplication.class)
public class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void test_when_findByAccountAccountId_is_called_with_existing_accountId_then_list_of_accounts_is_returned() {
        //when
        List<Url> urls = urlRepository.findByAccountAccountId("mlovrekov");
        //then
        assertEquals(urls.size(), 2);
    }

    @Test
    public void test_when_findByAccountAccountId_is_called_with_non_existing_accountId_then_empty_list_is_returned() {
        //when
        List<Url> urls = urlRepository.findByAccountAccountId(null);
        //then
        assertEquals(urls.size(), 0);
    }

    @Test
    public void test_when_findByToken_is_called_with_existing_token_then_url_is_returned() {
        //when
        Optional<Url> url = urlRepository.findByToken("3dFQlsf");
        //then
        assertTrue(url.isPresent());
        Url actual = url.get();
        assertEquals(actual.getToken(), "3dFQlsf");
        assertEquals(actual.getOriginalUrl(), "http://testtwo.com");
        assertEquals(actual.getRedirectType(), 301);
        assertEquals(actual.getHitCount().longValue(), 3L);
        assertNotNull(actual.getAccount());
        assertEquals(actual.getAccount().getId().longValue(), 2L);
    }

    @Test
    public void test_when_findByToken_is_called_with_non_existing_token_then_nothing_is_returned() {
        //when
        Optional<Url> url = urlRepository.findByToken(null);
        //then
        assertFalse(url.isPresent());
    }
}