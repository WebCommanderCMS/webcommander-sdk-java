# WebCommander Java SDK

A Java client for the WebCommander v4 REST API, with OAuth2 token handling, typed resources,
retries and request diagnostics built in.

## Requirements

- JDK 17 or newer.
- Maven 3.9 or newer, or Gradle.
- Runtime dependencies, resolved by Maven: OkHttp 4.12, Jackson Databind 2.22 and the SLF4J API
  2.0.
- An SLF4J binding of your choice (`slf4j-simple`, `logback-classic`) if you want the SDK's log
  records. Without one, logging is silent.

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.webcommander</groupId>
    <artifactId>webcommander</artifactId>
    <version>4.0.0</version>
</dependency>
```

Maven resolves it from Maven Central. For Gradle, use `implementation "com.webcommander:webcommander:4.0.0"`.

## Quick start

The SDK does not read credentials from the environment. Read them in your own code and pass them
to `AuthConfig`:

```java
import com.webcommander.client.ApiResponse;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.config.AuthConfig;
import com.webcommander.config.SdkConfig;

import java.util.List;
import java.util.Map;

public final class QuickStart {
    public static void main(String[] args) {
        AuthConfig auth = AuthConfig.builder()
                .baseUrl(System.getenv("WC_BASE_URL"))
                .clientId(System.getenv("WC_CLIENT_ID"))
                .clientSecret(System.getenv("WC_CLIENT_SECRET"))
                .redirectUri(System.getenv("WC_REDIRECT_URI"))
                .authString(System.getenv("WC_AUTH_STRING"))
                .clientName("default")
                .build();
        SdkConfig config = SdkConfig.builder(auth).raiseOnError(true).build();

        try (WebCommanderClient client = new WebCommanderClient(config)) {
            ApiResponse response = client.adminCustomers().getCustomers(Map.of("limit", 5));
            Map<?, ?> body = (Map<?, ?>) response.getMap();
            for (Object row : (List<?>) body.get("customers")) {
                Map<?, ?> customer = (Map<?, ?>) row;
                System.out.println(customer.get("customer_id") + "  " + customer.get("email"));
            }
        }
    }
}
```

`authString` is the base64 encoding of `operator-email:password`. The client fetches the OAuth2
token on the first call and renews it for you. `raiseOnError(true)` makes a failed call throw;
without it, a failed call returns a response whose `succeeded()` is `false`. `WebCommanderClient`
is `AutoCloseable`, so try-with-resources releases its connections.

Customers are identified by `customer_id`. A customer record also carries `internal_id`, a second
id space that overlaps the first, so passing it where a path expects `{customer_id}` can return a
different customer.

## Configuration

`AuthConfig` holds the credentials for one store. `SdkConfig` holds everything else, and every
setting has a default, so a builder names only what it changes:

```java
SdkConfig config = SdkConfig.builder(auth)
        .timeoutSeconds(60)
        .maxRetries(3)
        .printApiInfo(true)
        .build();

SdkConfig debug = config.toBuilder().logLevel("DEBUG").printCurl(true).build();
```

Both types are immutable and validated at construction, and a validation error lists every
problem at once. To talk to several stores, register several `AuthConfig` entries and select one
by name or index.

## Usage

### Bound and explicit calls

Each resource is an accessor on the client, with one method per endpoint. Every method also has an
overload taking a trailing `RequestOptions` for this call's `raiseOnError`, `responseFormat` and
headers:

```java
client.orders().listOrders(Map.of("limit", 20));
client.adminCustomers().getCustomer(1192,
        RequestOptions.builder().raiseOnError(false).build());
```

A resource can also be built explicitly around a client, and registered instances can be built
anywhere by name without passing credentials:

```java
AdminCustomers customers = new AdminCustomers(client);

ClientRegistry.registerInstances(List.of(auth));
try (WebCommanderClient named = ClientRegistry.buildClient("default")) {
    named.taxCodes().listTaxCodes();
}
```

The raw senders reach any path, including endpoints without a typed method:

```java
client.sendGetRequest("/api/{api_version}/admin/products",
        RequestOptions.builder().queryParams(Map.of("limit", 20)).build());
```

### Payload forms

A request body can be a `Map`, a string of JSON (sent byte for byte) or a payload DTO:

```java
customers.createCustomer(Map.of("customer", Map.of("first_name", "Jane", "email", "jane@example.com")));

customers.createCustomer("{\"customer\": {\"first_name\": \"Jane\", \"email\": \"jane@example.com\"}}");

CustomerInput payload = new CustomerInput();
payload.firstName = "Jane";
payload.email = "jane@example.com";
customers.createCustomer(payload);
```

### Response forms

Every call returns an `ApiResponse`. `getMap()` is the decoded body, `getJson()` is the body text as
the store sent it, and `getDto()` is the typed view. `getData()` is whichever form `responseFormat`
selects (`"map"` by default):

```java
ApiResponse response = customers.getCustomer(1192);

