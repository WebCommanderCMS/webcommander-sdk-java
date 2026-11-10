package com.webcommander.resources.dashboards.dashboards_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the dashboard layout bodies. */
public final class DashboardsDto {

    private DashboardsDto() {
    }

    /**
     * One part of the {@code seo} section. Read back, the title and {@code can_hide} come with it;
     * a write needs only {@code key} and {@code visible}. The {@code kpis} part cannot be hidden.
     */
    public static final class DashboardPart extends Dto {

        public String key;

        public String title;

        public Boolean visible;

        public Boolean canHide;
    }

    /**
     * One tab of the {@code section_tabs} section: {@code ecommerce}, {@code content},
     * {@code bookings}, {@code courses} or {@code blog}. A write needs {@code key} and sets
     * {@code visible} or {@code position}, counted from 1 among the tabs.
     */
    public static final class DashboardTab extends Dto {

        public String key;

        public String title;

        public String description;

        public Boolean visible;

        public Boolean canHide;

        public Long position;
    }

    /**
     * A section of a layout. The standard ones are {@code announcement}, {@code performance},
     * {@code activity} and {@code contact}; they only show or hide, and {@code contact} cannot
     * hide. The customisable ones are {@code setup_guide}, {@code section_tabs} (with
     * {@code tabs}) and {@code seo} (with {@code parts}); they also take a {@code position}. Read
     * back, the title, description and {@code can_hide} come with it; a write needs only
     * {@code key} and what changes.
     */
    public static final class DashboardSection extends Dto {

        public String key;

        public String title;

        public String description;

        public Boolean visible;

        public Boolean canHide;

        public Long position;

        public List<DashboardTab> tabs;

        public List<DashboardPart> parts;
    }

    /** One KPI tile of a layout, in tile order. {@code key} is a {@code kpi_options} metric key. */
    public static final class DashboardKpi extends Dto {

        public Long position;

        public String key;

        public String label;

        public String group;
    }

    /**
     * One layout. {@code id} is the {@code dashboard_id} of the other calls, a UUID. Exactly one
     * layout is the default.
     */
    public static final class Dashboard extends Dto {

        public String id;

        public String name;

        public Boolean Default;

        public List<DashboardSection> standardSections;

        public List<DashboardSection> customisableSections;

        public List<DashboardKpi> kpis;

        public String createdAt;

        public String updatedAt;
    }

    /** A layout as the listing shows it, without its sections and tiles. */
    public static final class DashboardSummary extends Dto {

        public String id;

        public String name;

        public Boolean Default;

        public String createdAt;

        public String updatedAt;
    }

    /** The listing: every layout of the admin, with no paging. */
    public static final class DashboardListing extends Dto {

        public List<DashboardSummary> dashboards;
    }

    /**
     * The write body, bare. A create needs {@code name}, unique among the admin's layouts
     * ({@code 409}); it starts as a copy of the {@code copy_from} layout, the default one when
     * that is left out. A patch sends only what changes; the sections named are merged into the
     * layout, the rest is kept. {@code kpis} is the full list of 2 to 5 metric keys in tile
     * order. A standard section takes no {@code position} ({@code 400}). {@code default} true
     * makes this layout the store's default layout; false on the default layout is {@code 409},
     * as the default moves only by making another layout the default.
     */
    public static final class DashboardInput extends Dto {

        public String name;

        public Boolean Default;

        public String copyFrom;

        public List<DashboardSection> standardSections;

        public List<DashboardSection> customisableSections;

        public List<String> kpis;
    }
}
