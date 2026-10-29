package com.webcommander.resources.categories.categories_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the category bodies. */
public final class CategoriesDto {

    private CategoriesDto() {
    }

    /** Another record a category points at, read as {@code {id, name}}; a write needs only id. */
    public static final class CategoryReference extends Dto {

        public Long id;

        public String name;
    }

    /** The {@code general} section as read. */
    public static final class CategoryGeneral extends Dto {

        public String name;

        public String title;

        public String sku;

        public String url;

        public String heading;

        public CategoryReference parentCategory;

        /** Read {@code null} without the store's filter plugin. */
        public Object filterProfile;

        public CategoryReference layout;

        public String summary;
    }

    /** The {@code description} section, the same one field read and written. */
    public static final class CategoryDescription extends Dto {

        public String content;
    }

    /** The {@code images_media} section as read: the two stored images' URLs and the image's alt
     * text, each "" when unset. */
    public static final class CategoryImagesMedia extends Dto {

        public String imageUrl;

        public String imageAlt;

        public String backgroundImageUrl;
    }

    /** Who {@code available_for: "selected"} admits, each customer and group as {id, name}. */
    public static final class CategorySelectedCustomers extends Dto {

        public List<CategoryReference> customers;

        public List<CategoryReference> groups;
    }

    /** The {@code availability_visibility} section as read; {@code password} is never echoed. */
    public static final class CategoryAvailabilityVisibility extends Dto {

        public Boolean available;

        public Boolean visible;

        public String availableFor;

        public CategorySelectedCustomers selectedCustomers;

        public Boolean availableOnDateRange;

        public String availableFromDate;

        public String availableToDate;

        public Boolean passwordProtected;

        public Boolean customSorting;

        public String defaultSorting;

        public Boolean disableTracking;
    }

    /** The {@code tax_shipping} section, the same eight fields read and written. */
    public static final class CategoryTaxShipping extends Dto {

        public Boolean taxExempt;

        public CategoryReference taxProfile;

        public String taxMessage;

        public Boolean showPriceWithTax;

        public Boolean pricesEnteredWithTax;

        public String basePriceRounding;

        /** Set by naming a shipping profile; {@code false} clears the profile. */
        public Boolean enableShipping;

        public CategoryReference shippingProfile;
    }

    /** One assigned product, as {@code assign_products} reads it. */
    public static final class CategoryProduct extends Dto {

        public Long id;

        public String name;

        public String sku;
    }

    /** The {@code assign_products} section as read. */
    public static final class CategoryAssignProducts extends Dto {

        public List<CategoryProduct> products;
    }

    /** One {@code group_meta} entry; {@code href} is the section's path, the id a placeholder. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block the listing, a row read and every whole-row write send. */
    public static final class GroupMeta extends Dto {

        public SectionMeta general;

        public SectionMeta description;

        public SectionMeta imagesMedia;

        public SectionMeta availabilityVisibility;

        public SectionMeta taxShipping;

        public SectionMeta assignProducts;
    }

    /** One category: a listing row, a row read, a whole-row write's answer, or a section's. */
    public static final class Category extends Dto {

        public CategoryGeneral general;

        public CategoryDescription description;

        public CategoryImagesMedia imagesMedia;

        public CategoryAvailabilityVisibility availabilityVisibility;

        public CategoryTaxShipping taxShipping;

        public CategoryAssignProducts assignProducts;

        public Long id;

        /** The number the category was filed under, {@code null} when none. */
        public String transactionNo;

        public Boolean disposable;

        public Boolean inTrash;

        public Boolean parentInTrash;

        public String createdBy;

        public String createdOn;

        public String lastUpdatedBy;

        public String lastUpdatedOn;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "category";
        }
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

    /** One page of the listing, with the {@code field_metadata} block when it was asked for. */
    public static final class CategoryPage extends Dto {

        public List<Category> categories;

        public GroupMeta groupMeta;

        public Pagination pagination;

        public Object fieldMetadata;
    }

    /** The count sub-path's {@code {"categories": {"count": N}}}. */
    public static final class CategoryCount extends Dto {

        public Long count;

        @Override
        protected String envelopeKey() {
            return "categories";
        }
    }

    /** The writable {@code general} fields; {@code name} is required on a create. */
    public static final class CategoryGeneralInput extends Dto {

        public String name;

        public String title;

        public String sku;

        public String url;

        public String heading;

        public CategoryReference parentCategory;

        public CategoryReference layout;

        public String summary;
    }

    /** One image upload. */
    public static final class CategoryImage extends Dto {

        public String filename;

        public String base64;
    }

    /** The writable {@code images_media} fields; neither null nor "" removes a stored image. */
    public static final class CategoryImagesMediaInput extends Dto {

        public CategoryImage image;

        public String imageAlt;

        public CategoryImage backgroundImage;
    }

    /** The ids {@code available_for: "selected"} admits; an id nothing holds is dropped. */
    public static final class CategorySelectedCustomersInput extends Dto {

        public List<Long> customers;

        public List<Long> groups;
    }

    /** The writable {@code availability_visibility} fields, with the write-only password. */
    public static final class CategoryAvailabilityVisibilityInput extends Dto {

        public Boolean available;

        public Boolean visible;

        public String availableFor;

        public CategorySelectedCustomersInput selectedCustomers;

        public Boolean availableOnDateRange;

        public String availableFromDate;

        public String availableToDate;

        public Boolean passwordProtected;

        public String password;

        public Boolean customSorting;

        public String defaultSorting;

        public Boolean disableTracking;
    }

    /** The product ids to assign; a write replaces the whole assignment. */
    public static final class CategoryAssignProductsInput extends Dto {

        public List<Long> products;
    }

    /** The body every write takes, a section {@code PATCH} reading only its own section. */
    public static final class CategoryInput extends Dto {

        public CategoryGeneralInput general;

        public CategoryDescription description;

        public CategoryImagesMediaInput imagesMedia;

        public CategoryAvailabilityVisibilityInput availabilityVisibility;

        public CategoryTaxShipping taxShipping;

        public CategoryAssignProductsInput assignProducts;

        /** Read inside the {@code category} wrapper only; beside it, it is ignored. */
        public String transactionNo;

        @Override
        protected String envelopeKey() {
            return "category";
        }
    }
}
