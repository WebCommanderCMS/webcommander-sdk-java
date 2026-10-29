package com.webcommander.resources.settings.page_settings.category_page_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the category page settings bodies. */
public final class CategoryPageDto {

    private CategoryPageDto() {
    }

    /** The {@code heading} block: whether the category name shows, and at which heading level. */
    public static final class CategoryHeading extends Dto {

        public Boolean enabled;

        public String style;
    }

    /** The {@code subcategory} block: whether subcategories are listed, and how that list pages. */
    public static final class Subcategory extends Dto {

        public Boolean enabled;

        public Boolean display;

        public String loadType;

        public String paginationPosition;

        public Long perPage;

        public Boolean allowUserPerPage;

        public Long perLoad;

        public Boolean displayProducts;

        public Boolean showSummary;
    }

    /** The {@code category_information} section: what the category itself shows. */
    public static final class CategoryInformation extends Dto {

        public CategoryHeading heading;

        public Boolean showDescription;

        public Boolean showBannerImage;

        public Boolean showImage;

        public Subcategory subcategory;

        @Override
        protected String envelopeKey() {
            return "category_information";
        }
    }

    /** The {@code interaction_buttons} block: which buttons each product card carries. */
    public static final class InteractionButtons extends Dto {

        public Boolean enabled;

        public Boolean addToCart;

        public Boolean viewDetails;

        public Boolean showOnHover;

        public Boolean addToWishList;

        public Boolean addToCompare;

        public Boolean quickView;
    }

    /** The {@code price} block: whether a card shows a price, and which price. */
    public static final class Price extends Dto {

        public Boolean enabled;

        public Boolean expectToPay;

        public Boolean expectToPayWithTax;

        public Boolean strikeThroughPrevious;
    }

    /** One entry of {@code variation_combination.types}, a variation type the store has. */
    public static final class VariationType extends Dto {

        public Long id;

        public String name;
    }

    /** The {@code variation_combination} block: whether combinations are shown, and which. */
    public static final class VariationCombination extends Dto {

        public Boolean enabled;

        public List<VariationType> types;
    }

    /** The {@code rating} block: whether the rating and the review count are shown. */
    public static final class Rating extends Dto {

        public Boolean enabled;

        public Boolean showReviewCount;
    }

    /** The {@code view_switcher} block: whether a shopper may change the display mode. */
    public static final class ViewSwitcher extends Dto {

        public Boolean enabled;

        public Boolean grid;

        public Boolean list;

        public Boolean tabular;
    }

    /** The {@code columns_per_row} block inside {@code default_display}. */
    public static final class ColumnsPerRow extends Dto {

        public Long grid;

        public Long list;
    }

    /** The {@code default_display} block: the mode a listing opens in and its row width. */
    public static final class DefaultDisplay extends Dto {

        public Boolean enabled;

        public String mode;

        public ColumnsPerRow columnsPerRow;

        public Boolean showVariationOptionsLabelInTabular;
    }

    /** The {@code load} block: how the product listing pages and where its control sits. */
    public static final class ProductLoad extends Dto {

        public Boolean enabled;

        public String type;

        public String paginationPosition;

        public Long perPage;

        public Boolean allowUserPerPage;

        public Long perLoad;
    }

    /** The {@code default_sort} block: whether a default order is applied, and which. */
    public static final class DefaultSort extends Dto {

        public Boolean enabled;

        public String by;
    }

    /** The {@code product_information} section: what each product card shows. */
    public static final class ProductInformation extends Dto {

        public Boolean useGlobalDefaults;

        public InteractionButtons interactionButtons;

        public Boolean showSummary;

        public Boolean showSalePercentage;

        public Boolean showStockAvailability;

        public Boolean showParentCategory;

        public Price price;

        public VariationCombination variationCombination;

        public Rating rating;

        public ViewSwitcher viewSwitcher;

        public DefaultDisplay defaultDisplay;

        public ProductLoad load;

        public DefaultSort defaultSort;

        /** The {@code sort_options} block: the orders a shopper may switch to. */
        public Object sortOptions;

        @Override
        protected String envelopeKey() {
            return "product_information";
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

        public SectionMeta categoryInformation;

        public SectionMeta productInformation;
    }

    /** The category page settings, as the facade or one section sends them. */
    public static final class CategoryPage extends Dto {

        public CategoryInformation categoryInformation;

        public ProductInformation productInformation;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "category_page";
        }
    }
}
