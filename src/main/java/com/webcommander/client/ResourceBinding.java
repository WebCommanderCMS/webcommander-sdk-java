package com.webcommander.client;

import com.webcommander.errors.SdkAttributeError;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.resources.aliases.Aliases;
import com.webcommander.resources.blog_categories.BlogCategories;
import com.webcommander.resources.blogs.Blogs;
import com.webcommander.resources.carts.Carts;
import com.webcommander.resources.categories.Categories;
import com.webcommander.resources.cities.Cities;
import com.webcommander.resources.common_settings.CommonSettings;
import com.webcommander.resources.countries.Countries;
import com.webcommander.resources.currencies.Currencies;
import com.webcommander.resources.customer_groups.CustomerGroups;
import com.webcommander.resources.customers.AdminCustomers;
import com.webcommander.resources.discount_profiles.DiscountProfiles;
import com.webcommander.resources.email.Email;
import com.webcommander.resources.inventory_locations.InventoryLocations;
import com.webcommander.resources.order_custom_fields.OrderCustomFields;
import com.webcommander.resources.order_flags.OrderFlags;
import com.webcommander.resources.orders.Orders;
import com.webcommander.resources.pages.Pages;
import com.webcommander.resources.payment_gateways.PaymentGateways;
import com.webcommander.resources.products.Products;
import com.webcommander.resources.settings.animation_effects.AnimationEffects;
import com.webcommander.resources.settings.customer_portal.CustomerPortal;
import com.webcommander.resources.settings.ecommerce_settings.EcommerceSettings;
import com.webcommander.resources.settings.page_settings.CategoryPage;
import com.webcommander.resources.settings.page_settings.CheckoutPage;
import com.webcommander.resources.settings.page_settings.ProductPage;
import com.webcommander.resources.settings.page_settings.SearchResultPage;
import com.webcommander.resources.settings.page_settings.ShoppingCart;
import com.webcommander.resources.settings.price_display_settings.PriceDisplaySettings;
import com.webcommander.resources.settings.product_listing.ProductListing;
import com.webcommander.resources.settings.search_settings.SearchSettings;
import com.webcommander.resources.settings.seo_configuration.SeoConfiguration;
import com.webcommander.resources.settings.shipping_settings.ShippingSettings;
import com.webcommander.resources.settings.site_settings.SiteSettings;
import com.webcommander.resources.shipping_classes.ShippingClasses;
import com.webcommander.resources.shipping_methods.ShippingMethods;
import com.webcommander.resources.tax_codes.TaxCodes;
import com.webcommander.resources.tax_country_taxes.TaxCountryTaxes;
import com.webcommander.resources.tax_custom_profiles.TaxCustomProfiles;
import com.webcommander.resources.tax_profiles.TaxProfiles;
import com.webcommander.resources.tax_rules.TaxRules;
import com.webcommander.resources.tax_settings.TaxSettings;
import com.webcommander.resources.tax_zones.TaxZones;
import com.webcommander.resources.transactions.Transactions;
import com.webcommander.resources.trash.Trash;
import com.webcommander.resources.variations.Variations;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/** The resource registry: which names exist and what each binds to a client. */
public final class ResourceBinding {

    private ResourceBinding() {}

    private static final Map<String, Function<WebCommanderClient, ? extends BoundResource>>
            RESOURCE_FACTORIES = new ConcurrentHashMap<>();

