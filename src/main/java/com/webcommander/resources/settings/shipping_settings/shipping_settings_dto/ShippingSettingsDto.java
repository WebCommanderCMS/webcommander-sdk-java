package com.webcommander.resources.settings.shipping_settings.shipping_settings_dto;

import com.webcommander.Dto;

/** Typed views over the shipping settings body. */
public final class ShippingSettingsDto {

    private ShippingSettingsDto() {
    }

    /** What a reference field holds: the id the store keeps and the name it resolves to. */
    public static final class ProfileReference extends Dto {

        public Long id;

        public String name;
    }

    /** One {@code group_meta} entry: what the area is, whether it can be written, and its path. */
    public static final class AreaMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block: the settings area and the two collections beside it. */
    public static final class GroupMeta extends Dto {

        public AreaMeta shippingSettings;

        public AreaMeta shippingMethods;

        public AreaMeta shippingClasses;
    }

    /** The shipping settings, in the one form the API sends them. */
    public static final class ShippingSettings extends Dto {

        public ProfileReference defaultShippingProfile;

        public Boolean applyStoreTaxSettingsToShipping;

        public ProfileReference defaultDeliveryProfile;

        public Boolean applyStoreTaxSettingsToDelivery;

        public ProfileReference shippingTaxProfile;

        public Boolean enableShippingClass;

        public Boolean enableClassInFlatView;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "shipping_settings";
        }
    }
}
