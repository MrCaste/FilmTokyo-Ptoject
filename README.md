# FilmTokio

Este es un proyecto sobre una página de reviews de películas que se compone de 4 módulos:

- **Common** : Módulo común (Entitys, repositorios etc..)
- **Web** : Modulo donde reside todo el apartado web (Vistas, html, login etc..)
- **REST** : Modulo para el servicio de API REST donde proporciona información sobre las reviews
- **Batch** : Proceso batch donde pasa todas las películas a un archivo csv

## Web

Es donde se gestiona el apartado gráfico (html) y se gestionan la lógica de las listas de películas, el login y permisos se componen de los usuarios normales y los admins en donde los admins tienen el poder de crear otros usuarios con el Rol que indiquen así como añadir artistas que posteriormente serán agregados al crear una película.

Una vez se crea la película es agregada a la lista de la videoteca para su visualización general, si quieres una vista mas detallada, editar o poder crear una review has de hacer click en la película que deseas para ir a su vista individual.

Este modulo también posee un REST client para poder conectarse a la API y obtener las valoraciones y reviews de cada película así como para poder crear una.

## REST

Es el módulo en donde se aloja el servicio de valoración y review para cada película, siendo sus end points los siguientes:

- **POST** /api/ratings
- **GET** /api/review/films/{filmId}/users/{userId}
- **GET** /api/ratings-average/films/{filmId}
- **GET** /api/review/films/{filmId}
- **GET** /api/ratings/films/{filmId}/users/{userId}/exists
- **PUT** /api/ratings/{reviewId}

Este módulo también tiene un JWT para la seguridad en donde el modulo web obtiene dicho token para tener acceso a los endpoints.

## Common

En este modulo es donde se alojan todas las entidades y recursos comunes entre web y REST así como sus repositorios de la base de datos.

### BBDD

En la base de datos lo único que hay que mencionar es la relación entre los artistas y las películas habiendo una tabla de relación entre ellas de por medio, ya que de esa manera si un director al mismo tiempo es actor de la misma película se podría llegar a acontecer de esta forma. Los roles de actores o director se añaden una vez se cree la película después simplemente tienes que crear el objeto MovieArtist y añadírselo a la lista que hay en Movies y con Lazy se creará la relación automáticamente.

## Batch

Esto es simple, simplemente es un processo batch que lo que hace es escribir en un archivo csv cada vez que se ejecute las películas que falten con los siguientes parámetros:

- Id
- Nombre de la película
- fecha de la película
- y fecha en la que se añadió al csv

También se ha tenido que crear una tabla nueva que solo este processo controla para poder tener un registro de las películas que ya se han añadido.

<img width="2485" height="1086" alt="image" src="https://github.com/user-attachments/assets/5788b473-b3c9-4426-8e61-726020f0ebac" />
<img width="564" height="721" alt="image" src="https://github.com/user-attachments/assets/851042ce-22bb-4bff-aec5-aebdce6dcaa3" />
<img width="518" height="1079" alt="image" src="https://github.com/user-attachments/assets/ad1c2cf1-c2c0-47cc-8276-76208c7d4787" />
<img width="540" height="720" alt="image" src="https://github.com/user-attachments/assets/83a66600-26b3-41ed-9896-53c2d68bddb6" />
<img width="543" height="1051" alt="image" src="https://github.com/user-attachments/assets/9f742271-4c11-4176-8bda-3660ab878779" />
<img width="2483" height="1302" alt="image" src="https://github.com/user-attachments/assets/7a9bdcfe-f82b-4a08-9dce-ee3db415dfb6" />
<img width="1547" height="1267" alt="image" src="https://github.com/user-attachments/assets/b99a42e6-f680-488e-9e05-4227f58a65fa" />
<img width="2478" height="1078" alt="image" src="https://github.com/user-attachments/assets/4a5f56ac-b640-4744-95ce-d46f7ae7cf7c" />
<img width="1576" height="1094" alt="image" src="https://github.com/user-attachments/assets/9848f7e8-cdd9-483c-b196-22ba9a3b8e22" />
<img width="1538" height="971" alt="image" src="https://github.com/user-attachments/assets/5d95e5fb-b325-47ca-a26a-203252d372b8" />
<img width="1522" height="835" alt="image" src="https://github.com/user-attachments/assets/627f9dfb-0461-45b4-ab71-11b82def1232" />











