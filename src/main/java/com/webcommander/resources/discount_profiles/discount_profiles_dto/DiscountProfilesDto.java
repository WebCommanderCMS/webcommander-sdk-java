package com.webcommander.resources.discount_profiles.discount_profiles_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the discount profile bodies. */
public final class DiscountProfilesDto {

    private DiscountProfilesDto() {
    }

    /** The block an {@code amount} profile carries; {@link #tiers} reads as tier ids. */
    public static final class AmountDetails extends Dto {

        public String type;

        public String applyTo;

        public Double singleAmount;

        public String singleAmountType;

        public String minimumAmountOn;

        public List<Long> tiers;
    }

    /** The block a {@code shipping} profile carries. */
    public static final class ShippingDetails extends Dto {

        public String type;

        public String applyTo;

        public String minimumQtyOn;

        public String amountType;

        public String singleAmountType;

        public Long minimumQty;

        public Double minimumAmount;

        public Object singleAmount;

        public Object capAmount;

        public Object maximumTime;

        public Object zone;

        public Object shippingClass;

        public List<Object> tiers;
    }

    /** The block a {@code product} profile carries. */
    public static final class ProductDetails extends Dto {

        public String type;

        public String freeProductMinQtyOn;

        public Long freeProductMaxQty;

        public Double freeProductMinAmount;

        public String minimumQtyOn;

        public String amountType;

        public String singleAmountType;

        public Object singleAmount;

        public Object capPrice;

        public Object capPriceMaxQty;

        public List<Object> productIds;

        public List<Object> categoryIds;

        public List<Object> tiers;
    }

    /** One generated coupon; {@link #customerEmail} is empty until a customer is assigned. */
    public static final class CustomerCoupon extends Dto {

        public Long id;

        public String code;

        public String customerEmail;
    }

    /** One discount profile, carrying the one details block {@link #discountDetailsType} names. */
    public static final class DiscountProfile extends Dto {

        public Long id;

        public String uuid;

        public String name;

        public String type;

        public String discountDetailsType;

        public AmountDetails amountDetails;

        public ShippingDetails shippingDetails;

        public ProductDetails productDetails;

        public Boolean isActive;

        /** Read back shifted to UTC with no zone; {@code null} when unset. */
        public String startDate;

        public Boolean isSpecifyEndDate;

        /** Read back shifted to UTC with no zone; empty when unset. */
        public String endDate;

        public Boolean isApplyCouponCode;

        public String defaultCouponCode;

        public Boolean isCouponCodeAutoGenerate;

        public Boolean isCreateUniqueCouponEachCustomer;

        public Boolean isImportedCoupon;

        public Boolean isMaximumUseTotal;

        public Long maximumUseCount;

        public Boolean isMaximumUseCustomer;

        public Long maximumUseCustomerCount;

        public Boolean isMaximumDiscountAllowed;

        public Double maximumDiscountAllowedAmount;

        public Boolean isExcludeProductsOnSale;

        public Boolean isDiscountUsedWithOtherDiscount;

        public Boolean isDisplayDiscountInformationProdDetail;

        public Boolean isDisplayTextCoupon;

        public String displayTextCoupon;

        public Boolean isDisplayTextCart;

        public String displayTextCart;

        public Boolean isDisplayTextPartialDiscountCondition;

        public String displayTextPartialDiscountCondition;

        public String invoiceNote;

        public List<Long> excludeProducts;

        public List<CustomerCoupon> customerCoupons;

        public List<Long> usage;

        public String createdOn;

        public String lastUpdatedOn;

        /** The store's field schema, present only when {@code field_metadata=true} was sent. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "discount_profile";
        }
    }

    /** The writable form of {@link DiscountProfile}; an update resets every field it omits. */
    public static final class DiscountProfileInput extends Dto {

        public String name;

        public String type;

        public String discountDetailsType;

        public AmountDetails amountDetails;

        public ShippingDetails shippingDetails;

        public ProductDetails productDetails;

        public Boolean isActive;

        /** Sent as {@code yyyy-MM-dd HH:mm:ss}. */
        public String startDate;

        public Boolean isSpecifyEndDate;

        /** Sent as {@code yyyy-MM-dd HH:mm:ss}; kept only with {@link #isSpecifyEndDate}. */
        public String endDate;

        public Boolean isApplyCouponCode;

        public String defaultCouponCode;

        public Boolean isCouponCodeAutoGenerate;

        public Boolean isCreateUniqueCouponEachCustomer;

        public Boolean isImportedCoupon;

        public Boolean isMaximumUseTotal;

        public Long maximumUseCount;

        public Boolean isMaximumUseCustomer;

        public Long maximumUseCustomerCount;

        public Boolean isMaximumDiscountAllowed;

        public Double maximumDiscountAllowedAmount;

        public Boolean isExcludeProductsOnSale;

        public Boolean isDiscountUsedWithOtherDiscount;

        public Boolean isDisplayDiscountInformationProdDetail;

        public Boolean isDisplayTextCoupon;

        public String displayTextCoupon;

        public Boolean isDisplayTextCart;

        public String displayTextCart;

        public Boolean isDisplayTextPartialDiscountCondition;

        public String displayTextPartialDiscountCondition;

        public String invoiceNote;

        /** Ids of existing products. */
        public List<Long> excludeProducts;
    }

    /** The listing's ten-key {@code pagination} block; {@link #limit} is the size applied. */
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

    /** The listing's one {@code group_meta} entry: what the collection is, and where. */
    public static final class ResourceMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The listing's {@code group_meta} block, keyed by the collection's own name. */
    public static final class GroupMeta extends Dto {

        public ResourceMeta discountProfiles;
    }

    /** One page of the listing, with the {@code field_metadata} block when it was asked for. */
    public static final class DiscountProfilePage extends Dto {

        public List<DiscountProfile> discountProfiles;

        public Pagination pagination;

        public GroupMeta groupMeta;

        public Object fieldMetadata;
    }

    /** The create-coupons answer: the generated codes with {@code status} and {@code message}. */
    public static final class DiscountProfileCoupons extends Dto {

        public String status;

        public String message;

        public List<String> codes;
    }

    /** The create-coupons body; {@link #requestedCodes} is required and at least 1. */
    public static final class DiscountProfileCouponsInput extends Dto {

        public Long requestedCodes;
    }

    /** The assign-customer body: a generated {@code code} and an existing customer's email. */
    public static final class DiscountCouponCustomerInput extends Dto {

        public String code;

        public String email;
    }
}
