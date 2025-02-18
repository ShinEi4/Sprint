@echo off
setlocal enabledelayedexpansion

REM Configurations
set PROJECT_NAME=Framework
set WILDFLY_PATH=E:\S5\wildfly-26.1.2.Final\wildfly-26.1.2.Final\standalone\deployments
set LIB_PATH=Framework/lib
set SRC_PATH=Framework/src

REM 1. Supprimer les anciennes versions
echo Suppression de l'ancienne version de %PROJECT_NAME%...
del /Q "%WILDFLY_PATH%\%PROJECT_NAME%.war"
del /Q "Framework\%PROJECT_NAME%.jar"

REM 2. Créer les dossiers nécessaires pour le projet
echo Création de la structure du projet...
mkdir "%PROJECT_NAME%\WEB-INF"
mkdir "%PROJECT_NAME%\WEB-INF\classes"
mkdir "%PROJECT_NAME%\WEB-INF\lib"

REM 3. Compiler les fichiers Java
echo Compilation des fichiers Java...
set "javaFiles="
for /r "%SRC_PATH%" %%G in (*.java) do (
    echo %%G
    set "javaFiles=!javaFiles! "%%G""
)
set "classpath="
for %%I in ("%LIB_PATH%\*.jar") do (
    set "classpath=!classpath!;"%%I""
)
javac -cp "%classpath%" -d "%PROJECT_NAME%\WEB-INF\classes" !javaFiles!

REM 4. Créer le fichier JAR du Framework
echo Création du fichier JAR du Framework...
cd "%PROJECT_NAME%\WEB-INF\classes"
jar -cvf "../../Framework.jar" *
cd ../../..

REM 5. Copier le JAR dans le dossier Framework
echo Copie du JAR dans le dossier Framework...
copy "%PROJECT_NAME%\Framework.jar" "Framework\"

REM 6. Copier les bibliothèques dans WEB-INF/lib
echo Copie des fichiers de bibliothèque...
xcopy "%LIB_PATH%\*.jar" "%PROJECT_NAME%\WEB-INF\lib\" /Y

REM 7. Copier le fichier web.xml
echo Copie du fichier web.xml dans WEB-INF...
copy "web.xml" "%PROJECT_NAME%\WEB-INF\"

REM 9. Copier les fichiers de lib dans WEB-INF/lib
echo Copie des fichiers de bibliothèque externes...
xcopy "%LIB_PATH%\*.jar" "%PROJECT_NAME%\WEB-INF\lib\" /Y

REM 8. Créer le fichier WAR
echo Création du fichier WAR...
cd "%PROJECT_NAME%"
jar -cvf "%PROJECT_NAME%.war" *
cd ..

REM 10. Déployer dans Wildfly
echo Déploiement vers Wildfly...
copy "%PROJECT_NAME%\%PROJECT_NAME%.war" "%WILDFLY_PATH%"



echo Déploiement terminé !
pause
endlocal
