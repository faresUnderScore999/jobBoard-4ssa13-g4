Your Neon credentials are currently hardcoded in `application.properties`. For production, consider externalizing them:

```javascript
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD
```
