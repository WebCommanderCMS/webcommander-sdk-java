package com.webcommander.resources.tax_profiles.tax_profiles_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the tax profile bodies. */
public final class TaxProfilesDto {

    private TaxProfilesDto() {
    }

    /** The tax listings' ten-key {@code pagination} block; {@link #limit} is the size applied. */
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

    /** The tax code a nested rule points at. */
    public static final class TaxProfileRuleCode extends Dto {

        public Long id;

        public String name;

        public Boolean isDefault;
    }

    /** One tax zone a nested rule points at. */
    public static final class TaxProfileRuleZone extends Dto {

        public Long id;

        public String name;

        public Boolean isSystemGenerated;

        public Boolean isDefault;
    }

    /**
     * One tax rule as a profile nests it, the same ten keys a {@code /admin/tax_rules} read sends;
     * {@link #Default} is the store's {@code default}, a Java keyword.
     */
    public static final class TaxProfileRule extends Dto {

        public Long id;

        public String name;

        public TaxProfileRuleCode code;

        public String description;

        public Boolean Default;

        public String roundingType;

        public Long decimalPoint;

        public List<TaxProfileRuleZone> zones;

        public String createdAt;

        public String updatedAt;
    }

    /** One tax profile; {@link #Default} is the store's {@code default}, a Java keyword. */
    public static final class TaxProfile extends Dto {

        public Long id;

        public String name;

        public String description;

        public Boolean Default;

        /** Full tax rule records; empty on a new profile. */
        public List<TaxProfileRule> taxRules;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "tax_profile";
        }
    }

    /** The writable form of {@link TaxProfile}, shared by the create and the update. */
    public static final class TaxProfileInput extends Dto {

        public String name;

        public String description;

        public Boolean Default;

        @Override
        protected String envelopeKey() {
            return "tax_profile";
        }
    }

    /** One page of the tax profile listing. */
    public static final class TaxProfilePage extends Dto {

        public List<TaxProfile> taxProfiles;

        public Pagination pagination;
    }
}
