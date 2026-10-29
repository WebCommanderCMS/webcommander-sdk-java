package com.webcommander.resources.blogs.blogs_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the blog and blog comment bodies. */
public final class BlogsDto {

    private BlogsDto() {
    }

    /** A category a blog is filed under; a write names it {@code {"id": …}}. */
    public static final class BlogCategoryReference extends Dto {

        public Long id;

        public String name;

        public Boolean isDisposable;

        public Boolean isInTrash;
    }

    /** The cover image as a read answers it, {@code {}} when there is none. */
    public static final class BlogImage extends Dto {

        public String title;

        public String alternativeText;

        public String fileName;

        public String link;
    }

    /** The cover image a write uploads; without {@code base64} the stored image is kept. */
    public static final class BlogImageInput extends Dto {

        public String fileName;

        public String base64;

        public String title;

        public String alternativeText;
    }

    /** {@code created_by}; {@code {}} on a row the store recorded no author for. */
    public static final class BlogAuthor extends Dto {

        public Long id;

        public String name;
    }

    /** One customer, by {@code internal_id}, or one customer group a blog is shown to. */
    public static final class BlogAudienceMember extends Dto {

        public Long id;

        public String name;
    }

    /** {@code selected_customers} once it names anyone; the store sends {@code []} otherwise. */
    public static final class BlogAudience extends Dto {

        public List<BlogAudienceMember> customers;

        public List<BlogAudienceMember> groups;
    }

    /** The audience a write names. */
    public static final class BlogAudienceInput extends Dto {

        /** Customer {@code internal_id} values. */
        public List<Long> customers;

        /** Customer group ids. */
        public List<Long> groups;
    }

    /** One {@code meta_tag} entry, the same two fields on a write and a read. */
    public static final class BlogMetaTag extends Dto {

        public String name;

        public String value;
    }

    /** {@code seo_configs}, an object holding the {@code meta_tag} list. */
    public static final class BlogSeoConfigs extends Dto {

        public List<BlogMetaTag> metaTag;
    }

    /** The one {@code group_meta} entry: what the collection is and whether it is writable. */
    public static final class ResourceMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block the listing, a row read and a {@code PATCH} answer carry. */
    public static final class GroupMeta extends Dto {

        public ResourceMeta blogs;
    }

    /** One blog, the same eighteen fields on a listing row, a row read and every write. */
    public static final class Blog extends Dto {

        public Long id;

        /** A UUID string the store assigns; a create naming it is {@code 400 unknown_field}. */
        public String uuid;

        public String title;

        /** The title's slug on the create; a replace, or a patch naming {@code title}, sets it. */
        public String url;

        public String content;

        public String date;

        public Boolean isPublished;

        public String visibility;

        /** Kept only while {@code visibility} is {@code restricted}, {@code null} otherwise. */
        public String visibleTo;

        public List<BlogCategoryReference> categories;

        public String createdOn;

        public String lastUpdatedOn;

        public Boolean isDisposable;

        public Boolean isInTrash;

        public BlogImage image;

        public BlogAuthor createdBy;

        public BlogAudience selectedCustomers;

        public BlogSeoConfigs seoConfigs;

        public GroupMeta groupMeta;

        /** The store's field schema, present only when {@code field_metadata=true} was sent. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "blog";
        }
    }

    /** The writable form of {@link Blog}; the store's own schema marks {@code url} read-only. */
    public static final class BlogInput extends Dto {

        public String title;

        public String content;

        public String date;

        public Boolean isPublished;

        public String visibility;

        public String visibleTo;

        public List<BlogCategoryReference> categories;

        public BlogImageInput image;

        public BlogSeoConfigs seoConfigs;

        public BlogAudienceInput selectedCustomers;

        @Override
        protected String envelopeKey() {
            return "blog";
        }
    }

    /** The listing's ten-key {@code pagination} block; {@link #limit} is capped at 20. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Long count;

        /** The page {@code offset} falls in. */
        public Long currentPage;

        public Long totalPages;

        public Boolean hasNext;

        public Boolean hasPrevious;

        /** A whole URL carrying the query, or {@code null}. */
        public String previousPage;

        /** A whole URL carrying the query, or {@code null}. */
        public String nextPage;
    }

    /** One page of the listing: the rows, {@code group_meta}, the paging block and any schema. */
    public static final class BlogPage extends Dto {

        public List<Blog> blogs;

        public GroupMeta groupMeta;

        public Pagination pagination;

        public Object fieldMetadata;
    }

    /** The count sub-path's body, {@code {"blogs": {"count": N}}}. */
    public static final class BlogCount extends Dto {

        public Long count;

        @Override
        protected String envelopeKey() {
            return "blogs";
        }
    }

    /** One comment, the same twelve fields in the listing and on its own path. */
    public static final class BlogComment extends Dto {

        public Long id;

        public String status;

        public String name;

        public String email;

        /** The blog's current title, following a later rename. */
        public String postTitle;

        public String content;

        public Boolean spam;

        public Long likes;

        public Long totalReply;

        public String createdOn;

        public String lastUpdatedOn;

        public List<Object> replies;
    }

    /** The comments sub-path's body: the blog's {@code id} beside every comment, unpaged. */
    public static final class BlogCommentList extends Dto {

        /** The blog's id, not a comment's. */
        public Long id;

        public List<BlogComment> comments;
    }

    /** One comment's body, from its own path and each comment write, beside the blog's id. */
    public static final class BlogCommentEnvelope extends Dto {

        /** The blog's id; the comment's own is {@code comment.id}. */
        public Long id;

        public BlogComment comment;
    }

    /** The comment a create sends: {@code content} required, at most 1000 characters. */
    public static final class BlogCommentDraft extends Dto {

        public String content;

        public String name;

        public String email;
    }

    /** The comment create's body, {@code {"blog": {"comment": {...}}}}. */
    public static final class BlogCommentInput extends Dto {

        public BlogCommentDraft comment;

        @Override
        protected String envelopeKey() {
            return "blog";
        }
    }
}
