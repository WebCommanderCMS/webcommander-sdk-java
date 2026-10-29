package com.webcommander.resources.products.products_dto;

import com.webcommander.Dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Typed views over the product bodies. */
public final class ProductsDto {

    private ProductsDto() {
    }

    /** The product's slug and its absolute storefront address. */
    public static final class ProductUrls extends Dto {

        public String url;

        public String productUrl;
    }

    /** An {@code id} and a {@code name}: an author, a page, a customer or a customer group. */
    public static final class ProductNamedReference extends Dto {

        public Long id;

        public String name;
    }

    /** One category the product is filed under. */
    public static final class ProductParent extends Dto {

        public Long id;

        public String name;

        public Boolean isInTrash;

        public Boolean isParentInTrash;

        public Boolean isDisposable;
    }

    /**
     * One customer an audience names: {@code id} is the customer's {@code customer_id}, {@code
     * internal_id} its {@code internal_id}.
     */
    public static final class ProductSelectedCustomer extends Dto {

        public Long id;

        public Long internalId;

        public String name;
    }

    /**
     * Who {@code available_for: selected} admits, or whom a restriction names; both lists are empty
     * when nobody is named.
     */
    public static final class ProductSelectedCustomers extends Dto {

        public List<ProductSelectedCustomer> customers;

        public List<ProductNamedReference> groups;
    }

    /**
     * An audience to write: customer and group ids. A group written as {@code {"id": ...}} is
     * {@code 400}.
     */
    public static final class ProductSelectedCustomersInput extends Dto {

        public List<Long> customers;

        public List<Long> groups;
    }

    /** One day of the weekday schedule. */
    public static final class ProductWeekday extends Dto {

        public String day;

        public Long startHour;

        public Long startMinute;

        public Long endHour;

        public Long endMinute;

        public Boolean isFullDay;

        public Boolean enabled;
    }

    /** The weekday schedule: a switch and the seven days. */
    public static final class ProductAvailableOnWeekdays extends Dto {

        public Boolean enabled;

        public List<ProductWeekday> weekdays;
    }

    /** Dimensions as written; the top level of the product repeats the four numbers read-only. */
    public static final class ProductProperties extends Dto {

        public Double length;

        public Double width;

        public Double height;

        public Double weight;

        public String lengthUnit;

        public String weightUnit;
    }

    /** Per-product tax overrides, applied only when {@link #overwriteDefaultConfig} is true. */
    public static final class ProductTaxSettings extends Dto {

        public Boolean overwriteDefaultConfig;

        public Boolean isTaxExempt;

        public Boolean showPriceWithTax;

        public Boolean isPriceWithTax;

        public Boolean systemDefaultTax;

        public String taxMessage;

        public String basePriceRounding;

        public String defaultTaxCode;

        public String customTaxProfileId;
    }

    /**
     * Which variant price a listing shows, and the labels it shows it with. {@code
     * lowest_price_label} and {@code highest_price_label} are second names for the two {@code
     * label_for_*} fields.
     */
    public static final class ProductVariationPriceDisplay extends Dto {

        public String method;

        public String labelForLowestPrice;

        public String labelForHighestPrice;

        public String lowestPriceLabel;

        public String highestPriceLabel;
    }

    /** One location's units in the legacy shape. */
    public static final class ProductWarehouseStock extends Dto {

        /** The location id, as text. */
        public String warehouse;

        public Long stock;
    }

    /** One location's holding of the product; exactly one row of a product holds the default. */
    public static final class ProductStockLocation extends Dto {

        public Long warehouse;

        public String warehouseName;

        public Long stock;

        /** {@code null} when the row was written without one. */
        public Long lowStockLevel;

        public Boolean isDefault;
    }

    /** The pricing and stock block. */
    public static final class ProductPricingAndStock extends Dto {

        public Boolean trackInventory;

        public Long availableStock;

        public Long lowLevelStock;

        public List<ProductWarehouseStock> warehouses;

        public Long minimumOrderQuantity;

        public Long maximumOrderQuantity;

        public Object taxProfile;

