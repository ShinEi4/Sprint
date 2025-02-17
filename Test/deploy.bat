@echo off
setlocal enabledelayedexpansion

REM Configurations
set PROJECT_NAME=TestFramework
set WILDFLY_PATH=E:\S5\wildfly-26.1.2.Final\wildfly-26.1.2.Final\standalone\deployments
set LIB_PATH=lib
set SRC_PATH=src
set VIEWS_PATH=views

REM 1. Supprimer les anciennes versions
echo Suppression de l'ancienne version de %PROJECT_NAME%...
del /Q "%WILDFLY_PATH%\%PROJECT_NAME%.war"

REM 2. Créer les dossiers nécessaires pour le projet
echo Création de la structure du projet...
mkdir "%PROJECT_NAME%\WEB-INF"
mkdir "%PROJECT_NAME%\WEB-INF\classes"
mkdir "%PROJECT_NAME%\WEB-INF\lib"

REM 3. Copier le Framework.jar dans le dossier lib du test
echo Copie du Framework.jar dans le dossier lib...
copy "..\Framework\Framework.jar" "%PROJECT_NAME%\WEB-INF\lib\"

REM 4. Compiler les fichiers Java du test
echo Compilation des fichiers Java...
set "javaFiles="
for /r "%SRC_PATH%" %%G in (*.java) do (
    echo %%G
    set "javaFiles=!javaFiles! "%%G""
)
set "classpath=%PROJECT_NAME%\WEB-INF\lib\Framework.jar"
for %%I in ("%LIB_PATH%\*.jar") do (
    set "classpath=!classpath!;"%%I""
)
javac -cp "%classpath%" -d "%PROJECT_NAME%\WEB-INF\classes" !javaFiles!

REM 5. Copier le web.xml
echo Copie du fichier web.xml...
copy "web.xml" "%PROJECT_NAME%\WEB-INF\"

REM 6. Copier les vues JSP
echo Copie des fichiers JSP...
xcopy /E /I /Y "%VIEWS_PATH%\*.*" "%PROJECT_NAME%\"

REM 7. Créer le fichier WAR
echo Création du fichier WAR...
cd "%PROJECT_NAME%"
jar -cvf "%PROJECT_NAME%.war" *
cd ..

REM 8. Déployer dans Wildfly
echo Déploiement vers Wildfly...
copy "%PROJECT_NAME%\%PROJECT_NAME%.war" "%WILDFLY_PATH%"

echo Déploiement terminé !
pause
endlocal 