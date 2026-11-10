package com.webcommander.resources.shipping_rules.shipping_rules_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the shipping rule bodies. */
public final class ShippingRulesDto {

    private ShippingRulesDto() {
    }

    /**
     * An {@code {id, name}} pair: the rule's class, rate, zones and profiles. A write names a
     * class, a rate or a zone by its id, bare or as {@code {"id": ...}}; {@code name} is read-only.
     */
    public static final class ShippingReference extends Dto {

        public Long id;

        public String name;
    }

    /**
     * One rule, the same ten keys on a listing row, the row read and every write's answer.
     * {@code shipping_class} and {@code shipping_rate} are {@code null} when unset, {@code zones}
     * and {@code profiles} empty lists.
     */
    public static final class ShippingRule extends Dto {

        public Long id;

        public String name;

        public String description;

        public ShippingReference shippingClass;

        public ShippingReference shippingRate;

        public List<ShippingReference> zones;

        public List<ShippingReference> profiles;

        public String transactionNo;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "shipping_rule";
        }
    }

    /**
     * The writable fields, wrapped in {@code shipping_rule}, for the create, the {@code PUT}
     * replace and the {@code PATCH}. {@code zones} is the whole list of zones, each once;
     * {@code profile_id} adds the new rule to a profile on a create. {@code PUT} needs
     * {@code name}, {@code description}, {@code shipping_class}, {@code shipping_rate} and
     * {@code zones}. A field left unset is not sent: to clear a class or a rate, pass a
     * {@code Map} holding {@code null}.
     */
    public static final class ShippingRuleInput extends Dto {

        public String name;

        public String description;

        /** An id, bare or as a {@link ShippingReference}. */
        public Object shippingClass;

        /** An id, bare or as a {@link ShippingReference}. */
        public Object shippingRate;

        /** Zone ids, bare or as a {@link ShippingReference}. */
        public List<Object> zones;

        public Long profileId;

        public String transactionNo;

        public Boolean suggestName;

        @Override
        protected String envelopeKey() {
            return "shipping_rule";
        }
    }

    /** The five-key {@code pagination} block; {@code limit} is the size the store applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing, newest first. */
    public static final class ShippingRuleListing extends Dto {

        public List<ShippingRule> shippingRules;

        public Pagination pagination;
    }

    /** The count sub-path's body, a bare {@code {"total": N}}. */
    public static final class ShippingRuleCount extends Dto {

        public Long total;
    }
}
