# Framework MVC Java

Un framework MVC léger et flexible inspiré de Spring MVC, développé pour Wildfly.

## 🚀 Fonctionnalités

- Routage MVC avec annotations
- Gestion des sessions
- Validation des formulaires
- Support REST API
- Système d'authentification et de rôles
- Upload de fichiers
- Injection de dépendances

## 📋 Prérequis

- Java 17
- Serveur Wildfly 26.1.2
- Pas de Maven requis (dépendances incluses)

## 🛠️ Installation

1. Clonez le projet
2. Placez le dossier `Framework/` dans votre projet
3. Configurez `web.xml` :

    ```xml
<servlet>
<servlet-name>FrontController</servlet-name>
<servlet-class>ituprom16.framework.servlet.FrontController</servlet-class>
<init-param>
<param-name>controllerPackage</param-name>
<param-value>votre.package.controllers</param-value>
</init-param>
</servlet>


## 📖 Guide d'utilisation

### 1. Création d'un contrôleur

    ```java
@AnnotationController
public class TestController {
@GET("/hello")
public String hello() {
return "Hello World!";
}
@GET("/users")
public ModelView getUsers() {
ModelView mv = new ModelView("/users.jsp");
mv.addObject("users", userList);
return mv;
}
}
```


### 2. Gestion des formulaires
```java
@POST("/save")
public ModelView saveUser(
@Param(name="username") String username,
@Param(name="email") @Email String email
) {
// Traitement...
}
```


### 3. Authentification et Rôles
```java
@AnnotationController
@Auth // Requiert une authentification
@Role({"ADMIN"}) // Requiert le rôle ADMIN
public class AdminController {
@GET("/users")
public ModelView getAllUsers() {
// Accessible uniquement aux admins
}
@GET("/public")
@Auth(required = false) // Désactive l'authentification pour cette méthode
public ModelView getPublicInfo() {
// Accessible à tous
}
@GET("/managers")
@Role({"MANAGER"}) // Surcharge le rôle de la classe
public ModelView getManagerInfo() {
// Accessible aux managers
}
}
```


### 4. Validation des données
```java
public class User {
@Required
private String username;
@Email
private String email;
@Min(18) @Max(100)
private int age;
}
```


### 5. API REST
```java
@AnnotationController
public class ApiController {
@GET("/api/data")
@RestAPI
public List<String> getData() {
// Retourne automatiquement du JSON
return dataList;
}
}
```


## 🔧 Configuration

### Structure recommandée du projet
```
YourProject/
├── Framework/
│ └── src/
│ └── ituprom16/
│ └── framework/
├── Test/
│ ├── src/
│ │ └── your/
│ │ └── controllers/
│ └── views/
└── web.xml
```

### Déploiement

Utilisez le script `deploy.bat` fourni pour compiler et déployer automatiquement vers Wildfly.

## 🔒 Sécurité

Le framework inclut :
- Protection CSRF
- Validation des entrées
- Gestion sécurisée des sessions
- Système de rôles et permissions

## 📝 Bonnes pratiques

1. Organisez vos contrôleurs par domaine fonctionnel
2. Utilisez ModelView pour passer des données aux vues
3. Validez toujours les entrées utilisateur
4. Gérez correctement les sessions et l'authentification
5. Documentez vos APIs

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :
- Signaler des bugs
- Proposer des améliorations
- Soumettre des pull requests