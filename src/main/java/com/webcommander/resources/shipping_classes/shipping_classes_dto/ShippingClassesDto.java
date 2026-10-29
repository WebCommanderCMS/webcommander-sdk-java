package com.webcommander.resources.shipping_classes.shipping_classes_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the shipping class bodies. */
public final class ShippingClassesDto {

    private ShippingClassesDto() {
    }

    /** One shipping class, as a listing row or a row path's {@code shipping_class} record. */
    public static final class ShippingClass extends Dto {

        public Long id;

        public String name;

        public String description;

        public Boolean isDefault;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "shipping_class";
        }
    }

    /** The body both writes take: a unique {@link #name} and an optional {@link #description}. */
    public static final class ShippingClassInput extends Dto {

        public String name;

        public String description;

        @Override
        protected String envelopeKey() {
            return "shipping_class";
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
    public static final class ShippingClassPage extends Dto {

        public List<ShippingClass> shippingClasses;

        public Pagination pagination;

        public Object fieldMetadata;
    }

    /** The count sub-path's {@code {"shipping_classes": {"count": N}}}. */
    public static final class ShippingClassCount extends Dto {

        public Long count;

        @Override
        protected String envelopeKey() {
            return "shipping_classes";
        }
    }
}
