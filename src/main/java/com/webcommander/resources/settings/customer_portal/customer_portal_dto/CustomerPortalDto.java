package com.webcommander.resources.settings.customer_portal.customer_portal_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the customer portal bodies. */
public final class CustomerPortalDto {

    private CustomerPortalDto() {
    }

    /** One entry of {@code default_groups} or {@code approval_groups} as a read sends it. */
    public static final class CustomerGroup extends Dto {

        public Long id;

        public String name;
    }

    /** The {@code portal} section: the one switch that turns the customer portal on. */
    public static final class Portal extends Dto {

        public Boolean enabled;

        @Override
        protected String envelopeKey() {
            return "portal";
        }
    }

    /** The {@code terms} block: the terms line shown on the registration form. */
    public static final class RegistrationTerms extends Dto {

        public Boolean enabled;

        public String text;

        public String anchorText;

        public String type;

        public String reference;
    }

    /** The {@code facebook} block: registering through Facebook. */
    public static final class FacebookLogin extends Dto {

        public Boolean enabled;

        public String appId;
    }

    /** The {@code google} block: registering through Google. */
    public static final class GoogleLogin extends Dto {

        public Boolean enabled;

        public String clientId;
    }

    /** The {@code b2b_configuration} block: what a new registrant is created as. */
    public static final class B2bConfiguration extends Dto {

        public Boolean enabled;

        public Boolean createAsOrganisation;

        public Boolean createAsCustomer;

        public Boolean organisationSelected;
    }

    /** The {@code alternate_login} block: third-party login and whether it offers email. */
    public static final class AlternateLogin extends Dto {

        public Boolean enabled;

        public Boolean enableEmailField;
    }

    /** The {@code registration} section: whether customers may register, and as what. */
    public static final class Registration extends Dto {

        public Boolean enabled;

        public String closeRegistrationMessage;

        public Boolean assignDefaultGroup;

        public List<CustomerGroup> defaultGroups;

        public Boolean requiresApproval;

        public List<CustomerGroup> approvalGroups;

        public RegistrationTerms terms;

        public FacebookLogin facebook;

        public GoogleLogin google;

        public B2bConfiguration b2bConfiguration;

        public AlternateLogin alternateLogin;

        @Override
        protected String envelopeKey() {
            return "registration";
        }
    }

    /** The {@code captcha} block: the challenge shown after failed logins. */
    public static final class LoginCaptcha extends Dto {

        public Boolean enabled;

        public Long failCount;
    }

    /** The {@code login} section: whether customers may sign in and what sign-in offers. */
    public static final class Login extends Dto {

        public Boolean enabled;

        public String loginDisabledMessage;

        public String awaitingApprovalMessage;

        public Boolean passwordRecovery;

        public Boolean registrationLink;

        public LoginCaptcha captcha;

        public Boolean enableLandingPageAfterSuccessfulLogin;

        public String landingPageAfterSuccessfulLogin;

        @Override
        protected String envelopeKey() {
            return "login";
        }
    }

    /** The {@code azure_ad.data_mapping} block: which assertion attribute fills which field. */
    public static final class AzureAdDataMapping extends Dto {

        public String addressLine1;

        public String addressLine2;

        public String postCode;

        public String phone;

        public String mobile;

        public Long countryId;

        public String state;

        public String city;
    }

    /** The {@code azure_ad} block: the SAML provider. */
    public static final class AzureAd extends Dto {

        public AzureAdDataMapping dataMapping;

        public String identifier;

        public String loginUrl;

        public String replyUrlPostfix;
    }

    /** The {@code azure_ad_b2c} block: the B2C provider, carrying {@link #clientSecret}. */
    public static final class AzureAdB2c extends Dto {

        public String tenantName;

        public String policyName;

        public String clientId;

        public String clientSecret;

        public String replyUrlPrefix;

        public String replyUrlPostfix;
    }

    /** The {@code sso} section: single sign-on and which provider serves it. */
    public static final class Sso extends Dto {

        public Boolean enabled;

        public Boolean requireSiteAccess;

        public String serviceProvider;

        public AzureAd azureAd;

        public AzureAdB2c azureAdB2c;

        @Override
        protected String envelopeKey() {
            return "sso";
        }
    }

    /** One row of an address or account form on the profile page. */
    public static class ProfileField extends Dto {

        public String key;

        public Boolean active;

        public Long order;
    }

    /** One row of a named list on the profile page: a tab, a section or an entitlement. */
    public static class ProfileEntry extends ProfileField {

        public String label;
    }

    /** One row of {@code overview}. */
    public static final class ProfileOverviewEntry extends ProfileEntry {

        public Boolean showProductInfo;
    }

    /** The {@code manage_my_account} block: the account screen's sections and their fields. */
    public static final class ManageMyAccount extends Dto {

        public List<ProfileEntry> sections;

        public List<ProfileField> accountInformation;

        public List<ProfileField> billingAddress;

        public List<ProfileField> shippingAddress;
    }

    /** The {@code my_orders.flags} block: six booleans. */
    public static final class MyOrderFlags extends Dto {

        public Boolean enableDuePayment;

        public Boolean showTrackMyOrders;

        public Boolean displayTrackInColumn;

        public Boolean showOrderChannel;

        public Boolean showPayNow;

        public Boolean allowOrderShare;
    }

    /** The {@code my_orders} block — what the orders tab shows and which order lists it offers. */
    public static final class MyOrders extends Dto {

        public MyOrderFlags flags;

        public List<ProfileEntry> sections;
    }

    /** The {@code assign_to_customer} block: the assigned-pages tab and how it paginates. */
    public static final class AssignToCustomer extends Dto {

        public List<ProfileEntry> items;

        public String showPagination;

        public Long itemPerPage;
    }

    /** The {@code profile_page} section: every tab of the logged-in customer's own page. */
    public static final class ProfilePage extends Dto {

        public String pageHtml;

        public List<ProfileEntry> tabs;

        public List<ProfileOverviewEntry> overview;

        public ManageMyAccount manageMyAccount;

        public MyOrders myOrders;

        public List<ProfileEntry> myEntitlements;

        public AssignToCustomer assignToCustomer;

        @Override
        protected String envelopeKey() {
            return "profile_page";
        }
    }

    /** One row of the registration form's field catalog. */
    public static final class RegistrationField extends Dto {

        public String key;

        public String label;

        public String defaultLabel;

        public Boolean active;

        public Boolean required;

        public Boolean system;

        public Long order;

        public String placeholder;
    }

    /** The {@code registration_fields} section: the registration form's field catalog. */
    public static final class RegistrationFields extends Dto {

        public List<RegistrationField> fields;

        @Override
        protected String envelopeKey() {
            return "registration_fields";
        }
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per section the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta portal;

        public SectionMeta registration;

        public SectionMeta login;

        public SectionMeta sso;

        public SectionMeta profilePage;

        public SectionMeta registrationFields;
    }

    /** The customer portal settings, as the facade or one section sends them. */
    public static final class CustomerPortal extends Dto {

        public Portal portal;

        public Registration registration;

        public Login login;

        public Sso sso;

        public ProfilePage profilePage;

        public RegistrationFields registrationFields;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "customer_portal";
        }
    }
}
