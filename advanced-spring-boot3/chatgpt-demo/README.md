TP : Intégration des API OpenAI ChatGPT avec Spring Boot

Objectif

L’objectif de ce TP est de guider les étudiants dans la création d’une application Spring Boot capable d’interagir avec l’API ChatGPT d’OpenAI. L’application enverra des requêtes à l’API /v1/chat/completions d’OpenAI et affichera les réponses générées.

Prérequis

Avant de commencer, assurez-vous que les éléments suivants sont en place :
•	Clé API OpenAI : Générez une clé API sur le site d’OpenAI. Cette clé sera utilisée pour authentifier vos requêtes auprès de l’API OpenAI.
•	Application Spring Boot : Créez une nouvelle application Spring Boot en incluant la dépendance spring-boot-starter-web pour utiliser RestTemplate ou WebClient lors des appels API.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```


Étape 1 : Configuration de l’application
1.	Clé API OpenAI : Ajoutez votre clé API dans le fichier application.properties de votre projet Spring Boot.

openai.api.key=VOTRE_CLE_API
openai.api.url=https://api.openai.com/v1/chat/completions
openai.model=gpt-3.5-turbo


	2.	Modèles de requête et de réponse : Créez des classes Java pour modéliser les requêtes et les réponses de l’API OpenAI.

```java
// BotRequest.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotRequest {
private String model;
private List<Message> messages;
private int n;
private double temperature;
private int max_tokens;
}

// Message.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
private String role;
private String content;
}

// BotResponse.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotResponse {
private List<Choice> choices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
    }
}
```


Étape 2 : Implémentation du service
1.	Service OpenAI : Créez un service qui envoie des requêtes à l’API OpenAI en utilisant RestTemplate.

```java
@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getChatResponse(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Message message = new Message("user", userMessage);
        List<Message> messages = Collections.singletonList(message);

        BotRequest request = new BotRequest(model, messages, 1, 0.0, 100);

        HttpEntity<BotRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<BotResponse> response = restTemplate.postForEntity(apiUrl, entity, BotResponse.class);

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}
```


Étape 3 : Création du contrôleur
1.	Contrôleur REST : Créez un contrôleur REST pour gérer les requêtes des utilisateurs et retourner les réponses de l’API OpenAI.

```java
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenAIService openAIService;

    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = openAIService.getChatResponse(userMessage);
        return ResponseEntity.ok(response);
    }
}
```


Étape 4 : Test de l’application
1.	Lancer l’application : Démarrez votre application Spring Boot.
2.	Tester l’API : Utilisez un outil comme Postman ou curl pour envoyer une requête POST à votre API.

```curl -X POST http://localhost:8080/api/chat -H "Content-Type: application/json" -d '{"message": "Bonjour"}'```
