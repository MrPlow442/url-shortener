package hr.mlovrekov.service.url.token;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomTokenGeneratorServiceTest {

    // Manually create service with fixed seed to make the results predictable
    private TokenGeneratorService tokenGeneratorService = new RandomTokenGeneratorService(1L);

    @Test
    public void test_when_generate_is_called_then_a_pseudorandom_string_between_6_and_12_chars_is_returned() {
        //when
        String result = tokenGeneratorService.generate();
        //then
        assertNotNull(result);
        assertTrue(result.length() >= 6);
        assertTrue(result.length() <= 12);
        assertTrue(result.matches("[A-Za-z0-9]{6,12}"));
        assertEquals(result, "0lPk64Ie89");
    }

    @Test
    public void test_when_parametered_generate_is_called_with_valid_length_then_a_pseudorandom_string_of_length_chars_is_returned() {
        //when
        String result = tokenGeneratorService.generate(6);
        //then
        assertNotNull(result);
        assertTrue(result.length() == 6);
        assertTrue(result.matches("[A-Za-z0-9]{6}"));
        assertEquals(result, "D0lPk6");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_when_parametered_generate_is_called_with_invalid_length_then_an_exception_is_thrown() {
        //when
        tokenGeneratorService.generate(1);
        //then
    }

}