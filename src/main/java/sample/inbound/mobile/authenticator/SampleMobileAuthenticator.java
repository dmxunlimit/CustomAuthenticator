package sample.inbound.mobile.authenticator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.AbstractApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorFlowStatus;
import org.wso2.carbon.identity.application.authentication.framework.FederatedApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.application.authentication.framework.exception.LogoutFailedException;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.common.model.Claim;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@link SampleMobileAuthenticator} authenticates the user based on the mobile number
 * provided in the 'login_hint'.
 */
public class SampleMobileAuthenticator extends AbstractApplicationAuthenticator implements
        FederatedApplicationAuthenticator {


    private static final long serialVersionUID = -8199087194545106872L;
    private static final Log log = LogFactory.getLog(SampleMobileAuthenticator.class);
    private static final String LOGIN_HINT = "login_hint";


    @Override
    protected void processAuthenticationResponse(HttpServletRequest httpServletRequest,
                                                 HttpServletResponse httpServletResponse,
                                                 AuthenticationContext authenticationContext)
            throws AuthenticationFailedException {

    }

    @Override
    public AuthenticatorFlowStatus process(HttpServletRequest request, HttpServletResponse response,
                                           AuthenticationContext context)
            throws AuthenticationFailedException, LogoutFailedException {

        // We are not handling logout scenarios in the authenticator.
        if (context.isLogoutRequest()) {
            return AuthenticatorFlowStatus.SUCCESS_COMPLETED;
        }

        // Get the mobile number from request.
        String mobile = request.getParameter(LOGIN_HINT);


        if (log.isDebugEnabled()) {
            log.debug("Received 'login_hint': " + mobile);
        }

        if (isValidMobile(mobile)) {
            // Construct an authenticated user with the mobile number as the subject.
           // AuthenticatedUser user = AuthenticatedUser.createFederateAuthenticatedUserFromSubjectIdentifier(mobile);
            AuthenticatedUser user = AuthenticatedUser.createLocalAuthenticatedUserFromSubjectIdentifier("admin");

            Map<ClaimMapping, String> claims = new HashMap<>();
//            String claimUri;
//            Object claimValueObject;
//


           // claims.put(buildClaimMapping(claimUri), claimValueObject.toString());

            ClaimMapping claimMapping = new ClaimMapping();

            Claim claim = new Claim();
            claim.setClaimUri("mobile");
            claimMapping.setRemoteClaim(claim);
            claims.put(claimMapping, mobile);



        //    user.setUserAttributes(claims);
            context.setSubject(user);
            context.getSubject().setUserAttributes(claims);
//            context.addParameter("CUSTOM_AUTHENTICATOR","YES");
//            context.addParameter("USER_NAME_CUSTOM",mobile);
//            context.addParameter("MOBILE_NUMBER_CUSTOM",mobile);
//            context.setProperty(,user);
            return AuthenticatorFlowStatus.SUCCESS_COMPLETED;
        } else {
            throw new AuthenticationFailedException("Error validating the mobile number: " + mobile);
        }
    }

    /**
     * Check the validity of the mobile number
     * @param mobile mobile number.
     * @return true if valid mobile number, false otherwise.
     */
    protected boolean isValidMobile(String mobile) {

        // TODO: Add additional validation for mobile number.
        return !StringUtils.isEmpty(mobile);
    }

    @Override
    public boolean canHandle(HttpServletRequest httpServletRequest) {

        String mobile = httpServletRequest.getParameter(LOGIN_HINT);
        return !StringUtils.isEmpty(mobile);
    }

    @Override
    public String getContextIdentifier(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("sessionDataKey");
    }

    @Override
    public String getName() {
        return AuthenticatorConstants.AUTHENTICATOR_NAME;
    }

    @Override
    public String getFriendlyName() {
        return AuthenticatorConstants.AUTHENTICATOR_FRIENDLY_NAME;
    }
}
