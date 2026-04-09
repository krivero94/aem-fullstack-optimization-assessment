# Architectural Decisions

## 1. Caching Strategy (Performance & Resilience)
- **Implementation**: Utilized a `ConcurrentHashMap` within the `WeatherServiceImpl` to store API responses.
- **Thread Safety**: Chose `ConcurrentHashMap` to ensure thread-safe operations without the overhead of full synchronization.
- **TTL Logic**: Implemented a manual expiration check (5-minute default) to ensure data freshness while staying within API rate limits.
- **Tenant Isolation**: The cache key is a composite of the `city` and `context path`, ensuring that different tenants (sites) don't leak data to one another even if they query the same city.

## 2. Configuration Strategy (Multi-tenancy)
- **Context-Aware Configuration (CAC)**: Implemented `WeatherConfig` using Sling CAC. This allows different brand sites or country branches (e.g., `/content/site-a` vs `/content/site-b`) to have different API endpoints, keys, or TTL values.

## 3. Data Transfer & Parsing
- **DTO Pattern**: Created a `WeatherData` POJO (Plain Old Java Object) to decouple the API's JSON structure from the Sling Model. This makes the system resilient to external API schema changes.
- **JSON Parsing**: Integrated **Google Gson** for robust serialization. 
- **Compatibility Note**: Explicitly implemented **Public Getters/Setters** in DTOs to ensure compatibility with Gson's reflection mechanism within the restricted OSGi ClassLoader environment.

## 4. Sling Models Implementation
- **Adaptables**: The `WeatherModel` is adaptable from both `SlingHttpServletRequest` and `Resource`, ensuring the component works across different rendering contexts.
- **Injection Strategy**: Used `DefaultInjectionStrategy.OPTIONAL` to prevent model failure if component properties (like `city`) are missing in the JCR.
- **Standard Injection**: Relied on `@Inject` for service and property acquisition, ensuring a clean and readable model structure.
- **Initialization**: Logic is encapsulated in a `@PostConstruct init()` method, ensuring data fetching and parsing occur exactly once per request.
- **Resilience**: Implemented error handling within the initialization. If the service fails or the JSON is malformed, the model fails gracefully, allowing the HTL to display a "data unavailable" message instead of breaking the page layout.

## 5. Security & Dispatcher Hardening
- **API Key Protection**: Moved sensitive keys from the frontend and hardcoded constants to **CAC (Context-Aware Cloud Config)**, ensuring they are never exposed in the client-side DOM.
- **Dispatcher**: 
    - Applied a **"Deny by Default"** strategy.
    - Path restriction: Specifically allowed only the assessment content path (`/content/assessment/*.html`) and its model representation (`*.model.json`),
    - JCR Protection: Added an explicit deny rule for `jcr:content` properties (`/9999`) to prevent the exposure of internal node metadata to end-users.

## 6. Testing Strategy
- **Unit Testing**: Implemented JUnit 4 and Mockito tests to validate the core service logic.
- **Mocking Strategy**: Used `MockitoJUnitRunner` to mock Sling `Resource` and `ConfigurationBuilder` objects.
- **Validation**: Tests specifically verify the **CAC resolution process**, ensuring the service correctly adapts the resource to retrieve configuration before attempting API calls.

## 7. Assumptions & Trade-offs
- **Error Handling**: When the API fails, the system returns a graceful "Unavailable" message instead of breaking the page layout.