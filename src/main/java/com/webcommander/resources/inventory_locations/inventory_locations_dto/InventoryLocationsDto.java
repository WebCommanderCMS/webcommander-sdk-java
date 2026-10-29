package com.webcommander.resources.inventory_locations.inventory_locations_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the inventory location bodies. */
public final class InventoryLocationsDto {

    private InventoryLocationsDto() {
    }

    /** One inventory location, in every form the API sends it. */
    public static final class InventoryLocation extends Dto {

        /** Assigned by the store, an integer; an {@code id} sent on a create is ignored. */
        public Long id;

        /** Required on the create and the update. */
        public String name;

        /** Optional text, or {@code null}. */
        public String address;

        public Boolean active;

        /** Held by at most one row at a time, and moved by the {@code /default} sub-path. */
        public Boolean Default;

        public String createdAt;

        public String updatedAt;

        @Override
        protected String envelopeKey() {
            return "inventory_location";
        }
    }

    /** The writable form of {@link InventoryLocation}; an update resets what it omits, a patch not. */
    public static final class InventoryLocationInput extends Dto {

        public String name;

        public String address;

        public Boolean active;

        public Boolean Default;
    }

    /** The body the status sub-path takes; {@link #active} is required and the only field. */
    public static final class InventoryLocationStatusInput extends Dto {

        public Boolean active;
    }

    /** The five-key {@code pagination} block of the listing and of the stock sub-path. */
    public static final class Pagination extends Dto {

        public Long total;

        /** The page size applied: the {@code limit} asked for, or 20. */
        public Long limit;

        public Long offset;

        public Boolean hasPrevious;

        public Boolean hasNext;
    }

    /** One page of the listing: the rows and the block that says where they sit. */
    public static final class InventoryLocationPage extends Dto {

        public List<InventoryLocation> inventoryLocations;

        public Pagination pagination;
    }

    /** The product one stock item belongs to. */
    public static final class InventoryLocationStockProduct extends Dto {

        public Long id;

        public String name;

        public String sku;
    }

    /** One product's holding at one location, a row of the stock sub-path. */
    public static final class InventoryLocationStockItem extends Dto {

        public InventoryLocationStockProduct product;

        public Object variation;

        public Long availableStock;

        public Long lowStockLevel;

        /** The product's own default-location flag, not the location's {@code default}. */
        public Boolean defaultForProduct;
    }

    /** The stock sub-path's body: the location, its items and their pagination. */
    public static final class InventoryLocationStock extends Dto {

        public InventoryLocation inventoryLocation;

        public List<InventoryLocationStockItem> items;

        public Pagination pagination;
    }
}
