package org.pac4j.http.credentials.extractor;

import org.junit.Test;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.http.credentials.DigestCredentials;

import static org.junit.Assert.*;

/**
 * This class tests the {@link DigestAuthExtractor}
 * @author Mircea Carasel
 * @since 1.9.0
 */
public class DigestExtractorTests implements TestsConstants {

    private final static DigestAuthExtractor digestExtractor = new DigestAuthExtractor();

    @Test
    public void testRetrieveDigestHeaderComponents() {
        final var context = MockWebContext.create();
        context.addRequestHeader(HttpConstants.AUTHORIZATION_HEADER, DIGEST_AUTHORIZATION_HEADER_VALUE);
        final var credentials = (DigestCredentials) digestExtractor.extract(context, new MockSessionStore()).get();
        assertEquals(DIGEST_RESPONSE, credentials.getToken());
        assertEquals(USERNAME, credentials.getUsername());
    }

    @Test
    public void testNotDigest() {
        final var context = MockWebContext.create();
        final var credentials = digestExtractor.extract(context, new MockSessionStore());
        assertFalse(credentials.isPresent());
    }

}
