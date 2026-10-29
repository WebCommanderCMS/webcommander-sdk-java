package com.webcommander.resources.orders.orders_dto;

import com.webcommander.Dto;

import java.util.List;
import java.util.Set;

/** Typed views over the order bodies. */
public final class OrdersDto {

    private OrdersDto() {
    }

    /** A country or a state as the store answers it. */
    public static final class Reference extends Dto {

        public Long id;

        public String name;

        public String code;
    }

    /** The order's {@code customer} section, sent as {@code customer_summary} when flat. */
    public static final class OrderCustomer extends Dto {

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

    /** A billing or shipping address. */
    public static final class OrderAddress extends Dto {

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

    /** One order line, under {@code items} or {@code order_line_details}. */
    public static final class OrderItem extends Dto {

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

    /** The {@code shipping} section; {@code shipping_type} is the first shipment's method. */
    public static final class OrderShipping extends Dto {

        public String deliveryType;

        public String shippingType;

        public String shippingStatus;

        public Double shippingCost;

        public Double shippingTax;
    }

    public static final class OrderDiscounts extends Dto {

        public Double totalDiscount;

        public String couponCode;

        public String giftCardCode;
    }

    /** {@code charges_totals}: a payment refund lowers {@code paid}; an order refund does not. */
    public static final class OrderChargesTotals extends Dto {

        public Double subTotal;

        public Double totalTax;

        public Double handlingCost;

        public Double totalSurcharge;

        public Double otherCharges;

        public Double grandTotal;

        public Double paid;

        public Double due;

        public Double refunded;

        public String refundStatus;

        public Long itemsTotal;
    }

    /** One entry of an order's {@code payments}; {@code gateway_response} comes only flat. */
    public static final class OrderPayment extends Dto {

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

    public static final class OrderFulfillment extends Dto {

        public String fulfillmentMode;

        public String fulfillmentTerm;

        public String invoiceMode;

        public String paymentTerm;

        public String customerPoNumber;

        public String externalReferenceOrderId;

        public String externalReferenceStoreId;

        public String externalReferenceOrderNo;

        public String externalReferenceStatus;

        public String externalReferenceUrl;

        public List<Object> customFields;
    }

    /** One {@code group_meta} entry. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block; the listing's has no entry for the two addresses. */
    public static final class GroupMeta extends Dto {

        public SectionMeta customer;

        public SectionMeta billingAddress;

        public SectionMeta shippingAddress;

        public SectionMeta items;

        public SectionMeta shipping;

        public SectionMeta discounts;

        public SectionMeta chargesTotals;

        public SectionMeta payments;

        public SectionMeta fulfillment;
    }

    /** One order in sections: the row read, a {@code PUT} or {@code PATCH} answer, a listed row. */
    public static final class Order extends Dto {

        public Long orderId;

        public Long orderNo;

        public Long internalId;

        /** A UUID string the store assigns; not writable. */
        public String uuid;

        public String orderStatus;

        public String paymentStatus;

        public String shippingStatus;

        public String currencyCode;

        public String transactionNo;

        public String ipAddress;

        public String orderChannel;

        public String createdOn;

        public String lastUpdatedOn;

        public OrderCustomer customer;

        public OrderAddress billingAddress;

        public OrderAddress shippingAddress;

        public List<OrderItem> items;

        public OrderShipping shipping;

        public OrderDiscounts discounts;

        public OrderChargesTotals chargesTotals;

        public List<OrderPayment> payments;

        public OrderFulfillment fulfillment;

        /** Read from beside the envelope on the row path. */
        public GroupMeta groupMeta;

        /** The schema {@code field_metadata=true} adds beside the envelope. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "order";
        }
    }

    /** The listing's ten-key {@code pagination}; {@code limit} is the size applied, at most 20. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Long count;

        public Long currentPage;

        public Long totalPages;

        public Boolean hasNext;

        public Boolean hasPrevious;

        public String previousPage;

        public String nextPage;
    }

    /** One page of the listing, with the {@code field_metadata} block when it was asked for. */
    public static final class OrderPage extends Dto {

        public List<Order> orders;

        public Pagination pagination;

        public GroupMeta groupMeta;

        public Object fieldMetadata;
    }

    /** The count sub-path's body, {@code {"orders": {"count": N}}}. */
    public static final class OrderCount extends Dto {

        public Long count;

        @Override
        protected String envelopeKey() {
            return "orders";
        }
    }

