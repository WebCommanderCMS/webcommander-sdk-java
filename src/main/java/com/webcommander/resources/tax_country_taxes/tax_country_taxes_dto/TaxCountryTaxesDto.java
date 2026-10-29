package com.webcommander.resources.tax_country_taxes.tax_country_taxes_dto;

import com.webcommander.Dto;

import java.util.List;
import java.util.Set;

/** Typed views over the country tax bodies. */
public final class TaxCountryTaxesDto {

    private TaxCountryTaxesDto() {
    }

    /** The ten-key {@code pagination} block; {@link #limit} is the size applied, capped at 100. */
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

    /** The country a row nests under {@code country}; {@code null} on the rest-of-the-world row. */
    public static final class TaxCountry extends Dto {

        public Long id;

        public String name;

        public String code;
    }

    /** A state as a state rule nests it, on its {@code state}. */
    public static final class TaxState extends Dto {

        public Long id;

        public String name;

        public String code;
    }

    /** One entry of {@code state_rules}, which only the row path and the update send. */
    public static final class CountryTaxStateRule extends Dto {

        public Long id;

        public String name;

        public TaxState state;

        public String addressLine1;

        public String addressLine2;

        public String city;

        public String postCode;

        public Double taxRate;

        public String taxRule;

        public Double effectiveTaxRate;

        public Boolean isDefault;

        public String createdOn;

        public String lastUpdatedOn;

        public Object lastUpdatedBy;
    }

    /** One country tax; the row path and the update add {@code state_rules} to twelve keys. */
    public static final class CountryTax extends Dto {

        public Long id;

        public String name;

        public TaxCountry country;

        public String taxCode;

        public Double taxRate;

        /** Reads lower case: {@code fixed_rate}, {@code api} or {@code no_tax}. */
        public String taxMethod;

        public Boolean isDefault;

        public Boolean isRestOfWorld;

        public Long stateRulesCount;

        public String createdOn;

        public String lastUpdatedOn;

        public Object lastUpdatedBy;

        public List<CountryTaxStateRule> stateRules;

        @Override
        protected String envelopeKey() {
            return "country_tax";
        }
    }

    /** The writable form of {@link CountryTax}; the create requires {@link #countryCode}. */
    public static final class CountryTaxInput extends Dto {

        public String countryCode;

        public String name;

        public String taxCode;

        public Double taxRate;

        public String taxMethod;

        public Boolean isDefault;

        /** Written beside the {@code country_tax} object rather than inside it. */
        public String transactionNo;

        @Override
        protected String envelopeKey() {
            return "country_tax";
        }

        @Override
        protected Set<String> topLevel() {
            return Set.of("transaction_no");
        }
    }

    /** One {@code group_meta} entry: what the path is, whether it is writable and where. */
    public static final class ResourceMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block only the listing sends. */
    public static final class GroupMeta extends Dto {

        public ResourceMeta countryTax;

        public ResourceMeta stateRules;
    }

    /** One page of the country tax listing. */
    public static final class CountryTaxPage extends Dto {

        public List<CountryTax> countryTaxes;

        public GroupMeta groupMeta;

        public Pagination pagination;
    }
}
