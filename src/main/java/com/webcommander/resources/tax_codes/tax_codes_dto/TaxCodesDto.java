package com.webcommander.resources.tax_codes.tax_codes_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the tax code bodies. */
public final class TaxCodesDto {

    private TaxCodesDto() {
    }

    /** The ten-key {@code pagination} block; {@link #limit} is the size applied, at most 100. */
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

    /** One tax code; {@link #Default} is the store's {@code default}, a Java keyword. */
    public static final class TaxCode extends Dto {

        public Long id;

        public String name;

        public String label;

        public String description;

        public Double rate;

        public String resolverType;

        public Long priority;

        public Boolean Default;

        @Override
        protected String envelopeKey() {
            return "tax_code";
        }
    }

    /** The writable form of {@link TaxCode}, shared by the create and the update. */
    public static final class TaxCodeInput extends Dto {

        public String name;

        public String label;

        public String description;

        public Double rate;

        public String resolverType;

        public Long priority;

        @Override
        protected String envelopeKey() {
            return "tax_code";
        }
    }

    /** One page of the tax code listing. */
    public static final class TaxCodePage extends Dto {

        public List<TaxCode> taxCodes;

        public Pagination pagination;
    }
}
