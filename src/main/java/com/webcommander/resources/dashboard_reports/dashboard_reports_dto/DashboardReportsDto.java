package com.webcommander.resources.dashboard_reports.dashboard_reports_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the dashboard report bodies. */
public final class DashboardReportsDto {

    private DashboardReportsDto() {
    }

    /**
     * The period a report covers and the one before it, as dates; {@code time_zone} is the
     * store's and {@code currency} the code money figures are in. {@code duration} is
     * {@code custom} for a date range.
     */
    public static final class ReportPeriod extends Dto {

        public String duration;

        public String startDate;

        public String endDate;

        public String previousStartDate;

        public String previousEndDate;

        public String timeZone;

        public String currency;
    }

    /**
     * One figure with the previous period's. {@code format} is {@code money}, {@code count} or
     * {@code percent} and {@code direction} is {@code up}, {@code down} or {@code flat};
     * {@code change_percent} is {@code null} when the previous value was zero. In {@code trends}
     * there is no {@code group}.
     */
    public static final class ReportKpi extends Dto {

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

    /** Order counts of the period. */
    public static final class ReportOrders extends Dto {

        public Long count;

        public Long pending;

        public Long cancelled;
    }

    /** Sales figures of the period, rounded to 2 decimals. */
    public static final class ReportSales extends Dto {

        public Double subtotal;

        public Double total;

        public Long itemsSold;

        public Double averageOrderValue;
    }

    /** Customers registered in the period. */
    public static final class ReportCustomers extends Dto {

        public Long New;
    }

    /** Products added in the period, and those out of stock now. */
    public static final class ReportProducts extends Dto {

        public Long New;

        public Long outOfStock;
    }

    /** Page views of the period. */
    public static final class ReportPageViews extends Dto {

        public Long total;

        public Long unique;
    }

    /** A page, product or post by id and name. */
    public static final class ReportEntity extends Dto {

        public Long id;

        public String name;
    }

    /**
     * One row of the most viewed list: a {@code page} or, with {@code entity_name}
     * {@code product}, a {@code product}.
     */
    public static final class ReportViewItem extends Dto {

        public ReportEntity page;

        public ReportEntity product;

        public Long views;
    }

    /**
     * The most viewed pages, or products when the query's {@code entity_name} is
     * {@code product}; {@code entity} says which.
     */
    public static final class ReportViews extends Dto {

        public String entity;

        public List<ReportViewItem> items;
    }

    /**
     * The quick report. {@code kpis} holds the 27 metrics, or only the ones the query's
     * {@code kpis} names, in that order. Inside a summary only the grouped totals and
     * {@code view_report} are present.
     */
    public static final class QuickReport extends Dto {

        public ReportPeriod period;

        public List<ReportKpi> kpis;

        public ReportOrders orders;

        public ReportSales sales;

        public ReportCustomers customers;

        public ReportProducts products;

        public ReportPageViews pageViews;

        public ReportViews viewReport;

        public List<ReportKpi> trends;
    }

    /**
     * One row of the sales report: a day for a period up to a month, else a month. Without
     * {@code breakdown} only the date fields, {@code label}, {@code month_name}, {@code sales}
     * and {@code total_sales} are present; {@code day} is present on day rows only.
     */
    public static final class SalesRow extends Dto {

        public String date;

        public Long year;

        public Long month;

        public Long day;

        public String label;

        public String monthName;

        public Double sales;

        public Double totalSales;

        public Long orders;

        public Double ordersValue;

        public Double grossSales;

        public Double shipping;

        public Double handling;

        public Double tax;

        public Double discount;

        public Double refunds;

        public Double averageOrderValue;

        public ReportEntity topProduct;
    }

    /** The rows added up; {@code sales} always, the rest with {@code breakdown}. */
    public static final class SalesTotals extends Dto {

        public Double sales;

        public Long orders;

        public Double ordersValue;

        public Double grossSales;

        public Double shipping;

        public Double handling;

        public Double tax;

        public Double discount;

        public Double refunds;

        public Double averageOrderValue;
    }

    /**
     * The sales report. {@code group_by} is {@code day} for a period up to a month and
     * {@code month} for a longer one; {@code sort} ({@code date} or {@code sales}) and
     * {@code order} ({@code asc} or {@code desc}) echo what was applied. A period with no sales
     * has no rows.
     */
    public static final class SalesReport extends Dto {

        public ReportPeriod period;

        public String groupBy;

        public String sort;

        public String order;

        public List<SalesRow> rows;

        public SalesTotals totals;
    }

    /** Orders placed in one month. */
    public static final class OrderCount extends Dto {

        public String date;

        public Long year;

        public Long month;

        public Long count;
    }

    /** A product and its views. */
    public static final class MostVisitedProduct extends Dto {

        public ReportEntity product;

        public Long views;
    }

    /** A product and the units sold, highest first. */
    public static final class BestSellingProduct extends Dto {

        public ReportEntity product;

        public Long unitsSold;
    }

    /** What needs attention now; {@code pending_fulfillment} is the orders waiting to ship. */
    public static final class OrderUpdates extends Dto {

        public Long lowStockItems;

        public Long pendingFulfillment;

        public Long paymentsToCapture;
    }

    /**
     * The summary: every section of the Dashboard in one answer. {@code quick_report} has no
     * {@code kpis}.
     */
    public static final class SummaryReport extends Dto {

        public ReportPeriod period;

        public QuickReport quickReport;

        public List<OrderCount> orderByMonth;

        public List<OrderCount> ordersOverTime;

        public List<SalesRow> salesReport;

        public List<MostVisitedProduct> mostVisitedProducts;

        public List<BestSellingProduct> bestSellingProducts;

        public OrderUpdates orderUpdates;
    }
}
