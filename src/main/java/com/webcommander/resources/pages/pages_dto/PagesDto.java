package com.webcommander.resources.pages.pages_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the page bodies. */
public final class PagesDto {

    private PagesDto() {
    }

    /**
     * An {@code {id, name}} pair: the layout, an access-list customer or group, the navigation and
     * the author. A write names one by {@code {"id": ...}}; {@code name} is read-only.
     */
    public static final class PageReference extends Dto {

        public Long id;

        public String name;
    }

    /** The password gate; switching it off keeps the stored password. */
    public static final class PagePasswordProtection extends Dto {

        public Boolean enabled;

        /** {@code ***} on a listing row, as saved on the row read and the writes' answers. */
        public String password;
    }

    /** Whether the page is linked from a navigation. */
    public static final class PageMenuPlacement extends Dto {

        public Boolean enabled;

        /** {@code null} when not placed; a write sending {@code enabled} true needs its id. */
        public PageReference navigation;
    }

    /** The draft and approval state; the draft and published ids are null, never 0. */
    public static final class PagePublishing extends Dto {

        public Boolean draft;

        public Long draftPageId;

        public Long publishedPageId;

        public Boolean pendingApproval;

        public Long pendingApprovalCount;

        public List<Long> pendingApprovalIds;

        public Boolean disposable;
    }

    /** One page: a listing row, the row read and every write's answer, the same 23 keys. */
    public static final class Page extends Dto {

        public Long id;

        public String name;

        public String title;

        public String subtitle;

        public String url;

        public String fullUrl;

        public PageReference layout;

        /** {@code open}, {@code hidden} or {@code restricted}. */
        public String visibility;

        /** {@code all} or {@code selected} on a restricted page, null otherwise. */
        public String visibleTo;

        public List<PageReference> customers;

        public List<PageReference> customerGroups;

        public String bodyClass;

        public PagePasswordProtection passwordProtection;

        public PageMenuPlacement menuPlacement;

        public Boolean active;

        public Boolean disableTracking;

        /** Held by exactly one page, and moved only by the {@code landing} sub-path. */
        public Boolean landing;

        public Boolean inTrash;

        public PagePublishing publishing;

        /** Null on every page the store holds, so its shape is unknown. */
        public Object store;

        public PageReference author;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "page";
        }
    }

    /**
     * The writable fields, wrapped in {@code page}, for the create, the {@code PUT} replace and
     * the {@code PATCH}. An unset field is not sent: to send a null, pass a {@code Map}.
     */
    public static final class PageInput extends Dto {

        public String name;

        public String title;

        public String subtitle;

        public String url;

        public PageReference layout;

        public String visibility;

        public String visibleTo;

        /** Public {@code customer_id} values, bare or as a {@link PageReference}. */
        public List<Object> customers;

        /** Group ids, bare or as a {@link PageReference}. */
        public List<Object> customerGroups;

        public String bodyClass;

        public PagePasswordProtection passwordProtection;

        public PageMenuPlacement menuPlacement;

        public Boolean active;

        public Boolean disableTracking;

        @Override
        protected String envelopeKey() {
            return "page";
        }
    }

    /** The URL write's bare body, {@code {"url": ...}}; a {@code page} wrapper is 400. */
    public static final class PageUrlInput extends Dto {

        public String url;
    }

    /** The five-key {@code pagination} block; under {@code limit=all} the limit is the total. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing: the rows outside the trash, and where they sit. */
    public static final class PageListing extends Dto {

        public List<Page> pages;

        public Pagination pagination;
    }

    /** The count sub-path's body, a bare {@code {"total": N}}. */
    public static final class PageCount extends Dto {

        public Long total;
    }

    /** One reason the page cannot be deleted, such as {@code landing_page}. */
    public static final class PageDeleteBlocker extends Dto {

        public String code;

        public String message;
    }

    /** The delete check's body: whether a delete would pass, and what blocks or links to it. */
    public static final class PageDeleteBlockers extends Dto {

        public Boolean canDelete;

        public List<PageDeleteBlocker> blockers;

        /** Empty on every page read, so its rows stay untyped. */
        public List<Object> linked;
    }
}
