package com.webcommander.resources.settings.page_settings.checkout_page_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the checkout page settings bodies. */
public final class CheckoutPageDto {

    private CheckoutPageDto() {
    }

    /** One entry of an address section's {@code fields}: a form field and how it is drawn. */
    public static final class AddressField extends Dto {

        public String key;

        public String label;

        public Boolean active;

        public Boolean required;

        public Long order;

        public String group;
    }

    /** An address section's record, in the two forms the API sends it. */
    public static class AddressForm extends Dto {

        public List<String> activeFields;

        public List<AddressField> fields;
    }

    /** The {@code billing_address} section, which {@code group_meta} marks read-only. */
    public static final class BillingAddress extends AddressForm {

        @Override
        protected String envelopeKey() {
            return "billing_address";
        }
    }

    /** The {@code shipping_address} section, which {@code group_meta} marks read-only. */
    public static final class ShippingAddress extends AddressForm {

        @Override
        protected String envelopeKey() {
            return "shipping_address";
        }
    }

    /** The {@code allow_sign_up} block: whether an account may be created during checkout. */
    public static final class SignUp extends Dto {

        public Boolean enabled;

        public Boolean required;
    }

    /** The {@code checkout_as_guest} block: whether an order may be placed without an account. */
    public static final class GuestCheckout extends Dto {

        public Boolean enabled;

        public Boolean quickSignup;
    }

    /** The {@code customer_checkout} section: who may check out, and how. */
    public static final class CustomerCheckout extends Dto {

        public Boolean allowSignIn;

        public SignUp allowSignUp;

        public GuestCheckout checkoutAsGuest;

        public Boolean newsletterSubscription;

        public Boolean multipleShippingMethods;

        @Override
        protected String envelopeKey() {
            return "customer_checkout";
        }
    }

    /** The {@code delivery_options} section: which delivery choices checkout offers. */
    public static final class DeliveryOptions extends Dto {

        public Boolean enableShipping;

        public Boolean enableStorePickup;

        public Boolean enableShippingQuote;

        public Boolean enableDeliveryService;

        @Override
        protected String envelopeKey() {
            return "delivery_options";
        }
    }

    /** The {@code order_summary} section: what the checkout's summary panel shows. */
    public static final class OrderSummary extends Dto {

        public Boolean showProductThumbnail;

        public Boolean showQuantity;

        public Boolean enablePaymentExpandView;

        public Boolean orderConfirmCaptcha;

        @Override
        protected String envelopeKey() {
            return "order_summary";
        }
    }

    /** The {@code comment} block: whether an order may carry a customer comment. */
    public static final class OrderComment extends Dto {

        public Boolean enabled;

        public Boolean sendNotification;
    }

    /** The {@code terms_and_condition.ref} block: where the terms text comes from. */
    public static final class TermsReference extends Dto {

        public Object page;

        public String link;

        public String text;
    }

    /** The {@code terms_and_condition} block: whether terms must be accepted, and their wording. */
    public static final class TermsAndCondition extends Dto {

        public Boolean enabled;

        public String text;

        public String linkLabel;

        public String type;

        public TermsReference ref;
    }

    /** The {@code others} section: the checkout page's logo, login link and other chrome. */
    public static final class Others extends Dto {

        public Boolean showLogo;

        public String loginUrl;

        public String customHtml;

        public OrderComment comment;

        public TermsAndCondition termsAndCondition;

        @Override
        protected String envelopeKey() {
            return "others";
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

        public SectionMeta billingAddress;

        public SectionMeta shippingAddress;

        public SectionMeta customerCheckout;

        public SectionMeta deliveryOptions;

        public SectionMeta orderSummary;

        public SectionMeta others;
    }

    /** The checkout page settings, as the facade or one section sends them. */
    public static final class CheckoutPage extends Dto {

        public BillingAddress billingAddress;

        public ShippingAddress shippingAddress;

        public CustomerCheckout customerCheckout;

        public DeliveryOptions deliveryOptions;

        public OrderSummary orderSummary;

        public Others others;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "checkout_page";
        }
    }
}
