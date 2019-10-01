Comandos de los Chat's

/privado nombre             /*para enviar mensaje privado*/

/bloquear nombre            /*para bloquear a alguien*/

/desbloquear nombre         /*para desbloquear a alguien*/


privadoServidor y privadoCliente solo pueden enviar mensajes y mensajes privados pero no bloquear.

bloqueosServidor y bloqueosCliente pueden bloquear y desbloquear usuarios en el chat además de enviar mensajes privados.

loginServidor y loginCliente guardan el usuario, contraseña y a los que bloqueó. Y también manda mensajes privados, bloquea y desbloquea usuarios. Los datos se guardan en una base de datos con SQLite, es necesario ejecutar el archivo .jar desde el proyecto por que ahí está el archivo de SQLite en la librería.

loginServidor creará una carpeta con el nombre "chat_asyncrono" y dentro de esa carpeta creará la base de datos.