    /** One order flat: a create's, a cancel's or a status move's answer, and a customer's row. */
    public static final class FlatOrder extends Dto {

        public Long orderId;

        public Long orderNo;

        public Long internalId;

        public String uuid;

        public String transactionNo;

        public String currencyCode;

        public String orderStatus;

        public String paymentStatus;

        /** {@code unshipped} where the sectioned order reads {@code not_shipped}. */
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

        public OrderCustomer customerSummary;

        public List<OrderItem> orderLineDetails;

        public List<OrderPayment> payments;

        public String externalReferenceOrderId;

        public String externalReferenceStoreId;

        public String externalReferenceOrderNo;

        public String externalReferenceStatus;

        public String externalReferenceUrl;

        public String customerPoNumber;

        public String paymentTerm;

        public String fulfillmentMode;

        public String fulfillmentTerm;

        public String invoiceMode;

        public OrderAddress billingAddress;

        public OrderAddress shippingAddress;

        public List<Object> customFields;

        /** Beside the envelope on a create's answer only. */
        public String status;

        @Override
        protected String envelopeKey() {
            return "order";
        }
    }

    /** One customer's orders, unpaged, cancelled ones left out. */
    public static final class CustomerOrders extends Dto {

        public Long customerId;

        public Long internalId;

        public List<FlatOrder> orders;
    }

    /** What the addresses sub-path answers: both addresses as they now stand. */
    public static final class OrderAddresses extends Dto {

        public Long orderId;

        public Long internalId;

        public OrderAddress billingAddress;

        public OrderAddress shippingAddress;
    }

    /** One comment; {@code created} is {@code M/D/YYYY}, two spaces and a time, not ISO 8601. */
    public static final class OrderComment extends Dto {

        public Long id;

        public String content;

        public String adminName;

        public Boolean visibleToCustomer;

        public Boolean admin;

        public String created;

        @Override
        protected String envelopeKey() {
            return "comment";
        }
    }

    /** An order's comments, oldest first and unpaged. */
    public static final class OrderCommentList extends Dto {

        public List<OrderComment> comments;
    }

    /** An order's custom field values; {@code order_id} echoes the {@code internal_id} sent. */
    public static final class OrderCustomFields extends Dto {

        public Long orderId;

        public List<Object> customFields;

        /** Sent for a {@code delivery} order only. */
        public Double deliveryCost;
    }

    /** The customer a payment receipt names; {@code id} is the public customer id. */
    public static final class PaymentReceiptCustomer extends Dto {

        public Long id;

        public Long internalId;

        public String name;
    }

    /** What recording a payment answers; {@code id} is the {@code payment_id} a refund takes. */
    public static final class PaymentReceipt extends Dto {

        public String status;

        public Long id;

        public String date;

        public String transactionDate;

        public String transactionReference;

        public Double paymentApplied;

        public String paymentMethod;

        public String payerInfo;

        public Long orderId;

        public Long orderInternalId;

        public Double orderTotal;

        public Double dueAmount;

        public Double paidAmount;

        public PaymentReceiptCustomer customer;

        public String createdBy;

        public String createdAt;

        @Override
        protected String envelopeKey() {
            return "payment";
        }
    }

    /** What an order refund answers; {@code amount} is the text that was sent. */
    public static final class OrderRefund extends Dto {

        public String status;

        public Long refundId;

        public Long orderId;

        public Long internalId;

        public String amount;
    }

    /**
     * One order line a refund was spread over; the money fields are decimal text such as
     * {@code "1.00"}. {@code ext_item_referer} was {@code null} on every line read, so it is not
     * typed.
     */
    public static final class OrderRefundRecordLine extends Dto {

        public Long orderItemId;

        public String productSku;

        public String productName;

        public Object extItemReferer;

        public Boolean pluginLine;

        public Long quantity;

        public String linePaid;

        public String amount;

        public String lineRefundedTotal;

        public Boolean lineFullyRefunded;
    }

    /**
     * One refund an order holds. {@code order_id} is the order's internal id. A payment refund
     * fills {@code record_only} to {@code lines}; an order refund leaves them {@code null} and
     * {@code lines} empty. {@code gateway_charge_id}, {@code gateway_refund_id} and
     * {@code failure_cause} were {@code null} on every refund read, so they are not typed.
     */
    public static final class OrderRefundRecord extends Dto {

        public String gateway;

        public Object gatewayChargeId;

        public Object gatewayRefundId;

