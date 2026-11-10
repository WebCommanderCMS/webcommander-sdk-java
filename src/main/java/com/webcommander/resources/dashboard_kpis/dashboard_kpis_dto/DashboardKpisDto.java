package com.webcommander.resources.dashboard_kpis.dashboard_kpis_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the dashboard KPI bodies. */
public final class DashboardKpisDto {

    private DashboardKpisDto() {
    }

    /** The default layout the KPI tiles belong to; {@code id} is its {@code dashboard_id}. */
    public static final class DashboardRef extends Dto {

        public String id;

        public String name;
    }

    /**
     * The period a figure covers and the one before it, as dates; {@code time_zone} is the
     * store's and {@code currency} the code money figures are in.
     */
    public static final class KpiPeriod extends Dto {

        public String duration;

        public String startDate;

        public String endDate;

        public String previousStartDate;

        public String previousEndDate;

        public String timeZone;

        public String currency;
    }

    /**
     * One metric a tile can show. {@code key} is what a selection takes; {@code code} is the
     * report's own name for it and differs for some, such as {@code aov} and
     * {@code average_order_value}.
     */
    public static final class KpiMetric extends Dto {

        public String key;

        public String code;

        public String label;

        public String format;

        public Boolean inUse;
    }

    /**
     * A group of metrics: {@code ecommerce}, {@code content}, {@code blog}, {@code bookings} or
     * {@code courses}.
     */
    public static final class KpiGroup extends Dto {

        public String key;

        public String label;

        public List<KpiMetric> metrics;
    }

    /**
     * Every metric a tile can show. {@code min} and {@code max} bound the number of tiles, and
     * {@code selected} lists the keys in use, in tile order.
     */
    public static final class KpiOptions extends Dto {

        public DashboardRef dashboard;

        public Long min;

        public Long max;

        public List<String> selected;

        public List<KpiGroup> groups;
    }

    /**
     * One selected tile with its figure. {@code format} is {@code money}, {@code count} or
     * {@code percent} and {@code direction} is {@code up}, {@code down} or {@code flat};
     * {@code change_percent} is {@code null} when the previous period was zero. Every count is a
     * float on the wire.
     */
    public static final class KpiTile extends Dto {

        public Long position;

        public String key;

        public String code;

        public String label;

        public String group;

        public String format;

        public Double value;

        public Double previousValue;

        public Double changePercent;

        public String direction;
    }

    /** The tiles in use with their figures for the period; the read and the save answer alike. */
    public static final class KpiSelection extends Dto {

        public DashboardRef dashboard;

        public Boolean onlineStore;

        public KpiPeriod period;

        public List<KpiTile> selected;
    }

    /**
     * The save body, bare: {@code metrics} is the full list of 2 to 5 distinct metric keys, in
     * tile order. It replaces the default layout's tiles.
     */
    public static final class KpiSelectionInput extends Dto {

        public List<String> metrics;
    }
}
