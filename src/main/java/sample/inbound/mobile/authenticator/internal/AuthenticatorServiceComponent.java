package sample.inbound.mobile.authenticator.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import sample.inbound.mobile.authenticator.SampleMobileAuthenticator;

/**
 * @scr.component name="sample.inbound.mobile.authenticator.component" immediate="true"
 */
public class AuthenticatorServiceComponent {

    private static Log log = LogFactory.getLog(AuthenticatorServiceComponent.class);

    protected void activate(ComponentContext ctxt) {
        try {
            SampleMobileAuthenticator basicCustomAuth = new SampleMobileAuthenticator();
            ctxt.getBundleContext().registerService(ApplicationAuthenticator.class.getName(), basicCustomAuth, null);
            if (log.isDebugEnabled()) {
                log.debug("MobileAuthenticator bundle is activated");
            }
        } catch (Throwable e) {
            log.error("MobileAuthenticator bundle activation Failed", e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.info("MobileAuthenticator bundle is deactivated");
        }
    }
}
