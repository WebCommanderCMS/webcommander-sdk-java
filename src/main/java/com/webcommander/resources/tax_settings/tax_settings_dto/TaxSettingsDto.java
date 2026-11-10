package com.webcommander.resources.tax_settings.tax_settings_dto;

import com.webcommander.Dto;

/** Typed views over the tax settings bodies. */
public final class TaxSettingsDto {

    private TaxSettingsDto() {
    }

    /** The country tax the store charges by default, as {@code tax_region} reads it. */
    public static final class TaxRegion extends Dto {

        public Long countryTaxId;

        public String countryCode;

        public String countryName;
    }

    /** A custom tax profile as a read names it: its id and name, or {@code null}. */
    public static final class TaxProfileReference extends Dto {

        public Long id;

        public String name;
    }

    /** One {@code group_meta} entry: what the area is, whether it can be written, and its path. */
    public static final class TaxSettingsAreaMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block: the settings area and the two tax collections beside it. */
    public static final class TaxSettingsGroupMeta extends Dto {

        public TaxSettingsAreaMeta taxSettings;

        public TaxSettingsAreaMeta taxProfiles;

        public TaxSettingsAreaMeta taxRules;
    }

    /**
     * The nine keys under {@code tax_settings}, with {@code group_meta} and, when asked for,
     * {@code field_metadata}.
     */
    public static final class TaxSettings extends Dto {

        public String taxRateForCustomers;

        public TaxRegion taxRegion;

        public TaxProfileReference customTaxProfile;

        public Boolean isPriceShownWithTax;

        public String pricesInCatalog;

        public String calculatedBy;

        public Boolean isOutOfRegionTaxFree;

        public String taxMessage;

        public String basePriceRounding;

        public TaxSettingsGroupMeta groupMeta;

        /** The store's field schema, present only when {@code field_metadata=true} was sent. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "tax_settings";
        }
    }

    /**
     * {@code tax_region} as a write takes it: a {@code country_code} (with an optional
     * {@code state_code}) or a {@code country_tax_id}.
     */
    public static final class TaxRegionInput extends Dto {

        public Long countryTaxId;

        public String countryCode;

        public String stateCode;
    }

    /**
     * The body {@code PUT} and {@code PATCH} take, wrapped in {@code tax_settings}; both merge.
     * The profile is written as {@code custom_tax_profile_id}; {@code calculated_by} is read-only.
     */
    public static final class TaxSettingsInput extends Dto {

        public String taxRateForCustomers;

        public TaxRegionInput taxRegion;

        public Long customTaxProfileId;

        public Boolean isPriceShownWithTax;

        public String pricesInCatalog;

        public Boolean isOutOfRegionTaxFree;

        public String taxMessage;

        public String basePriceRounding;

        @Override
        protected String envelopeKey() {
            return "tax_settings";
        }
    }
}
