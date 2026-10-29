package com.webcommander.resources.transactions.transactions_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the transaction-number bodies; every body is bare, with no envelope. */
public final class TransactionsDto {

    private TransactionsDto() {
    }

    /** One listing row: a number, how many records carry it, and its read path. */
    public static final class TransactionListEntry extends Dto {

        public String transactionNo;

        public Long totalRecords;

        public String url;
    }

    /** One {@code by_type} row of a number's read. */
    public static final class TransactionTypeCount extends Dto {

        public String type;

        /** {@code created} plus {@code updated}. */
        public Long count;

        public Long created;

        public Long updated;

        /** How many of the counted records are now in the trash. */
        public Long trashed;

        /** Sent on every row, a {@code count} of 0 included; it form-encodes a space as +. */
        public String itemsUrl;
    }

    /** One number's read: its 15 {@code by_type} rows, always in the same order. */
    public static final class Transaction extends Dto {

        public String transactionNo;

        public Long totalRecords;

        public List<TransactionTypeCount> byType;
    }

    /**
     * One record under a number as {@code detail=slim} sends it: {@code type}, {@code id},
     * {@code stamp}, {@code trashed} and the fields of its {@code type}, so a field the type does
     * not send reads null. A full row stays in the map.
     */
    public static final class TransactionItem extends Dto {

        public String type;

        public Long id;

        /** {@code created} or {@code updated}. */
        public String stamp;

        public Boolean trashed;

        /** Not sent for {@code customer}, {@code order} or {@code custom_tax_rule_zone}. */
        public String name;

        /** On {@code product} and {@code product_variation}. */
        public String sku;

        /** On {@code product_review}. */
        public Double rating;

        /** On {@code customer}, with {@code firstName} and {@code lastName}. */
        public String email;

        public String firstName;

        public String lastName;

        /**
         * On {@code order}, with {@code grandTotal} and {@code status}; its {@code url} ends
         * with this number, not with {@code id}.
         */
        public Long orderNo;

        public Double grandTotal;

        public String status;

        /** On {@code currency}, beside {@code symbol}, and on {@code custom_tax_profile}. */
        public String code;

        public String symbol;

        /**
         * On {@code custom_tax_rule_zone}, with {@code stateName} (null when the zone has
         * none), {@code postcode}, {@code rate} and {@code priority}.
         */
        public String countryCode;

        public String stateName;

        public String postcode;

        public Double rate;

        public Long priority;

        /** On {@code shipping_profile}. */
        public String description;

        /** On {@code shipping_rate}. */
        public String policyType;

        /** The record's read path; not sent for a custom tax rule or one of its zones. */
        public String url;
    }

    /** The five-key {@code pagination} block of both listings; there is no next-page link. */
    public static final class Pagination extends Dto {

        public Long total;

        /** The page size applied. */
        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the numbers; a {@code transaction_no} filter answers pagination too. */
    public static final class TransactionPage extends Dto {

        public List<TransactionListEntry> transactions;

        public Pagination pagination;
    }

    /**
     * One page of the records carrying a number, beside the number; each item names its own
     * {@code type}. A number or a type with no records answers {@code items} empty and a
     * {@code total} of 0.
     */
    public static final class TransactionItemPage extends Dto {

        public String transactionNo;

        public List<TransactionItem> items;

        public Pagination pagination;
    }
}
