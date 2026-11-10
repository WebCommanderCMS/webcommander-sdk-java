package com.webcommander.resources.dashboard_sections.dashboard_sections_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the dashboard section bodies. */
public final class DashboardSectionsDto {

    private DashboardSectionsDto() {
    }

    /**
     * The period a section covers and the one before it, as dates; {@code time_zone} is the
     * store's and {@code currency} the code money figures are in.
     */
    public static final class SectionPeriod extends Dto {

        public String duration;

        public String startDate;

        public String endDate;

        public String previousStartDate;

        public String previousEndDate;

        public String timeZone;

        public String currency;
    }

    /** A product, page or post by id and name. */
    public static final class SectionEntity extends Dto {

        public Long id;

        public String name;
    }

    /** What needs attention now; {@code pending_fulfillment} is the orders waiting to ship. */
    public static final class SectionOrderUpdates extends Dto {

        public Long lowStockItems;

        public Long pendingFulfillment;

        public Long paymentsToCapture;
    }

    /** A product and the units sold, highest first. */
    public static final class SectionBestSeller extends Dto {

        public SectionEntity product;

        public Long unitsSold;
    }

    /** One content item to review; {@code count} is an empty string on the rows measured. */
    public static final class SectionContentUpdate extends Dto {

        public String key;

        public String label;

        public String count;
    }

    /** A page and its views. */
    public static final class SectionTopPage extends Dto {

        public SectionEntity page;

        public Long views;
    }

    /**
     * The bookings that need attention: how many, and the latest of them. {@code latest} is empty
     * on the store measured, so the row's shape is not known.
     */
    public static final class SectionNeedsAttention extends Dto {

        public Long count;

        public List<Object> latest;
    }

    /** A blog draft waiting to be published. */
    public static final class SectionDraft extends Dto {

        public SectionEntity post;

        public String updatedAt;
    }

    /** The blog drafts to publish: how many, and the latest. */
    public static final class SectionDrafts extends Dto {

        public Long count;

        public List<SectionDraft> latest;
    }

    /** A blog post and its views. */
    public static final class SectionViewedPost extends Dto {

        public SectionEntity post;

        public Long views;
    }

    /**
     * One tab of the Dashboard. {@code section} echoes the path value, and only the fields of
     * that tab are present. {@code ecommerce}: {@code order_updates},
     * {@code best_selling_products}. {@code content}: {@code content_updates},
     * {@code top_pages}. {@code bookings}: {@code needs_attention}, {@code upcoming}.
     * {@code courses}: {@code new_enrolments}, {@code unpaid_orders}, {@code popular_courses}.
     * {@code blog}: {@code comments_to_moderate}, {@code drafts_to_publish},
     * {@code most_viewed_posts}. {@code upcoming} and {@code popular_courses} are empty on the
     * store measured, so the row's shape is not known.
     */
    public static final class DashboardSectionReport extends Dto {

        public String section;

        public SectionPeriod period;

        public SectionOrderUpdates orderUpdates;

        public List<SectionBestSeller> bestSellingProducts;

        public List<SectionContentUpdate> contentUpdates;

        public List<SectionTopPage> topPages;

        public SectionNeedsAttention needsAttention;

        public List<Object> upcoming;

        public Long newEnrolments;

        public Long unpaidOrders;

        public List<Object> popularCourses;

        public Long commentsToModerate;

        public SectionDrafts draftsToPublish;

        public List<SectionViewedPost> mostViewedPosts;
    }
}
