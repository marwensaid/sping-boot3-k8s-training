Utilisation de Resilience4j pour la Limitation de Débit et le Cloisonnement dans une Application Spring Boot

Resilience4j est une bibliothèque légère utilisée pour construire des systèmes résilients et tolérants aux pannes. Elle offre diverses fonctionnalités telles que le disjoncteur (Circuit Breaker), la limitation de débit (Rate Limiting), la nouvelle tentative (Retry), le cloisonnement (Bulkhead), etc.

Dans cet article, nous nous concentrerons sur la limitation de débit et le cloisonnement en utilisant Resilience4j 2 dans une application Spring Boot 3.

Remarque : Resilience4j v2.0 nécessite Java 17 ou une version ultérieure.

1. Qu’est-ce que la Limitation de Débit et le Cloisonnement ?

Bien que la limitation de débit et le cloisonnement puissent sembler similaires, ils diffèrent dans leur fonctionnement.
•	Limitation de Débit : Définit la fréquence à laquelle une opération est autorisée pendant une durée spécifiée. Cela évite de surcharger le serveur et aide à prévenir les attaques par déni de service.
•	Cloisonnement : Limite le nombre d’opérations distantes simultanées à un moment donné. Cela aide à prévenir les défaillances du système.

Limitation de Débit	Cloisonnement
Limite le nombre total d’appels sur une période spécifiée	Limite le nombre total d’exécutions simultanées/parallèles
Utilisé pour prévenir les attaques par déni de service	Utilisé pour empêcher les défaillances d’un système/API d’affecter d’autres systèmes/APIs
Exemple : Autoriser un maximum de 10 appels parallèles à tout moment	Exemple : Autoriser un maximum de 10 appels parallèles à tout moment

En résumé, si nous avons une API avec un temps de réponse inférieur à la milliseconde, nous pouvons utiliser une limitation de débit pour prévenir les abus de l’API. En revanche, si nous avons une API intensive en calcul et en temps, nous pouvons opter pour le cloisonnement afin d’éviter que le système ne plante.

2. Configuration

Créons une application Spring Boot simple avec un seul point de terminaison pour récupérer le nom d’un étudiant en utilisant son identifiant.

2.1. Maven

Commencez par ajouter la dernière version de la dépendance resilience4j-spring-boot3 dans le fichier pom.xml pour activer les différents modules de Resilience4j.

Resilience4j nécessite les dépendances Spring AOP et Actuator en tant que prérequis, nous devons donc les ajouter également. Puisque nous exposerons nos points de terminaison via un appel GET, nous ajouterons la dépendance Spring Web.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
</dependency>
```

2.2. API REST

Nous avons un seul contrôleur nommé StudentController. Il possède deux points de terminaison : /student/{id} et /course/{id}.

```java
@RestController
public class StudentController {

    @GetMapping(value = "/student/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable int id) {
        return ResponseEntity.ok("Détails demandés pour " + id);
    }

    @GetMapping(value = "/course/{id}")
    public ResponseEntity<String> getCourse(@PathVariable int id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Cours " + id);
    }
}
```

3. Limitation de Débit

La limitation de débit permet de configurer la fenêtre de la période de temps pendant laquelle un nombre prédéfini d’appels serait autorisé, indépendamment du temps pris par chaque appel pour se terminer.

3.1. Annotation @RateLimiter

@RateLimiter est l’annotation utilisée pour limiter le débit d’un appel API et s’applique au niveau de la méthode ou de la classe. Si elle est appliquée au niveau de la classe, elle s’applique à toutes les méthodes publiques. Cette annotation prend deux paramètres. Le premier est le nom du limiteur de débit et le second est la méthode de repli à invoquer via fallbackMethod. La méthode de repli est optionnelle et serait invoquée si la requête n’est pas autorisée à être exécutée en raison des limites que nous avons définies dans nos configurations.

Pour activer la limitation de débit sur le point de terminaison student, nous annoterons la méthode getStudentById avec l’annotation @RateLimiter et déclarerons la méthode de repli rateLimitingFallback().

La méthode de repli doit imiter les paramètres de la méthode getStudentById() avec l’ajout d’un argument Throwable qui, dans ce cas, serait une instance de io.github.resilience4j.ratelimiter.RequestNotPermitted.

```java
@RestController
public class StudentController {

    @GetMapping(value = "/student/{id}")
    @RateLimiter(name = "rateLimiterStudent", fallbackMethod = "rateLimitingFallback")
    public ResponseEntity<String> getStudentById(@PathVariable int id) {
        return ResponseEntity.ok("Détails demandés pour " + id);
    }

    public ResponseEntity<String> rateLimitingFallback(int id, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                             .body("Trop de requêtes - Limite de débit atteinte");
    }

