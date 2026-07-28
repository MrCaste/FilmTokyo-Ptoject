#FilmTokio
---
Este es un proyecto sobre una página de reviews de películas que se compone de 4 módulos:

- **Common** : Módulo común (Entitys, repositorios etc..)
- **Web** : Modulo donde reside todo el apartado web (Vistas, html, login etc..)
- **REST** : Modulo para el servicio de API REST donde proporciona información sobre las reviews
- **Batch** : Proceso batch donde pasa todas las películas a un archivo csv

## Web
---
> Es donde se gestiona el apartado gráfico (html) y se gestionan la lógica de las listas de películas, el login y permisos se componen de los usuarios normales y los admins en donde los admins tienen el poder de crear otros usuarios con el Rol que indiquen así como añadir artistas que posteriormente serán agregados al crear una película.

>Una vez se crea la película es agregada a la lista de la videoteca para su visualización general, si quieres una vista mas detallada, editar o poder crear una review has de hacer click en la película que deseas para ir a su vista individual.

>Este modulo también posee un REST client para poder conectarse a la API y obtener las valoraciones y reviews de cada película así como para poder crear una.

## REST
---
>Es el módulo en donde se aloja el servicio de valoración y review para cada película, siendo sus end points los siguientes:

- **POST** /api/ratings
- **GET** /api/review/films/{filmId}/users/{userId}
- **GET** /api/ratings-average/films/{filmId}
- **GET** /api/review/films/{filmId}
- **GET** /api/ratings/films/{filmId}/users/{userId}/exists
- **PUT** /api/ratings/{reviewId}

>Este módulo también tiene un JWT para la seguridad en donde el modulo web obtiene dicho token para tener acceso a los endpoints.

## Common
---
>En este modulo es donde se alojan todas las entidades y recursos comunes entre web y REST así como sus repositorios de la base de datos.

### BBDD

>En la base de datos lo único que hay que mencionar es la relación entre los artistas y las películas habiendo una tabla de relación entre ellas de por medio, ya que de esa manera si un director al mismo tiempo es actor de la misma película se podría llegar a acontecer de esta forma. Los roles de actores o director se añaden una vez se cree la película después simplemente tienes que crear el objeto MovieArtist y añadírselo a la lista que hay en Movies y con Lazy se creará la relación automáticamente.

## Batch
---
>Esto es simple, simplemente es un processo batch que lo que hace es escribir en un archivo csv cada vez que se ejecute las películas que falten con los siguientes parámetros:

- Id
- Nombre de la película
- fecha de la película
- y fecha en la que se añadió al csv

>También se ha tenido que crear una tabla nueva que solo este processo controla para poder tener un registro de las películas que ya se han añadido.