        public Object shippingProfile;

        public Boolean enableMultipleOrder;

        public Long multipleOrderQuantity;

        public Boolean onSale;

        public String onSalePriceType;

        public Double onSalePrice;

        public Boolean expectedToPay;

        public Double expectedToPayPrice;

        public Boolean enableCallForPrice;

        public String restrictPriceFor;

        public List<Object> restrictPriceForExceptCustomers;

        public String restrictPurchaseFor;

        public List<Object> restrictPurchaseForSelectedCustomers;

        public Boolean hidePrice;

        public Boolean enableCombinedStockSellingLimit;

        public Long combinedStockSellingLimit;

        public Boolean enableAdvancePricing;

        public List<Object> suppliers;

        public Boolean isMultiLocationEnabled;

        public List<ProductStockLocation> locations;
    }

    /** One product image. */
    public static final class ProductImage extends Dto {

        public Long id;

        public String filename;

        public String altText;

        public String thumbnail;

        public String link;
    }

    /** One product video, on the product or on a variant. */
    public static final class ProductVideo extends Dto {

        public Long id;

        public String filename;

        public String thumbnail;

        public String link;
    }

    /** The media block. */
    public static final class ProductImageAndVideo extends Dto {

        public List<ProductImage> images;

        public List<ProductVideo> videos;

        public List<Object> specs;
    }

    /** Identifiers and origin: {@code mpn}, {@code upc}, {@code barcode}, {@code hs_code}. */
    public static final class ProductAdvanceInformation extends Dto {

        public String globalTradeItemNumber;

        public Boolean enableProductIdentifiers;

        public String gtin;

        public String mpn;

        public String upc;

        public String barcode;

        public String countryOfOrigin;

        public String hsCode;

        public String productCondition;

        public String model;
    }

    /** A flag and the label shown with it. */
    public static final class ProductFlag extends Dto {

        public Boolean enabled;

        public String label;
    }

    /**
     * The flags in blocks: {@code is_featured}, {@code is_new}, {@code sold_out} and {@code model}
     * again. {@code sold_out} is read-only.
     */
    public static final class ProductFlags extends Dto {

        public ProductFlag featured;

        public Boolean newProduct;

        public ProductFlag soldOut;

        public String modelNumber;
    }

    /** {@code product_properties} again; the two units are read-only here. */
    public static final class ProductDimension extends Dto {

        public String lengthUnit;

        public String weightUnit;

        public Double width;

        public Double height;

        public Double length;

        public Double weight;
    }

    /**
     * {@code advance_information} again: a write here changes it there, {@code condition} being its
     * {@code product_condition}.
     */
    public static final class ProductIdentifiers extends Dto {

        public Boolean enabled;

        public String gtin;

        public String condition;

        public String barcode;

        public String hsCode;

        public String countryOfOrigin;

        public String mpn;

        public String upc;
    }

    /** {@code webtool_disable_tracking} again, read-only. */
    public static final class ProductWebtool extends Dto {

        public Boolean disableTracking;
    }

    /** The advanced block. */
    public static final class ProductAdvanced extends Dto {

        public ProductAdvanceInformation advanceInformation;

        public Boolean webtoolDisableTracking;

        public List<Object> metaTags;

        public ProductFlags productFlags;

        public ProductDimension productDimension;

        public ProductIdentifiers productIdentifiers;

        public ProductWebtool webtool;
    }

    /**
     * A related, similar or up-sell product. A write takes {@code {"id": ...}}; a list of bare ids
     * in {@code up_sell_products} is answered {@code 200} and leaves it empty.
     */
    public static final class ProductLink extends Dto {

        public Long id;

        public String name;

        public String url;

        public String status;

        public String sku;

        public Boolean inStock;

        public Double price;
    }

    /** The bundle block; {@code bundle} is null on a product that is not a bundle. */
    public static final class ProductBundle extends Dto {

        public Boolean priceFixed;

        public Boolean quantityFlexible;

        public List<Object> items;
    }

    /** One SEO setting on the product. */
    public static final class ProductSeoConfig extends Dto {

