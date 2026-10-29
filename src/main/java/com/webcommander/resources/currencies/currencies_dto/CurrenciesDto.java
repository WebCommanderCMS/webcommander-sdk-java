package com.webcommander.resources.currencies.currencies_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the currency bodies. */
public final class CurrenciesDto {

    private CurrenciesDto() {
    }

    /** The country a currency is filed under, as every row answers it. */
    public static final class CurrencyCountry extends Dto {

        public Long id;

        public String code;

        public String name;
    }

    /** One currency: a listing row, the row path's body and every row write's answer. */
    public static final class Currency extends Dto {

        public Long id;

        public String name;

        /** Three letters, stored upper-case. */
        public String code;

        public String symbol;

        public CurrencyCountry country;

        /** Stored to 6 decimal places. */
        public Double conversionRate;

        public String rounding;

        public Long precision;

        public Boolean active;

        /** Held by exactly one row, and moved only by the {@code base} sub-path. */
        public Boolean base;

        public String updatedAt;

        /** The store's field schema, present only when {@code field_metadata=true} was sent. */
        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "currency";
        }
    }

    /** The writable fields, for the create, the {@code PUT} replace and the {@code PATCH}. */
    public static final class CurrencyInput extends Dto {

        public String name;

        public String code;

        public String symbol;

        /** A country's id, code, name or {@code {"code": ...}}. */
        public Object country;

        public Double conversionRate;

        public String rounding;

        public Long precision;

        public Boolean active;
    }

    /** The five-key {@code pagination} block; {@code limit} is the page size applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing: the rows, where they sit and any schema. */
    public static final class CurrencyPage extends Dto {

        public List<Currency> currencies;

        public Pagination pagination;

        public Object fieldMetadata;
    }

    /** The count sub-path's body, a bare {@code {"total": N}}. */
    public static final class CurrencyCount extends Dto {

        public Long total;
    }

    /** The row that held the base flag before a base write. */
    public static final class CurrencyReference extends Dto {

        public Long id;

        public String code;

        public String name;
    }

    /** The base write's body: the new base, and the row that held the flag or {@code null}. */
    public static final class CurrencyBaseChange extends Dto {

        public Currency currency;

        public CurrencyReference previousBase;
    }
}
