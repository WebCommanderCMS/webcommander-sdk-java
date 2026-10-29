package com.webcommander.resources.variations.variations_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the variation type and product variant bodies. */
public final class VariationsDto {

    private VariationsDto() {
    }

    /** An image option's stored picture; {@code null} on text and color options. */
    public static final class VariationOptionImage extends Dto {

        public String url;

        public String fileName;
    }

    /** One option of a type. */
    public static final class VariationOption extends Dto {

        public Long id;

        public String label;

        /** {@code null} on an image option. */
        public String value;

        public Long order;

        public Boolean Default;

        public VariationOptionImage image;
    }

    /** One variation type: a listing row, a row read and every type write's answer. */
    public static final class Variation extends Dto {

        public Long id;

        public String name;

        public String standard;

        public Boolean disposable;

        public List<VariationOption> options;

        /** The store's field schema, present only when {@code field_metadata=true} was sent. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "variation";
        }
    }

    /** The listing's five-key {@code pagination} block. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing, and the schema when {@code field_metadata} is asked for. */
    public static final class VariationPage extends Dto {

        public List<Variation> variations;

        public Object fieldMetadata;

        public Pagination pagination;
    }

    /** An image option's upload; written as {@code filename}, read back as {@code file_name}. */
    public static final class VariationOptionImageInput extends Dto {

        public String base64;

        public String filename;
    }

    /** One option in a type write; an {@code id} names an option of this type to change. */
    public static final class VariationOptionInput extends Dto {

        public Long id;

        public String label;

        public String value;

        public Long order;

        public Boolean Default;

        public VariationOptionImageInput image;
    }

    /** The type writes' body, sent as {@code {"variation": {...}}}; standard is fixed on create. */
    public static final class VariationInput extends Dto {

        public String name;

        public String standard;

        public List<VariationOptionInput> options;

        @Override
        protected String envelopeKey() {
            return "variation";
        }
    }

    /** A variant's expected-price display. */
    public static final class ProductVariantExpectToPay extends Dto {

        public Boolean isEnabled;

        public Double price;
    }

    /** A variant's prices; the bulk write's full answer carries the first four. */
    public static final class ProductVariantPricing extends Dto {

        public Double basePrice;

        public Double costPrice;

        public Double salePrice;

        public Boolean isOnSale;

        public String saleAmountType;

        public Boolean hidePrice;

        public Boolean isCallForPrice;

        public ProductVariantExpectToPay expectToPay;

        public Object tieredPricing;
    }

    /** A variant's stock; the bulk write's full answer carries the first three. */
    public static final class ProductVariantInventory extends Dto {

        public Boolean isEnabled;

        public Long quantity;

        public Long lowStockThreshold;

        public Boolean isSoldOut;

        public String soldOutLabel;
    }

    /** A variant's order quantities; {@code max_quantity} 0 is no maximum. */
    public static final class ProductVariantOrdering extends Dto {

        public Long minQuantity;

        public Long maxQuantity;

        public Long multipleOf;

        public Boolean isMultipleQuantityRequired;
    }

    /** A variant's size and weight. */
    public static final class ProductVariantDimensions extends Dto {

        public Double length;

        public Double width;

        public Double height;

        public Double weight;
    }

    /** A variant's two badges. */
    public static final class ProductVariantBadges extends Dto {

        public Boolean isFeatured;

        public Boolean isNewArrival;
    }

    /** One variant of a product; {@code key} joins its option labels with {@code /}. */
    public static final class ProductVariant extends Dto {

        public Long variationId;

        public Boolean available;

        public Boolean isBase;

        public String key;

        public String name;

        public String sku;

        public String customSku;

        public String title;

        public String summary;

        public String description;

        public String barcode;

        public String countryOfOrigin;

        public String hsCode;

        public ProductVariantPricing pricing;

        public ProductVariantInventory inventory;

        public ProductVariantOrdering ordering;

        public ProductVariantDimensions dimensions;

        public ProductVariantBadges badges;

        public List<Object> images;
    }

    /** A product's variants, under {@code product_key}: its type names joined with {@code /}. */
    public static final class ProductVariationGroup extends Dto {

        public String productKey;

        public List<ProductVariant> variants;
    }

    /** The product read's body; {@code variations} is empty for a product without variants. */
    public static final class ProductVariations extends Dto {

        public List<ProductVariationGroup> variations;
    }

    /** One option a variant carries, in the bulk write's full answer. */
    public static final class ProductVariantOptionReference extends Dto {

        public Long id;

        public String type;

        public String label;
    }

    /** One written variant: {@code id} and {@code name}, the rest with {@code response=full}. */
    public static final class ProductVariantUpdate extends Dto {

        public Long id;

        public String name;

        public String sku;

        public String customSku;

        public String barcode;

        public String countryOfOrigin;

        public String hsCode;

        public Boolean available;

        public List<ProductVariantOptionReference> options;

        public ProductVariantPricing pricing;

        public ProductVariantInventory inventory;

        public ProductVariantOrdering ordering;

        public String transactionNo;

        public String url;
    }

    /** The bulk write's answer. */
    public static final class ProductVariationsBulkResult extends Dto {

        public String status;

        public String message;

        public List<ProductVariantUpdate> updated;

        public List<Object> failed;
    }

    /** The bulk write's body, no envelope: the variant ids and at least one of the six fields. */
    public static final class ProductVariationsBulkInput extends Dto {

        public List<Long> variationIds;

        public Boolean available;

        public Double basePrice;

        public Double costPrice;

        public Long availableStock;

        public Long minOrderQuantity;

        public Long maxOrderQuantity;
    }

    /** One variant to import; a {@code key} the product has is that variant, changed. */
    public static final class ProductVariantInput extends Dto {

        /** One option label per type, joined with {@code /}; required. */
        public String key;

        public String name;

        public String sku;

        public String customSku;

        public String title;

        public String summary;

        public String description;

        public String barcode;

        public String countryOfOrigin;

        public String hsCode;

        public ProductVariantPricing pricing;

        public ProductVariantInventory inventory;

        public ProductVariantOrdering ordering;

        public ProductVariantDimensions dimensions;

        public ProductVariantBadges badges;

        public List<Object> images;
    }

    /** One group to import: {@code product_key} names the types, joined with {@code /}. */
    public static final class ProductVariationGroupInput extends Dto {

        public String productKey;

        /** Must not be empty. */
        public List<ProductVariantInput> variants;
    }

    /** The import's body, no envelope; {@code variations} must not be empty. */
    public static final class ProductVariationsImportInput extends Dto {

        public String transactionNo;

        public List<ProductVariationGroupInput> variations;
    }

    /** The import's answer: a {@code message} counting the variants, and their total. */
    public static final class ProductVariationsImportResult extends Dto {

        public String status;

        public String message;

        public Long totalVariants;
    }
}
