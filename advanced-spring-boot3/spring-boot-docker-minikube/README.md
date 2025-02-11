Déploiement d’une application Spring Boot sur Kubernetes (Minikube)

Déployer une application Spring Boot sur Kubernetes peut sembler intimidant au début, mais avec les bons outils et
connaissances, cela peut être un processus simple. Dans cet article, nous allons parcourir les étapes nécessaires pour
déployer une application Spring Boot dockerisée sur Kubernetes en utilisant Minikube.

1. Prérequis

Avant de commencer, assurez-vous d’avoir les éléments suivants :
• Un cluster Kubernetes opérationnel.
• Docker installé.
• kubectl installé.

2. Introduction à Kubernetes

Kubernetes est une plateforme open-source, portable et extensible pour la gestion des charges de travail et des services
déployés dans des conteneurs. Kubernetes facilite à la fois la configuration déclarative et l’automatisation. La
plateforme Kubernetes dispose d’un écosystème riche et en croissance rapide, et son support, ses services et ses outils
sont largement disponibles.

Kubernetes est basé sur une décennie et demie d’expérience de Google dans l’exécution de charges de travail en
production à grande échelle, ainsi que sur les meilleures idées et pratiques suggérées par la communauté.

Les composants clés de Kubernetes sont :
• Nœuds (Nodes) : Les nœuds sont responsables de l’exécution des conteneurs, de la communication avec le maître et du
rapport de l’état de leurs tâches assignées.
• Pods : Les pods sont les plus petits objets dans un cluster Kubernetes situés sur un nœud.
• ReplicaSets : Permet de faire évoluer le nombre de réplicas en fonction de la demande.
• Services : Un service est un objet utilisé pour diriger les requêtes ou le trafic vers plusieurs pods en utilisant une
adresse IP.
• Cluster : Un cluster Kubernetes est une sorte d’hôte pour vos conteneurs en cours d’exécution. Chaque cluster de nœuds
aura au moins un nœud maître et des nœuds de travail ; ce nœud maître est responsable de maintenir l’état global du
cluster en fonction de la configuration, et le nœud de travail est celui qui exécute les applications à l’intérieur d’un
conteneur.

3. Initialiser une application Spring Boot

Pour générer rapidement un projet simple, utilisez Spring Initializr.

Ensuite, créez un contrôleur pour vérifier que tout fonctionne correctement lors du déploiement du projet :

```java

@RestController
public class FooController {

    @RequestMapping(value = "/controller", method = RequestMethod.GET)
    public String foo() {
        return "Réponse!";
    }

}
```

Dans cet exemple, nous créons un endpoint appelé /controller. Si nous envoyons une requête à cet endpoint, nous devrions
obtenir le message “Réponse!”.

Testez votre application en créant un fichier JAR exécutable dans le dossier /target en utilisant la commande suivante :

```mvn clean install```

4. Dockeriser l’application Spring Boot

Pour containeriser l’application, créez un fichier Dockerfile avec les instructions suivantes :

```dockerfile
FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/spring-demo-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Ensuite, construisez l’image avec la commande :

```docker build -t springboot-app:latest .```

5. Déployer sur Minikube

5.1. Installation

Minikube offre un moyen simple de configurer un environnement Kubernetes local pour les tests et le développement. Vous
pouvez trouver les instructions d’installation pour votre système sur le site de Minikube.

Après avoir installé Minikube, vérifiez son statut avec la commande :

```minikube status```

Si Minikube n’est pas en cours d’exécution, démarrez-le avec :

```minikube start```

Après avoir démarré Minikube, configurez-le pour utiliser votre image locale en exécutant :

```eval $(minikube docker-env)```

5.2. Déploiement

Nous avons deux options pour déployer notre application : utiliser des commandes kubectl ou un fichier YAML avec toute
la configuration. Nous opterons pour un fichier YAML.

Dans le dossier racine de votre application, créez un fichier nommé deployment.yaml avec le contenu suivant :

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
name: spring-app
spec:
replicas: 2
selector:
matchLabels:
app: spring-app
template:
metadata:
labels:
app: spring-app
spec:
containers:
  - name: spring-app
image: springboot-app:latest
imagePullPolicy: IfNotPresent
ports:
  - containerPort: 8080
```

Ce fichier décrit un déploiement Kubernetes pour une application containerisée. Les sections principales incluent :
• apiVersion : La version de l’API Kubernetes utilisée par le déploiement.

• kind : Le type de ressource Kubernetes créée.

• metadata : Contient des métadonnées sur le déploiement, comme son nom.

