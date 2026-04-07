# Architectural Decisions

## Caching Strategy
- Used a simple ConcurrentHashMap-based cache with manual expiration check.
- Cache TTL of 5 minutes to reduce external API calls.
- Cache key includes city and API key for tenant isolation.
- This improves performance by avoiding repeated API calls without external dependencies.

## Configuration Strategy
- Implemented Context-Aware Configuration (CAC) for tenant-aware settings.
- API key and endpoint are configurable per context (e.g., per site).
- Default config provided in ui.config, but can be overridden in /conf or page-specific configs.

## Dispatcher Hardening
- Restricted /content/* to /content/assessment/* to limit exposure.
- Removed /bin/* allow rule as it's not needed.
- Added deny-all rule at the end.
- Allowed POST to weather selector for potential future use.

## Security Improvements
- Moved API keys from code and frontend to OSGi config.
- Used Apache HttpClient for better security and resilience.
- Removed inline JavaScript from HTL.

## Performance Improvements
- Added caching to avoid repeated API calls.
- Used HttpClient with proper error handling.

## Assumptions
- The external weather API is reliable; added basic error handling.
- Tenant config is managed via AEM's CAC mechanism.
- No authentication needed for the API in this refactor.