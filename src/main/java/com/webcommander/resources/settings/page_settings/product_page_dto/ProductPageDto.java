package com.webcommander.resources.settings.page_settings.product_page_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the product page settings bodies. */
public final class ProductPageDto {

    private ProductPageDto() {
    }

    /** The {@code actions_buttons} block: which buttons a product page carries. */
    public static final class ActionButtons extends Dto {

        public Boolean showProductActionButtons;

        public Boolean displayAddToCartButton;

        public Boolean displayBuyNowButton;

        public Boolean displayOnHover;

        public Boolean displayAddToCompareButton;

        public Boolean displayAddToWishlistButton;
    }

    /** The {@code details} block: which parts of the product's description are shown. */
    public static final class ProductDetails extends Dto {

        public Boolean displayDescription;

        public Boolean displaySalePercentage;

        public Boolean useStrikeThroughOnPreviousPrice;

        public Boolean displayTaxWithExpectToPayPrice;
    }

    /** The {@code social_sharing} block: whether share buttons appear, and for which networks. */
    public static final class SocialSharing extends Dto {

        public Boolean enableShareOnSocialMedia;

        public Boolean facebook;

        public Boolean twitter;

        public Boolean pinterest;

        public Boolean messenger;

        public Boolean linkedin;

        public Boolean email;

        public Boolean snapchat;

        public Boolean telegram;

        public Boolean reddit;

        public Boolean instagram;

        public Boolean youtube;

        public Boolean tiktok;

        public Boolean threads;

        public Boolean whatsapp;

        public Boolean bluesky;

        public Boolean mastodon;

        public Boolean copyLink;

        public Boolean displayTitle;

        public String title;
    }

    /** The {@code set_product_properties} block: which physical properties a page lists. */
    public static final class SetProductProperties extends Dto {

        public Boolean displayHeight;

        public Boolean displayWidth;

        public Boolean displayLength;

        public Boolean displayWeight;

        public Boolean displayAddToGiftRegistry;
    }

    /** The {@code configuration} section: what the product page itself shows. */
    public static final class Configuration extends Dto {

        public ActionButtons actionsButtons;

        public ProductDetails details;

        public SocialSharing socialSharing;

        public SetProductProperties setProductProperties;

        @Override
        protected String envelopeKey() {
            return "configuration";
        }
    }

    /** The {@code labels_and_display} block: how a variation is labelled and drawn. */
    public static final class LabelsAndDisplay extends Dto {

        public Boolean disableVariationLabelForPrice;

        public Boolean displayProductImageAsVariationIcons;

        public Boolean displaySelectedVariationValue;

        public Boolean enableColourTypeVariationImageDisplay;

        public Boolean useStrikeThroughForOutOfStockVariations;

        public Boolean enableSingleClickVariationLoading;
    }

    /** One entry of {@code layout.variation_combinations}, a variation type the store has. */
    public static final class VariationType extends Dto {

        public Long id;

        public String name;
    }

    /** The {@code layout} block: how variations are laid out and which price is shown. */
    public static final class VariationLayout extends Dto {

        public Boolean selectVariationView;

        public String selectVariationViewType;

        public Boolean displayVariationCombinations;

        public List<VariationType> variationCombinations;

        public Boolean variationPriceDisplayMode;

        public String variationPriceDisplayType;
    }

    /** The {@code variations} section: how a product's variations are labelled and laid out. */
    public static final class Variations extends Dto {

        public LabelsAndDisplay labelsAndDisplay;

        public VariationLayout layout;

        @Override
        protected String envelopeKey() {
            return "variations";
        }
    }

    /** The {@code image_display} block: how product images are laid out and zoomed. */
    public static final class ImageDisplay extends Dto {

        public Boolean enableImageSlider;

        public Boolean productThumbnailVerticalView;

        public Boolean productImageView;

        public String productImageViewType;

        public Boolean imageZoomType;

        public String zoomType;
    }

    /** The {@code video} block: whether a video is previewed and where its thumbnail sits. */
    public static final class ProductVideo extends Dto {

        public Boolean enableVideoPreview;

        public Boolean displayVideoThumbnailInFirstOrder;
    }

    /** The {@code image_and_video} section: one field of its own beside two blocks. */
    public static final class ImageAndVideo extends Dto {

        public Boolean allowSingleFileUploadPerProduct;

        public ImageDisplay imageDisplay;

        public ProductVideo video;

        @Override
        protected String envelopeKey() {
            return "image_and_video";
        }
    }

    /** The fields a product listing on this page carries, shared by up-sell and related. */
    public static class ProductListing extends Dto {

        public Long numberOfProducts;

        public Boolean allowViewersToChangeDisplayMode;

        public Boolean gridView;

        public Boolean listView;

        public Boolean tabularView;

        public String selectDisplayType;

        public Long maximumNumberOfColumnsPerRow;

        public Long maximumNumberOfColumnsPerRowList;

        public Boolean shortDescription;

        public Boolean displayOnHover;

        public Boolean displayAddToCartButton;

        public Boolean displayAddToCompareButton;

        public Boolean displayAddToWishlistButton;

        public Boolean price;

        public Boolean showExpectToPayPrice;

        public Boolean displayRating;

        public Boolean displayReviewCount;
    }

    /** The {@code up_sell} section, a flat {@link ProductListing}. */
    public static final class UpSell extends ProductListing {

        @Override
        protected String envelopeKey() {
            return "up_sell";
        }
    }

    /** The {@code related_products} section, a flat {@link ProductListing}. */
    public static final class RelatedProducts extends ProductListing {

        @Override
        protected String envelopeKey() {
            return "related_products";
        }
    }

    /** The {@code capabilities} block: what the review plugin makes available. */
    public static final class ReviewCapabilities extends Dto {

        public Boolean available;
    }

    /** The {@code review_and_ratings} section: where reviews appear and which version renders. */
    public static final class ReviewAndRatings extends Dto {

        public ReviewCapabilities capabilities;

        public Boolean enableReviewInTab;

        public Boolean enableNewVersion;

        @Override
        protected String envelopeKey() {
            return "review_and_ratings";
        }
    }

    /** The {@code quick_view} section: the quick-view popup and what it shows. */
    public static final class QuickView extends Dto {

        public Boolean productQuickView;

        public Boolean displayProductName;

        public Boolean displaySku;

        public Boolean displayProductRating;

        public Boolean displayProductStock;

        public Boolean displayProductVariation;

        public Boolean displayProductImage;

        public Boolean displayVariationImage;

        public Boolean displayProductSummary;

        public Boolean displayAddToCartButton;

        public Boolean displayAddToCompareButton;

        public Boolean displayAddToWishlistButton;

        @Override
        protected String envelopeKey() {
            return "quick_view";
        }
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String plugin;

        public Boolean available;

        public String uiLabel;

        public String href;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per section the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta configuration;

        public SectionMeta variations;

        public SectionMeta imageAndVideo;

        public SectionMeta upSell;

        public SectionMeta relatedProducts;

        public SectionMeta reviewAndRatings;

        public SectionMeta quickView;
    }

    /** The product page settings, as the facade or one section sends them. */
    public static final class ProductPage extends Dto {

        public Configuration configuration;

        public Variations variations;

        public ImageAndVideo imageAndVideo;

        public UpSell upSell;

        public RelatedProducts relatedProducts;

        public ReviewAndRatings reviewAndRatings;

        public QuickView quickView;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "product_page";
        }
    }
}
