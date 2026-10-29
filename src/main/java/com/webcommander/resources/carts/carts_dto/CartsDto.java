package com.webcommander.resources.carts.carts_dto;

import com.webcommander.Dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Typed views over the cart bodies. */
public final class CartsDto {

    private CartsDto() {
    }

    /** The cart's {@code totals}; {@code subtotal} sums each line's unit price times quantity. */
    public static final class CartTotals extends Dto {

        public Double subtotal;

        public Double discount;

        public Double shippingDiscount;

        public Double tax;

        public Double total;

        public Double grandTotal;

        public Double paid;
    }

    /** The cart's {@code shipping} block; its two taxes follow the shipping address's country. */
    public static final class CartShipping extends Dto {

        public Double cost;

        public Double handlingFee;

        public Double handlingTax;

        public Double shippingTax;

        public Double totalTax;
    }

    /** One line's {@code pricing}. */
    public static final class CartItemPricing extends Dto {

        public Double unitPrice;

        public Double displayUnitPrice;

        public Double total;

        public Double discount;

        public Double discountTax;

        public Double tax;
    }

    /** The product one line holds. */
    public static final class CartItemProduct extends Dto {

        public Long id;

        public String sku;

        public Double basePrice;

        /** The product's stock, not the line's. */
        public Long availableStock;

        public Boolean onSale;

        public Double costPrice;

        public Double salePrice;

        public String url;

        public String name;

        public String productType;

        public String imageLink;
    }

    /** One line of the cart. */
    public static final class CartItem extends Dto {

        /** The {@code cart_item_id} the line's writes take, unique across carts. */
        public Long id;

        public Long quantity;

        public Object variations;

        public Boolean taxable;

        public Boolean shippable;

        public CartItemPricing pricing;

        public CartItemProduct product;
    }

    /** The cart's {@code discount} block. */
    public static final class CartDiscount extends Dto {

        public Double discount;

        public Double orderDiscount;

        public Double shippingDiscount;

        public Double discountOnTax;

        public Double discountOnShippingTax;
    }

    /** The {@code discount} entry of {@code messages}. */
    public static final class CartDiscountMessage extends Dto {

        public String message;

        public Object status;
    }

    /** The cart's {@code messages} block. */
    public static final class CartMessages extends Dto {

        public CartDiscountMessage discount;
    }

    /** The one entry of {@code group_meta}. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block, which only a read sends. */
    public static final class GroupMeta extends Dto {

        public SectionMeta cart;
    }

    /** One cart: the answer of the add, the read and the line update. */
    public static final class Cart extends Dto {

        /** The add answers the id it was sent; a read and a line update answer {@code null}. */
        public Long customerId;

        public String sessionId;

        public String currencyCode;

        public String currencySymbol;

        public String deliveryType;

        public CartTotals totals;

        public CartShipping shipping;

        public List<CartItem> items;

        public CartDiscount discount;

        public CartMessages messages;

        /** Read from beside the {@code cart} envelope. */
        public GroupMeta groupMeta;

        @Override
        protected String envelopeKey() {
            return "cart";
        }
    }

    /** A {@code shipping_address} or {@code billing_address}; no cart body answers either back. */
    public static final class CartAddressInput extends Dto {

        public String firstName;

        public String lastName;

        public String address_line1;

        public String address_line2;

        public String city;

        public String postCode;

        public String phone;

        public String mobile;

        public String fax;

        public String email;

        public String countryCode;

        public String stateCode;
    }

    /** The add's body, sent without an envelope. */
    public static final class CartItemInput extends Dto {

        public Long productId;

        public Long quantity;

        /** The customer's {@code internal_id}. */
        public Long customerId;

        public String sessionId;

        public CartAddressInput shippingAddress;
    }

    /** The line update's body, sent without an envelope; the two addresses are optional. */
    public static final class CartItemUpdateInput extends Dto {

        public Long cartItemId;

        public Long quantity;

        public CartAddressInput shippingAddress;

