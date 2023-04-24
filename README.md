# server-voting-system
Server app para plataforma para la gestión de finanzas personales

## Los aspectos técnicos a tener en cuenta:

1. Java versión 17 es requerido.
2. La aplicación utiliza Spring Boot versión 3.0.5 como base.
3. El manejo de la capa de persistencia con spring-jpa y MySQL.
4. La seguridad es manejada mediante spring-Security y una implementación de autenticactión y autorización mediante JWT.
5. Está desarrollado como un servicio web RESTful.

## Requisitos

Antes de poner en funcionamiento el proyecto, asegúrese de tener lo siguiente:

1. Java 17 instalado en su sistema
2. Una instancia de MySQL en funcionamiento
3. Un editor de código como IntelliJ IDEA o Eclipse

## Paso 1: Clonar el repositorio

Comience por clonar el repositorio de GitHub en su máquina local. Puede hacerlo ejecutando el siguiente comando en su terminal:

    git clone https://github.com/{usuario-github}/server-finance-app.git

## Paso 2: Configurar la base de datos

Asegúrese de tener una instancia de MySQL en funcionamiento y cree una base de datos para el proyecto con el archivo DDL contenido en src/main/resources/sql. Luego, abra el archivo application.properties en la carpeta src/main/resources/ y configure la conexión a la base de datos de la siguiente manera:

        spring.datasource.url=jdbc:mysql://localhost:3306/finances_app_db
        spring.datasource.username=nombre-usuario
        spring.datasource.password=contraseña-usuario
        spring.jpa.hibernate.ddl-auto=none

## Paso 3: Ejecutar el proyecto

Abra su editor de código y abra el proyecto clonado. Una vez que se haya cargado el proyecto, puede ejecutarlo haciendo clic derecho en la clase Application.java en la carpeta src/main/java y seleccionando la opción "Run".

## Conclusión

Siguiendo estos pasos, debería poder poner en funcionamiento el proyecto en su máquina local y comenzar a trabajar en él. Si tiene algún problema, asegúrese de verificar la documentación del proyecto.
