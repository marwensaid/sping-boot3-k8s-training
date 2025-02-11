
TP : Déploiement d’une Application Spring Boot sur Pivotal Cloud Foundry

Objectif

L’objectif de ce TP est de guider les étudiants dans le déploiement d’une application Spring Boot sur la plateforme Pivotal Cloud Foundry (PCF). Les étudiants apprendront à configurer leur environnement, à créer une application Spring Boot simple, et à la déployer sur PCF en utilisant l’interface en ligne de commande (CLI) de Cloud Foundry.

Prérequis

Avant de commencer, assurez-vous que les éléments suivants sont en place :
•	Connaissances en Java et Spring Boot : Les étudiants doivent être familiers avec le développement d’applications Spring Boot.
•	Environnement de développement : Un environnement configuré avec Java et Maven pour gérer les dépendances du projet.
•	Compte Pivotal Web Services (PWS) : Les étudiants doivent créer un compte sur Pivotal Web Services pour déployer leurs applications.

Étape 1 : Introduction à Cloud Foundry

Cloud Foundry est une plateforme en tant que service (PaaS) open-source qui offre un choix de clouds, de frameworks de développement et de services applicatifs. Initialement développée par VMware, elle est actuellement gérée par Pivotal, une coentreprise de GE, EMC et VMware. Plusieurs fournisseurs proposent des distributions certifiées de Cloud Foundry, notamment Pivotal Cloud Foundry, IBM Bluemix, SAP Cloud Platform, et d’autres.

Étape 2 : Installation de Cloud Foundry CLI sur Windows

Pour interagir avec Cloud Foundry depuis votre poste de travail, vous devez installer l’interface en ligne de commande (CLI) de Cloud Foundry.
1.	Téléchargement : Téléchargez l’installateur Windows de la CLI Cloud Foundry depuis le site officiel.
2.	Installation : Décompressez le fichier téléchargé et exécutez l’installateur. Suivez les instructions à l’écran en acceptant les valeurs par défaut.
3.	Vérification : Après l’installation, ouvrez une fenêtre de terminal et tapez cf. Si l’installation a réussi, une liste d’aide des commandes cf s’affichera, indiquant que vous êtes prêt à utiliser Cloud Foundry depuis votre poste de travail.

Étape 3 : Configuration du Compte PWS
1.	Inscription : Créez un compte sur Pivotal Web Services en vous inscrivant sur la page dédiée. L’inscription est gratuite et nécessite des informations de base telles que votre adresse e-mail et votre nom.
2.	Connexion : Une fois inscrit, connectez-vous à la console PWS. Après une connexion réussie, vous accéderez à la console Cloud Foundry où vous pourrez gérer vos applications, surveiller leur état, et effectuer diverses autres tâches.

Étape 4 : Connexion et Déconnexion de la Console PWS via la CLI
1.	Connexion : Pour vous connecter à PWS via la CLI, ouvrez une fenêtre de terminal et exécutez la commande suivante :

cf login -a api.run.pivotal.io

Cette commande vous demandera votre adresse e-mail et votre mot de passe enregistrés. Après une authentification réussie, vous serez connecté à la plateforme PWS via la CLI.

	2.	Déconnexion : Une fois vos tâches terminées, vous pouvez vous déconnecter en exécutant la commande suivante :

cf logout



Étape 5 : Création d’une Application Spring Boot

Nous allons créer une application Spring Boot simple qui expose un point de terminaison REST.
1.	Pile Technologique :
•	Spring Boot
•	Spring REST
•	Maven
•	Eclipse ou tout autre IDE Java
•	Cloud Foundry CLI
•	Navigateur Web
2.	Génération de l’Application :
•	Utilisez le site Spring Initializr pour générer un nouveau projet Spring Boot. Sélectionnez les dépendances nécessaires, telles que Spring Web. Téléchargez le projet généré et importez-le dans votre IDE.
3.	Ajout d’un Contrôleur REST :
•	Dans la classe principale de l’application (SpringHelloworldCfApplication.java), ajoutez un contrôleur REST qui répondra à une requête HTTP GET.

