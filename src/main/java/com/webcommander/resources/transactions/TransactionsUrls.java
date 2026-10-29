package com.webcommander.resources.transactions;

/** Path templates for the transaction-number endpoints. */
public final class TransactionsUrls {

    private TransactionsUrls() {}

    public static final String TRANSACTIONS = "/api/{api_version}/admin/transactions";

    public static final String TRANSACTION =
            "/api/{api_version}/admin/transactions/{transaction_no}";

    public static final String TRANSACTION_ITEMS =
            "/api/{api_version}/admin/transactions/{transaction_no}/items";
}
