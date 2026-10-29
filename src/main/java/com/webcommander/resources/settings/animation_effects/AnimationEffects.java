package com.webcommander.resources.settings.animation_effects;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.animation_effects.animation_effects_dto
        .AnimationEffectsDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Animation effects settings — {@code /api/{api_version}/admin/settings/animation_effects}. */
public final class AnimationEffects implements BoundResource {

    private final WebCommanderClient client;

    public AnimationEffects(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/animation_effects}. */
    public ApiResponse getAnimationEffects() {
        return getAnimationEffects(null, null);
    }

    public ApiResponse getAnimationEffects(Map<String, Object> queryParams) {
        return getAnimationEffects(queryParams, null);
    }

    public ApiResponse getAnimationEffects(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(AnimationEffectsUrls.ANIMATION_EFFECTS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                AnimationEffectsDto.AnimationEffects.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/animation_effects}, in its facade wrapper. */
    public ApiResponse patchAnimationEffects(Object payload) {
        return patchAnimationEffects(payload, null);
    }

    public ApiResponse patchAnimationEffects(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(AnimationEffectsUrls.ANIMATION_EFFECTS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                AnimationEffectsDto.AnimationEffects.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/animation_effects}. */
    public ApiResponse headAnimationEffects() {
        return headAnimationEffects(null);
    }

    public ApiResponse headAnimationEffects(RequestOptions options) {
        return client.sendApiRequest("HEAD", AnimationEffectsUrls.ANIMATION_EFFECTS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET /admin/settings/animation_effects/{section}}. */
    public ApiResponse getAnimationEffectsSection(String section) {
        return getAnimationEffectsSection(section, null, null);
    }

    public ApiResponse getAnimationEffectsSection(String section,
            Map<String, Object> queryParams) {
        return getAnimationEffectsSection(section, queryParams, null);
    }

    public ApiResponse getAnimationEffectsSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(AnimationEffectsUrls.ANIMATION_EFFECTS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                AnimationEffectsDto.AnimationEffects.class, body)).build());
    }

    /** Write one section — {@code PATCH .../animation_effects/{section}}, wrapped in its key. */
    public ApiResponse patchAnimationEffectsSection(String section, Object payload) {
        return patchAnimationEffectsSection(section, payload, null);
    }

    public ApiResponse patchAnimationEffectsSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(AnimationEffectsUrls.ANIMATION_EFFECTS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                AnimationEffectsDto.AnimationEffects.class, body)).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
