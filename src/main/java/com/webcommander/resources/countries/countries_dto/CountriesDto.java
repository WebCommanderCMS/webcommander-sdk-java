package com.webcommander.resources.countries.countries_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the country and state bodies. */
public final class CountriesDto {

    private CountriesDto() {
    }

    /** One country: a listing row, and the country path's body. */
    public static final class Country extends Dto {

        public Long id;

        /** Two upper-case letters. */
        public String code;

        public String name;

        /** Equal to the {@code total} of this country's state listing. */
        public Long stateCount;

        public Boolean active;

        /** Held by one country, the {@code administration} section's default_country. */
        public Boolean Default;

        @Override
        protected String envelopeKey() {
            return "country";
        }
    }

    /** The short parent country a state listing and a state read carry beside the state. */
    public static final class CountryReference extends Dto {

        public Long id;

        public String code;

        public String name;
    }

    /** One state: a listing row, and the state path's body with its parent beside it. */
    public static final class CountryState extends Dto {

        public Long id;

        /** One to three letters or digits. */
        public String code;

        public String name;

        public Boolean active;

        public Boolean Default;

        /** Set on the state read, from the {@code country} beside {@code state}; null on a row. */
        public CountryReference country;

        @Override
        protected String envelopeKey() {
            return "state";
        }
    }

    /** The five-key {@code pagination} block of both listings. */
    public static final class Pagination extends Dto {

        public Long total;

        /** The page size applied: the one asked for, 20, or under limit=all the rows that came. */
        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the country listing: the rows and the block that says where they sit. */
    public static final class CountryPage extends Dto {

        public List<Country> countries;

        public Pagination pagination;
    }

    /** One page of a country's states, beside the short parent country. */
    public static final class CountryStatePage extends Dto {

        public CountryReference country;

        public List<CountryState> states;

        public Pagination pagination;
    }
}