    @GetMapping(value = "/course/{id}")
    public ResponseEntity<String> getCourse(@PathVariable int id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Cours " + id);
    }
}
```
3.2. Valeurs par Défaut de la Limitation de Débit et Configuration des Propriétés

Les valeurs par défaut de la limitation de débit sont les suivantes :
•	limitForPeriod : 50
•	limitRefreshPeriod : 500ns
•	timeoutDuration : 5s

Nous pouvons remplacer ces valeurs par défaut en définissant les

propriétés suivantes dans le fichier application.properties :

```properties
resilience4j.ratelimiter.instances.rateLimiterAPI.limitRefreshPeriod=1s
resilience4j.ratelimiter.instances.rateLimiterAPI.limitForPeriod=10 
resilience4j.ratelimiter.instances.rateLimiterAPI.timeoutDuration=0s
# Configuration for another limiter 'rateLimiterApi_2'
resilience4j.ratelimiter.instances.rateLimiterApi_2.limitRefreshPeriod=500ms
resilience4j.ratelimiter.instances.rateLimiterApi_2.limitForPeriod=6
resilience4j.ratelimiter.instances.rateLimiterApi_2.timeoutDuration=3s
# health endpoint will still report "UP" even if one of the component's circuit breakers is in an open or half-open state.
resilience4j.ratelimiter.instances.rateLimiterApi_2.allowHealthIndicator-to-fail=false
#Buffer size to store the ratelimiter events
resilience4j.ratelimiter.instances.rateLimiterApi_2.eventConsumerBufferSize=50
resilience4j.ratelimiter.instances.rateLimiterApi_2.subscribeForEvents=true
```



4. Cloisonnement (Bulkhead)

Le cloisonnement permet de configurer le nombre maximal d’appels concurrents autorisés à être exécutés à un moment donné. Le terme “Bulkhead” est emprunté aux compartiments étanches d’un navire. Si une section de la coque est compromise, seule cette section sera inondée, empêchant le navire de couler. Dans un contexte applicatif, si nous avons un point de terminaison API qui prend trop de temps à s’exécuter, le cloisonnement empêchera les autres services (points de terminaison) de l’application d’être affectés.

4.1. Annotation @Bulkhead

L’annotation @Bulkhead est utilisée pour activer le cloisonnement sur un appel API. Elle peut être appliquée au niveau de la méthode ou de la classe. Si elle est appliquée au niveau de la classe, elle s’applique à toutes les méthodes publiques. Cette annotation prend deux paramètres : le name du cloisonnement et la méthode de repli à invoquer via fallbackMethod.

La méthode de repli est optionnelle et serait invoquée si la requête n’est pas autorisée à être exécutée en raison des limites définies dans nos configurations.

Pour activer le cloisonnement sur l’API getCourse, nous avons défini le cloisonnement avec le nom courseBulkheadApi et la méthode de repli bulkheadFallback.

La méthode de repli doit imiter les paramètres de la méthode getCourse avec l’ajout d’un argument Throwable qui, dans ce cas, serait une instance de io.github.resilience4j.bulkhead.BulkheadFullException.

```java
@RestController
public class StudentController {

@GetMapping(value = "/course/{id}")
@Bulkhead(name = "courseBulkheadApi", fallbackMethod = "bulkheadFallback")
public ResponseEntity<String> getCourse(@PathVariable int id) {

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return ResponseEntity.ok("Cours " + id);
}

public ResponseEntity<String> bulkheadFallback(int id, BulkheadFullException ex) {
return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
.body("Trop de requêtes - Aucune autre requête n'est acceptée");
}
}
```

Si nous n’avons pas de méthode de repli, une BulkheadFullException se produira, entraînant des échecs non gérés. Il est recommandé d’ajouter un @ExceptionHandler pour la gérer.

```java
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
@ExceptionHandler({ BulkheadFullException.class })
@ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
public void bandwidthExceeded() {
}
}
```

4.2. Valeurs par Défaut du Cloisonnement et Configuration des Propriétés

Il y a deux configurations principales que nous devons définir dans les propriétés de notre application :

Propriété de configuration	Valeur par défaut	Description
maxConcurrentCalls	5	Nombre d’appels concurrents autorisés
maxWaitDuration	10 ms	Temps d’attente avant échec en cas de dépassement de la limite

Nous pouvons remplacer les valeurs par défaut du cloisonnement dans la configuration des propriétés.

Avec la configuration suivante, nous voulons limiter le nombre maximal d’appels concurrents à 10, de sorte que chaque thread puisse attendre seulement 100 ms si le cloisonnement est plein. Après cela, les requêtes seront rejetées avec l’exception BulkheadFullException.

```properties
resilience4j.bulkhead.instances.courseBulkheadApi.maxConcurrentCalls=10
resilience4j.bulkhead.instances.courseBulkheadApi.maxWaitDuration=100ms
```

5. Enregistrer les Vérifications de Santé avec Actuator

En utilisant la configuration suivante, nous pouvons enregistrer l’indicateur de santé avec le module Actuator de Spring Boot. Consultez toutes les propriétés prises en charge sur GitHub.

```properties
management.endpoints.web.exposure.include=*  # Pour exposer tous les points de terminaison
management.endpoint.health.show-details=always  # Pour afficher tous les détails dans le point de terminaison /health

management.health.ratelimiters.enabled=true  # Pour activer la vérification de santé des RateLimiters

resilience4j.ratelimiter.metrics.enabled=true  # Pour activer les métriques du rate limiter
resilience4j.bulkhead.metrics.enabled=true  # Pour activer les métriques du bulkhead

resilience4j.ratelimiter.instances.rateLimitingAPI.registerHealthIndicator=true
resilience4j.ratelimiter.instances.courseBulkheadApi.registerHealthIndicator=true
```

6. Démonstration

Pour tester les configurations de limitation de débit et de cloisonnement, nous utilisons Apache JMeter. Si vous ne l’avez pas déjà fait, il est recommandé de consulter le guide de démarrage pour JMeter.

6.1. Tester la Limitation de Débit

Étant donné que nous avons défini la limitation de débit dans notre application pour autoriser 5 appels toutes les 60 secondes, nous testerons notre application en envoyant 7 appels à l’API.

La configuration du groupe de threads ressemblera à ceci :

Nous appellerons le point de terminaison student avec 7 utilisateurs, chacun avec un délai de 1 seconde, indiqué par la période de montée en charge.

Comme prévu, nous devrions voir les 5 premiers appels réussir et les suivants échouer.

6.2. Tester le Cloisonnement

Testons l’API course avec 10 threads avec une période de montée en charge de 2 secondes.

Comme nous avons un appel Thread.sleep() dans l’API course pour simuler une API intensive en temps, le test de l’application produit les résultats attendus, et la 11e requête échoue.
