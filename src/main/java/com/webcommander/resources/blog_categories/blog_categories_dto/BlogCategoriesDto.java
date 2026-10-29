package com.webcommander.resources.blog_categories.blog_categories_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the blog category bodies. */
public final class BlogCategoriesDto {

    private BlogCategoriesDto() {
    }

    /** A category's {@code image}, {@code {}} until one is uploaded. */
    public static final class BlogCategoryImage extends Dto {

        public String title;

        public String alternativeText;

        public String fileName;

        public String link;
    }

    /** One {@code seo_configs} entry; no write here sets the list. */
    public static final class BlogCategorySeoConfig extends Dto {

        public Long id;

        public String value;

        public String type;

        public String configKey;
    }

    /** One blog category: a listing row, a row path's body and every write's answer. */
    public static final class BlogCategory extends Dto {

        public Long id;

        /** A UUID string the store assigns; a create or update naming another one keeps the stored one. */
        public String uuid;

        /** Required on both writes, 2 to 255 characters, and unique. */
        public String name;

        public String url;

        public String description;

        public Boolean isDisposable;

        public Boolean isInTrash;

        public BlogCategoryImage image;

        public String createdOn;

        public String lastUpdatedOn;

        public List<BlogCategorySeoConfig> seoConfigs;

        @Override
        protected String envelopeKey() {
            return "blog_category";
        }
    }

    /** An upload; {@code title} and {@code alternativeText} are kept only beside {@code base64}. */
    public static final class BlogCategoryImageInput extends Dto {

        public String fileName;

        public String base64;

        public String title;

        public String alternativeText;
    }

    /** A blog post the category is linked to, by {@code id}. */
    public static final class BlogCategoryBlog extends Dto {

        public Long id;
    }

    /** The body both writes take; an update blanks an unset {@code description}. */
    public static final class BlogCategoryInput extends Dto {

        public String name;

        public String url;

        public String description;

        public BlogCategoryImageInput image;

        public List<BlogCategoryBlog> blogs;

        @Override
        protected String envelopeKey() {
            return "blog_category";
        }
    }

    /** The listing's ten-key {@code pagination} block; {@link #limit} is the size applied. */
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

    /** One page of the listing; {@code field_metadata} is accepted and adds no key. */
    public static final class BlogCategoryPage extends Dto {

        public List<BlogCategory> blogCategories;

        public Pagination pagination;
    }
}