Customer customer = response.getDto(Customer.class);
System.out.println(customer.email);
```

Keys are never renamed: a key you write is the key that is sent, and a key the store sends is the
key you read.

### Pagination

`Pagination.iterRecords` follows `pagination.next_page` for you:

```java
for (Map<String, Object> row : Pagination.iterRecords(client,
        AdminCustomersUrls.ADMIN_CUSTOMERS, "customers", Map.of("limit", 20))) {
    System.out.println(row.get("customer_id"));
}
```

Most listings serve at most 20 rows per page whatever `limit` asks for, so reading a whole
collection takes several requests.

## Error handling

By default a failed call returns an `ApiResponse` with `succeeded()` false. Set `raiseOnError` on
the config or on one call to throw an `SdkError` instead:

```java
try {
    client.adminCustomers().getCustomer(999999,
            RequestOptions.builder().raiseOnError(true).build());
} catch (SdkError error) {
    System.out.println(error.explain());
    System.out.println(error.getStatusCode() + " " + error.getErrorType());
}
```

`SdkConfigError`, `SdkUsageError`, `SdkAuthError` and `SdkAttributeError` extend `SdkError`, which
is unchecked, so one `catch (SdkError error)` covers every failure the SDK throws. A `200` whose
body carries `"isSuccess": false` (the store's answer to a mistyped path) counts as a failure.

## Resources

The client exposes 46 resources with 349 operations. The [API reference](https://newlify.mintlify.app)
lists the method, path and behaviour of each one.

| Resource | Client accessor | Operations |
|---|---|---:|
| [Customers](https://newlify.mintlify.app) | `client.adminCustomers()` | 15 |
| [Aliases](https://newlify.mintlify.app) | `client.aliases()` | 4 |
| [Animation effects settings](https://newlify.mintlify.app) | `client.animationEffects()` | 5 |
| [Blog categories](https://newlify.mintlify.app) | `client.blogCategories()` | 5 |
| [Blogs](https://newlify.mintlify.app) | `client.blogs()` | 12 |
| [Carts](https://newlify.mintlify.app) | `client.carts()` | 7 |
| [Categories](https://newlify.mintlify.app) | `client.categories()` | 10 |
| [Category page settings](https://newlify.mintlify.app) | `client.categoryPage()` | 5 |
| [Checkout page settings](https://newlify.mintlify.app) | `client.checkoutPage()` | 5 |
| [Cities](https://newlify.mintlify.app) | `client.cities()` | 2 |
| [Common settings](https://newlify.mintlify.app) | `client.commonSettings()` | 6 |
| [Countries](https://newlify.mintlify.app) | `client.countries()` | 8 |
| [Currencies](https://newlify.mintlify.app) | `client.currencies()` | 11 |
| [Customer groups](https://newlify.mintlify.app) | `client.customerGroups()` | 9 |
| [Customer portal settings](https://newlify.mintlify.app) | `client.customerPortal()` | 5 |
| [Discount profiles](https://newlify.mintlify.app) | `client.discountProfiles()` | 7 |
| [Ecommerce settings](https://newlify.mintlify.app) | `client.ecommerceSettings()` | 5 |
| [Email](https://newlify.mintlify.app) | `client.email()` | 1 |
| [Inventory locations](https://newlify.mintlify.app) | `client.inventoryLocations()` | 11 |
| [Order custom fields](https://newlify.mintlify.app) | `client.orderCustomFields()` | 4 |
| [Order flags](https://newlify.mintlify.app) | `client.orderFlags()` | 7 |
| [Orders](https://newlify.mintlify.app) | `client.orders()` | 25 |
| [Pages](https://newlify.mintlify.app) | `client.pages()` | 15 |
| [Payment gateways](https://newlify.mintlify.app) | `client.paymentGateways()` | 8 |
| [Price display settings](https://newlify.mintlify.app) | `client.priceDisplaySettings()` | 5 |
| [Product listing settings](https://newlify.mintlify.app) | `client.productListing()` | 3 |
| [Product page settings](https://newlify.mintlify.app) | `client.productPage()` | 5 |
| [Products](https://newlify.mintlify.app) | `client.products()` | 10 |
| [Search result page settings](https://newlify.mintlify.app) | `client.searchResultPage()` | 13 |
| [Search settings](https://newlify.mintlify.app) | `client.searchSettings()` | 9 |
| [SEO configuration](https://newlify.mintlify.app) | `client.seoConfiguration()` | 18 |
| [Shipping classes](https://newlify.mintlify.app) | `client.shippingClasses()` | 9 |
| [Shipping methods](https://newlify.mintlify.app) | `client.shippingMethods()` | 9 |
| [Shipping settings](https://newlify.mintlify.app) | `client.shippingSettings()` | 3 |
| [Shopping cart settings](https://newlify.mintlify.app) | `client.shoppingCart()` | 5 |
| [Site settings](https://newlify.mintlify.app) | `client.siteSettings()` | 5 |
| [Tax codes](https://newlify.mintlify.app) | `client.taxCodes()` | 7 |
| [Tax country taxes](https://newlify.mintlify.app) | `client.taxCountryTaxes()` | 5 |
| [Tax custom profiles](https://newlify.mintlify.app) | `client.taxCustomProfiles()` | 5 |
| [Tax profiles](https://newlify.mintlify.app) | `client.taxProfiles()` | 7 |
| [Tax rules](https://newlify.mintlify.app) | `client.taxRules()` | 7 |
| [Tax settings](https://newlify.mintlify.app) | `client.taxSettings()` | 2 |
| [Tax zones](https://newlify.mintlify.app) | `client.taxZones()` | 7 |
| [Transactions](https://newlify.mintlify.app) | `client.transactions()` | 6 |
| [Trash](https://newlify.mintlify.app) | `client.trash()` | 8 |
| [Variations](https://newlify.mintlify.app) | `client.variations()` | 12 |

Each resource's path templates are constants on its `<Resource>Urls` class, and its typed views
are nested in the `<Resource>Dto` class of the `<resource>_dto` package beside it. The Javadoc on
each method names the endpoint it calls.

## Documentation

- API reference: <https://newlify.mintlify.app>

## License

MIT. See [LICENSE](LICENSE).
