package com.webcommander.resources.transactions;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.transactions.transactions_dto.TransactionsDto.Transaction;
import com.webcommander.resources.transactions.transactions_dto.TransactionsDto.TransactionItemPage;
import com.webcommander.resources.transactions.transactions_dto.TransactionsDto.TransactionPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Transaction numbers — {@code /api/{api_version}/admin/transactions}, read-only. */
public final class Transactions implements BoundResource {

    private final WebCommanderClient client;

    public Transactions(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the numbers in ascending order — {@code GET /admin/transactions}. {@code limit} or
     * {@code max} from 1, default 20, held to 200; {@code transaction_no} matches exactly.
     */
    public ApiResponse listTransactions() {
        return listTransactions(null, null);
    }

    public ApiResponse listTransactions(Map<String, Object> queryParams) {
        return listTransactions(queryParams, null);
    }

    public ApiResponse listTransactions(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(TransactionsUrls.TRANSACTIONS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TransactionPage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/transactions}; no body. */
    public ApiResponse headTransactions() {
        return headTransactions(null);
    }

    public ApiResponse headTransactions(RequestOptions options) {
        return client.sendApiRequest("HEAD", TransactionsUrls.TRANSACTIONS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * Count the records one number tagged, by type —
     * {@code GET /admin/transactions/{transaction_no}}. Matched exactly and case-sensitively; a
     * number holding {@code /} is refused by the server, {@code 400} HTML, as an encoded slash.
     */
    public ApiResponse getTransaction(String transactionNo) {
        return getTransaction(transactionNo, null);
    }

    public ApiResponse getTransaction(String transactionNo, RequestOptions options) {
        return client.sendGetRequest(TransactionsUrls.TRANSACTION,
                RequestOptions.builder(options).pathParams(pathParams(transactionNo))
                        .dtoFactory(body -> Dto.fromMap(Transaction.class, body)).build());
    }

    /** Ask whether one number has records — {@code HEAD /admin/transactions/{transaction_no}}. */
    public ApiResponse headTransaction(String transactionNo) {
        return headTransaction(transactionNo, null);
    }

    public ApiResponse headTransaction(String transactionNo, RequestOptions options) {
        return client.sendApiRequest("HEAD", TransactionsUrls.TRANSACTION,
                RequestOptions.builder(options).pathParams(pathParams(transactionNo))
                        .dtoFactory(null).build());
    }

    /**
     * List the records a number tagged, newest first — {@code GET .../{transaction_no}/items}.
     * {@code type} is optional: left out, every type is listed; sent, it takes one or more of the
     * 15 {@code by_type} names, comma-separated. {@code limit} 1 to 200, {@code detail}
     * {@code slim} or {@code full}. A {@code slim} item is {@code type}, {@code id},
     * {@code stamp}, {@code trashed} and the fields of its type, such as {@code name},
     * {@code sku} and {@code url} for a product. A number or a type with no records answers
     * {@code 200} with {@code items} empty and a {@code total} of 0, not {@code 404}.
     */
    public ApiResponse listTransactionItems(String transactionNo) {
        return listTransactionItems(transactionNo, null, null);
    }

    public ApiResponse listTransactionItems(String transactionNo, Map<String, Object> queryParams) {
        return listTransactionItems(transactionNo, queryParams, null);
    }

    public ApiResponse listTransactionItems(String transactionNo, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(TransactionsUrls.TRANSACTION_ITEMS,
                RequestOptions.builder(options).pathParams(pathParams(transactionNo))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TransactionItemPage.class, body)).build());
    }

    /**
     * Ask whether the records under a number are reachable —
     * {@code HEAD .../{transaction_no}/items}; no body. {@code type} is optional here too.
     */
    public ApiResponse headTransactionItems(String transactionNo) {
        return headTransactionItems(transactionNo, null, null);
    }

    public ApiResponse headTransactionItems(String transactionNo, Map<String, Object> queryParams) {
        return headTransactionItems(transactionNo, queryParams, null);
    }

    public ApiResponse headTransactionItems(String transactionNo, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendApiRequest("HEAD", TransactionsUrls.TRANSACTION_ITEMS,
                RequestOptions.builder(options).pathParams(pathParams(transactionNo))
                        .queryParams(queryParams).dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(String transactionNo) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("transaction_no", transactionNo);
        return params;
    }
}