        public CartAddressInput billingAddress;
    }

    /** The session write's body; an unset {@link #sessionId} sends {@code {}}. */
    public static final class CartSessionInput extends Dto {

        public String sessionId;

        @Override
        public Map<String, Object> toMap() {
            // The derived key would be session_id; the wire key is sessionId.
            Map<String, Object> body = new LinkedHashMap<>();
            if (sessionId != null) {
                body.put("sessionId", sessionId);
            }
            return body;
        }
    }

    /** The session write's answer; {@code resolveCartSession} reads {@code sessionId} into it. */
    public static final class CartSession extends Dto {

        public String sessionId;
    }

    /** The order write's body, sent without an envelope. */
    public static final class CartOrderInput extends Dto {

        /** The customer's {@code customer_id}. */
        public Long customerId;

        public String paymentGateway;

        public String deliveryType;

        public String transactionNo;
    }

    /** A country or a state; a state arrives {@code {}} when the address has none. */
    public static final class Reference extends Dto {

        public Long id;

        public String name;

        public String code;
    }

    /** The order's {@code customer_summary}, carrying both of the customer's ids. */
    public static final class CartOrderCustomer extends Dto {

        public String firstName;

        public String lastName;

        public String customerGroup;

        public String gender;

        public Long customerId;

        public Long internalId;

        public String email;

        public String addressLine1;

        public String addressLine2;

        public String city;

        public Reference country;

        public Reference state;

        public String postCode;

        public String phone;

        public String mobile;

        public String fax;

        public String companyName;
    }

    /** One entry of the order's {@code order_line_details}. */
    public static final class CartOrderLine extends Dto {

        public Long itemId;

        public String productName;

        public Long productId;

        public String sku;

        public Long quantity;

        public Double price;

        public Double totalAmount;

        public Double tax;

        public Double discount;

        public Double taxDiscount;

        public Boolean isTaxable;

        public Boolean isShippable;

        public String imageUrl;

        public List<Object> variations;

        public String serialBatchNumber;
    }

    /** One entry of the order's {@code payments}. */
    public static final class CartOrderPayment extends Dto {

        public Long id;

        public String status;

        public Double amount;

        public String paymentMethod;

        public String trackInfo;

        public String gatewayName;

        public String gatewayCode;

        public String gatewayResponse;

        public String payerInfo;

        public String payingDate;

        public String transactionDate;

        public String transactionReference;
    }

    /** The order's billing or shipping address: the customer's, unless a line update sent one. */
    public static final class CartOrderAddress extends Dto {

        public Long id;

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

    /** The order a cart became, with the write's {@code status} read from beside the envelope. */
    public static final class CartOrder extends Dto {

        public String status;

        public Long orderId;

        public Long internalId;

        public Long orderNo;

        public String transactionNo;

        public String currencyCode;

        public String orderStatus;

        public String paymentStatus;

        public String shippingStatus;

        public Double subTotal;

        public Double shippingCost;

        public Double shippingTax;

        public Double handlingCost;

        public Double totalSurcharge;

        public Double totalDiscount;

        public Double totalTax;

        public Double grandTotal;

        public Double paid;

        public Double due;

        public Long itemsTotal;

        public String ipAddress;

        public String orderChannel;

        public String createdAt;

        public String updatedAt;

        public CartOrderCustomer customerSummary;

        public List<CartOrderLine> orderLineDetails;

        public List<CartOrderPayment> payments;

        public Object externalReferenceOrderId;

        public Object externalReferenceStoreId;

        public Object externalReferenceOrderNo;

        public Object externalReferenceStatus;

        public Object externalReferenceUrl;

        public String customerPoNumber;

        public String paymentTerm;

        public String fulfillmentMode;

        public String fulfillmentTerm;

        public String invoiceMode;

        public CartOrderAddress billingAddress;

        public CartOrderAddress shippingAddress;

        public List<Object> customFields;

        @Override
        protected String envelopeKey() {
            return "order";
        }
    }
}
