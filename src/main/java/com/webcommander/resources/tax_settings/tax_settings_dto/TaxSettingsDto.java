package com.webcommander.resources.tax_settings.tax_settings_dto;

import com.webcommander.Dto;

/** Typed views over the tax settings bodies. */
public final class TaxSettingsDto {

    private TaxSettingsDto() {
    }

    /** The nine keys the settings facade sends, flat under {@code tax_settings}. */
    public static final class TaxSettings extends Dto {

        public String configurationType;

        public Boolean systemDefaultTax;

        public String defaultCountry;

        public Long customTaxProfileId;

        public Long defaultTaxProfile;

        public Boolean showPriceWithTax;

        public Boolean isPriceWithTax;

        public String defaultTaxCode;

        public String taxMessage;

        @Override
        protected String envelopeKey() {
            return "tax_settings";
        }
    }

    /** The body the settings {@code PUT} takes, wrapped in {@code settings}. */
    public static final class TaxSettingsInput extends Dto {

        public String configurationType;

        public Boolean systemDefaultTax;

        public String defaultCountry;

        public Long customTaxProfileId;

        public Long defaultTaxProfile;

        public Boolean showPriceWithTax;

        public Boolean isPriceWithTax;

        public String defaultTaxCode;

        public String taxMessage;

        @Override
        protected String envelopeKey() {
            return "settings";
        }
    }
}