        public Long id;

        public String value;

        public String type;

        public String configKey;

        public List<Object> metaTags;
    }

    /** One value of a product tag. */
    public static final class ProductTagValue extends Dto {

        public Long id;

        public String title;

        public String value;

        public String url;
    }

    /** One tag on the product, with its values. */
    public static final class ProductTag extends Dto {

        public Long id;

        public Long tagId;

        public String name;

        public List<ProductTagValue> values;

        public Boolean displayInProductPage;

        public Boolean enableExpandedView;
    }

    /** One custom property. */
    public static final class ProductCustomProperty extends Dto {

        public Long id;

        public String label;

        public String description;
    }

    /** Who wrote a review; {@code name} and {@code email} again. */
    public static final class ProductReviewer extends Dto {

        public String name;

        public String email;
    }

    /**
     * One review. {@code product_id}, {@code comment} and {@code created_at} repeat {@code
     * productId}, {@code review} and {@code date}; {@code created_at} carries a {@code Z}.
     */
    public static final class ProductReview extends Dto {

        public Long id;

        /** The wire key is the camel-case {@code productId}. */
        public Long productId;

        public String name;

        public String email;

        public String review;

        public Double rating;

        public String date;

        public String status;

        public List<Object> files;

        public ProductReviewer reviewer;

        public String comment;

        public String createdAt;
    }

    /** One option of a variation type the product uses. */
    public static final class ProductVariationTypeOption extends Dto {

        public Long id;

        public String label;

        public String value;

        public Boolean Default;
    }

    /** One variation type the product uses, with its options. */
    public static final class ProductVariationType extends Dto {

        public Long id;

        public String name;

        public String standard;

        public List<ProductVariationTypeOption> options;
    }

    /** The type one option of a combination belongs to. */
    public static final class ProductCombinationOptionType extends Dto {

        public Long id;

        public String role;

        public String name;

        public String standard;

        public Boolean isDisposable;
    }

    /** One option of a combination; {@link #type} is a list of one type. */
    public static final class ProductCombinationOption extends Dto {

        public Long id;

        public Long index;

        public List<ProductCombinationOptionType> type;

        public String label;

        public String value;

        public Boolean colorHasSwatch;

        public String colorSwatchValue;

        public String imageBaseUrl;
    }

    /** One variant image. */
    public static final class ProductCombinationImage extends Dto {

        public Long id;

        public String thumbnail;

        public String url;
    }

    /** A variant's pricing and stock block. */
    public static final class ProductCombinationPriceAndStock extends Dto {

        public Boolean trackInventory;

        public Long availableStock;

        public Long lowLevelStock;

        public Long minimumOrderQuantity;

        public Long maximumOrderQuantity;

        public Long supportedMaxOrderQuantity;

        public Boolean enableMultipleOrder;

        public Long multipleOrderQuantity;

        public Boolean onSale;

        public Boolean soldOut;

        public String soldOutLabel;

        public String onSalePriceType;

        public Double onSalePrice;

        public Boolean expectedToPay;

        public Double expectedToPayPrice;

        public Boolean enableCallForPrice;

        public Boolean hidePrice;
    }

    /** One variant. */
    public static final class ProductCombinationDetails extends Dto {

        public Long variationId;

        public Boolean available;

        public String productName;

        public String sku;

        public String customSku;

        public ProductUrls urls;

        public List<Object> metaTags;

        public Boolean isInventoryEnabled;

        public Long availableStock;

        public Long lowStockLevel;

        public String spec;

        public Boolean isMultipleOrderQuantity;

        public Boolean isOnSale;

        public Boolean isExpectToPay;

        public Boolean isCallForPriceEnabled;

        public Boolean isNew;

        public Boolean isFeatured;

        public String onSaleAmountType;

        public String title;

        public String heading;

        public String summary;

        public String description;

        public String model;

        public String productCondition;

        public String featuredLabel;

        public Double basePrice;

        public Double costPrice;

        public Double compareAtPrice;

        public Double salePrice;

        public Double expectToPayPrice;