```java
package com.example.howtodoinjava.springhelloworldcf;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringHelloworldCfApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHelloworldCfApplication.class, args);
    }
}

@RestController
class MessageRestController {

    @GetMapping("/hello")
    public String getMessage(@RequestParam(value = "name") String name) {
        String response = "Hi " + name + " : responded on - " + new Date();
        System.out.println(response);
        return response;
    }
}
```


	4.	Configuration du Projet :
	•	Dans le fichier application.properties situé dans le répertoire src/main/resources, ajoutez les propriétés suivantes :
```properties
server.servlet.context-path=/hello
management.security.enabled=false
```

Ces propriétés définissent le chemin de contexte de l’application et désactivent la sécurité pour les points de terminaison de gestion de Spring Boot.

	5.	Test Local :
	•	Exécutez l’application localement en tant qu’application Spring Boot.
	•	Ouvrez un navigateur et accédez à l’URL suivante : http://localhost:8080/hello?name=howtodoinjava.


Étape 6 : Déploiement de l’Application sur Pivotal Cloud Foundry
1.	Préparation du Fichier manifest.yml :
Pour faciliter le déploiement de l’application sur Cloud Foundry, créez un fichier nommé manifest.yml à la racine de votre projet Spring Boot. Ce fichier décrit les paramètres de déploiement de votre application.
Voici un exemple de contenu pour manifest.yml :

```yaml
---
applications:
- name: spring-helloworld-cf
  memory: 512M
  instances: 1
  path: target/spring-helloworld-cf-0.0.1-SNAPSHOT.jar
  buildpack: java_buildpack
```

Explication des paramètres :
•	name : Nom de l’application tel qu’il apparaîtra sur Cloud Foundry.
•	memory : Quantité de mémoire allouée à l’application.
•	instances : Nombre d’instances de l’application à exécuter.
•	path : Chemin vers le fichier JAR de l’application.
•	buildpack : Buildpack à utiliser pour déployer l’application.

	2.	Construction du Fichier JAR :
Avant de déployer l’application, construisez le fichier JAR en exécutant la commande suivante dans le répertoire racine de votre projet :

```mvn clean package```

Cette commande génère le fichier JAR dans le répertoire target.

	3.	Déploiement de l’Application :
Une fois le fichier JAR généré et le fichier manifest.yml configuré, déployez l’application sur Pivotal Cloud Foundry en exécutant la commande suivante :

```cf push```

La commande cf push lit le fichier manifest.yml et déploie l’application en conséquence.

	4.	Vérification du Déploiement :
Après un déploiement réussi, vous pouvez vérifier l’état de votre application en exécutant :

```cf apps```

Cette commande affiche la liste des applications déployées, leur état, et les URL associées.

	5.	Accès à l’Application Déployée :
Ouvrez un navigateur web et accédez à l’URL fournie pour votre application. Ajoutez le chemin /hello?name=VotreNom à l’URL pour interagir avec le point de terminaison REST que vous avez défini.
Par exemple : https://spring-helloworld-cf.cfapps.io/hello?name=Etudiant
Vous devriez voir une réponse similaire à : Hi Etudiant : responded on - [date et heure].
6.	Gestion de l’Application :
Vous pouvez gérer votre application (par exemple, la redémarrer, afficher les journaux, etc.) en utilisant les commandes cf appropriées.
•	Pour afficher les journaux en temps réel :

```cf logs spring-helloworld-cf```


	•	Pour redémarrer l’application :

```cf restart spring-helloworld-cf```


	•	Pour arrêter l’application :

```cf stop spring-helloworld-cf```


	•	Pour supprimer l’application :

```cf delete spring-helloworld-cf```
