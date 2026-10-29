package com.webcommander.resources.trash.trash_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the trash bodies. */
public final class TrashDto {

    private TrashDto() {
    }

    /**
     * One trashed row, named by its type and its own id, and the {@code item} a restore answers.
     * A restore and a bulk delete send no {@code removedAt}; a bulk delete sends no {@code url}.
     */
    public static final class TrashItem extends Dto {

        /** The snake_case name the type list answers, whatever spelling was sent. */
        public String type;

        public Long id;

        public String name;

        /** When the row was trashed, UTC with a trailing {@code Z}. */
        public String removedAt;

        /** The record's own API path; null for a {@code blog_post}. */
        public String url;

        @Override
        protected String envelopeKey() {
            return "item";
        }
    }

    /** The five-key {@code pagination} block; there is no next-page link, so step offset. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing: the rows and where they sit. */
    public static final class TrashPage extends Dto {

        public List<TrashItem> items;

        public Pagination pagination;
    }

    /** One type the trash holds, as a path's {@code {trash_type}} takes it. */
    public static final class TrashType extends Dto {

        public String type;
    }

    /** The {@code types} body: every type, alphabetical. */
    public static final class TrashTypeList extends Dto {

        public List<TrashType> types;
    }

    /** One item a bulk call could not act on: an error code, and why in words. */
    public static final class TrashBulkError extends Dto {

        public String type;

        public Long id;

        /** Such as {@code not_found} or {@code not_in_trash}. */
        public String code;

        public String message;
    }

    /** The bulk restore's body: the rows restored, and an entry per failed item. */
    public static final class TrashBulkRestoreResult extends Dto {

        public List<TrashItem> restored;

        public List<TrashBulkError> failed;
    }

    /** The bulk delete's body: the rows deleted, each type, id and name, and the failures. */
    public static final class TrashBulkDeleteResult extends Dto {

        public List<TrashItem> deleted;

        public List<TrashBulkError> failed;
    }

    /** One item of a bulk body; {@code type} in any letter case, {@code id} a number. */
    public static final class TrashBulkItemInput extends Dto {

        public String type;

        public Long id;
    }

    /** The bulk body, {@code {"items": [...]}}, 1 to 100 items; no envelope. */
    public static final class TrashBulkInput extends Dto {

        public List<TrashBulkItemInput> items;
    }
}
