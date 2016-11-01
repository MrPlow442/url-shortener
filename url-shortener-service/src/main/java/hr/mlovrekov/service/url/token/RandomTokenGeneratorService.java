package hr.mlovrekov.service.url.token;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Generates a token by stringing together random alphanumeric characters
 */
@Service
public class RandomTokenGeneratorService implements TokenGeneratorService {

    private static final String ALLOWED_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int MIN_CHARS = 6;
    private static final int MAX_CHARS = 12;

    private final Random random;

    public RandomTokenGeneratorService() {
        this.random = new Random();
    }

    public RandomTokenGeneratorService(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public String generate(int length) {
        if (length < MIN_CHARS || length > MAX_CHARS) {
            throw new IllegalArgumentException(String.format("Invalid length provided. Length must be in range [%d,..,%d]", MIN_CHARS, MAX_CHARS));
        }

        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            builder.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }

        return builder.toString();
    }

    @Override
    public String generate() {
        int bound = (MAX_CHARS + 1) - MIN_CHARS;
        int shift = MIN_CHARS;
        return generate(random.nextInt(bound) + shift);
    }
}
