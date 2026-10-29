package com.webcommander.resources.settings.animation_effects.animation_effects_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the animation effects bodies. */
public final class AnimationEffectsDto {

    private AnimationEffectsDto() {
    }

    /** The uploaded loader that goes with {@code enable_custom_preloader}; read-only. */
    public static final class CustomLoader extends Dto {

        public Boolean enabled;

        public String fileName;

        public String fileUrl;
    }

    /** One entry of {@code preloader_custom_page} as a read sends it; a write takes page ids. */
    public static final class PreloaderPage extends Dto {

        public Long id;

        public String name;
    }

    /** The {@code preloader_design} section: the loader shown while a page loads. */
    public static final class PreloaderDesign extends Dto {

        public Boolean enablePreloader;

        public Boolean enableCustomPreloader;

        public String preloaderPreset;

        public Long preloaderSize;

        public Double preloaderBackgroundOpacity;

        public String backgroundPreloader;

        public String preloaderHideCondition;

        public String preloaderShowTimeCondition;

        public Double preloaderHideAfterTime;

        public String preloaderPage;

        public List<PreloaderPage> preloaderCustomPage;

        public CustomLoader customLoader;

        @Override
        protected String envelopeKey() {
            return "preloader_design";
        }
    }

    /** The {@code widget_transition} section: whether widgets fade in, and over how long. */
    public static final class WidgetTransition extends Dto {

        public Boolean widgetSmoothRender;

        public Long widgetSmoothRenderDuration;

        @Override
        protected String envelopeKey() {
            return "widget_transition";
        }
    }

    /** The {@code page_transition} section: the animation between pages and when it plays. */
    public static final class PageTransition extends Dto {

        public String pageTransitionAnimationType;

        public Double transitionAnimationDuration;

        public Boolean animationBeforeEnteringNewPage;

        public Boolean animationAfterEnteringNewPage;

        @Override
        protected String envelopeKey() {
            return "page_transition";
        }
    }

    /** The {@code image_lazy_effect} section: which listings and pages lazy-load their images. */
    public static final class ImageLazyEffect extends Dto {

        public String preloaderImageLazyEffectPreset;

        public Boolean productDetailsPageImageLazyEffect;

        public Boolean flyingCartImageLazyEffect;

        public Boolean productListImageLazyEffect;

        public Boolean propertyListImageLazyEffect;

        public Boolean propertyDetailsPageImageLazyEffect;

        public Boolean blogListImageLazyEffect;

        public Boolean blogDetailsPageImageLazyEffect;

        public Long lazyLoaderSize;

        public String backgroundLazyLoader;

        public String contentImageLazyLoadBackground;

        @Override
        protected String envelopeKey() {
            return "image_lazy_effect";
        }
    }

    /** The {@code page_skeleton} section, the area's one-field section. */
    public static final class PageSkeleton extends Dto {

        public Boolean checkoutPageSkeleton;

        @Override
        protected String envelopeKey() {
            return "page_skeleton";
        }
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per section the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta preloaderDesign;

        public SectionMeta widgetTransition;

        public SectionMeta pageTransition;

        public SectionMeta imageLazyEffect;

        public SectionMeta pageSkeleton;
    }

    /** The animation effects settings, as the facade or one section sends them. */
    public static final class AnimationEffects extends Dto {

        public PreloaderDesign preloaderDesign;

        public WidgetTransition widgetTransition;

        public PageTransition pageTransition;

        public ImageLazyEffect imageLazyEffect;

        public PageSkeleton pageSkeleton;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "animation_effects";
        }
    }
}
