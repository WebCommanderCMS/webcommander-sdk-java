package com.webcommander.resources.settings.product_listing.product_listing_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the product listing body. */
public final class ProductListingDto {

    private ProductListingDto() {
    }

    /** The {@code add to cart} and {@code view details} buttons on a listing tile. */
    public static final class InteractionButtons extends Dto {

        public Boolean enabled;

        public Boolean addToCart;

        public Boolean showViewDetails;

        public Boolean showOnHover;
    }

    /** Which prices a listing tile shows. */
    public static final class ListingPrice extends Dto {

        public Boolean enabled;

        public Boolean expectToPay;

        public Boolean expectToPayWithTax;
    }

    /** The rating stars on a listing tile and the count beside them. */
    public static final class Reviews extends Dto {

        public Boolean showRating;

        public Boolean showReviewCount;
    }

    /** Which variation types a listing tile combines. */
    public static final class VariationCombination extends Dto {

        public Boolean enabled;

        public List<String> types;
    }

    /** The {@code product_information} section: what each product shows on a listing tile. */
    public static final class ProductInformation extends Dto {

        public InteractionButtons interactionButtons;

        public Boolean showSummary;

        public Boolean showSalePercentage;

        public Boolean showStockAvailability;

        public Boolean showParentCategory;

        public Boolean showLoyaltyPoints;

        public ListingPrice price;

        public Reviews reviews;

        public VariationCombination variationCombination;

        public Boolean showVariationsAsProducts;
    }

    /** Where the pager sits and how many products a page holds. */
    public static final class Pagination extends Dto {

        public String position;

        public Long perPage;

        public Boolean allowUserPerPage;
    }

    /** How many products each press of <i>load more</i> adds. */
    public static final class LoadMore extends Dto {

        public Long perLoad;
    }

    /** The {@code load} section: whether a listing pages or loads more, and how. */
    public static final class Load extends Dto {

        public Boolean enabled;

        public String type;

        public Pagination pagination;

        public LoadMore loadMore;
    }

    /** The {@code sorting} section: the order a listing arrives in. */
    public static final class Sorting extends Dto {

        public Boolean enabled;

        public String sortBy;
    }

    /** Which orders a shopper may pick; {@link #New} is the store's {@code new}, a Java keyword. */
    public static final class SortableOptions extends Dto {

        public Boolean alphabetic;

        public Boolean featured;

        public Boolean New;

        public Boolean topSelling;

        public Boolean price;

        public Boolean createdDate;
    }

    /** The {@code sortable_items} section: whether a shopper may re-sort a listing, and by what. */
    public static final class SortableItems extends Dto {

        public Boolean enabled;

        public SortableOptions options;
    }

    /** The one entry of {@code group_meta}: what the area is, whether it is writable and where. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block — a single entry for the whole area, not one per section. */
    public static final class GroupMeta extends Dto {

        public SectionMeta productListing;
    }

    /** The product listing settings, in the one form the API sends them. */
    public static final class ProductListing extends Dto {

        public ProductInformation productInformation;

        public Load load;

        public Sorting sorting;

        public SortableItems sortableItems;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "product_listing";
        }
    }
}