        public Boolean recordOnly;

        public String initiatedBy;

        public Boolean cancelledWithOrder;

        public Boolean notifyCustomer;

        public String allocation;

        public String otherAmount;

        public List<OrderRefundRecordLine> lines;

        public Long refundId;

        public Long paymentId;

        public Long orderId;

        public Double amount;

        public String status;

        public String transactionStatus;

        public String transactionId;

        public String reason;

        public Object failureCause;

        public String createdAt;

        public String updatedAt;
    }

    /**
     * One page of an order's refunds, newest first. {@code order_id} is the order's internal id
     * and {@code order_no} the {@code {order_id}} the path took.
     */
    public static final class OrderRefundPage extends Dto {

        public Long orderId;

        public Long orderNo;

        public List<OrderRefundRecord> refunds;

        public Pagination pagination;
    }

    /** What a payment refund answers; {@code order_id} holds the order's internal id. */
    public static final class PaymentRefund extends Dto {

        public String status;

        public Long refundId;

        public Long paymentId;

        public Long orderId;

        public Long orderNo;

        public String currency;

        public Double amount;

        public String reason;

        public Boolean gatewayRefund;

        public String gatewayTransactionId;

        public String transactionStatus;

        public String paymentStatus;

        public String paymentRefundStatus;

        public String orderRefundStatus;

        public Double orderRefundedTotal;

        public Double orderAdjustment;

        public Double orderTotal;

        public Double availableForRefund;

        public Boolean adjustWithOrder;

        public Boolean refundEmailQueued;

        public Boolean inventoryReadjusted;

        public String refundedAt;

        public Boolean idempotentReplay;
    }

    /** What sending an invoice answers: who it went to and when. */
    public static final class InvoiceDispatch extends Dto {

        public String status;

        public Long orderId;

        public Long internalId;

        public String sentTo;

        public String template;

        public Object invoiceDocument;

        public String sentAt;
    }

    /** One shipment as the shipment status lists it. */
    public static final class ShipmentTracking extends Dto {

        public String shippingProvider;

        public String trackingNumber;

        public String lastUpdated;
    }

    /** The shipment status: {@code unshipped}, {@code partial} or {@code shipped}. */
    public static final class ShipmentStatus extends Dto {

        public Long orderId;

        public Long internalId;

        public String shipmentStatus;

        public List<ShipmentTracking> shipmentsInfo;
    }

    /** One line a shipment carries. */
    public static final class ShipmentItem extends Dto {

        public Long orderItemId;

        public Long productId;

        public String productName;

        public String productSku;

        public String shippingClass;

        public Long orderQuantity;

        public Long shippedQuantity;

        public Double totalAmount;
    }

    /** One shipment. */
    public static final class OrderShipment extends Dto {

        public Long shippingId;

        public Long orderId;

        public Long orderInternalId;

        public Long customerId;

        public Long customerInternalId;

        public Long orderAddressId;

        public String dateCreated;

        public String trackingNumber;

        public String shippingMethod;

        public OrderAddress shippingAddress;

        public OrderAddress billingAddress;

        public List<ShipmentItem> items;

        /** Beside the envelope on a write's answer. */
        public String status;

        @Override
        protected String envelopeKey() {
            return "shipment";
        }
    }

    /** An order's shipments, unpaged. */
    public static final class OrderShipmentList extends Dto {

        public List<OrderShipment> shipments;
    }

    /** An address to write, replacing the whole one: a field left out reads {@code ""}. */
    public static final class OrderAddressInput extends Dto {

        public String firstName;

        public String lastName;

        public String email;

        public String companyName;

        public String addressLine1;

        public String addressLine2;

        public String city;

        public String postCode;

        public String phone;

        public String mobile;

        public String fax;

        public String countryCode;

        public String stateCode;
    }

    /** One line to order, by the product's {@code id}. */
    public static final class OrderProductInput extends Dto {

        public Long productId;

        public Long quantity;
    }

    /** The create body, no envelope; {@code payment_gateway} takes a code such as {@code PIS}. */
    public static class OrderInput extends Dto {

        public Long customerId;

        public String paymentGateway;

        public String deliveryType;

        public Boolean isSendEmail;

        public String transactionNo;

        public String externalReferenceOrderId;

        public OrderAddressInput billingAddress;

        public OrderAddressInput shippingAddress;

        public List<OrderProductInput> products;

        public String orderChannel;

        public String ipAddress;
    }

