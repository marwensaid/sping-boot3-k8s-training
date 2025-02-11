TP : Utilisation de RestClient avec Spring Boot

Objectif

L’objectif de ce TP est de guider les étudiants dans la création d’une application Spring Boot capable d’effectuer des requêtes HTTP synchrones en utilisant la classe RestClient. Les étudiants apprendront à configurer RestClient, à effectuer des requêtes HTTP (GET, POST, PUT, DELETE), à utiliser la méthode exchange() pour des requêtes personnalisées, et à gérer les exceptions.

Prérequis

Avant de commencer, assurez-vous que les éléments suivants sont en place :
•	Connaissances en Java et Spring Boot : Les étudiants doivent être familiers avec le développement d’applications Spring Boot et la communication avec des services RESTful.
•	Environnement de développement : Un environnement configuré avec Java et Maven pour gérer les dépendances du projet.

Étape 1 : Choix entre RestTemplate, RestClient et WebClient

Avant Spring Framework 6.1, RestTemplate était couramment utilisé pour effectuer des requêtes HTTP synchrones. Cependant, il est considéré comme obsolète et sera remplacé par RestClient, qui offre une API plus moderne et fluide. WebClient, introduit avec Spring WebFlux, est conçu pour des communications asynchrones et réactives, mais peut également être utilisé pour des requêtes synchrones. Toutefois, pour des cas d’utilisation simples et synchrones, RestClient est recommandé car il ne nécessite pas la pile réactive complète.

Étape 2 : Configuration du Projet Maven
1.	Ajouter les dépendances nécessaires : Incluez les dépendances spring-boot-starter-web et, si nécessaire, httpclient5 dans votre fichier pom.xml.

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Dépendance pour HttpClient si utilisé -->
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
        <version>5.2.1</version>
    </dependency>
</dependencies>
```


Étape 3 : Création du Bean RestClient
1.	Configuration simple : Créez un bean RestClient en utilisant la méthode create().

```java
import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
```

	2.	Configuration avancée avec HttpClient : Si vous souhaitez personnaliser davantage le client HTTP, vous pouvez utiliser HttpClient et le configurer avec RestClient.

```java
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public RestClient restClient(CloseableHttpClient httpClient) {
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
            .build();
    }
}
```


Étape 4 : Effectuer des Requêtes HTTP
1.	Requête GET : Utilisez RestClient pour effectuer une requête GET et récupérer une ressource.

```java
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getResource(String url) {
        return restClient.get()
            .uri(url)
            .retrieve()
            .body(String.class);
    }
}
```

	2.	Requête POST : Envoyez des données à un serveur en utilisant une requête POST.

```java
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String createResource(String url, Object request) {
        return restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(String.class);
    }
}
```

	3.	Requête PUT : Mettez à jour une ressource existante en utilisant une requête PUT.

```java
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void updateResource(String url, Object request) {
        restClient.put()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .toBodilessEntity();
    }
}
```

4.	Requête DELETE : Supprimez une ressource en utilisant une requête DELETE.

```java
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void deleteResource(String url) {
        restClient.delete()
            .uri(url)
            .retrieve()
            .toBodilessEntity();
    }
}
```

Voici la suite du TP avec les parties manquantes.

Étape 5 : Utilisation de la Méthode exchange()

La méthode exchange() permet de gérer les requêtes de manière plus flexible en définissant le verbe HTTP, les en-têtes et le type de réponse.

```java
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String customRequest(String url) {
        ResponseEntity<String> response = restClient.method(HttpMethod.GET)
            .uri(url)
            .retrieve()
            .toEntity(String.class);

        return response.getBody();
    }
}
```

Étape 6 : Gestion des Exceptions

Spring Boot propose des mécanismes intégrés pour gérer les erreurs lors des appels HTTP.
1.	Gestion simple des erreurs :

```java
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getResourceWithExceptionHandling(String url) {
        try {
            return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
        } catch (HttpClientErrorException e) {
            return "Erreur : " + e.getStatusCode();
        }
    }
}
```

	2.	Gestion avancée avec un RestClientResponseException :

```java
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final RestClient restClient;

    public ApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getResourceWithDetailedErrorHandling(String url) {
        try {
            return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
        } catch (RestClientResponseException e) {
            return "Erreur HTTP " + e.getRawStatusCode() + ": " + e.getResponseBodyAsString();
        }
    }
}
```


Étape 7 : Sérialisation et Désérialisation JSON
1.	Définition d’un modèle de données (User.java) :

```java
public class User {
private String name;
private String email;

    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

	2.	Utilisation du modèle dans les appels API :

```java
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final RestClient restClient;

    public UserService(RestClient restClient) {
        this.restClient = restClient;
    }

    public User getUser(String url) {
        return restClient.get()
            .uri(url)
            .retrieve()
            .body(User.class);
    }

    public User createUser(String url, User user) {
        return restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(user)
            .retrieve()
            .body(User.class);
    }
}
```

Étape 8 : Configuration des Timeouts

Pour éviter que des requêtes bloquent indéfiniment, configurez des timeouts avec HttpClient.
```java
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
            .setResponseTimeout(Duration.ofSeconds(5))
            .setConnectTimeout(Duration.ofSeconds(3))
            .build();

        return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build();
    }

    @Bean
    public RestClient restClient(CloseableHttpClient httpClient) {
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
            .build();
    }
}
```

Étape 9 : Test et Validation
1.	Tester avec JUnit :

```java
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiServiceTest {

    private final RestClient restClient = mock(RestClient.class);
    private final ApiService apiService = new ApiService(restClient);

    @Test
    void testGetResource() {
        String expectedResponse = "Hello, world!";
        when(restClient.get().uri("http://example.com").retrieve().body(String.class))
            .thenReturn(expectedResponse);

        String result = apiService.getResource("http://example.com");
        assertEquals(expectedResponse, result);
    }
}
```
