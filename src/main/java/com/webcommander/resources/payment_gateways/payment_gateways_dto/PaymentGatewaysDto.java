package com.webcommander.resources.payment_gateways.payment_gateways_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the payment gateway bodies. */
public final class PaymentGatewaysDto {

    private PaymentGatewaysDto() {
    }

    /** An {@code {id, name}} pair: the gateway's {@code zone}, and each audience member. */
    public static final class PaymentGatewayReference extends Dto {

        public Long id;

        public String name;
    }

    /** Who sees the gateway at checkout; the lists fill only under {@code type} selected. */
    public static final class PaymentGatewayVisibility extends Dto {

        /** {@code everyone}, {@code customer} or {@code selected}. */
        public String type;

        public List<PaymentGatewayReference> customers;

        public List<PaymentGatewayReference> customerGroups;
    }

    /** The delivery types the gateway is offered for; null on gateways without the choice. */
    public static final class PaymentGatewayAllowedFor extends Dto {

        public Boolean shipping;

        public Boolean delivery;

        public Boolean storePickup;
    }

    /**
     * The surcharge block, null on gateways without one, and its write; {@code percentAmount}
     * reads null when no percentage is set.
     */
    public static final class PaymentGatewaySurcharge extends Dto {

        public String type;

        public Double flatAmount;

        public Double percentAmount;

        /** Each range an untyped map ({@code id}, {@code from}, {@code to}, {@code amount}). */
        public List<Object> ranges;
    }

    /** The pending and completed messages; one the gateway's form lacks reads null. */
    public static final class PaymentGatewayMessages extends Dto {

        public String pending;

        public String completed;
    }

    /** PayPal's {@code standard} credentials. */
    public static final class PaymentGatewayPayPalStandard extends Dto {

        public String clientId;

        public String clientSecret;
    }

    /** PayPal's {@code classic} account. */
    public static final class PaymentGatewayPayPalClassic extends Dto {

        public String emailAddress;
    }

    /** A one-key {@code {"enabled": ...}} switch. */
    public static final class PaymentGatewayToggle extends Dto {

        public Boolean enabled;
    }

    /** The card gateway's processor; the client id and secret read {@code ********} when set. */
    public static final class PaymentGatewayCardProcessor extends Dto {

        public String type;

        public String mode;

        public String merchantCode;

        public String clientId;

        public String clientSecret;

        public PaymentGatewayToggle walletPayment;
    }

    /** The card logos shown at checkout. */
    public static final class PaymentGatewayCardLogo extends Dto {

        public String type;

        public List<String> cards;

        public Object customImage;
    }

    /** Express Checkout's Google Pay block. */
    public static final class PaymentGatewayGooglePay extends Dto {

        public Boolean enabled;

        public String paymentMode;

        public String merchantName;

        public String merchantId;

        public String processor;

        public String secretKey;

        public String publicKey;

        public Double freeShippingConditionAmount;

        public Double shippingCost;
    }

    /** Express Checkout's Apple Pay block. */
    public static final class PaymentGatewayApplePay extends Dto {

        public Boolean enabled;

        public String paymentMode;

        public String merchantIdentifier;

        public String domainName;

        public String displayName;

        public String processor;

        public String secretKey;

        public String publicKey;
    }

    /** Express Checkout's PayPal block. */
    public static final class PaymentGatewayExpressPayPal extends Dto {

        public Boolean showInstallmentAmount;
    }

    /** The row read's {@code settings}: every key any gateway answers; offline ones answer {}. */
    public static final class PaymentGatewaySettings extends Dto {

        public String version;

        public String mode;

        public PaymentGatewayPayPalStandard standard;

        public PaymentGatewayPayPalClassic classic;

        public PaymentGatewayCardProcessor processor;

        public PaymentGatewayCardLogo cardLogo;

        public PaymentGatewayGooglePay googlePay;

        public PaymentGatewayApplePay applePay;

        public PaymentGatewayExpressPayPal paypal;

        public Boolean showInstallmentAmount;

        public Boolean showInProductDetails;

        public String apiKey;

        public Long defaultTerm;

        public Double amountPer100Points;

        public String merchantId;

        public String merchantSecretKey;

        public Object image;
    }

    /** One gateway: a listing row, and the row read's and every write's body, adding settings. */
    public static final class PaymentGateway extends Dto {

        public Long id;

        /** Fixed per gateway type, and read-only. */
        public String code;

        public String name;

        public Boolean added;

        public Boolean enabled;

        public Boolean Default;

        /** Read-only. */
        public Boolean promotional;

        public PaymentGatewayReference zone;

        public String information;

        public PaymentGatewayVisibility visibility;

        public PaymentGatewayAllowedFor allowedFor;

        public PaymentGatewaySurcharge surcharge;

        public PaymentGatewayMessages messages;

        /** Absent from listing rows. */
        public PaymentGatewaySettings settings;

        @Override
        protected String envelopeKey() {
            return "payment_gateway";
        }
    }

    /** The visibility a write takes: customers and customer groups by id. */
    public static final class PaymentGatewayVisibilityInput extends Dto {

        public String type;

        public List<Long> customers;

        public List<Long> customerGroups;
    }

    /** The writable fields, for the {@code PUT} and the {@code PATCH}; no envelope. */
    public static final class PaymentGatewayInput extends Dto {

        public String name;

        public Boolean added;

        public Boolean enabled;

        public Boolean Default;

        /** A zone's id or {@code {"id": ...}}. */
        public Object zone;

        /** At most 2000 characters. */
        public String information;

        public PaymentGatewayVisibilityInput visibility;

        public PaymentGatewayAllowedFor allowedFor;

        public PaymentGatewaySurcharge surcharge;

        public PaymentGatewayMessages messages;

        public PaymentGatewaySettings settings;
    }

    /**
     * The five-key {@code pagination} block; {@code limit} is the page size applied.
     * {@code hasPrevious} is true past the first page and false on a page past the end.
     */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing: the rows, without settings, and where they sit. */
    public static final class PaymentGatewayPage extends Dto {

        public List<PaymentGateway> paymentGateways;

        public Pagination pagination;
    }

    /** The count sub-path's body, a bare {@code {"total": N}}. */
    public static final class PaymentGatewayCount extends Dto {

        public Long total;
    }
}
