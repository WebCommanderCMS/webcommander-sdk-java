package com.webcommander.resources.cities.cities_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the city listing. */
public final class CitiesDto {

    private CitiesDto() {
    }

    /** The state a city row carries; {@code id} is the one a country's states listing answers. */
    public static final class CityState extends Dto {

        public Long id;

        public String code;

        public String name;

        public Boolean active;

        /** Follows the {@code administration} section's default_state. */
        public Boolean Default;
    }

    /** The country a city row carries; {@code id} is the one the countries listing answers. */
    public static final class CityCountry extends Dto {

        public Long id;

        public String code;

        public String name;
    }

    /** One listing row. */
    public static final class City extends Dto {

        public Long id;

        public String name;

        public String postCode;

        public CityState state;

        public CityCountry country;
    }

    /** The five-key {@code pagination} block: the matched total, then the window applied. */
    public static final class Pagination extends Dto {

        public Long total;

        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the city listing: the rows and the block that says where they sit. */
    public static final class CityPage extends Dto {

        public List<City> cities;

        public Pagination pagination;
    }
}
