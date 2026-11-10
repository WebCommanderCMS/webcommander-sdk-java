package com.webcommander.resources.dashboard_banners.dashboard_banners_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the dashboard banner bodies. */
public final class DashboardBannersDto {

    private DashboardBannersDto() {
    }

    /** One step of an {@code open_modal} banner's walkthrough. */
    public static final class BannerStep extends Dto {

        public String title;

        public String description;
    }

    /**
     * What the banner's learn-more button does. {@code action} is {@code open_link}, with an
     * {@code https} {@code url}, or {@code open_modal}, with a {@code title}, a {@code subtitle}
     * and at least one of {@code steps}. A patch replaces the whole block, so an
     * {@code open_modal} block sent over an {@code open_link} one drops the {@code url}.
     */
    public static final class BannerLearnMore extends Dto {

        public String action;

        public String label;

        public String url;

        public String title;

        public String subtitle;

        public List<BannerStep> steps;
    }

    /** The optional second button; both fields are needed. */
    public static final class BannerTryIt extends Dto {

        public String label;

        public String url;
    }

    /**
     * One banner. {@code key} is the {@code banner_key} of the other calls. {@code position}
     * counts from 1 over every banner, hidden ones included, and {@code active} false hides it
     * from the Dashboard feed only.
     */
    public static final class DashboardBanner extends Dto {

        public String key;

        public Long position;

        public Boolean active;

        public String icon;

        public String title;

        public String description;

        public List<String> tags;

        public BannerLearnMore learnMore;

        public BannerTryIt tryIt;

        public String createdAt;

        public String updatedAt;
    }

    /** The management list: every banner, active or not, in position order. */
    public static final class DashboardBannerListing extends Dto {

        public List<DashboardBanner> banners;
    }

    /**
     * The write body, bare. A create needs {@code key} ({@code a-z}, {@code 0-9} and {@code _},
     * starting with a letter, up to 64 characters, unique), {@code title} (1 to 80 characters),
     * {@code description} (1 to 200) and {@code learn_more}; {@code tags} holds at most 3,
     * {@code active} defaults to {@code true} and {@code position} to the end of the list. A
     * patch sends only what changes and cannot send {@code key}. {@code icon} is stored as sent.
     * {@code position} runs from 1 to one past the banner count, and the banners after it shift
     * down.
     */
    public static final class DashboardBannerInput extends Dto {

        public String key;

        public String icon;

        public String title;

        public String description;

        public List<String> tags;

        public BannerLearnMore learnMore;

        public BannerTryIt tryIt;

        public Long position;

        public Boolean active;
    }

    /** The support card of the Dashboard. */
    public static final class BannerContact extends Dto {

        public String email;

        public String phone;

        public String liveChat;

        public String helpCenterUrl;
    }

    /**
     * The Dashboard's feed: only the active banners, in position order, the support card, whether
     * the store is an online store and the label of the membership.
     */
    public static final class DashboardContentInfo extends Dto {

        public List<DashboardBanner> banners;

        public BannerContact contact;

        public Boolean onlineStore;

        public String memberLabel;
    }
}
