package com.webcommander.resources.customer_groups.customer_groups_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the customer group bodies. */
public final class CustomerGroupsDto {

    private CustomerGroupsDto() {
    }

    /** The shipping profile a group names; a write sends the bare {@code id}, not this record. */
    public static final class CustomerGroupShippingProfile extends Dto {

        public Long id;

        public String name;
    }

    /** The stored image; a write sends a base64 or {@code data:} URI string, null removes it. */
    public static final class CustomerGroupBackgroundImage extends Dto {

        public String url;

        public Long width;

        public Long height;
    }

    /** An {@code enabled} switch and its {@code amount}. */
    public static final class CustomerGroupAmountSetting extends Dto {

        public Boolean enabled;

        public Double amount;
    }

    /** {@code terms} is written {@code NET_30}, {@code NET_60} or {@code NET_90}. */
    public static final class CustomerGroupPaymentTerms extends Dto {

        public Boolean enabled;

        public String terms;
    }

    public static final class CustomerGroupOnAccountPurchase extends Dto {

        public Boolean enabled;

        public CustomerGroupAmountSetting maxPurchase;

        public CustomerGroupAmountSetting creditLimit;

        public CustomerGroupPaymentTerms paymentTerms;
    }

    public static final class CustomerGroupPurchasePreference extends Dto {

        public CustomerGroupAmountSetting minimumPurchase;

        public CustomerGroupOnAccountPurchase onAccountPurchase;
    }

    /** {@code base_price_rounding} is {@code nearest}, {@code up} or {@code down}. */
    public static final class CustomerGroupTaxConfiguration extends Dto {

        public Boolean overrideStoreTax;

        public Boolean taxExempt;

        public String taxMessage;

        public Boolean showPricesWithTaxIncluded;

        public Boolean pricesEnteredWithTaxIncluded;

        public String basePriceRounding;
    }

    /** {@code price_display_method} is {@code lowest} or {@code highest}. */
    public static final class CustomerGroupProductDisplay extends Dto {

        public Boolean overwriteDefault;

        public String variationDisplayType;

        public Boolean displayParentImage;

        public Boolean activateColourVariationImage;

        public Boolean showSelectedVariationValue;

        public Boolean strikeThroughOutOfStock;

        public Boolean singleClickVariationLoading;

        public String priceDisplayMethod;
    }

    /** One member, by its {@code customer_id}: a {@code customers} entry and the member read. */
    public static final class CustomerGroupMember extends Dto {

        public Long id;

        public String firstName;

        public String lastName;

        public String email;

        @Override
        protected String envelopeKey() {
            return "customer";
        }
    }

    /** One group: a listing row, the row read and every write's answer. */
    public static final class CustomerGroup extends Dto {

        public Long id;

        public String name;

        public String description;

        public String status;

        public String defaultTaxCode;

        public Boolean addAsNewCustomerDefaultGroup;

        public CustomerGroupShippingProfile shippingProfile;

        public CustomerGroupBackgroundImage backgroundImage;

        public CustomerGroupPurchasePreference purchasePreference;

        public CustomerGroupTaxConfiguration taxConfiguration;

        public CustomerGroupProductDisplay productDisplay;

        public String createdAt;

        public String updatedAt;

        public Long customerCount;

        public List<CustomerGroupMember> customers;

        /** The store's field schema, present only when {@code field_metadata=true} was sent. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "customer_group";
        }
    }

    /** The five-key {@code pagination} block; {@link #limit} is the page size applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing, with the {@code field_metadata} block when it was asked for. */
    public static final class CustomerGroupPage extends Dto {

        public List<CustomerGroup> customerGroups;

        public Pagination pagination;

        public Object fieldMetadata;
    }

    /** The count sub-path's body, a bare {@code {"total": N}}. */
    public static final class CustomerGroupCount extends Dto {

        public Long total;
    }

    /** The assign answer: {@link #added} is {@code false} for a customer already a member. */
    public static final class CustomerGroupAssignment extends Dto {

        public String status;

        public Long customerId;

        public String email;

        public CustomerGroup group;

        public Boolean added;
    }

    /** The writable form of {@link CustomerGroup}, for the create and the patch. */
    public static final class CustomerGroupInput extends Dto {

        public String name;

        public String description;

        public String status;

        public String defaultTaxCode;

        public Boolean addAsNewCustomerDefaultGroup;

        public Long shippingProfile;

        public String backgroundImage;

        public CustomerGroupPurchasePreference purchasePreference;

        public CustomerGroupTaxConfiguration taxConfiguration;

        public CustomerGroupProductDisplay productDisplay;

        @Override
        protected String envelopeKey() {
            return "customer_group";
        }
    }

    /** The membership body: the whole member list, replacing the stored one; empty clears it. */
    public static final class CustomerGroupCustomersInput extends Dto {

        /** {@code customer_id} values, as numbers or as strings. */
        public List<Object> customers;
    }

    /** The assign body, both ids bare: {@link #customer} is a {@code customer_id}. */
    public static final class CustomerGroupAssignInput extends Dto {

        public Object customer;

        public Object group;
    }
}
