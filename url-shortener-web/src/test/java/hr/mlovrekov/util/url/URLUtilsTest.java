package hr.mlovrekov.util.url;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class URLUtilsTest {

    @Test
    public void test_when_extractRootFromURL_called_with_url_with_protocol_then_root_url_with_protocol_is_returned() {
        //when
        String root = URLUtils.extractRootFromURL("http://stackoverflow.com/questions/40264556/what-is-the-right-way-to-send-data-between-modules-in-angularjs");
        //then
        assertEquals(root, "http://stackoverflow.com");
    }

    @Test
    public void test_when_extractRootFromURL_called_with_url_without_protocol_then_root_url_without_protocol_is_returned() {
        //when
        String root = URLUtils.extractRootFromURL("stackoverflow.com/questions/40264556/what-is-the-right-way-to-send-data-between-modules-in-angularjs");
        //then
        assertEquals(root, "stackoverflow.com");
    }

}