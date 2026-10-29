package com.webcommander.resources.settings.seo_configuration.seo_configuration_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the SEO configuration settings bodies. */
public final class SeoConfigurationDto {

    private SeoConfigurationDto() {
    }

    /** One content type's block of {@code global_store_settings}: its SEO templates. */
    public static final class SeoTemplates extends Dto {

        public Boolean disableIndexing;

        public List<String> titleTemplate;

        public List<String> descriptionTemplate;
    }

    /** The {@code global_store_settings} section: the store name and the SEO templates. */
    public static final class GlobalStoreSettings extends Dto {

        public String storeName;

        public String titleSeparator;

        public SeoTemplates pages;

        public SeoTemplates products;

        public SeoTemplates categories;

        @Override
        protected String envelopeKey() {
            return "global_store_settings";
        }
    }

    /** The {@code header} or {@code footer} block of {@code custom_code}; {@link #code} is raw. */
    public static final class CustomCodeBlock extends Dto {

        public Boolean enabled;

        public String code;
    }

    /** The {@code custom_code} section: two blocks whose {@code code} reaches every page. */
    public static final class CustomCode extends Dto {

        public CustomCodeBlock header;

        public CustomCodeBlock footer;

        @Override
        protected String envelopeKey() {
            return "custom_code";
        }
    }

    /** One redirect row: the store's {@code id} and the pair of URLs. */
    public static final class Redirect extends Dto {

        public Long id;

        public String sourceUrl;

        public String destinationUrl;

        @Override
        protected String envelopeKey() {
            return "redirect";
        }
    }

    /** The {@code redirects} block inside {@code url_settings}, inlined for reading. */
    public static final class RedirectsPreview extends Dto {

        public List<Redirect> items;
    }

    /** The {@code url_settings} section: whether renaming an item rewrites its URL. */
    public static final class UrlSettings extends Dto {

        public Boolean regenerateUrlOnItemUpdate;

        public Boolean autoRedirectOnUrlChange;

        public RedirectsPreview redirects;

        @Override
        protected String envelopeKey() {
            return "url_settings";
        }
    }

    /** One file served from the store's document root. */
    public static final class DocumentRootFile extends Dto {

        public String configKey;

        public String fileName;

        public String filePath;

        @Override
        protected String envelopeKey() {
            return "document_root_file";
        }
    }

    /** The {@code document_root} block the facade inlines, without {@code pagination}. */
    public static final class DocumentRootPreview extends Dto {

        public List<DocumentRootFile> items;
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;

        public SectionMeta redirects;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per thing the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta globalStoreSettings;

        public SectionMeta customCode;

        public SectionMeta documentRoot;

        public SectionMeta urlSettings;
    }

    /** The SEO configuration, as the facade or one section sends it. */
    public static final class SeoConfiguration extends Dto {

        public GlobalStoreSettings globalStoreSettings;

        public CustomCode customCode;

        public DocumentRootPreview documentRoot;

        public UrlSettings urlSettings;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "seo_configuration";
        }
    }
}
