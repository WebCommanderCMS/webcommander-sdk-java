package com.webcommander.resources.carts.admin_carts_dto;

import com.webcommander.Dto;
import com.webcommander.resources.carts.carts_dto.CartsDto.Reference;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderAddressInput;

import java.util.List;

/** Typed views over the admin cart and checkout bodies. */
public final class AdminCartsDto {

    private AdminCartsDto() {
    }

    /**
     * The session block. {@code session_id} starts {@code PRV-} and is the {@code session_id} of
     * every other call. {@code expires_at} is 30 minutes after the last write.
     */
    public static final class AdminCartSession extends Dto {

        public String sessionId;

        public String status;

        public String createdAt;

        public String expiresAt;
    }

    /** The customer the cart is for; {@code customer_id} is the customer's normal id. */
    public static final class AdminCartCustomer extends Dto {

        public Long customerId;

        public String firstName;

        public String lastName;

        public String email;
    }

    /**
     * One line, priced by the store. {@code cart_item_id} is what the line's update and delete
     * take; a replace of the cart numbers its lines again.
     */
    public static final class AdminCartItem extends Dto {

        public Long cartItemId;

        public Long productId;

        public String productName;

        public Long variationId;

        public String variationName;

        public String sku;

        public Double price;

        public Long quantity;

        public Double discount;

        public Double tax;

        public Double totalAmount;

        public Boolean taxable;

        public Boolean shippable;
    }

    /** The cart's money before shipping. */
    public static final class AdminCartTotals extends Dto {

        public Double subTotal;

        public Double totalDiscount;

        public Double totalTax;

        public Double grandTotal;
    }

    /** A cart: what every cart call but the deletes answers. */
    public static final class AdminCart extends Dto {

        public AdminCartSession session;

        public AdminCartCustomer customer;

        public String currencyCode;

        public List<AdminCartItem> items;

        public AdminCartTotals totals;

        @Override
        protected String envelopeKey() {
            return "cart";
        }
    }

    /**
     * A billing or shipping address as the checkout reads it back; {@code state} is {@code {}}
     * when the address has none.
     */
    public static final class CheckoutAddress extends Dto {

        public String firstName;

        public String lastName;

        public String email;

        public String companyName;

        public String addressLine1;

        public String addressLine2;

        public Reference country;

        public Reference state;

        public String city;

        public String postCode;

        public String mobile;

        public String phone;

        public String fax;
    }

    /** The delivery the checkout was priced for; empty until a {@code delivery_type} is set. */
    public static final class CheckoutShipping extends Dto {

        public String deliveryType;

        public String shippingType;

        public Double shippingCost;

        public Double shippingTax;
    }

    /** The coupon applied: {@code applied_amount} is the discount it takes off. */
    public static final class CheckoutCoupon extends Dto {

        public String code;

        public String status;

        public Double appliedAmount;
    }

    /**
     * The gift card applied: {@code applied_amount} is the lesser of its balance and the grand
     * total.
     */
    public static final class CheckoutGiftCard extends Dto {

        public String code;

        public String status;

        public Double appliedAmount;

        public Double remainingBalance;
    }

    /** At most one coupon and one gift card; {@code null} for one not applied. */
    public static final class CheckoutDiscounts extends Dto {

        public CheckoutCoupon coupon;

        public CheckoutGiftCard giftCard;
    }

    /** The money. A gift card lowers {@code due}, not {@code grand_total}. */
    public static final class CheckoutTotals extends Dto {

        public Double subTotal;

        public Double totalDiscount;

        public Double totalTax;

        public Double shippingCost;

        public Double handlingCost;

        public Double totalSurcharge;

        public Double giftCardApplied;

        public Double grandTotal;

        public Double due;
    }

    /** One thing still blocking a confirm, such as {@code delivery_type} {@code required}. */
    public static final class CheckoutIssue extends Dto {

        public String field;

        public String code;

        public String message;
    }

    /**
     * The checkout. The read and the update answer every field; the coupon, gift card and delivery
     * schedule calls answer the parts they change. {@code delivery_schedule} is {@code null}
     * until one is set.
     */
    public static final class AdminCheckout extends Dto {

        public AdminCartSession session;

        public AdminCartCustomer customer;

        public String deliveryType;

        public String currencyCode;

        public List<AdminCartItem> items;

        public CheckoutAddress billingAddress;

        public CheckoutAddress shippingAddress;

        public CheckoutShipping shipping;

        public CheckoutDiscounts discounts;

        public Object deliverySchedule;

        public CheckoutTotals totals;

        public List<CheckoutIssue> issues;

        @Override
        protected String envelopeKey() {
            return "checkout";
        }
    }

    /**
     * One product line to put on a cart. {@code product_id} and {@code quantity} are required;
     * {@code variation_id} picks the variation of a variable product.
     */
    public static final class AdminCartProductInput extends Dto {

        public Long productId;

        public Long variationId;

        public Long quantity;
    }

    /**
     * The create and replace body, bare. {@code products} is required, and so is
     * {@code customer_id} on a replace; {@code session_id} on a create names the session yourself,
     * in the {@code PRV-} form. An unknown field is {@code 400 unknown_field}.
     */
    public static final class AdminCartInput extends Dto {

        public Long customerId;

        public String sessionId;

        public List<AdminCartProductInput> products;
    }

    /** The cart update body: the customer to move the cart to. {@code customer_id} is required. */
    public static final class AdminCartCustomerInput extends Dto {

        public Long customerId;
    }

    /** The add-lines body. Adding a product already on the cart raises that line's quantity. */
    public static final class AdminCartItemsInput extends Dto {

        public List<AdminCartProductInput> products;
    }

    /** The line update body: the new {@code quantity}, required. */
    public static final class AdminCartItemUpdateInput extends Dto {

        public Long quantity;
    }

    /**
     * The checkout update body. {@code delivery_type} is {@code shipping}, {@code delivery},
     * {@code others_shipping} or {@code store_pickup}; an address replaces the whole one.
     */
    public static final class AdminCheckoutInput extends Dto {

        public String deliveryType;

        public OrderAddressInput billingAddress;

        public OrderAddressInput shippingAddress;
    }

    /** The body of a coupon or gift card call: {@code code}, required. */
    public static final class AdminCheckoutCodeInput extends Dto {

        public String code;
    }

    /**
     * The payment a confirm records. {@code amount} and {@code payment_method} are required; an
     * {@code amount} above what is due is refused.
     */
    public static final class AdminCheckoutPaymentInput extends Dto {

        public Double amount;

        public String paymentStatus;

        public String paymentMethod;

        public String transactionReference;

        public String transactionDate;
    }

    /**
     * The confirm body, bare, and optional. {@code payment_gateway} takes one of the store's
     * codes, such as {@code PIS}; an unknown field is {@code 400}.
     */
    public static final class AdminCheckoutConfirmInput extends Dto {

        public String paymentGateway;

        public String transactionNo;

        public Boolean sendEmail;

        public AdminCheckoutPaymentInput payment;
    }

    /** The delivery schedule to choose: its {@code id} and the {@code date} wanted, both required. */
    public static final class AdminCheckoutDeliveryScheduleInput extends Dto {

        public Long id;

        public String date;
    }
}
