package com.webcommander.resources.settings.page_settings.shopping_cart_dto;

import com.webcommander.Dto;

/** Typed views over the shopping cart settings bodies. */
public final class ShoppingCartDto {

    private ShoppingCartDto() {
    }

    /** The {@code cart_page} section: what the cart page itself offers a shopper. */
    public static final class CartPage extends Dto {

        public Boolean showProductThumbnail;

        public Boolean enableClearCart;

        public Boolean saveCart;

        @Override
        protected String envelopeKey() {
            return "cart_page";
        }
    }

    /** The {@code flying_cart} section: the slide-out cart and what it offers. */
    public static final class FlyingCart extends Dto {

        public Boolean enabled;

        public Boolean enableClearCart;

        public Boolean saveCart;

        @Override
        protected String envelopeKey() {
            return "flying_cart";
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

        public SectionMeta cartPage;

        public SectionMeta flyingCart;
    }

    /** The shopping cart settings, as the facade or one section sends them. */
    public static final class ShoppingCart extends Dto {

        public CartPage cartPage;

        public FlyingCart flyingCart;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "shopping_cart";
        }
    }
}
