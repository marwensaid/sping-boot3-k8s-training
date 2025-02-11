TP : Génération de la Documentation REST API avec Springdoc-OpenAPI et Swagger pour Spring Boot

Objectif

L’objectif de ce TP est de guider les étudiants dans la personnalisation de la documentation générée pour une API REST
développée avec Spring Boot, en utilisant la bibliothèque Springdoc-OpenAPI 3.0. Les étudiants apprendront à enrichir la
documentation de l’API en ajoutant des descriptions, des exemples, des informations sur les paramètres et les réponses,
ainsi qu’à configurer des informations générales sur l’API.

Prérequis

Avant de commencer, assurez-vous que les éléments suivants sont en place :
• Projet Spring Boot : Un projet Spring Boot avec des contrôleurs REST déjà définis.
• Dépendance Springdoc-OpenAPI : La dépendance springdoc-openapi-starter-webmvc-ui est ajoutée au fichier pom.xml du
projet.

Étape 4 : Personnalisation de la Documentation Générée à l’aide des Annotations

La documentation générée par défaut est suffisante pour commencer. Cependant, au fil du temps, vous devrez peut-être la
configurer pour répondre à diverses exigences. Apprenons quelques-unes de ces configurations.

4.1. Ajout de Descriptions aux Contrôleurs et Méthodes

Utilisez l’annotation @Operation pour ajouter des descriptions aux méthodes de vos contrôleurs.

```java

@RestController
@RequestMapping("/api")
public class UserController {

    @Operation(summary = "Obtenir un utilisateur par son ID", description = "Retourne les détails de l'utilisateur spécifié par l'ID.")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // Logique pour obtenir l'utilisateur
    }
}
```

4.2. Documentation des Paramètres et des Réponses

Utilisez les annotations @Parameter et @ApiResponse pour documenter les paramètres d’entrée et les réponses de vos API.

```java

@RestController
@RequestMapping("/api")
public class UserController {

    @Operation(summary = "Créer un nouvel utilisateur", description = "Ajoute un nouvel utilisateur au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping("/users")
    public ResponseEntity<User> createUser(
            @Parameter(description = "Détails de l'utilisateur à créer") @RequestBody User user) {
        // Logique pour créer l'utilisateur
    }
}
```

4.3. Ajout d’Exemples aux Modèles

Utilisez l’annotation @Schema pour ajouter des descriptions et des exemples aux modèles de données.

```java

@Schema(description = "Représentation d'un utilisateur dans le système")
public class User {

    @Schema(description = "Identifiant unique de l'utilisateur", example = "1")
    private Long id;

    @Schema(description = "Nom complet de l'utilisateur", example = "Jean Dupont")
    private String name;

    @Schema(description = "Adresse e-mail de l'utilisateur", example = "jean.dupont@example.com")
    private String email;

    // Getters et setters
}
```

Étape 5 : Configuration des Informations Générales de l’API

Vous pouvez configurer des informations générales sur votre API, telles que le titre, la description, la version, les
termes du service, les informations de contact, etc., en définissant un bean OpenAPI dans votre configuration Spring.

```java

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion des Utilisateurs")
                        .version("1.0")
                        .description("Cette API permet de gérer les utilisateurs du système.")
                        .termsOfService("http://example.com/terms/")
                        .contact(new Contact()
                                .name("Support API")
                                .url("http://example.com/support")
                                .email("support@example.com"))
                        .license(new License()
                                .name("Licence MIT")
                                .url("http://example.com/license")));
    }
}
```

Étape 6 : Personnalisation Avancée de Swagger UI

Vous pouvez personnaliser davantage l’apparence et le comportement de Swagger UI en définissant des propriétés
supplémentaires dans votre fichier application.properties.

```properties
springdoc.swagger-ui.doc-expansion=none
springdoc.swagger-ui.operations-sorter=alpha
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.default-model-rendering=model
```

	•	doc-expansion : Contrôle l’expansion par défaut des sections de documentation (options : none, list, full).
	•	operations-sorter et tags-sorter : Permettent de trier les opérations et les tags par ordre alphabétique.
	•	display-request-duration : Affiche la durée des requêtes dans Swagger UI.
	•	default-model-rendering : Définit la vue par défaut des modèles (model ou example).

Étape 7 : Génération de la Documentation de l’API

Après avoir configuré et personnalisé votre documentation, démarrez votre application Spring Boot. Vous pouvez accéder à
la documentation interactive de votre API en ouvrant un navigateur et en naviguant vers :

http://localhost:8080/swagger-ui.html

Ici, vous pourrez explorer vos endpoints API, tester des requêtes et voir les descriptions, paramètres et réponses que
vous avez définis.
