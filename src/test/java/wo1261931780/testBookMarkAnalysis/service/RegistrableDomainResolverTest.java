package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class RegistrableDomainResolverTest {
    private final RegistrableDomainResolver resolver = new RegistrableDomainResolver();

    @Test
    void reducesSubdomainsToTheSameRegistrableDomain() {
        assertEquals("github.com", resolver.resolve("https://docs.github.com/en/rest"));
        assertEquals("github.com", resolver.resolve("https://gist.github.com/user/1"));
        assertEquals("github.com", resolver.resolve("https://github.com/openai"));
    }

    @Test
    void preservesPublicSuffixAwareChineseDomains() {
        assertEquals("example.com.cn", resolver.resolve("https://a.b.example.com.cn/path"));
    }

    @Test
    void fallsBackToNormalizedHostForIpAndInvalidUrls() {
        assertEquals("127.0.0.1", resolver.resolve("http://127.0.0.1:8080"));
        assertNull(resolver.resolve("not a valid url"));
    }
}
