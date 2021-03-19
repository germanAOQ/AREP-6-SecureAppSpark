# Taller de Arquitectura Segura
### Pre-requisitos
Para hacer uso del software es necesario tener instalado:
* Java: Como lenguaje de programación                                      
    Siga las instrucciones en https://docs.oracle.com/en/java/javase/15/install/
* Maven: Automatiza y estandariza el flujo de vida de la construcción de software.                 
    Siga las instrucciones en http://maven.apache.org/download.cgi#Installation
* Git: Administrador descentralizado de configuraciones.                     
    Siga las instrucciones en http://git-scm.com/book/en/v2/Getting-Started-Installing-Git
* Docker Desktop: Automatizar el despliegue de aplicaciones dentro de contenedores de software                                   
    Siga las instrucciones en https://www.docker.com/products/docker-desktop
* Keytool de java: Para generar y administrar las llaves y los certificados

### Introducción
La seguridad de la información consiste en proteger los datos y los sistemas de información contra el acceso no autorizado o la modifación de la información en almacenamiento, 
proceso o tránsito, y contra la denegación de servicio a usuarios autorizados. Además, la implementación de sistemas de información debe seguir unos principios de diseño y unas
buenas prácticas para que se minimicen los riesgos de un potencial ataque.                                          
La aplicación desarrollada, garantiza el principio de "Complete mediation" desde los frentes de usuario y servidor. Además, el BackEnd consiste de dos máquinas virtuales EC2 de AWS 
que alojan un servidor, desarrollado con SparkJava, con los servicios propios de la aplicación. El código fuente de los servicios está disponible en dos repositorios GitHub, uno
por cada servicio. Más adelante, se explicará, con mayor detalle técnico, la estructura de archivos, el diagrama de clases y la arquitectura de la aplicación, junto con un
video que muestra la ejecución y la implementación de las medidas de seguridad a la aplicación. 

### Ejecución de la aplicación
https://youtu.be/0SN1-E1yA8Y