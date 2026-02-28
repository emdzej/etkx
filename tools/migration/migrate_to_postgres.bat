@echo off
REM Transbase to PostgreSQL Migration Script
REM
REM Prerequisites:
REM   1. PostgreSQL database must exist (CREATE DATABASE etk;)
REM   2. Network access to Transbase server (192.168.101.150:2024)
REM
REM Usage:
REM   migrate_to_postgres.bat [pg_host:port] [pg_database] [pg_user] [pg_password] [schema]
REM
REM Example:
REM   migrate_to_postgres.bat localhost:5432 etk postgres mypassword etk

setlocal

set PG_HOST=%1
set PG_DB=%2
set PG_USER=%3
set PG_PASS=%4
set PG_SCHEMA=%5

if "%PG_HOST%"=="" set PG_HOST=localhost:5432
if "%PG_DB%"=="" set PG_DB=etk
if "%PG_USER%"=="" set PG_USER=postgres
if "%PG_PASS%"=="" set PG_PASS=postgres
if "%PG_SCHEMA%"=="" set PG_SCHEMA=public

echo.
echo Transbase to PostgreSQL Migration
echo ==================================
echo.
echo Target: jdbc:postgresql://%PG_HOST%/%PG_DB%
echo Schema: %PG_SCHEMA%
echo User:   %PG_USER%
echo.

REM Check if PostgreSQL driver exists
if not exist postgresql.jar (
    echo ERROR: postgresql.jar not found!
    echo.
    echo Download from: https://jdbc.postgresql.org/download/
    echo Place postgresql-XX.X.X.jar as postgresql.jar in this directory.
    exit /b 1
)

REM Compile if needed
if not exist TransbaseToPostgres.class (
    echo Compiling TransbaseToPostgres.java...
    javac -cp "tbjdbc.jar;postgresql.jar" TransbaseToPostgres.java
    if errorlevel 1 (
        echo Compilation failed!
        exit /b 1
    )
)

echo Starting migration...
echo.

java -Xmx4g -cp "tbjdbc.jar;postgresql.jar;." TransbaseToPostgres %PG_HOST% %PG_DB% %PG_USER% %PG_PASS% %PG_SCHEMA%

echo.
echo Migration finished.
pause
