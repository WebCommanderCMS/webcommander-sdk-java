package com.webcommander.resources.gift_cards.gift_cards_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the gift card bodies. */
public final class GiftCardsDto {

    private GiftCardsDto() {
    }

    /**
     * One gift card. {@code code} is what an order preview's {@code gift-card} call takes; a card
     * can be applied only while {@code is_active} and {@code is_paid}, with a {@code balance} above
     * zero, before its {@code available_to} date and with {@code email} equal to the previewed
     * customer's email.
     */
    public static final class GiftCard extends Dto {

        public Long id;

        public String code;

        public Double balance;

        public Double initialAmount;

        public String email;

        public String firstName;

        public String lastName;

        public String message;

        public Boolean isActive;

        public Boolean isPaid;

        public String availableTo;

        public String externalReference;

        public String productName;

        public String createdOn;
    }

    /** The listing's ten-key {@code pagination} block; {@link #limit} is capped at 20. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Long count;

        /** The page {@code offset} falls in. */
        public Long currentPage;

        public Long totalPages;

        public Boolean hasNext;

        public Boolean hasPrevious;

        /** A whole URL carrying the query, or {@code null}. */
        public String previousPage;

        /** A whole URL carrying the query, or {@code null}. */
        public String nextPage;
    }

    /** One page of the listing, newest first. */
    public static final class GiftCardPage extends Dto {

        public List<GiftCard> giftCards;

        public Pagination pagination;
    }
}
