@echo off
chcp 65001 > nul
title Nosso Canto - Compilador

echo ==========================================
echo    COMPILANDO NOSSO CANTO COM MYSQL
echo ==========================================

echo.
echo 📁 Estrutura detectada: NossoCanto/src/src/

echo.
echo 🔨 Criando pasta bin...
if not exist bin mkdir bin

echo.
echo ⚡ Compilando classes...

echo - Entites...
javac -cp ".;lib/mysql-connector-j-8.0.33.jar" -d bin src/entites/*.java
if %errorlevel% neq 0 goto erro

echo - Utils...
javac -cp ".;lib/mysql-connector-j-8.0.33.jar;bin" -d bin src/utils/*.java
if %errorlevel% neq 0 goto erro

echo - DAOs...
javac -cp ".;lib/mysql-connector-j-8.0.33.jar;bin" -d bin src/dao/*.java
if %errorlevel% neq 0 goto erro

echo - Services...
javac -cp ".;lib/mysql-connector-j-8.0.33.jar;bin" -d bin src/services/*.java
if %errorlevel% neq 0 goto erro

echo - GUI...
javac -cp ".;lib/mysql-connector-j-8.0.33.jar;bin" -d bin src/gui/*.java
if %errorlevel% neq 0 goto erro

echo - Main...
javac -cp ".;lib/mysql-connector-j-8.0.33.jar;bin" -d bin src/Main.java
if %errorlevel% neq 0 goto erro

echo.
echo 🚀 Executando programa...
echo ==========================================
java -cp ".;bin;lib/mysql-connector-j-8.0.33.jar" Main

echo.
echo 💤 Programa finalizado.
pause
exit /b 0

:erro
echo.
echo ❌ ERRO NA COMPILACAO!
echo.
echo 📍 Verifique se:
echo - O JAR esta em: lib/mysql-connector-j-8.0.33.jar
echo - DatabaseConnection.java esta em: src/utils/
echo - UsuarioDAO.java esta em: src/dao/
echo - Main.java esta em: src/
echo.
pause
exit /b 1