        /** This and the next three are a number or text, depending on the product. */
        public Object weight;

        public Object height;

        public Object length;

        public Object width;

        public Double displayPrice;

        public Double previousPrice;

        public ProductCombinationPriceAndStock priceAndStock;

        public List<ProductCombinationImage> images;

        public List<ProductVideo> videos;

        public List<Object> specs;

        public Long supportedMaxOrderQuantity;

        public Boolean isBase;

        public String barcode;

        public String countryOfOrigin;

        public String hsCode;

        public Double tax;

        public String taxMessage;
    }

    /** One option combination and the variant it makes. */
    public static final class ProductCombination extends Dto {

        public List<ProductCombinationOption> options;

        public ProductCombinationDetails details;
    }

    /** The variation block; {@code {}} on a product with no variations, so every field is null. */
    public static final class ProductVariation extends Dto {

        public List<ProductVariationType> types;

        public List<ProductCombination> availableCombinations;
    }

    /**
     * One category the product is filed under, as {@code categories} lists it; {@code parents}
     * again.
     */
    public static final class ProductCategory extends Dto {

        public Long id;

        public String name;

        public Boolean inTrash;

        public Boolean parentInTrash;

        public Boolean disposable;
    }

    /** One image in the media block. */
    public static final class ProductMediaImage extends Dto {

        public Long id;

        public String filename;

        public Long position;

        public Boolean primary;

        public String altText;

        public String url;

        public String thumbnailUrl;
    }

    /** One video in the media block. */
    public static final class ProductMediaVideo extends Dto {

        public Long id;

        public String filename;

        public Long position;

        public String url;

        public String thumbnailUrl;
    }

    /**
     * The media block. A write names files the product already holds, by {@code id}; another id is
     * {@code 400}. {@code specification_file} was {@code null} on every product read, so it is not
     * typed.
     */
    public static final class ProductMedia extends Dto {

        public List<ProductMediaImage> images;

        public List<ProductMediaVideo> videos;

        public Object specificationFile;
    }

    /** One day of the schedule; times are {@code HH:mm}. */
    public static final class ProductWeekdayWindow extends Dto {

        public Boolean enabled;

        public Boolean fullDay;

        public String startTime;

        public String endTime;
    }

    /** The seven days of the schedule, each always present. */
    public static final class ProductScheduleWeekdays extends Dto {

        public ProductWeekdayWindow monday;

        public ProductWeekdayWindow tuesday;

        public ProductWeekdayWindow wednesday;

        public ProductWeekdayWindow thursday;

        public ProductWeekdayWindow friday;

        public ProductWeekdayWindow saturday;

        public ProductWeekdayWindow sunday;
    }

    /**
     * When the product is on sale. {@code type} is {@code schedule_type} ({@code date_range} or
     * {@code custom_schedule}), {@code preset} is {@code custom_schedule_option} ({@code all_days},
     * {@code weekdays}, {@code business_hour}, {@code weekends} or {@code custom}).
     */
    public static final class ProductSchedule extends Dto {

        public Boolean enabled;

        public String type;

        public String startDate;

        public String endDate;

        public String preset;

        public ProductScheduleWeekdays weekdays;
    }

    /** {@code password_protected} and {@code password} again; a set password reads {@code ***}. */
    public static final class ProductPasswordProtection extends Dto {

        public Boolean enabled;

        public String password;
    }

    /**
     * Availability in one block: {@code enabled} is {@code available}, {@code active} is {@code
     * administrative_status}, {@code who_can_view} is {@code available_for}. {@code
     * restrict_for_selected_customers} is read-only.
     */
    public static final class ProductAvailableForPurchase extends Dto {

        public Boolean enabled;

        public Boolean active;

        public String whoCanView;

        public ProductSelectedCustomers selectedCustomers;

        public ProductSelectedCustomers restrictForSelectedCustomers;

        public ProductSchedule schedule;

        public ProductPasswordProtection passwordProtection;
    }

