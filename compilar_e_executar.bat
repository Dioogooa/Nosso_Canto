@echo off
chcp 65001 > nul
title Nosso Canto - Compilador

echo ==========================================
echo    COMPILANDO NOSSO CANTO COM MYSQL
echo ==========================================

echo.
echo 📁 Verificando estrutura de pastas...

if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo ❌ ERRO: mysql-connector-j-8.0.33.jar nao encontrado na pasta lib!
    echo 📍 Verifique se o JAR do MySQL esta em: lib\mysql-connector-j-8.0.33.jar
    pause
    exit /b 1
)

if not exist "src\utils\DatabaseConnection.java" (
    echo ❌ ERRO: Pasta utils nao encontrada!
    echo 📍 Verifique se criou a pasta utils com DatabaseConnection.java
    pause
    exit /b 1
)

if not exist "src\dao\UsuarioDAO.java" (
    echo ❌ ERRO: Pasta dao nao encontrada!
    echo 📍 Verifique se criou a pasta dao com os arquivos DAO
    pause
    exit /b 1
)

echo ✅ Estrutura OK!

echo.
echo 🔨 Criando pasta bin...
if not exist bin mkdir bin

echo.
echo ⚡ Compilando classes...

echo - Entities...
javac -cp ".;lib\mysql-connector-j-8.0.33.jar" -d bin src\entities\*.java
if %errorlevel% neq 0 goto erro

echo - Utils... 
javac -cp ".;lib\mysql-connector-j-8.0.33.jar;bin" -d bin src\utils\*.java
if %errorlevel% neq 0 goto erro

echo - DAOs...
javac -cp ".;lib\mysql-connector-j-8.0.33.jar;bin" -d bin src\dao\*.java
if %errorlevel% neq 0 goto erro

echo - Services...
javac -cp ".;lib\mysql-connector-j-8.0.33.jar;bin" -d bin src\services\*.java
if %errorlevel% neq 0 goto erro

echo - Main...
javac -cp ".;lib\mysql-connector-j-8.0.33.jar;bin" -d bin src\Main.java
if %errorlevel% neq 0 goto erro

echo.
echo 🚀 Executando programa...
echo ==========================================
java -cp ".;bin;lib\mysql-connector-j-8.0.33.jar" Main

echo.
echo 💤 Programa finalizado.
pause
exit /b 0

:erro
echo.
echo ❌ ERRO NA COMPILACAO!
echo.
echo 🔧 Solucoes possiveis:
echo - Verifique se todos os arquivos .java estao nas pastas corretas
echo - Confirme os nomes dos pacotes (entities, services, utils, dao)
echo - MySQL rodando? Senha correta no DatabaseConnection.java?
echo - Verifique se o nome da pasta e entities (com i) e nao entites (com e)
echo.
pause
exit /b 1