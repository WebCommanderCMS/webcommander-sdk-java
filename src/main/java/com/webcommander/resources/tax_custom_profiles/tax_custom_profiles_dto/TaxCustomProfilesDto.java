package com.webcommander.resources.tax_custom_profiles.tax_custom_profiles_dto;

import com.webcommander.Dto;

import java.util.List;
import java.util.Set;

/** Typed views over the custom tax profile bodies. */
public final class TaxCustomProfilesDto {

    private TaxCustomProfilesDto() {
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

    /** One profile a rule is filed under, on the rule's {@code profiles}. */
    public static final class CustomProfileReference extends Dto {

        public Long id;

        public String name;
    }

    /** One entry of a custom profile's {@code rules}, which only the row path sends. */
    public static final class CustomProfileRule extends Dto {

        public Long id;

        public String name;

        public String description;

        public Long zonesCount;

        public List<CustomProfileReference> profiles;

        public String createdOn;

        public String lastUpdatedOn;
    }

    /** One custom tax profile; only the row path adds {@code rules} to the eight keys. */
    public static final class CustomProfile extends Dto {

        public Long id;

        public String name;

        public String taxCode;

        public String description;

        public Boolean isDefault;

        public Long rulesCount;

        public String createdOn;

        public String lastUpdatedOn;

        public List<CustomProfileRule> rules;

        @Override
        protected String envelopeKey() {
            return "custom_profile";
        }
    }

    /** The writable form of {@link CustomProfile}; the create requires {@link #name}. */
    public static final class CustomProfileInput extends Dto {

        /** Kept by the update when left out. */
        public String name;

        /** Emptied by the update when left out, as {@link #description} is. */
        public String taxCode;

        public String description;

        public Boolean isDefault;

        /** Written beside the {@code custom_profile} object rather than inside it. */
        public String transactionNo;

        @Override
        protected String envelopeKey() {
            return "custom_profile";
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

        public ResourceMeta customProfile;

        public ResourceMeta rules;
    }

    /** One page of the custom profile listing. */
    public static final class CustomProfilePage extends Dto {

        public List<CustomProfile> customProfiles;

        public GroupMeta groupMeta;

        public Pagination pagination;
    }
}
