package cn.goktech.dolphin.security.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.Assert;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月05日
 */
public class DolOAuth2FeignRequestInterceptor implements RequestInterceptor{

    /**
     * The logger instance used by this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DolOAuth2FeignRequestInterceptor.class);

    /**
     * The authorization header name.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * The {@code Bearer} token type.
     */
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    /**
     * Current OAuth2 authentication context.
     */
    private final OAuth2ClientContext oauth2ClientContext;

    /**
     * Creates new instance of {@link DolOAuth2FeignRequestInterceptor} with client context.
     *
     * @param oauth2ClientContext the OAuth2 client context
     */
    public DolOAuth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
        Assert.notNull(oauth2ClientContext, "Context can not be null");
        this.oauth2ClientContext = oauth2ClientContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(RequestTemplate template) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null && ctx.getAuthentication() != null
                && ctx.getAuthentication().getDetails() instanceof OAuth2AuthenticationDetails) {
            String token = ((OAuth2AuthenticationDetails)ctx.getAuthentication().getDetails()).getTokenValue();
            template.header(AUTHORIZATION_HEADER, String.format("%s %s",BEARER_TOKEN_TYPE,token));
            LOGGER.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
        } else {
            LOGGER.warn("Can not obtain existing token for request, if it is a non secured request, ignore.");
        }

    }
}
