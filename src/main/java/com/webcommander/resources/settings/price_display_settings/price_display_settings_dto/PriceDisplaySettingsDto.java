package com.webcommander.resources.settings.price_display_settings.price_display_settings_dto;

import com.webcommander.Dto;

/** Typed views over the price display settings bodies. */
public final class PriceDisplaySettingsDto {

    private PriceDisplaySettingsDto() {
    }

    /** The storefront surfaces one setting applies to. */
    public static final class DisplayScopes extends Dto {

        public Boolean shoppingCart;

        public Boolean checkout;

        public Boolean orderSuccess;
    }

    /** A setting with a master switch, {@link #enabled}, and optional per-surface control. */
    public static final class ScopedToggle extends Dto {

        public Boolean enabled;

        public Boolean customize;

        public DisplayScopes scopes;
    }

    /** A setting with nothing but a master switch. */
    public static final class PlainToggle extends Dto {

        public Boolean enabled;
    }

    /** The {@code subtotal} block: where the subtotal line shows, and whether it carries tax. */
    public static final class Subtotal extends Dto {

        public DisplayScopes show;

        public ScopedToggle includeTax;

        public ScopedToggle includeDiscount;
    }

    /** The {@code discount} block: where the discount line shows, and whether it hides at zero. */
    public static final class Discount extends Dto {

        public DisplayScopes show;

        public ScopedToggle hideIfZero;

        public ScopedToggle includeTax;
    }

    /** The {@code shipping_cost} block: where the shipping line shows, and whether it has tax. */
    public static final class ShippingCost extends Dto {

        public DisplayScopes show;

        public ScopedToggle includeTax;

        public ScopedToggle includeDiscount;
    }

    /** The {@code shipping_discount} block: where the shipping discount line is shown. */
    public static final class ShippingDiscount extends Dto {

        public DisplayScopes show;

        public ScopedToggle includeTax;

        public PlainToggle shippingTax;
    }

    /** The {@code handling} block: whether handling is shown apart from shipping. */
    public static final class Handling extends Dto {

        public DisplayScopes showSeparately;

        public ScopedToggle includeTax;

        public ScopedToggle handlingTax;
    }

    /** The {@code pricing_info} block: a note shown after the total. */
    public static final class PricingInfo extends Dto {

        public Boolean enabled;

        public String text;

        public Boolean customize;

        public DisplayScopes scopes;

        public Boolean doNotShowDueAmount;
    }

    /** The {@code order_item} section: how a line item's own price is displayed. */
    public static final class OrderItem extends Dto {

        public ScopedToggle priceIncludingTax;

        public ScopedToggle priceIncludingDiscount;

        public ScopedToggle taxInLine;

        public ScopedToggle discountInLineItem;

        public ScopedToggle itemTotalIncludingTax;

        @Override
        protected String envelopeKey() {
            return "order_item";
        }
    }

    /** The {@code order_summary} section: which lines the summary panel shows. */
    public static final class OrderSummary extends Dto {

        public ScopedToggle orderTotalExcludingTax;

        public ScopedToggle orderTotalIncludingDiscount;

        public Subtotal subtotal;

        public Discount discount;

        public PlainToggle subtotalTax;

        public PlainToggle totalTax;

        public PlainToggle orderTotal;

        public ShippingCost shippingCost;

        public ShippingDiscount shippingDiscount;

        public Handling handling;

        public ScopedToggle paymentSurcharge;

        public PricingInfo pricingInfo;

        @Override
        protected String envelopeKey() {
            return "order_summary";
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

        public SectionMeta orderItem;

        public SectionMeta orderSummary;
    }

    /** The price display settings, as the facade or one section sends them. */
    public static final class PriceDisplaySettings extends Dto {

        public OrderItem orderItem;

        public OrderSummary orderSummary;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "price_display_settings";
        }
    }
}
