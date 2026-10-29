package com.webcommander.resources.settings.animation_effects;

/** Path templates for the animation effects endpoints. */
public final class AnimationEffectsUrls {

    private AnimationEffectsUrls() {}

    public static final String ANIMATION_EFFECTS =
            "/api/{api_version}/admin/settings/animation_effects";

    public static final String ANIMATION_EFFECTS_SECTION =
            "/api/{api_version}/admin/settings/animation_effects/{section}";
}
