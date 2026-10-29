package com.webcommander.resources.tax_rules.tax_rules_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the tax rule bodies. */
public final class TaxRulesDto {

    private TaxRulesDto() {
    }

    /** The ten-key {@code pagination} block; {@link #limit} is the size applied, at most 100. */
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

    /** The tax code a rule points at, as the rule nests it; {@code {}} when unset. */
    public static final class TaxRuleCode extends Dto {

        public Long id;

        public String name;

        public Boolean isDefault;
    }

    /** One tax zone a rule points at, as the rule nests it. */
    public static final class TaxRuleZone extends Dto {

        public Long id;

        public String name;

        public Boolean isSystemGenerated;

        public Boolean isDefault;
    }

    /** One tax rule; {@link #Default} is the store's {@code default}, a Java keyword. */
    public static final class TaxRule extends Dto {

        public Long id;

        public String name;

        public TaxRuleCode code;

        public String description;

        public Boolean Default;

        public String roundingType;

        public Long decimalPoint;

        public List<TaxRuleZone> zones;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "tax_rule";
        }
    }

    /** The writable form of {@link TaxRule}, shared by the create and the update. */
    public static final class TaxRuleInput extends Dto {

        public String name;

        public String description;

        @Override
        protected String envelopeKey() {
            return "tax_rule";
        }
    }

    /** One page of the tax rule listing. */
    public static final class TaxRulePage extends Dto {

        public List<TaxRule> taxRules;

        public Pagination pagination;
    }
}
