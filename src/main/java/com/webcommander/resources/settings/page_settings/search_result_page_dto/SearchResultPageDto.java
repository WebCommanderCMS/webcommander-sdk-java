package com.webcommander.resources.settings.page_settings.search_result_page_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the search result page settings bodies. */
public final class SearchResultPageDto {

    private SearchResultPageDto() {
    }

    /** The {@code product_interaction_buttons} block: which buttons each result carries. */
    public static final class ProductInteractionButtons extends Dto {

        public Boolean enabled;

        public Boolean displayAddToCartButton;

        public Boolean displayAddToCompareButton;

        public Boolean displayAddToWishlistButton;

        public Boolean displayViewDetailsButton;

        public Boolean displayOnHover;

        public Boolean displayQuickViewButton;
    }

    /** The {@code number_of_searches_per_result} block: whether and how results paginate. */
    public static final class NumberOfSearchesPerResult extends Dto {

        public Boolean enabled;

        public Long itemsPerPage;
    }

    /** The {@code set_product_display_preferences} block: how results load. */
    public static final class SetProductDisplayPreferences extends Dto {

        public Boolean enabled;

        public String displayMode;

        public String selectPaginationPosition;

        public Long numberOfProductsPerPage;

        public Boolean allowUsersToSelectNumberOfProducts;

        public Long numberOfProductsPerLoad;
    }

    /** The {@code set_product_sorting} block: whether a default sort applies, and which. */
    public static final class SetProductSorting extends Dto {

        public Boolean enabled;

        public String defaultSort;
    }

    /** The {@code display_price} block: whether a result shows a price, and which. */
    public static final class DisplayPrice extends Dto {

        public Boolean enabled;

        public Boolean displayExpectToPayPrice;

        public Boolean showTaxWithTheExpectToPayPrice;
    }

    /** The {@code product_information} section: what each search result shows. */
    public static final class ProductInformation extends Dto {

        public Boolean useDefaultProductListingConfigurations;

        public ProductInteractionButtons productInteractionButtons;

        public Boolean displayContentPage;

        public NumberOfSearchesPerResult numberOfSearchesPerResult;

        public Boolean displayShortDescription;

        public Boolean displayVariationsCombination;

        public SetProductDisplayPreferences setProductDisplayPreferences;

        public SetProductSorting setProductSorting;

        /** The {@code configure_sorting_options} block: the sort orders a shopper may switch to. */
        public Object configureSortingOptions;

        public Boolean displayLoyaltyPoints;

        public DisplayPrice displayPrice;

        public String labelForPrice;

        @Override
        protected String envelopeKey() {
            return "product_information";
        }
    }

    /** The {@code seo} section: two read-only markers rather than settings. */
    public static final class Seo extends Dto {

        public Boolean readOnly;

        public String pageKind;

        @Override
        protected String envelopeKey() {
            return "seo";
        }
    }

    /** The {@code reinitialise} section, one field naming the verb its {@code group_meta} lists. */
    public static final class Reinitialise extends Dto {

        public String action;

        @Override
        protected String envelopeKey() {
            return "reinitialise";
        }
    }

    /** The {@code product_promotion} block the facade inlines: a marker, not the rows. */
    public static final class ProductPromotionPreview extends Dto {

        public Boolean readOnly;
    }

    /** One promotion row, as the promotion collection sends it. */
    public static final class ProductPromotion extends Dto {

        public Long id;

        public String name;

        public String rule;

        public String position;

        public String specificKeywords;

        public Boolean isActive;

        public Boolean isAlwaysAvailable;

        public String availableFromDate;

        public String availableToDate;

        public List<Long> productIds;

        public String created;

        public String updated;

        @Override
        protected String envelopeKey() {
            return "product_promotion";
        }
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String uiLabel;

        public String href;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per thing the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta productInformation;

        public SectionMeta seo;

        public SectionMeta productPromotion;

        public SectionMeta reinitialise;
    }

    /** The search result page settings, as the facade or one section sends them. */
    public static final class SearchResultPage extends Dto {

        public ProductInformation productInformation;

        public Seo seo;

        public ProductPromotionPreview productPromotion;

        public Reinitialise reinitialise;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "search_result_page";
        }
    }
}
