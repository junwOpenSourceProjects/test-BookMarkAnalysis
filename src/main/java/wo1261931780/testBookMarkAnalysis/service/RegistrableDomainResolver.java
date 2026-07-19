package wo1261931780.testBookMarkAnalysis.service;

import com.google.common.net.InetAddresses;
import com.google.common.net.InternetDomainName;
import org.springframework.stereotype.Component;

@Component
public class RegistrableDomainResolver {

    public String resolve(String url) {
        String host = DomainCategoryMapper.extractDomain(url);
        if (host == null) {
            return null;
        }
        if (InetAddresses.isInetAddress(host)) {
            return host;
        }
        try {
            InternetDomainName domain = InternetDomainName.from(host);
            if (domain.hasPublicSuffix() && domain.isUnderPublicSuffix()) {
                return domain.topPrivateDomain().toString();
            }
        } catch (IllegalArgumentException ignored) {
            // Keep the normalized host as the stable fallback for non-standard hosts.
        }
        return host;
    }
}
