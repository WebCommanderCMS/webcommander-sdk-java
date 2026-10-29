package com.webcommander.resources.tax_zones.tax_zones_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the tax zone bodies. */
public final class TaxZonesDto {

    private TaxZonesDto() {
    }

    /** The tax listings' ten-key {@code pagination} block; {@link #limit} is the size applied. */
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

    /** A country as a tax row nests it; a zone write sends this record, never a bare id. */
    public static final class TaxCountry extends Dto {

        public Long id;

        public String name;

        public String code;
    }

    /** A state as a tax zone nests it in {@code states}. */
    public static final class TaxState extends Dto {

        public Long id;

        public String name;

        public String code;
    }

    /** One tax zone; {@link #Default} is the store's {@code default}, a Java keyword. */
    public static final class TaxZone extends Dto {

        public Long id;

        public String name;

        public Boolean systemGenerated;

        public Boolean Default;

        public Boolean useRadius;

        public Double radius;

        public String startLocation;

        public List<TaxCountry> countries;

        public List<TaxState> states;

        /** The flat list of codes a write sends, on every read. */
        public List<String> postCode;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "tax_zone";
        }
    }

    /** The writable form of {@link TaxZone}; the update replaces every list left unset. */
    public static final class TaxZoneInput extends Dto {

        public String name;

        public List<TaxCountry> countries;

        public List<TaxState> states;

        public List<String> postCode;

        public Double radius;

        public String startLocation;

        public Boolean useRadius;

        @Override
        protected String envelopeKey() {
            return "tax_zone";
        }
    }

    /** One page of the tax zone listing. */
    public static final class TaxZonePage extends Dto {

        public List<TaxZone> taxZones;

        public Pagination pagination;
    }
}