    /**
     * The availability block to write. While {@code enabled} is {@code false} the store answers
     * {@code 200} and keeps none of {@code who_can_view}, {@code schedule} or {@code
     * password_protection}.
     */
    public static final class ProductAvailableForPurchaseInput extends Dto {

        public Boolean enabled;

        public Boolean active;

        public String whoCanView;

        public ProductSelectedCustomersInput selectedCustomers;

        public ProductSchedule schedule;

        public ProductPasswordProtection passwordProtection;
    }

    /** The sale price; {@code amount_type} is {@code on_sale_price_type}. */
    public static final class ProductOnSale extends Dto {

        public Boolean enabled;

        public Double discountedPrice;

        public String amountType;
    }

    /** Order in multiples of {@code multiple_of}. */
    public static final class ProductQuantityMultiple extends Dto {

        public Boolean enabled;

        public Long multipleOf;
    }

    /** The expect-to-pay price. */
    public static final class ProductExpectToPay extends Dto {

        public Boolean enabled;

        public Double price;
    }

    /** The advanced pricing switches. */
    public static final class ProductAdvancedPricing extends Dto {

        public Boolean enabled;

        public ProductQuantityMultiple quantityMultiple;

        public ProductExpectToPay expectToPay;

        public Boolean callForPrice;
    }

    /**
     * Prices in one block; {@code rrp_price} is {@code compare_at_price}. On a create {@code
     * base_price} here stands in for the top-level one.
     */
    public static final class ProductPricing extends Dto {

        public Double basePrice;

        public Double costPrice;

        public Double rrpPrice;

        public ProductOnSale onSale;

        public ProductAdvancedPricing advancedPricing;
    }

    /** Stock tracking; {@code suppliers} echoes the ids written. */
    public static final class ProductInventoryTracking extends Dto {

        public Boolean enabled;

        public Long availableStock;

        public Long lowStockLevel;

        public List<Object> suppliers;
    }

    /** The combined stock selling limit. */
    public static final class ProductCombinedStockLimit extends Dto {

        public Boolean enabled;

        public Long units;
    }

    /** Stock in one block; {@code preorder} is {@code preorder_enabled}. */
    public static final class ProductInventory extends Dto {

        public ProductInventoryTracking tracking;

        public ProductCombinedStockLimit combinedStockLimit;

        public Boolean preorder;
    }

    /** The order quantity limits; a {@code max_quantity} of {@code 0} is no maximum. */
    public static final class ProductOrdering extends Dto {

        public Long minQuantity;

        public Long maxQuantity;
    }

    /**
     * A price or purchase restriction. Only {@code mode} is written; {@code enabled} is read-only
     * and {@code customers} is answered {@code 200} but kept empty.
     */
    public static final class ProductRestriction extends Dto {

        public Boolean enabled;

        public String mode;

        public List<Object> customers;
    }

    /**
     * {@code restrict_price_for} and {@code restrict_purchase_for} in one block; {@code enabled}
     * and {@code display_only} are read-only.
     */
    public static final class ProductRestrictions extends Dto {

        public Boolean enabled;

        public ProductRestriction price;

        public ProductRestriction purchase;

        public Boolean displayOnly;
    }

    /** Whether {@code base_price} includes tax, and how it is rounded. */
    public static final class ProductPriceIncludesTax extends Dto {

        public Boolean enabled;

        public String basePriceRounding;
    }

    /**
     * {@code tax_settings} in one block; {@code enabled} is {@code overwrite_default_config}. A
     * write takes {@code custom_tax_profile} as an id or {@code {"id": ...}} of a custom tax
     * profile.
     */
    public static final class ProductTax extends Dto {

        public Boolean enabled;

        public Boolean exempt;

        public ProductNamedReference customTaxProfile;

        public String message;

        public Boolean showPriceWithTax;

        public ProductPriceIncludesTax priceIncludesTax;
    }

    /**
     * The shipping profile; {@code enabled} is read-only and true while a profile is set. A create
     * drops {@code profile}; an update takes it as an id or {@code {"id": ...}}.
     */
    public static final class ProductShipping extends Dto {

        public Boolean enabled;

        public ProductNamedReference profile;
    }