    /** The payment a create with payment records, filed under method {@code API}. */
    public static final class InitialPaymentInput extends Dto {

        public Double amount;

        public Double surcharge;

        public String status;

        public String paymentMethod;

        public String trackInfo;

        public String payerInfo;

        public String transactionReference;

        public String transactionDate;
    }

    /** The create with payment body: a create's fields and the {@code payment} it records. */
    public static final class OrderWithPaymentInput extends OrderInput {

        public InitialPaymentInput payment;
    }

    /** The {@code PUT} body; it writes the customer and the two addresses, and nothing else. */
    public static final class OrderUpdateInput extends Dto {

        public Long customerId;

        public OrderAddressInput billingAddress;

        public OrderAddressInput shippingAddress;

        @Override
        protected String envelopeKey() {
            return "order";
        }
    }

    /** The {@code PATCH} body; an address sent replaces that address. */
    public static final class OrderPatchInput extends Dto {

        public String orderStatus;

        public String customerPoNumber;

        public String paymentTerm;

        public OrderAddressInput billingAddress;

        public OrderAddressInput shippingAddress;

        public String orderChannel;

        public String ipAddress;

        @Override
        protected String envelopeKey() {
            return "order";
        }
    }

    /** The addresses sub-path's body; at least one address, and one left out is untouched. */
    public static final class OrderAddressesInput extends Dto {

        public String transactionNo;

        public OrderAddressInput billingAddress;

        public OrderAddressInput shippingAddress;
    }

    /** The cancel body; every field is optional and an empty request cancels too. */
    public static final class OrderCancelInput extends Dto {

        public String transactionNo;

        public String reason;

        public Boolean refundAutomatically;

        public Boolean isGatewayRefund;
    }

    /** The comment body, with no envelope; {@code content} is required. */
    public static final class OrderCommentInput extends Dto {

        public String content;

        public Boolean saveAndSend;

        public String transactionNo;
    }

    /** The payment body: {@code {"payment": {...}}} with {@code transaction_no} beside it. */
    public static final class OrderPaymentInput extends Dto {

        public Double amount;

        public String paymentMethod;

        public String paymentStatus;

        public String trackInfo;

        public String payerInfo;

        public String transactionDate;

        public String transactionReference;

        public Double surcharge;

        public String transactionNo;

        @Override
        protected String envelopeKey() {
            return "payment";
        }

        @Override
        protected Set<String> topLevel() {
            return Set.of("transaction_no");
        }
    }

    /** One line an order refund names, by the order line's {@code item_id}. */
    public static final class RefundLineInput extends Dto {

        public Long orderItemId;

        public Long quantity;

        public Double amount;
    }

    /** The order refund body, wrapped in {@code refund}; {@code refund_date} is required. */
    public static final class OrderRefundInput extends Dto {

        public String amount;

        public String refundDate;

        public String reason;

        public List<RefundLineInput> lineItems;

        @Override
        protected String envelopeKey() {
            return "refund";
        }
    }

    /** The payment refund body: {@code {"refund": {...}}} with {@code transaction_no} beside it. */
    public static final class PaymentRefundInput extends Dto {

        public String amount;

        public String reason;

        public Boolean isGatewayRefund;

        public Boolean sendRefundEmail;

        public Boolean adjustWithOrder;

        public String transactionNo;

        @Override
        protected String envelopeKey() {
            return "refund";
        }

        @Override
        protected Set<String> topLevel() {
            return Set.of("transaction_no");
        }
    }

    /** One line to ship, by the order line's {@code item_id}. */
    public static final class ShipmentItemInput extends Dto {

        public Long orderItemId;

        public Long quantity;
    }

    /** The shipment body; a create needs {@code method}, {@code shipping_date} and items. */
    public static final class OrderShipmentInput extends Dto {

        public String transactionNo;

        public String method;

        public String trackingInfo;

        public String shippingDate;

        public List<ShipmentItemInput> shipmentItems;

        public String changeNote;
    }

    /** The status body; {@code status} is required, one of the six order statuses. */
    public static final class OrderStatusInput extends Dto {

        public String status;

        public String transactionNo;

        public Boolean refundAutomatically;
    }

    /** The custom status body; {@code order_id} is required. */
    public static final class CustomOrderStatusInput extends Dto {

        public Long orderId;

        public String orderStatusValue;

        public String orderStatusLabel;

        public String orderType;

        public String statusType;
    }
}
