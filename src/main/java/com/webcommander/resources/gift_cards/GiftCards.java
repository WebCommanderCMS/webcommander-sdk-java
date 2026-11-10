package com.webcommander.resources.gift_cards;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.gift_cards.gift_cards_dto.GiftCardsDto.GiftCardPage;

import java.util.Map;

/** Gift cards — {@code /api/{api_version}/admin/gift_cards}. */
public final class GiftCards implements BoundResource {

    private final WebCommanderClient client;

    public GiftCards(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List gift cards — {@code GET /admin/gift_cards}, newest first. {@code code} matches a whole
     * code and {@code email} a whole address, in any case; {@code is_active} takes {@code true},
     * {@code false}, {@code 1} or {@code 0}, and {@code limit} caps at 20 with {@code offset} or
     * {@code page} to move on. Any other name is {@code 400} {@code unknown_parameter}.
     */
    public ApiResponse listGiftCards() {
        return listGiftCards(null, null);
    }

    public ApiResponse listGiftCards(Map<String, Object> queryParams) {
        return listGiftCards(queryParams, null);
    }

    public ApiResponse listGiftCards(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(GiftCardsUrls.GIFT_CARDS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(GiftCardPage.class, body)).build());
    }
}