    /** API paths of the product and its sub-paths, read-only. */
    public static final class ProductLinks extends Dto {

        public String self;

        public String variations;

        public String stock;

        public String reviews;
    }

    /** One product: the row read's body and every write's answer. */
    public static final class Product extends Dto {

        public Long id;

        public String name;

        public String sku;

        public String status;

        public ProductUrls urls;

        public String heading;

        public List<ProductParent> parents;

        public ProductNamedReference createdBy;

        public String createdAt;

        public String updatedAt;

        public String transactionNo;

        public String productType;

        public String customType;

        public Boolean isVirtual;

        public String invoiceNote;

        public Boolean available;

        public ProductNamedReference productPage;

        public Object productLayout;

        public String customClass;

        public Boolean availableOnDateRange;

        public String availableFromDate;

        public String availableToDate;

        public String availableFor;

        public ProductSelectedCustomers selectedCustomers;

        public ProductSelectedCustomers restrictForSelectedCustomers;

        public Boolean passwordProtected;

        /** Reads {@code ***} when one is set and {@code ""} otherwise. */
        public String password;

        public ProductAvailableOnWeekdays availableOnWeekdays;

        public Double basePrice;

        public Double costPrice;

        public Double compareAtPrice;

        public Boolean isOnSale;

        public Double salePrice;

        public Boolean soldOut;

        public Double length;

        public Double width;

        public Double height;

        public Double weight;

        public String soldOutLabel;

        public Boolean isFeatured;

        public String featuredLabel;

        public Boolean isNew;

        public Boolean featured;

        public Boolean New;

        public String model;

        public ProductProperties productProperties;

        public Long version;

        public String createdOn;

        public String lastUpdatedOn;

        public ProductNamedReference lastUpdatedBy;

        public String scheduleType;

        public String customScheduleOption;

        public ProductTaxSettings taxSettings;

        public ProductVariationPriceDisplay variationPriceDisplay;

        public Boolean administrativeStatus;

        public Boolean isDraft;

        public String productSummary;

        public String productDescription;

        public ProductPricingAndStock productPricingAndStock;

        public ProductImageAndVideo imageAndVideo;

        public ProductAdvanced advanced;

        public List<ProductLink> relatedProducts;

        public List<ProductLink> similarProducts;

        public ProductBundle bundle;

        public List<ProductSeoConfig> seoConfigs;

        public List<ProductTag> tags;

        public List<ProductCustomProperty> customProperties;

        public List<ProductReview> reviews;

        public ProductVariation productVariation;

        public Boolean preorderEnabled;

        public Boolean hasVariations;

        /** The block-shaped copy of the fields above; a write to either changes both. */
        public String productUrl;

        public ProductNamedReference updatedBy;

        public Boolean virtual;

        public String summary;

        public String description;

        public List<ProductCategory> categories;

        public List<ProductLink> upSellProducts;

        public ProductMedia media;

        public ProductAvailableForPurchase availableForPurchase;

        public ProductPricing pricing;

        public ProductInventory inventory;

        public ProductOrdering ordering;

        public ProductRestrictions restrictions;

        public ProductTax tax;

        public ProductShipping shipping;

