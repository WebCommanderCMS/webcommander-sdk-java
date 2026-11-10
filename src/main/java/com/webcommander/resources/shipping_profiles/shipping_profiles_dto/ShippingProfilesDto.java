package com.webcommander.resources.shipping_profiles.shipping_profiles_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the shipping profile bodies. */
public final class ShippingProfilesDto {

    private ShippingProfilesDto() {
    }

    /**
     * An {@code {id, name}} pair: a rule's rate, and a zone named in the wizard, where a write
     * sends {@code {"id": ...}} for an existing zone or {@code {"name": ...}} to create one.
     */
    public static final class ShippingReference extends Dto {

        public Long id;

        public String name;
    }

    /** A rule on a profile; {@code shipping_rate} is {@code null} on a rule that has no rate. */
    public static final class ShippingProfileRule extends Dto {

        public Long id;

        public String name;

        public ShippingReference shippingRate;
    }

    /**
     * One profile, the same ten keys on a listing row, the row read and every write's answer.
     * {@code rule_preference} reads {@code product_assigned_rule},
     * {@code lowest_shipping_cost_rule} or {@code highest_shipping_cost_rule}; exactly one profile
     * has {@code default} true.
     */
    public static final class ShippingProfile extends Dto {

        public Long id;

        public String name;

        public String description;

        public String rulePreference;

        public Boolean Default;

        public Long rulesCount;

        public List<ShippingProfileRule> shippingRules;

        public String transactionNo;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "shipping_profile";
        }
    }

    /**
     * The writable fields, wrapped in {@code shipping_profile}, for the create, the {@code PUT}
     * replace and the {@code PATCH}. {@code shipping_rules} is the whole list of rule ids, each
     * rule once; {@code PUT} needs {@code name}, {@code description}, {@code rule_preference} and
     * {@code shipping_rules}. {@code default} takes only {@code true} or {@code false}. A field
     * left unset is not sent: to clear one, pass a {@code Map}.
     */
    public static final class ShippingProfileInput extends Dto {

        public String name;

        public String description;

        public String rulePreference;

        public Boolean Default;

        public List<Long> shippingRules;

        public String transactionNo;

        public Boolean suggestName;

        @Override
        protected String envelopeKey() {
            return "shipping_profile";
        }
    }

    /**
     * The rate a wizard rule creates: the fields of a rate write, {@code name} and
     * {@code method} required.
     */
    public static final class ShippingProfileWizardRate extends Dto {

        public String name;

        public String method;

        public Boolean calculateForEntireOrder;

        public Boolean priceEnteredWithTax;

        /** A {@code {amount, type}} object. */
        public Object shippingCost;

        /** A {@code {amount, type}} object. */
        public Object handlingCost;

        /** Price, quantity, weight and variation rows each have their own keys, so rows stay untyped. */
        public List<Object> conditions;

        public Object additionalRule;

        public String transactionNo;
    }

    /**
     * A rule the wizard creates beside its rate: {@code name} is required, {@code shipping_class}
     * is a class id, and {@code zones} name existing zones by {@code {"id": ...}} or new ones by
     * {@code {"name": ...}}.
     */
    public static final class ShippingProfileWizardRule extends Dto {

        public String name;

        public String description;

        public Long shippingClass;

        public ShippingProfileWizardRate shippingRate;

        public List<ShippingReference> zones;
    }

    /**
     * The wizard's body, wrapped in {@code shipping_profile}: the profile fields and the rules,
     * each with its rate, to create under it.
     */
    public static final class ShippingProfileWizardInput extends Dto {

        public String name;

        public String description;

        public String rulePreference;

        public List<ShippingProfileWizardRule> shippingRules;

        @Override
        protected String envelopeKey() {
            return "shipping_profile";
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
    public static final class ShippingProfileListing extends Dto {

        public List<ShippingProfile> shippingProfiles;

        public Pagination pagination;
    }

    /** The count sub-path's body, a bare {@code {"total": N}}. */
    public static final class ShippingProfileCount extends Dto {

        public Long total;
    }
}
