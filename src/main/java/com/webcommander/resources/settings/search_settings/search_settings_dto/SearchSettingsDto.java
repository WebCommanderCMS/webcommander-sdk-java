package com.webcommander.resources.settings.search_settings.search_settings_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the search settings bodies. */
public final class SearchSettingsDto {

    private SearchSettingsDto() {
    }

    /** One entry of an {@code excluded} list as a read sends it. */
    public static final class ExcludedRecord extends Dto {

        public Long id;

        public String name;
    }

    /** One content type the site search covers: whether it is searched, and how hits show. */
    public static class SearchableContentGroup extends Dto {

        public Boolean enabled;

        public String displayHeading;

        public List<ExcludedRecord> excluded;
    }

    /** The {@code product_categories} block — the only content group carrying a fourth field. */
    public static final class ProductCategoriesContent extends SearchableContentGroup {

        public Boolean hideParentCategory;
    }

    /** The {@code searchable_content} section: the search box text and the content it covers. */
    public static final class SearchableContent extends Dto {

        public String placeholderText;

        public SearchableContentGroup product;

        public ProductCategoriesContent productCategories;

        public SearchableContentGroup blog;

        public SearchableContentGroup otherPages;

        @Override
        protected String envelopeKey() {
            return "searchable_content";
        }
    }

    /** One row of {@code search_fields.fields}: a product field and where it ranks. */
    public static final class SearchFieldPriority extends Dto {

        public String field;

        public Long priority;
    }

    /** The {@code search_fields} section: how a query is matched, and against which fields. */
    public static final class SearchFields extends Dto {

        public Boolean matchKeywordsExactly;

        public Boolean matchFullSentences;

        public List<SearchFieldPriority> fields;

        @Override
        protected String envelopeKey() {
            return "search_fields";
        }
    }

    /** One group of {@code synonyms.groups}: words a query treats as each other. */
    public static final class SynonymGroup extends Dto {

        public List<String> keywords;
    }

    /** The {@code synonyms} section: read it, do not write it. */
    public static final class Synonyms extends Dto {

        public List<SynonymGroup> groups;

        @Override
        protected String envelopeKey() {
            return "synonyms";
        }
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per section the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta searchableContent;

        public SectionMeta searchFields;

        public SectionMeta synonyms;
    }

    /** The {@code reindex_status} sub-resource: where the Elasticsearch reindex stands. */
    public static final class ReindexStatus extends Dto {

        public String state;

        public Object startedAt;

        public Object finishedAt;

        public Object error;
    }

    /** The search settings, as the facade or one section sends them. */
    public static final class SearchSettings extends Dto {

        public SearchableContent searchableContent;

        public SearchFields searchFields;

        public Synonyms synonyms;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "search_settings";
        }
    }
}
