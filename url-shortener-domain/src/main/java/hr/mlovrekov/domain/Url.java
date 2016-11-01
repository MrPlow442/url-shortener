package hr.mlovrekov.domain;

import javax.persistence.*;

@Entity
@Table(name = "URL")
public class Url {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "ORIGINAL_URL", nullable = false, updatable = false)
    private String originalUrl;

    @Column(name = "TOKEN", nullable = false, updatable = false)
    private String token;

    @Column(name = "REDIRECT_TYPE", nullable = false, updatable = false)
    private int redirectType;

    @Column(name = "HIT_COUNT", nullable = false)
    private Long hitCount = 0L;

    @ManyToOne(optional = false)
    private Account account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public Long getHitCount() {
        return hitCount;
    }

    public void setHitCount(Long hitCount) {
        this.hitCount = hitCount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
