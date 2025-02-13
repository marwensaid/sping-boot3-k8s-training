# TP : Installation de Kubernetes en local avec Kind et déploiement de Grafana

## Objectifs
L'objectif de ce TP est de :
- Installer Kubernetes en local avec Kind.
- Déployer Grafana dans le cluster Kubernetes.
- Déployer une application générant des logs.
- Configurer Prometheus pour collecter ces logs.
- Visualiser les logs dans Grafana.

## Prérequis
Avant de commencer, assurez-vous d'avoir installé :
- **Docker** (https://docs.docker.com/get-docker/)
- **kubectl** (https://kubernetes.io/docs/tasks/tools/)
- **Kind** (https://kind.sigs.k8s.io/docs/user/quick-start/)
- **Helm** (https://helm.sh/docs/intro/install/)

## 1. Installation et configuration de Kind

### 1.1 Vérification de l'installation de Kind
Exécutez la commande suivante pour vérifier que Kind est bien installé :
```sh
kind version
```

Si la commande retourne une version, Kind est bien installé.

### 1.2 Création d'un cluster Kubernetes avec Kind
Créez un fichier de configuration `kind-config.yaml` :
```yaml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
  - role: control-plane
  - role: worker
  - role: worker
```

Créez ensuite le cluster avec la commande :
```sh
kind create cluster --name my-cluster --config kind-config.yaml
```

Vérifiez que le cluster fonctionne :
```sh
kubectl cluster-info --context kind-my-cluster
```

## 2. Déploiement de Grafana et Prometheus dans Kubernetes

### 2.1 Création d'un Namespace
Créons un namespace pour organiser notre déploiement :
```sh
kubectl create namespace monitoring
```

### 2.2 Déploiement de Prometheus et Grafana avec Helm
Ajoutez le repository Helm de Prometheus et Grafana :
```sh
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
```

Déployez Prometheus et Grafana dans Kubernetes :
```sh
helm install prometheus prometheus-community/kube-prometheus-stack --namespace monitoring
helm install my-grafana grafana/grafana --namespace monitoring
```

### 2.3 Vérification du déploiement
Vérifiez que les pods sont bien en cours d'exécution :
```sh
kubectl get pods -n monitoring
```

## 3. Déploiement d'une application générant des logs

### 3.1 Déploiement de l'application
Créons un fichier de déploiement `app-deployment.yaml` :
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: log-app
  namespace: monitoring
spec:
  replicas: 1
  selector:
    matchLabels:
      app: log-app
  template:
    metadata:
      labels:
        app: log-app
    spec:
      containers:
      - name: log-app
        image: busybox
        command: ["sh", "-c", "while true; do echo $(date) - Log de test; sleep 2; done"]
```

Appliquez ce fichier :
```sh
kubectl apply -f app-deployment.yaml
```

### 3.2 Exposition des logs avec Prometheus
Créez un `ServiceMonitor` pour récupérer les logs :
```yaml
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: log-app-monitor
  namespace: monitoring
spec:
  selector:
    matchLabels:
      app: log-app
  endpoints:
  - port: http
```
Appliquez ce fichier :
```sh
kubectl apply -f service-monitor.yaml
```

## 4. Accès à l'interface Grafana

### 4.1 Récupération du mot de passe administrateur
Obtenez le mot de passe de l'administrateur Grafana :
```sh
kubectl get secret --namespace monitoring my-grafana -o jsonpath="{.data.admin-password}" | base64 --decode
```

### 4.2 Exposition du service Grafana
Exposez Grafana en local avec un port-forwarding :
```sh
kubectl port-forward svc/my-grafana 3000:80 -n monitoring
```

Ouvrez un navigateur et accédez à Grafana à l'URL :
```
http://localhost:3000
```
Utilisez `admin` comme identifiant et le mot de passe récupéré précédemment.

### 4.3 Ajout de Prometheus comme source de données dans Grafana
- Allez dans **Configuration > Data Sources**.
- Cliquez sur **Add data source** et choisissez **Prometheus**.
- Entrez l'URL `http://prometheus-server.monitoring.svc.cluster.local`.
- Cliquez sur **Save & Test**.

### 4.4 Visualisation des logs dans Grafana
- Allez dans **Explore**.
- Sélectionnez **Prometheus** comme source de données.
- Recherchez `log_app_log_total` pour voir les logs de l'application.

## 5. Nettoyage du cluster
Une fois le TP terminé, vous pouvez supprimer le cluster avec :
```sh
kind delete cluster --name my-cluster
```


