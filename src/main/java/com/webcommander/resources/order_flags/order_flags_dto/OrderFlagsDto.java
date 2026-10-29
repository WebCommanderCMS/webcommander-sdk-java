package com.webcommander.resources.order_flags.order_flags_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the order flag bodies. */
public final class OrderFlagsDto {

    private OrderFlagsDto() {
    }

    /** One order flag, as a listing row or a row path's {@code order_flag}. */
    public static final class OrderFlag extends Dto {

        /** Assigned by the store, an integer; an {@code id} sent on a create is ignored. */
        public Long id;

        /** Unique across the collection: a name another flag holds answers {@code 409}. */
        public String name;

        /** A hex code such as {@code #FF6868}; anything else is refused. */
        public String color;

        public String description;

        /** Reads {@code order} on every row and is not writable. */
        public String type;

        @Override
        protected String envelopeKey() {
            return "order_flag";
        }
    }

    /** The writable form of {@link OrderFlag}; {@link #name} and {@link #color} are required. */
    public static final class OrderFlagInput extends Dto {

        public String name;

        public String color;

        public String description;

        @Override
        protected String envelopeKey() {
            return "order_flag";
        }
    }

    /** The listing's five-key {@code pagination} block; {@link #limit} is the size applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasNext;

        public Boolean hasPrevious;
    }

    /** One page of the listing: the rows and the block that says where they sit. */
    public static final class OrderFlagPage extends Dto {

        public List<OrderFlag> orderFlags;

        public Pagination pagination;
    }
}
