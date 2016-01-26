# moviesearcher 
Buscador de películas

Servicio REST:
	
	Indexar películas:
		http://localhost:8080/moviesearcher/index
		
	Buscar películas: 
		http://localhost:8080/moviesearcher/search?q=query&tit=title&...

	Buscar películas similares a otra película:
		http://localhost:8080/moviesearcher/findSimilar?id=identificador
		
Aplicación Web: 
	
		Abrir el archivo /app/index.html en un navegador.
		Pulsar el botón "Generar índice" para indexar el contenido.
		Cargada la información ya se podrán realizar las búsquedas.
		
![Búsqueda](/doc/img/busqueda.png)
![Similares](/doc/img/similares.png)
