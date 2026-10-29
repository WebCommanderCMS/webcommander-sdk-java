package com.webcommander.resources.shipping_methods.shipping_methods_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the shipping method bodies. */
public final class ShippingMethodsDto {

    private ShippingMethodsDto() {
    }

    /** One shipping method, as a listing row or a row path's {@code shipping_method}. */
    public static final class ShippingMethod extends Dto {

        /** Assigned by the store, an integer; an {@code id} sent on either write is discarded. */
        public Long id;

        /** Unique, at most 100 characters: the one field either write reads. */
        public String name;

        /** Read-only and exclusive: moved only by {@code setShippingMethodDefault}. */
        public Boolean isDefault;

        /** The store's field schema, present only when {@code field_metadata=true} was sent. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "shipping_method";
        }
    }

    /** The writable form of {@link ShippingMethod}; {@link #name} is all the store reads. */
    public static final class ShippingMethodInput extends Dto {

        public String name;

        @Override
        protected String envelopeKey() {
            return "shipping_method";
        }
    }

    /** The listing's ten-key {@code pagination} block; {@link #limit} is the size applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Long count;

        /** The page {@code offset} falls in, clamped to {@code totalPages}. */
        public Long currentPage;

        public Long totalPages;

        public Boolean hasNext;

        public Boolean hasPrevious;

        /** A whole URL carrying the query, or {@code null}. */
        public String previousPage;

        /** A whole URL carrying the query, or {@code null}. */
        public String nextPage;
    }

    /** One page of the listing, with the {@code field_metadata} block when it was asked for. */
    public static final class ShippingMethodPage extends Dto {

        public List<ShippingMethod> shippingMethods;

        public Pagination pagination;

        /** The same schema block the row path serves, keyed by field name. */
        public Object fieldMetadata;
    }

    /** The count sub-path's body, {@code {"shipping_methods": {"count": N}}}. */
    public static final class ShippingMethodCount extends Dto {

        public Long count;

        @Override
        protected String envelopeKey() {
            return "shipping_methods";
        }
    }
}
