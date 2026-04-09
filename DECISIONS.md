# Architectural Decisions

## 1. Caching Strategy (Performance & Resilience)
- **Implementation**: Utilized a `ConcurrentHashMap` within the `WeatherServiceImpl` to store API responses.
- **Thread Safety**: Chose `ConcurrentHashMap` to ensure thread-safe operations in a multi-threaded OSGi environment without full synchronization overhead.
- **TTL Logic**: Implemented a manual expiration check (5-minute default) to ensure data freshness while staying within API rate limits.
- **Tenant Isolation**: The cache key is a composite of the `city` and `context path`, ensuring that different tenants (sites) maintain isolated data.

## 2. Configuration Strategy (Multi-tenancy)
- **Context-Aware Configuration (CAC)**: Implemented `WeatherConfig` using Sling CAC. This allows different brand sites or country branches (e.g., `/content/site-a` vs `/content/site-b`) to have unique API endpoints, keys, or TTL values.
- **Flexibility**: Settings are managed via `/conf`, allowing for hierarchical overrides from global defaults down to specific site branches.

## 3. Data Transfer & Parsing
- **DTO Pattern**: Created a `WeatherData` POJO (Plain Old Java Object) to decouple the API's JSON structure from the Sling Model. This makes the system resilient to external API schema changes.
- **JSON Parsing**: Integrated **Google Gson** for robust serialization. 
- **Compatibility Note**: Explicitly implemented **Public Getters/Setters** in DTOs to ensure compatibility with Gson's reflection mechanism within the restricted OSGi ClassLoader environment.

## 4. Sling Models & Template Integration
- **Adaptables**: Configured `WeatherModel` to be adaptable from both `SlingHttpServletRequest` and `Resource`. This ensures the component works across standard rendering, **Editable Templates**, and Content Fragments.
- **Template Support**: Validated that the model correctly resolves the context, allowing the component to be used within AEM Templates while maintaining proper configuration inheritance.
- **Injection Strategy**: Used `DefaultInjectionStrategy.OPTIONAL` to prevent model failure if component properties (like `city`) are missing in the JCR.
- **Standard Injection**: Relied on `@Inject` for service and property acquisition, ensuring a clean and readable model structure.
- **Initialization**: Logic is encapsulated in a `@PostConstruct init()` method, ensuring data fetching and parsing occur exactly once per request.

## 5. Security & Dispatcher Hardening
- **API Key Protection**: Moved sensitive keys from the frontend to **CAC (Context-Aware Cloud Config)**, ensuring they are never exposed in the client-side DOM.
- **Dispatcher Filters**: 
    - Applied a **"Deny by Default"** strategy.
    - Path restriction: Specifically allowed only the assessment content path (`/content/assessment/*.html`) and its model representation (`*.model.json`).
    - JCR Protection: Added an explicit deny rule for `jcr:content` properties (`/9999`) to prevent the exposure of internal node metadata.

## 6. Environment, Build & Deployment
- **Build Success**: Resolved critical Maven dependency conflicts (specifically related to Gson versioning and Testing libraries) to stabilize the build pipeline.
- **Deployment**: Confirmed successful compilation and automated deployment to the AEM instance using the `autoInstallBundle` and `autoInstallPackage` profiles. (mvn clean install -PautoInstallBundle,autoInstallPackage -DskipTests)
- **OSGi Resolution**: Verified that all bundles reach the `ACTIVE` state without unresolved dependencies in the Felix Console.

## 7. Testing Strategy
- **Unit Testing**: Implemented JUnit 4 and Mockito tests to validate the core service logic.
- **Mocking Strategy**: Used `MockitoJUnitRunner` to mock Sling `Resource` and `ConfigurationBuilder` objects.
- **Validation**: Tests specifically verify the **CAC resolution process**, ensuring the service correctly adapts the resource to retrieve configuration before attempting API calls.

## 8. Assumptions & Trade-offs
- **Error Handling**: When the API fails or returns invalid data, the system returns a graceful "Unavailable" message instead of breaking the page layout.