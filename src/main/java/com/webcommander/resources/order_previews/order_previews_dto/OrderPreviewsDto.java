package com.webcommander.resources.order_previews.order_previews_dto;

import com.webcommander.Dto;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderAddressInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderProductInput;

import java.util.List;

/** Typed views over the order preview bodies. */
public final class OrderPreviewsDto {

    private OrderPreviewsDto() {
    }

    /**
     * The session block. {@code session_id} starts {@code PRV-} and is the
     * {@code preview_session_id} of every other call. {@code expires_at} is 30 minutes after
     * {@code created_at}; a coupon or gift card call, applying or removing, moves it 30 minutes
     * past that call, and a read does not.
     */
    public static final class PreviewSession extends Dto {

        public String sessionId;

        public String status;

        public String createdAt;

        public String expiresAt;
    }

    /** The customer as the preview shows it: the billing address names win over the record's. */
    public static final class PreviewCustomer extends Dto {

        public Long customerId;

        public String firstName;

        public String lastName;

        public String email;
    }

    /** One line of the preview, priced by the store. */
    public static final class PreviewItem extends Dto {

        public Long productId;

        public String productName;

        public String sku;

        public Long quantity;

        public Double price;

        public Double discount;

        public Double tax;

        public Double totalAmount;

        public Boolean taxable;

        public Boolean shippable;
    }

    /** The delivery the preview was priced for. A {@code store_pickup} has no shipping cost. */
    public static final class PreviewShipping extends Dto {

        public String deliveryType;

        public String shippingType;

        public Double shippingCost;

        public Double shippingTax;
    }

    /**
     * The coupon on the preview: {@code amount} is the discount it takes off, {@code status}
     * {@code applied}.
     */
    public static final class PreviewCoupon extends Dto {

        public String code;

        public String status;

        public Double amount;
    }

    /**
     * The gift card on the preview: {@code applied_amount} is the lesser of its balance and the
     * grand total. {@code remaining_balance} came back {@code null}.
     */
    public static final class PreviewGiftCard extends Dto {

        public String code;

        public String status;

        public Double appliedAmount;

        public Object remainingBalance;
    }

    /** At most one coupon and one gift card; {@code null} for one not applied. */
    public static final class PreviewDiscounts extends Dto {

        public PreviewCoupon coupon;

        public PreviewGiftCard giftCard;
    }

    /**
     * The money. {@code grand_total} is {@code sub_total} less {@code total_discount} plus the
     * tax, shipping (and its tax), handling and surcharge; a gift card lowers {@code due}, not
     * {@code grand_total}.
     */
    public static final class PreviewTotals extends Dto {

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

    /**
     * A preview. The start and the read answer every field; a coupon or gift card call answers
     * only {@code session}, {@code discounts} and {@code totals}.
     */
    public static final class OrderPreview extends Dto {

        public PreviewSession session;

        public PreviewCustomer customer;

        public String deliveryType;

        public String currencyCode;

        public List<PreviewItem> items;

        public PreviewShipping shipping;

        public PreviewDiscounts discounts;

        public PreviewTotals totals;

        @Override
        protected String envelopeKey() {
            return "order";
        }
    }

    /**
     * The start body, bare. {@code products} is required. Without {@code customer_id} the billing
     * address is required, and a shipping address too unless {@code delivery_type} is
     * {@code store_pickup}. {@code delivery_type} is {@code shipping} unless it is
     * {@code delivery}, {@code others_shipping} or {@code store_pickup}. An unknown field, such as
     * {@code coupon_code} or a product line's {@code price}, is {@code 400}.
     */
    public static final class OrderPreviewInput extends Dto {

        public Long customerId;

        public String deliveryType;

        public OrderAddressInput billingAddress;

        public OrderAddressInput shippingAddress;

        public List<OrderProductInput> products;
    }

    /** The body of a coupon or gift card call: {@code code}, required, at most 100 characters. */
    public static final class OrderPreviewCodeInput extends Dto {

        public String code;
    }

    /**
     * The payment a confirm records. {@code amount} above what is due is {@code 422}, a missing
     * {@code payment_method} is {@code 400}, and {@code payment_status} {@code failed} leaves the
     * order unpaid.
     */
    public static final class OrderPreviewPaymentInput extends Dto {

        public Double amount;

        public String paymentStatus;

        public String paymentMethod;

        public String transactionReference;

        public String transactionDate;
    }

    /**
     * The confirm body, bare, and optional. {@code payment_gateway} takes a code such as
     * {@code PIS} and leaves the order unpaid; an unknown field is {@code 400}.
     */
    public static final class OrderPreviewConfirmInput extends Dto {

        public String paymentGateway;

        public String transactionNo;

        public Boolean sendEmail;

        public OrderPreviewPaymentInput payment;
    }
}
