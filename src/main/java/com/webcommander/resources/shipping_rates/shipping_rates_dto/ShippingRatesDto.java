package com.webcommander.resources.shipping_rates.shipping_rates_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the shipping rate bodies. */
public final class ShippingRatesDto {

    private ShippingRatesDto() {
    }

    /** An {@code {id, name}} pair: a rule that uses the rate. */
    public static final class ShippingReference extends Dto {

        public Long id;

        public String name;
    }

    /** A shipping or handling cost; {@code type} is {@code fixed} or {@code percent}. */
    public static final class ShippingCost extends Dto {

        public Double amount;

        public String type;
    }

    /**
     * The charge for each item past the first; the other two keys read {@code null} while
     * {@code enabled} is false, and {@code enabled} true needs both.
     */
    public static final class ShippingAdditionalRule extends Dto {

        public Boolean enabled;

        public Long forEachAdditional;

        public Double additionalCost;
    }

    /**
     * One rate, the same fourteen keys on a listing row, the row read and every write's answer,
     * whatever the method. {@code method} is {@code flat_rate}, {@code free_shipping},
     * {@code ship_by_price}, {@code ship_by_quantity}, {@code ship_by_weight},
     * {@code ship_by_variation} or {@code shipping_variant}. A flat rate carries
     * {@code shipping_cost} and {@code handling_cost}, the {@code ship_by_*} methods carry them per
     * row in {@code conditions}, and a key the method does not use reads {@code null} or an empty
     * list. {@code weight_unit} is the store's, {@code null} unless the method is
     * {@code ship_by_weight}.
     */
    public static final class ShippingRate extends Dto {

        public Long id;

        public String name;

        public String method;

        public Boolean calculateForEntireOrder;

        public Boolean priceEnteredWithTax;

        public ShippingCost shippingCost;

        public ShippingCost handlingCost;

        public String weightUnit;

        /** Price, quantity, weight and variation rows each have their own keys, so rows stay untyped. */
        public List<Object> conditions;

        public ShippingAdditionalRule additionalRule;

        public List<ShippingReference> shippingRules;

        public String transactionNo;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "shipping_rate";
        }
    }

    /**
     * The writable fields, wrapped in {@code shipping_rate}, for the create, the {@code PUT}
     * replace and the {@code PATCH}. {@code name} and {@code method} are required on a create; a
     * flat rate also needs {@code shipping_cost} and the {@code ship_by_*} methods need
     * {@code conditions}. {@code PUT} needs every field of the rate, {@code handling_cost} and
     * {@code additional_rule} as {@code null} when unused, which takes a {@code Map}. A field left
     * unset is not sent. {@code shipping_rule_id} attaches the new rate to a rule that has none.
     */
    public static final class ShippingRateInput extends Dto {

        public String name;

        public String method;

        public Boolean calculateForEntireOrder;

        public Boolean priceEnteredWithTax;

        public ShippingCost shippingCost;

        public ShippingCost handlingCost;

        public List<Object> conditions;

        public ShippingAdditionalRule additionalRule;

        public Long shippingRuleId;

        public String transactionNo;

        public Boolean suggestName;

        @Override
        protected String envelopeKey() {
            return "shipping_rate";
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
    public static final class ShippingRateListing extends Dto {

        public List<ShippingRate> shippingRates;

        public Pagination pagination;
    }

    /** The count sub-path's body, a bare {@code {"total": N}}. */
    public static final class ShippingRateCount extends Dto {

        public Long total;
    }
}
