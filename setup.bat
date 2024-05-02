@echo off

REM Run maven build if the environment flag is vscode
if "%1" == "vscode" (
    echo %0: Setup vscode development environment
    cd "spring-boot-react"
    if exist "target\classes\static" (
        echo Removing target\classes\static directory...
        rmdir /s /q "target\classes\static"
    )
    mvnw package -Dmaven.test.skip=true
    exit
)

REM Check if the environment flag is provided
if "%1" == "dev" (
    echo %0: Run docker in development mode
    docker-compose --env-file .env -f .docker\docker-compose.yml -f .docker\docker-compose.dev.yml up --build --force-recreate 

) else if "%1" == "prod" (
    echo %0: Run docker in production mode
    docker-compose --env-file prod.env -f .docker\docker-compose.yml -f .docker\docker-compose.prod.yml up --build --force-recreate --detach
) else if "%1" == "db" (
    echo %0: Run Postgres Database
    docker-compose --env-file .env -f .docker\docker-compose.db.yml up --build --force-recreate --detach
) else (
    echo Usage: %0 ^<vscode/dev/prod/db^>
    exit /b 1
)