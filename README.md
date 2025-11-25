# Rest Assured API Test Framework – Restful-Booker

This is a **fully functional API automation framework** built with:

- Java 17  
- Maven  
- Rest Assured  
- JUnit 4  
- Builder pattern for payloads  
- Service / Manager layer (API POM)  
- Request & Response specifications  
- Externalised configuration  
- GitHub Actions CI pipeline

Target API: [Restful-Booker](https://restful-booker.herokuapp.com/apidoc/index.html)

---

## 1. Project Structure

```text
restassured-restfulbooker-framework
├── pom.xml
├── .github
│   └── workflows
│       └── ci.yml
└── src
    └── test
        ├── java
        │   └── com
        │       └── example
        │           └── restfulbooker
        │               ├── client
        │               │   ├── AuthClient.java
        │               │   ├── BaseClient.java
        │               │   ├── BookingClient.java
        │               │   └── HealthClient.java
        │               ├── config
        │               │   └── ConfigManager.java
        │               ├── payloads
        │               │   ├── AuthPayload.java
        │               │   ├── BookingDates.java
        │               │   └── BookingPayload.java
        │               ├── specs
        │               │   ├── RequestSpecs.java
        │               │   └── ResponseSpecs.java
        │               └── tests
        │                   ├── AuthTests.java
        │                   ├── BaseTest.java
        │                   ├── BookingCrudTests.java
        │                   └── HealthCheckTests.java
        └── resources
            └── config.properties
```

### Layers

- **config** – loads environment settings (`BASE_URI`, credentials).  
- **specs** – reusable Request & Response specifications.  
- **payloads** – POJOs using **Builder pattern** for clean test data setup.  
- **client** – Service / Manager layer (API POM) with methods like `createBooking`, `updateBooking`, etc.  
- **tests** – JUnit tests that use the client classes and focus only on assertions.

---

## 2. Prerequisites

- Java 17 installed (`java -version`)  
- Maven installed (`mvn -version`)  
- Internet access (API is public & online)

---

## 3. Configuration

Config is in: `src/test/resources/config.properties`

```properties
BASE_URI=https://restful-booker.herokuapp.com
USERNAME=admin
PASSWORD=password123
```

You can override any value via:

- JVM system property: `-DBASE_URI=https://...`  
- Environment variable: `export BASE_URI=https://...`

---

## 4. How the Framework Works

### 4.1 ConfigManager

`ConfigManager` reads configuration in this priority order:

1. Java system properties  
2. Environment variables  
3. `config.properties` on the classpath  

This makes it easy to switch environments without touching code.

---

### 4.2 Request / Response Specifications

`RequestSpecs`:

- Sets the base URI  
- Sets `Content-Type: application/json`  
- Logs URI and Method  
- Provides:
  - `unauthenticated()` – for open endpoints  
  - `withToken(token)` – for authenticated calls via `Cookie: token=...`  

`ResponseSpecs`:

- Common 2xx response spec with:
  - Status + body logging  
  - Max response time assertion (5 seconds)

---

### 4.3 Builder Pattern Payloads

Example: `BookingPayload` and `BookingDates`:

```java
BookingDates dates = new BookingDates.Builder("2025-01-01", "2025-01-05").build();

BookingPayload payload = new BookingPayload.Builder("Shubhanshu", "Rastogi", 150, true, dates)
        .withAdditionalNeeds("Breakfast")
        .build();
```

This keeps test data creation clean, readable and easy to reuse.

---

### 4.4 Service / Manager Layer (API POM)

**AuthClient**:

- `createToken(username, password)`  
- `createDefaultAdminToken()` – uses credentials from config.

**BookingClient**:

- `getBookingIds()`  
- `getBookingById(int id)`  
- `createBooking(BookingPayload payload)`  
- `updateBooking(int id, BookingPayload payload, String token)`  
- `partialUpdateBooking(int id, String jsonPatchBody, String token)`  
- `deleteBooking(int id, String token)`

**HealthClient**:

- `ping()` → `/ping` health-check endpoint.

All HTTP details live here, not in the tests.

---

### 4.5 BaseTest

- Enables global Rest Assured logging.  
- Lazily creates an auth token once and reuses it:

```java
protected static String getOrCreateToken() {
    if (token == null) {
        AuthClient authClient = new AuthClient();
        token = authClient.createDefaultAdminToken()
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
    return token;
}
```

---

## 5. Test Suites Included

### 5.1 HealthCheckTests

- Verifies `/ping` returns `200` or `201` and meets response spec.

### 5.2 AuthTests

- Valid credentials → returns token, status 200.  
- Invalid credentials → returns `403`.

### 5.3 BookingCrudTests

Covers the main **CRUD flow** against `/booking`:

1. `createBooking_shouldReturnBookingIdAndBookingObject`  
2. `getBookingIds_shouldReturnListOfIds`  
3. `updateBooking_shouldRequireAuthAndReturnUpdatedBooking`  
4. `deleteBooking_shouldRemoveBooking`

---

## 6. Running the Tests Locally

From the project root:

```bash
mvn clean test
```

To override base URI or credentials at runtime:

```bash
mvn clean test -DBASE_URI=https://restful-booker.herokuapp.com -DUSERNAME=admin -DPASSWORD=password123
```

If you see intermittent failures, remember Restful-Booker resets its data roughly every 10 minutes, so IDs can disappear or change.

---

## 7. CI/CD – GitHub Actions

The workflow file is at:

```text
.github/workflows/ci.yml
```

It:

1. Triggers on `push` and `pull_request` to `main` / `master`.  
2. Sets up Java 17.  
3. Caches Maven dependencies.  
4. Runs:

```bash
mvn -B clean test
```

### How to use it

1. Push this project to a GitHub repo.  
2. Ensure the default branch is `main` or `master`.  
3. GitHub will automatically run the pipeline on each push / PR.  
4. You can see results under **Actions** tab.

---

## 8. How to Extend the Framework

### Add a new endpoint

1. Create a new method in an existing client or add a new client class, e.g.:

```java
public Response getBookingByName(String firstname, String lastname) {
    return given()
        .spec(RequestSpecs.unauthenticated())
        .queryParam("firstname", firstname)
        .queryParam("lastname", lastname)
        .when()
        .get("/booking");
}
```

2. Write a test in `tests` package that calls the new method and asserts the behaviour.

### Add new payload fields

Update the Builder in the corresponding payload class, then use the new method in tests.

---

## 9. Typical Workflow You’ll Use

1. **Read API doc** (Restful-Booker site).  
2. **Create / update payload** in `payloads`.  
3. **Add a client method** for the endpoint in `client`.  
4. **Write test** in `tests` using:
   - Payload Builder  
   - Client method  
   - Hamcrest matchers for assertions  
5. Run `mvn test` locally.  
6. Push to GitHub; GitHub Actions will run the same tests.

---

## 10. Troubleshooting

- **401 / 403 on update / delete**  
  - Likely missing or invalid token. Check `getOrCreateToken()` and credentials in `config.properties`.

- **404 on GET booking after delete**  
  - This is expected and asserted in `deleteBooking_shouldRemoveBooking`.

- **Random failures**  
  - Restful-Booker resets every ~10 minutes. Run tests quickly after creating data, or re-run.

---

Happy testing!  
You can now use this as a base and keep extending it as your own real-world REST Assured framework.
