package com.webcommander.resources.currencies;

/** Path templates for the currency endpoints. */
public final class CurrenciesUrls {

    private CurrenciesUrls() {}

    public static final String CURRENCIES = "/api/{api_version}/admin/currencies";

    public static final String CURRENCIES_COUNT = "/api/{api_version}/admin/currencies/count";

    public static final String CURRENCY = "/api/{api_version}/admin/currencies/{currency_id}";

    public static final String CURRENCY_BASE =
            "/api/{api_version}/admin/currencies/{currency_id}/base";

    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
