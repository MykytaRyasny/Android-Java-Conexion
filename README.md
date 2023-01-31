# Como usar el proyecto
## Compilado
- Para usar el proyecto compilado hay que descargar la apk e instalarlo en un movil
- Descargar el servidor en un dispositivo que tenga java 1.8 o superior
- Ejecutar el servidor con comando:
  <br />
  `java -jar Server.jar`
- tener el archivo de la base de datos en el mismo directorio que el servidor
- Tanto el servidor como el dispositivo movil tiene que estar en la misma red
  Ya puedes registrarte y loguearte

## Sin compilar
- Necesitas AndroidStudio y Cualquier IDE a elección (Eclipse, VSCode, IntelliJ)
- Java 1.8 o superior
- Correr el servidor ejecutando el main de Java
- Correr la APK con el emulador que trae AndroidStudio
- En caso de que no quieras tener una maquina virtual, puedes ejecutarlo en el mismo dispositivo para eso en la app de Android Studio tendras que poner como IP 127.0.0.1 o la ip local que uses en caso de cambiarla
# El proyecto de PSP de Mykyta Ryasny
El proyecto consiste en una aplicacion cliente servidor que inician el contacto emdiante login-register y posteriormente interactuan para realizar diversas tareas

## Planing
- [x] Impllementacio inciial de hilos
- [x] Almcenamiento local de la semilla de encriptacion
- [x] Implementados hilos en el servidor para los metodos login y register
- [x] Familiarizarme con el modelo VMMV de AndroidStudio (26-01-2023)
- [x] Instalar una maquina virtual con Linux para que me sirva de Servidor (26-01-2023)
  - He instalado Linux 22.04 Desktop para tener una interfaz y tener mayor comodidad a la hora de testear, la optimización de recursos no es necesaria en este momento
- [x] Crear una aplicacion-cliente basica para comprobar la comunicación con el servidor (30-01-2023)
- [x] Crear el primer diseño de Android para ir implementando la comunicación cliente-servidor (30-01-2023)
- [x] Implementar una conexión segura. (30-01-2023)
- [x] Interactuar con una base de datos.


## Objetivos
- Crear una aplicacion en AndroidStudio que ejecute diversos metodos en el servidor
- Crear una aplicacion Java (Server) que este a la escucha constantemete
- Almacenar datos del movil en el servidor entre ellos base de datos de login

### Extras
- Implementar otras funciones relacionados con BBDD o con el server en si como
    - Almacenamiento de diversos archivos en el server como imagenes o documentos

## Modulos
Aquí se irán poninedo los modulos funcionales o en proceso de desarollo del proyecto para
su completa implementación
En esta fase del proyecto el proyecto dispone de los siguientes metodos en el Server:
- login (se le pasa nombre de usuario y contraseña)
- register (se le pasa nombre, nombre de usuario y contraseña para almacenar en la base de datos)
## UML
Se irá implementando la relación entre diferentes métodos y la interacción completa a su vez
el funcionamiento completo de los métodos
Ejempolo de funcionamiento de login:
<image src="./Ejemplo de login" alt="UML Login">
## Herramientas de desarrollo
- Java 1.9
- Kotlin
- AndroidStudio
- Base de Datos (SQLite)
- Virtual machine (Con Linux para simular un servidor)
- Maquina anfitriona Windows
### Extra:
    - Implementacion de Docker
## Autor
GitHub
- [@MykytaRyasny](https://github.com/MykytaRyasny)

