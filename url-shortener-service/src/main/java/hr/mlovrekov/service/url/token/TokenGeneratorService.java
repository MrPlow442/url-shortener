package hr.mlovrekov.service.url.token;

public interface TokenGeneratorService {

    String generate();

    String generate(int length);
}
