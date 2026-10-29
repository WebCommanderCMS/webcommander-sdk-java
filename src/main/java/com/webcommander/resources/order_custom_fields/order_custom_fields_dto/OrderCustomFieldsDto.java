package com.webcommander.resources.order_custom_fields.order_custom_fields_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the order custom field bodies. */
public final class OrderCustomFieldsDto {

    private OrderCustomFieldsDto() {
    }

    /** One order custom field; a listing row carries the fields up to {@link #maxLength}. */
    public static final class OrderCustomField extends Dto {

        public Long id;

        public String fieldType;

        public String label;

        public String name;

        public Boolean isActive;

        public Long orderNo;

        public String createdOn;

        public String lastUpdatedOn;

        public Boolean isRequired;

        public String inputFormat;

        public Boolean hasMaxLength;

        public Long maxLength;

        public String hoverText;

        public String placeholder;

        public String defaultValue;

        public String htmlClass;

        public List<String> options;

        public Boolean isAllowedForShipping;

        public Boolean isAllowedForDelivery;

        public Boolean isAllowedForStorePickup;

        public Boolean isAllowedForPreOrder;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "order_custom_field";
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

    /** The listing's one {@code group_meta} entry: {@code kind} {@code resource}, read-only. */
    public static final class ResourceMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The listing's {@code group_meta} block, keyed by the collection's own name. */
    public static final class GroupMeta extends Dto {

        public ResourceMeta orderCustomFields;
    }

    /** One page of the listing, with the {@code field_metadata} block when it was asked for. */
    public static final class OrderCustomFieldPage extends Dto {

        public List<OrderCustomField> orderCustomFields;

        public Pagination pagination;

        public GroupMeta groupMeta;

        public Object fieldMetadata;
    }

    /** The {@code validate_timeslot} body; {@link #selectedDate} is required, as dd/MM/yyyy. */
    public static final class OrderCustomFieldTimeslotInput extends Dto {

        public String selectedDate;

        public String fieldName;

        public Long weekdayId;

        public Long startHour;

        public Long endHour;

        public Long orderLimit;
    }
}
