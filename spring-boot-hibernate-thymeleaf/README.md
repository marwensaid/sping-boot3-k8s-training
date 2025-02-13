TP : Création d’une Application CRUD avec Spring Boot, Thymeleaf et Hibernate

Objectif

L’objectif de ce TP est de guider les étudiants dans le développement d’une application web Spring Boot prenant en charge les opérations CRUD (Créer, Lire, Mettre à jour, Supprimer) via une interface utilisateur basée sur des formulaires, construite avec Thymeleaf et le support de Spring MVC.


Étape 1 : Vue d’Ensemble

Dans ce TP, nous allons créer une application web avec deux vues principales :
1.	Vue Liste des Employés : Cette vue affiche tous les employés de la base de données sous forme de tableau. De plus, il y a des liens pour mettre à jour ou supprimer un employé. Cette page comprend également une option pour naviguer vers l’écran de création d’un employé.
2.	Vue Créer/Mettre à Jour un Employé : Cette vue est utilisée pour ajouter un nouvel employé ou modifier les détails d’un employé existant.

Il y a deux composants principaux dans cet exemple sur lesquels se concentrer : le contrôleur MVC et les vues de l’interface utilisateur.

Étape 2 : Contrôleur Spring MVC

La classe contrôleur contient les mappages d’URL et leurs méthodes gestionnaires. Il existe des méthodes gestionnaires pour toutes les opérations CRUD, y compris une opération POST pour gérer la soumission de formulaires afin de créer ou mettre à jour un employé.

Notez comment les méthodes gestionnaires lient les données du modèle à la vue et renvoient les noms des vues sous forme de chaînes, qui sont résolues par le résolveur de vues en fichiers HTML.


Explication des méthodes :
•	getAllEmployees() : Renvoie une liste de tous les employés et est mappée au chemin “/”. C’est la vue par défaut de l’application.
•	editEmployeeById() : Utilisée pour ajouter un nouvel employé ou modifier un employé existant. Elle utilise la même vue HTML pour les deux opérations. S’il y a un ID d’employé dans le contexte, cet employé est modifié ; sinon, un nouvel employé est créé.
•	deleteEmployeeById() : Une simple requête URL pour supprimer un employé par son ID.
•	createOrUpdateEmployee() : Cette méthode gère les requêtes HTTP POST utilisées pour créer un nouvel employé ou mettre à jour un employé. Les opérations de création ou de mise à jour dépendent de la présence de l’ID de l’employé dans le modèle.

Étape 3 : Modèle de Données (Entité Employé)
Nous devons définir une entité JPA pour représenter la table des employés dans la base de données.


Étape 4 : Référentiel (Repository)
Créez une interface référentiel pour gérer les opérations de base de données sur l’entité Employé.


Étape 5 : Service
Le service contient la logique métier et interagit avec le référentiel pour effectuer les opérations CRUD.


Étape 6 : Vue Thymeleaf
Nous devons maintenant créer les fichiers de vue Thymeleaf pour afficher la liste des employés et un formulaire pour ajouter/modifier un employé.

6.1 Liste des employés (list-employees.html)
Ce fichier affichera la liste des employés et proposera des options pour modifier ou supprimer chaque employé.

6.2 Formulaire d’ajout/modification (add-edit-employee.html)
Ce fichier permet de créer un nouvel employé ou de modifier un employé existant.


Étape 7 : Configuration de la Base de Données
Dans application.properties, configurez la connexion à une base de données H2 (mémoire) pour simplifier le TP.


Cette configuration permet d’utiliser une base H2 accessible depuis la console sur http://localhost:8080/h2-console.

Étape 8 : Exécution et Test de l’Application
8.1 Démarrer l’application
Exécutez la classe SpringBootCrudApplication qui contient l’annotation @SpringBootApplication :


8.2 Tester l’application
1.	Accédez à l’application : Ouvrez un navigateur et rendez-vous sur http://localhost:8080/.
2.	Ajoutez un employé en cliquant sur “Ajouter un Employé”.
3.	Modifiez un employé en cliquant sur “Modifier”.
4.	Supprimez un employé en cliquant sur “Supprimer”.


Étape 9 : Améliorations Possible
Quelques pistes pour approfondir le TP :
•	Utilisation d’une base de données MySQL au lieu de H2.
•	Ajout de validations avec @Valid et @NotNull pour les champs de formulaire.
•	Intégration de Spring Security pour gérer l’accès à l’application.
•	Ajout de tests unitaires et tests d’intégration.