    static {
        RESOURCE_FACTORIES.put("adminCustomers", AdminCustomers::new);
        RESOURCE_FACTORIES.put("seoConfiguration", SeoConfiguration::new);
        RESOURCE_FACTORIES.put("searchResultPage", SearchResultPage::new);
        RESOURCE_FACTORIES.put("categoryPage", CategoryPage::new);
        RESOURCE_FACTORIES.put("shoppingCart", ShoppingCart::new);
        RESOURCE_FACTORIES.put("productPage", ProductPage::new);
        RESOURCE_FACTORIES.put("checkoutPage", CheckoutPage::new);
        RESOURCE_FACTORIES.put("priceDisplaySettings", PriceDisplaySettings::new);
        RESOURCE_FACTORIES.put("animationEffects", AnimationEffects::new);
        RESOURCE_FACTORIES.put("productListing", ProductListing::new);
        RESOURCE_FACTORIES.put("customerPortal", CustomerPortal::new);
        RESOURCE_FACTORIES.put("siteSettings", SiteSettings::new);
        RESOURCE_FACTORIES.put("shippingSettings", ShippingSettings::new);
        RESOURCE_FACTORIES.put("searchSettings", SearchSettings::new);
        RESOURCE_FACTORIES.put("ecommerceSettings", EcommerceSettings::new);
        RESOURCE_FACTORIES.put("orderFlags", OrderFlags::new);
        RESOURCE_FACTORIES.put("inventoryLocations", InventoryLocations::new);
        RESOURCE_FACTORIES.put("taxCodes", TaxCodes::new);
        RESOURCE_FACTORIES.put("taxProfiles", TaxProfiles::new);
        RESOURCE_FACTORIES.put("taxRules", TaxRules::new);
        RESOURCE_FACTORIES.put("taxZones", TaxZones::new);
        RESOURCE_FACTORIES.put("taxSettings", TaxSettings::new);
        RESOURCE_FACTORIES.put("taxCountryTaxes", TaxCountryTaxes::new);
        RESOURCE_FACTORIES.put("taxCustomProfiles", TaxCustomProfiles::new);
        RESOURCE_FACTORIES.put("shippingMethods", ShippingMethods::new);
        RESOURCE_FACTORIES.put("shippingClasses", ShippingClasses::new);
        RESOURCE_FACTORIES.put("aliases", Aliases::new);
        RESOURCE_FACTORIES.put("orderCustomFields", OrderCustomFields::new);
        RESOURCE_FACTORIES.put("blogCategories", BlogCategories::new);
        RESOURCE_FACTORIES.put("categories", Categories::new);
        RESOURCE_FACTORIES.put("discountProfiles", DiscountProfiles::new);
        RESOURCE_FACTORIES.put("blogs", Blogs::new);
        RESOURCE_FACTORIES.put("carts", Carts::new);
        RESOURCE_FACTORIES.put("orders", Orders::new);
        RESOURCE_FACTORIES.put("countries", Countries::new);
        RESOURCE_FACTORIES.put("customerGroups", CustomerGroups::new);
        RESOURCE_FACTORIES.put("currencies", Currencies::new);
        RESOURCE_FACTORIES.put("variations", Variations::new);
        RESOURCE_FACTORIES.put("products", Products::new);
        RESOURCE_FACTORIES.put("cities", Cities::new);
        RESOURCE_FACTORIES.put("transactions", Transactions::new);
        RESOURCE_FACTORIES.put("paymentGateways", PaymentGateways::new);
        RESOURCE_FACTORIES.put("trash", Trash::new);
        RESOURCE_FACTORIES.put("email", Email::new);
        RESOURCE_FACTORIES.put("commonSettings", CommonSettings::new);
        RESOURCE_FACTORIES.put("pages", Pages::new);
    }

    /** Make a resource reachable as {@code client.resource(name)}; a clash is refused. */
    public static void registerResourceModule(String resourceName,
            Function<WebCommanderClient, ? extends BoundResource> factory) {
        if (resourceName == null || resourceName.isBlank()) {
            throw new SdkUsageError("cannot register a resource under a blank name: "
                    + "pass the name it should be reached by, as in "
                    + "registerResourceModule(\"adminProducts\", AdminProducts::new)");
        }
        if (!isIdentifier(resourceName)) {
            throw new SdkUsageError("cannot register resource '" + resourceName
                    + "': a resource name must read as an identifier — letters, digits and "
                    + "underscores, not starting with a digit — because it names an accessor");
        }
        if (factory == null) {
            throw new SdkUsageError("cannot register resource '" + resourceName
                    + "' with no factory: pass the constructor, as in AdminProducts::new");
        }
        Function<WebCommanderClient, ? extends BoundResource> registered =
                RESOURCE_FACTORIES.putIfAbsent(resourceName, factory);
        if (registered != null && registered != factory) {
            throw new SdkUsageError("cannot register resource '" + resourceName
                    + "' against a second factory: it is already registered, and clients "
                    + "built before the change would keep the first one — pick another name");
        }
    }

    /** Every registered resource name, sorted, as a snapshot. */
    public static List<String> registeredResourceNames() {
        return RESOURCE_FACTORIES.keySet().stream().sorted().toList();
    }

    /** The factory registered under a name, or an {@link SdkAttributeError}. */
    public static Function<WebCommanderClient, ? extends BoundResource> resolveResourceModule(
            String resourceName) {
        Function<WebCommanderClient, ? extends BoundResource> factory =
                RESOURCE_FACTORIES.get(resourceName);
        if (factory == null) {
            throw new SdkAttributeError("no resource '" + resourceName + "'; available: "
                    + String.join(", ", registeredResourceNames()));
        }
        return factory;
    }

    /** Refuse a {@code null} payload for an endpoint that takes a body. */
    public static void requirePayload(Object payload) {
        if (payload == null) {
            throw new SdkUsageError("payload is required: this endpoint takes a body, "
                    + "and null would send the request with no body at all");
        }
    }

    /** Whether a name reads as a Java identifier, which is what an accessor name is. */
    private static boolean isIdentifier(String name) {
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        for (int index = 1; index < name.length(); index++) {
            if (!Character.isJavaIdentifierPart(name.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
