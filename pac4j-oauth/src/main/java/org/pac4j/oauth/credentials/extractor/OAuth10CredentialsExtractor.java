package org.pac4j.oauth.credentials.extractor;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.utils.OAuthEncoder;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.oauth.config.OAuth10Configuration;
import org.pac4j.oauth.credentials.OAuth10Credentials;
import org.pac4j.oauth.exception.OAuthCredentialsException;

import java.util.Optional;

/**
 * OAuth 1.0 credentials extractor.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public class OAuth10CredentialsExtractor extends OAuthCredentialsExtractor {

    public OAuth10CredentialsExtractor(final OAuth10Configuration configuration, final IndirectClient client) {
        super(configuration, client);
    }

    @Override
    protected Optional<Credentials> getOAuthCredentials(final WebContext context, final SessionStore sessionStore) {
        final var tokenParameter = context.getRequestParameter(OAuth10Configuration.OAUTH_TOKEN);
        final var verifierParameter = context.getRequestParameter(OAuth10Configuration.OAUTH_VERIFIER);
        if (tokenParameter.isPresent() && verifierParameter.isPresent()) {
            // get request token from session
            final var tokenSession = (OAuth1RequestToken) sessionStore
                    .get(context, ((OAuth10Configuration) configuration)
                    .getRequestTokenSessionAttributeName(client.getName())).orElse(null);
            logger.debug("tokenRequest: {}", tokenSession);
            final var token = OAuthEncoder.decode(tokenParameter.get());
            final var verifier = OAuthEncoder.decode(verifierParameter.get());
            logger.debug("token: {} / verifier: {}", token, verifier);
            return Optional.of(new OAuth10Credentials(tokenSession, token, verifier));
        } else {
            final var message = "No credential found";
            throw new OAuthCredentialsException(message);
        }
    }
}
