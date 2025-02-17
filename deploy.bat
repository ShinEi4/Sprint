@echo off
setlocal enabledelayedexpansion

REM Configurations
set PROJECT_NAME=Framework
set TOMCAT_PATH=E:\S5\wildfly-26.1.2.Final\wildfly-26.1.2.Final\standalone\deployments
set LIB_PATH=Framework/lib
set SRC_PATH=Framework/src

REM 1. Supprimer les anciennes versions
echo Suppression de l'ancienne version de %PROJECT_NAME% dans webapps...
rmdir /S /Q "%TOMCAT_PATH%\%PROJECT_NAME%"

REM 2. Créer les dossiers nécessaires pour le projet
echo Création de la structure du projet...
mkdir "%PROJECT_NAME%\WEB-INF"
mkdir "%PROJECT_NAME%\WEB-INF\classes"
mkdir "%PROJECT_NAME%\WEB-INF\lib"

REM 3. Copier les fichiers JSP et assets
echo Copie des fichiers JSP et des assets...
xcopy "views\*.jsp" "%PROJECT_NAME%" /S /I
xcopy "assets" "%PROJECT_NAME%\assets" /S /I

REM 4. Compiler java
set "javaFiles="
echo Fichiers Java à compiler :
for /r "%SRC_PATH%" %%G in (*.java) do (
    echo %%G
    rem Ajouter le fichier Java à la liste des fichiers à compiler
    set "javaFiles=!javaFiles! "%%G""
)
set "classpath="
for %%I in ("%LIB_PATH%\*.jar") do (
    set "classpath=!classpath!;"%%I""
)
javac -cp "%classpath%" -d "%PROJECT_NAME%\WEB-INF\classes" !javaFiles!
pause

REM 6. Copier les bibliothèques dans WEB-INF/lib
echo Copie des fichiers de bibliothèque...
xcopy "%LIB_PATH%\*.jar" "%PROJECT_NAME%\WEB-INF\lib\" /Y

REM 7. Copier le fichier web.xml
echo Copie du fichier web.xml dans WEB-INF...
copy "web.xml" "%PROJECT_NAME%\WEB-INF\"

REM 8. Créer le fichier WAR
echo Création du fichier WAR...
cd "%PROJECT_NAME%"
jar -cvf "%PROJECT_NAME%.war" *
cd ..

REM 9. Copier le fichier WAR dans le répertoire webapps de Tomcat
echo Déploiement de %PROJECT_NAME%.war vers Tomcat...
xcopy "%PROJECT_NAME%\%PROJECT_NAME%.war" "%TOMCAT_PATH%" /Y

echo Déploiement terminé ! Démarrez Tomcat pour accéder à l'application.
endlocal
