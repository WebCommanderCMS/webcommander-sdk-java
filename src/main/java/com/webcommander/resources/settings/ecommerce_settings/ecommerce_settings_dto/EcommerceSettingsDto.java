package com.webcommander.resources.settings.ecommerce_settings.ecommerce_settings_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the ecommerce settings bodies. */
public final class EcommerceSettingsDto {

    private EcommerceSettingsDto() {
    }

    /** The {@code continue_shopping} section: where the continue-shopping link leads. */
    public static final class ContinueShopping extends Dto {

        public Boolean enableContinueShopping;

        public String continueShoppingTarget;

        public String continueShoppingSpecifiedTarget;

        @Override
        protected String envelopeKey() {
            return "continue_shopping";
        }
    }

    /** The {@code product_inventory} section: storefront behaviour around stock levels. */
    public static final class ProductInventory extends Dto {

        public Boolean showOutOfStockProducts;

        public Boolean lowStockNotification;

        public Boolean outOfStockNotification;

        public Boolean hideOnSaleTagForOutOfStockProduct;

        public String orderQuantityOverStock;

        public String updateStock;

        @Override
        protected String envelopeKey() {
            return "product_inventory";
        }
    }

    /** The {@code pricing} section: purchase floors, credit and payment terms. */
    public static final class Pricing extends Dto {

        public Boolean updatePriceRange;

        public Boolean enableMinimumPurchaseAmount;

        public Double minimumPurchaseAmount;

        public Boolean enableMinimumPurchaseAmountInCart;

        public Double minimumPurchaseAmountInCart;

        public Boolean enableOnAccountPurchase;

        public Boolean assignCreditLimitPerOrder;

        public Double creditLimitPerOrder;

        public Boolean enablePaymentTerms;

        public String paymentTerms;

        public Boolean enableCustomerPriceRestriction;

        @Override
        protected String envelopeKey() {
            return "pricing";
        }
    }

    /** The {@code product_restriction} section: a single switch. */
    public static final class ProductRestriction extends Dto {

        public Boolean enableProductRestrictionBasedOnParentCategory;

        @Override
        protected String envelopeKey() {
            return "product_restriction";
        }
    }

    /** The {@code order_filters} section: the status each admin order list opens on. */
    public static final class OrderFilters extends Dto {

        public String defaultOrderStatus;

        public String defaultPaymentStatus;

        public String defaultShipmentStatus;

        @Override
        protected String envelopeKey() {
            return "order_filters";
        }
    }

    /** One row of {@code location_settings.locations}, a previewed stock location. */
    public static final class InventoryLocation extends Dto {

        public Long id;

        public String name;

        public String address;

        public Boolean active;

        public Boolean Default;

        public String createdAt;

        public String updatedAt;
    }

    /** The {@code location_settings} section: the multi-location switch and a location preview. */
    public static final class LocationSettings extends Dto {

        public Boolean enableMultiLocation;

        public List<InventoryLocation> locations;

        @Override
        protected String envelopeKey() {
            return "location_settings";
        }
    }

    /** One row of {@code order_flags}, a flag as the facade inlines it. */
    public static final class OrderFlag extends Dto {

        public Long id;

        public String name;

        public String color;

        public String description;

        public String type;
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;

        public String locationsHref;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per key the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta continueShopping;

        public SectionMeta productInventory;

        public SectionMeta pricing;

        public SectionMeta productRestriction;

        public SectionMeta orderFilters;

        public SectionMeta locationSettings;

        public SectionMeta orderFlags;
    }

    /** The ecommerce settings, as the facade or one section sends them. */
    public static final class EcommerceSettings extends Dto {

        public ContinueShopping continueShopping;

        public ProductInventory productInventory;

        public Pricing pricing;

        public ProductRestriction productRestriction;

        public OrderFilters orderFilters;

        public LocationSettings locationSettings;

        public List<OrderFlag> orderFlags;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "ecommerce_settings";
        }
    }
}
