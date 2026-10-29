package com.webcommander.resources.common_settings.common_settings_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the common settings bodies. */
public final class CommonSettingsDto {

    private CommonSettingsDto() {
    }

    /**
     * One entry of a settings group, for a write's lists; a read's entries have the same keys.
     * Reads answer {@code value} as text, {@code ***} for a masked one; a number is stored as text.
     */
    public static final class Setting extends Dto {

        public String configKey;

        public String value;
    }

    /**
     * What the read and both writes answer: {@code settings}, group name to a list of
     * {@code {"config_key", "value"}} maps. The {@code notifications} group alone is a name to
     * boolean map, so the groups stay an untyped map.
     */
    public static final class CommonSettings extends Dto {

        public Object settings;
    }

    /**
     * The list form both writes take: {@code settings} maps a group name to a list of
     * {@link Setting} entries. A group may instead be {@code {config_key: value}} or
     * {@code {name: {"config_key", "value"}}}; send those as a {@code Map} or JSON text.
     */
    public static final class CommonSettingsInput extends Dto {

        public Object settings;
    }

    /**
     * The registration form's {@code fields_configs}: six keys for each of the store's 20 fields.
     * {@code _order} reads as text; {@code _active} and {@code _required} are booleans here,
     * though the settings read answers them as text.
     */
    public static final class CustomerFieldConfigs extends Dto {

        public String firstNameOrder;

        public String firstNameKey;

        public String firstNameLabel;

        public String firstNamePlaceholder;

        public Boolean firstNameActive;

        public Boolean firstNameRequired;

        public String lastNameOrder;

        public String lastNameKey;

        public String lastNameLabel;

        public String lastNamePlaceholder;

        public Boolean lastNameActive;

        public Boolean lastNameRequired;

        public String addressLine1Order;

        public String addressLine1Key;

        public String addressLine1Label;

        public String addressLine1Placeholder;

        public Boolean addressLine1Active;

        public Boolean addressLine1Required;

        public String addressLine2Order;

        public String addressLine2Key;

        public String addressLine2Label;

        public String addressLine2Placeholder;

        public Boolean addressLine2Active;

        public Boolean addressLine2Required;

        public String postCodeOrder;

        public String postCodeKey;

        public String postCodeLabel;

        public String postCodePlaceholder;

        public Boolean postCodeActive;

        public Boolean postCodeRequired;

        public String cityOrder;

        public String cityKey;

        public String cityLabel;

        public String cityPlaceholder;

        public Boolean cityActive;

        public Boolean cityRequired;

        public String phoneOrder;

        public String phoneKey;

        public String phoneLabel;

        public String phonePlaceholder;

        public Boolean phoneActive;

        public Boolean phoneRequired;

        public String mobileOrder;

        public String mobileKey;

        public String mobileLabel;

        public String mobilePlaceholder;

        public Boolean mobileActive;

        public Boolean mobileRequired;

        public String faxOrder;

        public String faxKey;

        public String faxLabel;

        public String faxPlaceholder;

        public Boolean faxActive;

        public Boolean faxRequired;

        public String emailOrder;

        public String emailKey;

        public String emailLabel;

        public String emailPlaceholder;

        public Boolean emailActive;

        public Boolean emailRequired;

        public String confirmEmailOrder;

        public String confirmEmailKey;

        public String confirmEmailLabel;

        public String confirmEmailPlaceholder;

        public Boolean confirmEmailActive;

        public Boolean confirmEmailRequired;

        public String passwordOrder;

        public String passwordKey;

        public String passwordLabel;

        public String passwordPlaceholder;

        public Boolean passwordActive;

        public Boolean passwordRequired;

        public String retypePasswordOrder;

        public String retypePasswordKey;

        public String retypePasswordLabel;

        public String retypePasswordPlaceholder;

        public Boolean retypePasswordActive;

        public Boolean retypePasswordRequired;

        public String countryOrder;

        public String countryKey;

        public String countryLabel;

        public String countryPlaceholder;

        public Boolean countryActive;

        public Boolean countryRequired;

        public String customerTypeOrder;

        public String customerTypeKey;

        public String customerTypeLabel;

        public String customerTypePlaceholder;

        public Boolean customerTypeActive;

        public Boolean customerTypeRequired;

        public String sexOrder;

        public String sexKey;

        public String sexLabel;

        public String sexPlaceholder;

        public Boolean sexActive;

        public Boolean sexRequired;

        public String abnOrder;

        public String abnKey;

        public String abnLabel;

        public String abnPlaceholder;

        public Boolean abnActive;

        public Boolean abnRequired;

        public String abnBranchOrder;

        public String abnBranchKey;

        public String abnBranchLabel;

        public String abnBranchPlaceholder;

        public Boolean abnBranchActive;

        public Boolean abnBranchRequired;

        public String companyNameOrder;

        public String companyNameKey;

        public String companyNameLabel;

        public String companyNamePlaceholder;

        public Boolean companyNameActive;

        public Boolean companyNameRequired;

        public String dateOfBirthOrder;

        public String dateOfBirthKey;

        public String dateOfBirthLabel;

        public String dateOfBirthPlaceholder;

        public Boolean dateOfBirthActive;

        public Boolean dateOfBirthRequired;
    }

    /**
     * The registration form: {@code fields} lists the active fields' keys in their order, and
     * {@code fields_configs} configures every field, inactive ones included.
     */
    public static final class CustomerFields extends Dto {

        public List<String> fields;

        public CustomerFieldConfigs fieldsConfigs;
    }
}
