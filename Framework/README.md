# Framework MVC

Un framework MVC léger inspiré de Spring MVC.

## Fonctionnalités

- Annotation `@AnnotationController` pour marquer les classes contrôleur
- FrontController qui scanne automatiquement les contrôleurs
- Configuration simple via web.xml

## Configuration

1. Ajoutez le Framework.jar à votre projet
2. Configurez le web.xml avec le package de vos contrôleurs :
xml
<init-param>
<param-name>controllerPackage</param-name>
<param-value>votre.package.controller</param-value>
</init-param>
3. Annotez vos contrôleurs avec `@AnnotationController`

## Structure
- `ituprom16.framework.annotation` : Annotations du framework
- `ituprom16.framework.servlet` : Servlets du framework

## Utilisation
1. Créez vos contrôleurs dans le package spécifié
2. Annotez-les avec `@AnnotationController`
3. Le FrontController les détectera automatiquement