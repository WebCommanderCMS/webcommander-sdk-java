package com.webcommander.resources.customers.admin_customers_dto;

import com.webcommander.Dto;
import com.webcommander.client.DtoFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Typed views over the admin customer bodies. */
public final class AdminCustomersDto {

    private AdminCustomersDto() {
    }

    /** A lookup value the API sends as an object rather than a bare id. */
    public static final class Reference extends Dto {

        public Long id;

        public String name;

        /** The short code — {@code "AU"}, {@code "VIC"}; {@code null} on a customer group. */
        public String code;
    }

    /** One address; {@code address1}, {@code suburb}, {@code postcode} are dropped spellings. */
    public static final class Address extends Dto {

        /** {@code primary} and {@code billing} share one id; {@code shipping} has its own. */
        public Long id;

        /** {@code PRIMARY}, {@code SHIPPING} or {@code BILLING}; anything else is refused. */
        public String type;

        public String firstName;

        public String lastName;

        public String email;

        public String companyName;

        public String addressLine1;

        public String addressLine2;

        public Reference country;

        /** The state, or {@code {}} when unset; it is validated against {@link #country}. */
        public Reference state;

        public String city;

        public String postCode;

        public String mobile;

        public String phone;

        public String fax;
    }

    /** One entry of {@code custom_properties}. */
    public static final class CustomProperty extends Dto {

        public Long id;

        public String label;

        public String description;
    }

    /** One customer; a listing row carries a subset of the fields a detail fetch sends. */
    public static final class Customer extends Dto {

        public Long customerId;

        /** A second id space over the same record; not what a {@code {customer_id}} slot takes. */
        public Long internalId;

        /**
         * A UUID string the store assigns; a create or update naming another keeps the stored one.
         */
        public String uuid;

        public String firstName;

        public String lastName;

        public String name;

        /** Unique; a trashed customer keeps it reserved, so re-registering answers {@code 409}. */
        public String email;

        /** One of active, inactive, pending, approval_awaiting or awaiting_verification. */
        public String status;

        public String gender;

        public String companyName;

        public Double storeCredit;

        /** An entry the body did not send as an object reads {@code null} in place. */
        public List<Reference> customerGroups;

        public Object shippingProfile;

        /** Written by the create-addresses endpoint rather than by an update. */
        public List<Address> addresses;

        /** When the record was created, as UTC text — {@code 2026-08-31T04:12:55Z}. */
        public String createdAt;

        public String updatedAt;

        public String abn;

        public String abnBranch;

        public Boolean accountHold;

        public Long activeBillingAddress;

        public Long activeShippingAddress;

        public Boolean allowCreditLimit;

        public String backgroundImage;

        public String baseUrl;

        public Long countReferralCodeUsed;

        /** The credit ceiling, which only bites while {@link #allowCreditLimit} is set. */
        public Double creditLimit;

        public List<CustomProperty> customProperties;

        public Boolean customerPasswordExpired;

        public String defaultTaxCode;

        public Boolean emailVerified;

        public Boolean hidePrice;

        public String howDoYouKnow;

        /** {@code true} once deleted: readable by id but gone from the listing. */
        public Boolean isInTrash;

        public List<Object> newsletters;

        public String note;

        public Object paymentTerm;

        public Object profilePicture;

        public Object referralCode;

        public Boolean smsSubscribed;

        public String source;

        public String timezone;

        /** Sent as {@link CustomerInput#transactionNo} or the {@code X-Transaction-No} header. */
        public String transactionNumber;

        public Object usedReferralCode;

        public Long version;

        @Override
        protected String envelopeKey() {
            return "customer";
        }
    }

    /** The writable form of {@link Customer}, the type every customer write sends. */
    public static final class CustomerInput extends Dto {

        /** Required on a create. */
        public String firstName;

        public String lastName;

        /** Required on a create, and unique: a duplicate answers {@code 409}. */
        public String email;

        public Boolean emailVerified;

        /** The password to set; alone, it is the whole body of a reset. */
        public String password;

        public String oldPassword;

        /** Send {@code "active"}, or the customer can never sign in. */
        public String status;

        public Boolean company;

        public String companyName;

        public String addressLine1;

        public String addressLine2;

        public String city;

        /** The state, as either its {@link Reference#id} or its name. */
        public Object state;

        /** The country, as either its {@link Reference#id} or its name. */
        public Object country;

        public String postCode;

        /** Full addresses — what a create-addresses call sends, rather than the inline fields. */
        public List<Address> addresses;

        /** Replaces whatever the customer had, rather than adding to it. */
        public List<CustomProperty> customProperties;

        /** Written beside the {@code customer} object rather than inside it. */
        public String transactionNo;

        @Override
        protected String envelopeKey() {
            return "customer";
        }

        @Override
        protected Set<String> topLevel() {
            return Set.of("transaction_no");
        }
    }

    /** One movement of a customer's store credit. */
    public static final class StoreCreditAdjustment extends Dto {

        public Long id;

        public Long customerId;

        public Long internalId;

        /** {@code "add"} or {@code "deduct"}: the direction, since {@link #amount} is unsigned. */
        public String action;

        public Double amount;

        public String note;

        public String createdAt;

        public Object createdBy;

        /** The balance before the movement — on a write's answer only, {@code null} in history. */
        public Double balanceBefore;

        public Double balanceAfter;

        /** {@link #balanceAfter} again, arriving beside the record and kept by unwrapping. */
        public Double storeCredit;

        @Override
        protected String envelopeKey() {
            return "store_credit_adjustment";
        }
    }

    /** The writable form of {@link StoreCreditAdjustment}. */
    public static final class StoreCreditAdjustmentInput extends Dto {

        /** {@code "add"} or {@code "deduct"}; required. */
        public String action;

        /** How much to move, positive; required. */
        public Double amount;

        public String note;
    }

    /** The {@code pagination} block of a listing. */
    public static final class Pagination extends Dto {

        public Long total;

        /** Rows per page as the store applied it, not as asked; customer listings cap it at 20. */
        public Long limit;

        public Long offset;

        public Long count;

        public Long currentPage;

        public Long totalPages;

        public Boolean hasNext;

        public Boolean hasPrevious;

        public String previousPage;

        public String nextPage;
    }

    /** One page of a listing: the records, and the {@code pagination} block beside them. */
    public static final class Page<T extends Dto> extends Dto {

        /** The records, or {@code null} when the body held no single list to read them from. */
        public List<T> items;

        public Pagination pagination;

        /** A factory reading a listing envelope into a page of {@code itemType}. */
        public static <T extends Dto> DtoFactory of(Class<T> itemType) {
            return body -> read(itemType, body);
        }

        private static <T extends Dto> Page<T> read(Class<T> itemType, Object body) {
            Map<?, ?> mapping = (Map<?, ?>) body;
            Page<T> page = new Page<>();
            List<?> records = onlyList(mapping);
            if (records != null) {
                List<T> items = new ArrayList<>(records.size());
                for (Object record : records) {
                    items.add(record instanceof Map ? Dto.fromMap(itemType, record) : null);
                }
                page.items = Collections.unmodifiableList(items);
            }

            Object block = mapping.get("pagination");
            page.pagination = block instanceof Map ? Dto.fromMap(Pagination.class, block) : null;
            return page;
        }

        /** {@code null} when there is no list-valued entry, or more than one to choose between. */
        private static List<?> onlyList(Map<?, ?> body) {
            List<?> found = null;
            for (Object value : body.values()) {
                if (value instanceof List<?> records) {
                    if (found != null) {
                        return null;
                    }
                    found = records;
                }
            }
            return found;
        }
    }
}
