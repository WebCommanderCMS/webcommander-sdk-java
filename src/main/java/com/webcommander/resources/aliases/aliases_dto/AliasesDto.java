package com.webcommander.resources.aliases.aliases_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the domain alias bodies. */
public final class AliasesDto {

    private AliasesDto() {
    }

    /** One alias, as a listing row or a create's answer; {@link #alias} is the hostname. */
    public static final class Alias extends Dto {

        public Long id;

        public String alias;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "alias";
        }
    }

    /** The create body, {@code {"alias": "<hostname>"}}, with the field bare. */
    public static final class AliasInput extends Dto {

        public String alias;
    }

    /** The listing's five-key {@code pagination} block; {@link #limit} is the size applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasNext;

        public Boolean hasPrevious;
    }

    /** One page of the listing. */
    public static final class AliasPage extends Dto {

        public List<Alias> aliases;

        public Pagination pagination;
    }
}
