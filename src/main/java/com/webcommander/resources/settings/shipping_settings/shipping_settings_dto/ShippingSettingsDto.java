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

    /** The {@code group_meta} entry: what the area is and whether it can be written. */
    public static final class AreaMeta extends Dto {

        public String kind;

        public Boolean writable;
    }

    /** The {@code group_meta} block: a single {@link AreaMeta}, keyed by the area itself. */
    public static final class GroupMeta extends Dto {

        public AreaMeta shippingSettings;
    }

    /** The shipping settings, in the one form the API sends them. */
    public static final class ShippingSettings extends Dto {

        public ProfileReference defaultShippingProfile;

        public Boolean enableShippingDefaultTax;

        public ProfileReference defaultDeliveryProfile;

        public Boolean enableDeliveryDefaultTax;

        public ProfileReference shippingTaxProfile;

        public Boolean enableShippingClass;

        public Boolean enableFlatViewClass;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "shipping_settings";
        }
    }
}