• spec : L’état souhaité du déploiement.

• replicas : Le nombre de réplicas (instances) de l’application à exécuter.

• selector : Les labels utilisés pour identifier les pods appartenant au déploiement.

• template : Le modèle de pod utilisé pour créer de nouveaux pods pour le déploiement.

• metadata : Les labels utilisés pour identifier le pod.

• spec : La spécification du pod, y compris le(s) conteneur(s) à exécuter.

• containers : Le(s) conteneur(s) à exécuter dans le pod.

• name : Le nom du conteneur.

• image : Le nom de l’image Docker utilisée pour le conteneur.
• ports : Les ports à exposer pour le conteneur.

Assurez-vous d’ajouter imagePullPolicy: IfNotPresent. Cela indique à Minikube de rechercher d’abord l’image localement,
puis, si elle n’est pas présente, de la rechercher sur Docker Hub.

Appliquez le fichier de déploiement avec la commande suivante :

```kubectl apply -f deployment.yaml```

5.3. Vérification

Après avoir exécuté cette commande, vous pouvez vérifier le déploiement avec :

```kubectl get deployments```

Et vérifier les pods, puisque nous avons défini deux réplicas dans le fichier YAML, avec la commande :

```kubectl get pods```

Pour vérifier si le pod fonctionne correctement, exécutez la commande :

```kubectl logs <nom-du-pod>```

6. Services Kubernetes

Notre application s’exécute sur deux pods différents, ce qui signifie deux adresses IP distinctes. Si un utilisateur
souhaite accéder à notre application, quel pod répondra à la requête ?

La réponse à cette question réside dans les Services de Kubernetes. Les services nous permettent de répartir la charge
du trafic entre nos pods en fournissant une adresse IP unique pour un groupe de pods. Dans notre cas, nous avons deux
pods. En ajoutant un service, nous résolvons la question de savoir quel pod répondra lorsqu’un utilisateur enverra une
requête à notre application.

En Kubernetes, il existe quatre types de services :
• ClusterIP : Fournit une adresse IP stable accessible uniquement au sein du cluster.
• NodePort : Expose un port sur chaque nœud du cluster.
• LoadBalancer : Crée un équilibreur de charge chez le fournisseur de cloud pour distribuer le trafic vers les pods.
• ExternalName : Ce type de service mappe un nom DNS à un service externe.
• Headless : Permet un accès direct à chaque pod via un nom DNS.

Ici, nous utiliserons NodePort. Créez un autre fichier YAML avec le contenu suivant :

```yaml
apiVersion: v1
kind: Service
metadata:
name: spring-service
spec:
type: NodePort
selector:
app: spring-app
ports:
  - name: http
    port: 80
    targetPort: 8080
    nodePort: 30005
```

La seule différence par rapport au fichier de déploiement est que, au lieu de kind: Deployment, il s’agit de kind:
Service. Pour le nom, vous pouvez utiliser n’importe quel nom, mais dans ce cas, nous utiliserons spring-service.

	Assurez-vous que le nom du sélecteur app est identique à celui que nous avons défini dans le fichier de déploiement.

```spec.ports.nodePort``` : spécifie le port sur lequel le service sera exposé à l’extérieur. Dans notre cas, nous
utilisons le port 30005 (la plage de ports valides est 30000-32767).

Appliquez le service avec la commande :

```kubectl apply -f service.yaml```

Pour vérifier si notre service fonctionne, exécutez la commande :

```kubectl get svc```

7. Démonstration

Maintenant que notre service fonctionne, nous devons obtenir l’IP du nœud avec kubectl get nodes -o wide afin de pouvoir
accéder à notre application.

Avec l’IP et le port, nous pouvons accéder à notre application. Dans notre cas, l’IP est 192.168.49.2 et le port est
30005. L’URL finale est 192.168.49.2:30005/controller, où controller est le point de terminaison que nous avons créé
précédemment.

Félicitations, nous avons reçu notre réponse de Kubernetes.

8. Tableau de bord Minikube

Après avoir hébergé avec succès notre application dans le cluster Kubernetes, nous pouvons accéder au tableau de bord
Minikube pour vérifier les différents composants.

```minikube dashboard```

Le tableau de bord nous montre tous les composants que nous avons ; dans notre exemple, nous avons 1 déploiement et 2
pods, et nous pouvons ajouter ou supprimer ce que nous voulons. Fondamentalement, le tableau de bord est juste une
interface qui nous permet d’interagir avec et de gérer notre cluster Kubernetes.
