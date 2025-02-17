# Framework MVC

Un framework MVC léger inspiré de Spring MVC.

## Configuration

### 1. Structure du Projet
Votre projet doit suivre cette structure :
MonProjet/
├── WEB-INF/
│ ├── classes/
│ │ └── [vos classes compilées]
│ ├── lib/
│ │ └── Framework.jar
│ └── web.xml

### 2. Configuration dans web.xml
xml
<web-app>
<servlet>
<servlet-name>FrontController</servlet-name>
<servlet-class>ituprom16.framework.servlet.FrontController</servlet-class>
<init-param>
<param-name>controllerPackage</param-name>
<param-value>com.monprojet.controllers</param-value>
</init-param>
</servlet>
<servlet-mapping>
<servlet-name>FrontController</servlet-name>
<url-pattern>/</url-pattern>
</servlet-mapping>
</web-app>

### 3. Création des Contrôleurs
java
import ituprom16.framework.annotation.AnnotationController;
@AnnotationController
public class MonController {
// Méthodes du contrôleur
}


### 4. Déploiement
1. Placez Framework.jar dans WEB-INF/lib
2. Compilez vos contrôleurs
3. Déployez sur votre serveur d'application
