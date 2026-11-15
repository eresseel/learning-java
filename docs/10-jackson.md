[← Előző: 09-hamcrest.md](09-hamcrest.md)

---

# Jackson – JSON ↔ Java átalakítás

**Dokumentáció:** <https://github.com/FasterXML/jackson-databind/wiki>  
**Annotációk:** <https://github.com/FasterXML/jackson-annotations/wiki/Jackson-Annotations>

---

## `ObjectMapper`

A Jackson belépési pontja. JSON szöveget Java objektummá, és fordítva.

```java
import com.fasterxml.jackson.databind.ObjectMapper;

ObjectMapper mapper = new ObjectMapper();

// JSON szöveg -> Java objektum:
Post post = mapper.readValue("{\"id\":1,\"title\":\"Hello\"}", Post.class);

// Java objektum -> JSON szöveg:
String json = mapper.writeValueAsString(post);

// JSON szöveg -> fa struktúra (típus nélkül):
JsonNode root = mapper.readTree(response.body());
String title = root.path("title").asText();
```

---

## `JsonNode`

A JSON fa egyetlen csomópontja. Hasznos, ha a struktúra nem ismert előre, vagy nem akarunk külön Java osztályt létrehozni.

```java
import com.fasterxml.jackson.databind.JsonNode;

JsonNode root = mapper.readTree("{\"id\":1,\"title\":\"Hello\",\"tags\":[\"a\",\"b\"]}");

root.path("title").asText();           // "Hello"
root.path("id").asInt();               // 1
root.path("missing").isMissingNode();  // true, ha a mező nincs jelen
root.path("missing").isNull();         // true, ha a mező null értékű

// Tömb iterálása:
for (JsonNode tag : root.path("tags")) {
    System.out.println(tag.asText());
}
```

---

## `@JsonIgnoreProperties(ignoreUnknown = true)`

Ha a szerver olyan mezőket is küld, amelyeket a Java osztály nem ismer, ne dobjon kivételt, hanem csendben hagyja figyelmen kívül. Érdemes mindig feltenni, hogy a tesztek ne törjenek el akkor, ha a szerver új mezőt vezet be.

```java
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Post(Integer userId, Integer id, String title, String body) {}
```

---

## `@JsonInclude(JsonInclude.Include.NON_NULL)`

A null értékű mezőket nem veszi bele a generált JSON-ba. Létrehozás (POST) kéréskor hasznos, ha az `id` mezőt a szerver adja ki, és nem akarjuk null-ként elküldeni.

```java
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(Integer userId, Integer id, String title, String body) {
    // Ha id = null, a {"userId":1,"title":"...","body":"..."} JSON jön létre,
    // az "id":null mező nélkül.
}
```

---

[ Következő: 11-wiremock.md → ](11-wiremock.md)