        public ProductLinks links;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "product";
        }
    }

    /** The five-key {@code pagination} block; {@code limit} is the page size applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing: full product records, where they sit and any schema. */
    public static final class ProductPage extends Dto {

        public List<Product> products;

        public Pagination pagination;

        public Object fieldMetadata;
    }

    /**
     * The create's, {@code PUT}'s and {@code PATCH}'s bare record; an update merges it, and a
     * {@code PUT} also needs the ten fields {@code updateProduct} lists. {@code product_url},
     * {@code links} and {@code restrict_for_selected_customers} are read-only, and {@code
     * updated_by} is answered {@code 200} and ignored.
     */
    public static final class ProductInput extends Dto {

        public String name;

        public String sku;

        /** Stored on a create; an update stores it only on a product without one. */
        public String transactionNo;

        public String heading;

        public String productType;

        public String customType;

        public Boolean isVirtual;

        public String invoiceNote;

        public Boolean available;

        public Boolean availableOnDateRange;

        public String availableFromDate;

        public String availableToDate;

        public String availableFor;

        public ProductSelectedCustomersInput selectedCustomers;

        public Boolean passwordProtected;

        public String password;

        public ProductAvailableOnWeekdays availableOnWeekdays;

        public Double basePrice;

        public Double costPrice;

        public Double compareAtPrice;

        public Boolean isOnSale;

        public Double salePrice;

        public Boolean isFeatured;

        public String featuredLabel;

        public Boolean isNew;

        public ProductProperties productProperties;

        public String scheduleType;

        public String customScheduleOption;

        public ProductTaxSettings taxSettings;

        public ProductVariationPriceDisplay variationPriceDisplay;

        public Boolean administrativeStatus;

        public String productSummary;

        public String productDescription;

        public ProductPricingAndStock productPricingAndStock;

        public ProductImageAndVideo imageAndVideo;

        public ProductAdvanced advanced;

        /** Category ids. */
        public List<Long> parents;

        public List<Object> relatedProducts;

        public List<Object> similarProducts;

        public List<Object> tags;

        public Boolean preorderEnabled;

        public Boolean virtual;

        public String summary;

        public String description;

        /**
         * Category ids or {@code {"id": ...}} records; {@code []} is answered {@code 200} and
         * changes nothing.
         */
        public List<Long> categories;

        public List<ProductLink> upSellProducts;

        public ProductMedia media;

        public ProductAvailableForPurchaseInput availableForPurchase;

        public ProductPricing pricing;

        public ProductInventory inventory;

        public ProductOrdering ordering;

        public ProductRestrictions restrictions;

        public ProductTax tax;

        public ProductShipping shipping;
    }

    /** The review import's answer: every review the product holds, the new ones last. */
    public static final class ProductReviews extends Dto {

        public List<ProductReview> reviews;

        @Override
        protected String envelopeKey() {
            return "product";
        }
    }

    /** One review to add; {@code name}, {@code email} and a {@code rating} of 1 to 5. */
    public static final class ProductReviewInput extends Dto {

        public String name;

        public String email;

        public Double rating;

        public String review;

        public String comment;

        public String status;

        /** {@code yyyy-MM-ddTHH:mm:ss}. */
        public String dateCreated;

        @Override
        public Map<String, Object> toMap() {
            // The derived key would be date_created; the wire key is dateCreated.
            Map<String, Object> body = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : super.toMap().entrySet()) {
                body.put(entry.getKey().equals("date_created") ? "dateCreated" : entry.getKey(),
                        entry.getValue());
            }
            return body;
        }
    }

    /** The review import's body, {@code {"product": {"reviews": [...]}}}, the number beside it. */
    public static final class ProductReviewsImportInput extends Dto {

        public List<ProductReviewInput> reviews;

        public String transactionNo;

        @Override
        protected String envelopeKey() {
            return "product";
        }

        @Override
        protected Set<String> topLevel() {
            return Set.of("transaction_no");
        }
    }

    /** One variant's stock on a product with variations; {@link #locations} read {@code []}. */
    public static final class ProductStockVariation extends Dto {

        public Long variationId;

        public Long productId;

        public String name;

        public String sku;

        public Long availableStock;

        public List<ProductStockLocation> locations;
    }

    /** The stock sub-path's body, read and written. */
    public static final class ProductStock extends Dto {

        public String status;

        public Long productId;

        public Boolean multiLocationEnabled;

        public Boolean trackInventory;

        public Long availableStock;

        public List<ProductStockLocation> locations;

        public List<ProductStockVariation> variations;
    }

    /** One row of the stock write. */
    public static final class ProductStockLocationInput extends Dto {

        public Long warehouse;

        public Long stock;

        public Long lowStockLevel;

        public Boolean isDefault;
    }

    /** The stock write's body; {@link #locations} replaces the product's whole set. */
    public static final class ProductStockInput extends Dto {

        public String note;

        public List<ProductStockLocationInput> locations;
    }
}
