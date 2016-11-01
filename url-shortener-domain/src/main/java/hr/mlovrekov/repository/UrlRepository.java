package hr.mlovrekov.repository;

import hr.mlovrekov.domain.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    List<Url> findByAccountAccountId(String accountId);

    Optional<Url> findByToken(String token);

}
