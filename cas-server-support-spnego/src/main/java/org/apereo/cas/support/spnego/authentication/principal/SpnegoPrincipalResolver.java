package org.apereo.cas.support.spnego.authentication.principal;

import org.apereo.cas.authentication.principal.PersonDirectoryPrincipalResolver;
import org.apereo.cas.authentication.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Implementation of a CredentialToPrincipalResolver that takes a
 * SpnegoCredential and returns a SimplePrincipal.
 *
 * @author Arnaud Lesueur
 * @author Marc-Antoine Garrigue
 * @since 3.1
 */
@RefreshScope
@Component("spnegoPrincipalResolver")
public class SpnegoPrincipalResolver extends PersonDirectoryPrincipalResolver {

    /** Transformation types. **/
    public enum Transform {NONE, UPPERCASE, LOWERCASE}

    
    private Transform transformPrincipalId = Transform.NONE;

    @Override
    protected String extractPrincipalId(final Credential credential) {
        final SpnegoCredential c = (SpnegoCredential) credential;
        final String id = c.getPrincipal().getId();

        switch (this.transformPrincipalId) {
        case UPPERCASE:
            return id.toUpperCase(Locale.ENGLISH);
        case LOWERCASE:
            return id.toLowerCase(Locale.ENGLISH);
        default:
            return id;
        }
    }

    @Override
    public boolean supports(final Credential credential) {
        return credential != null
                && SpnegoCredential.class.equals(credential.getClass());
    }

    @Autowired
    @Value("${cas.spnego.principal.resolver.transform:NONE}")
    public void setTransformPrincipalId(final Transform transform) {
        this.transformPrincipalId = transform;
    }
}
