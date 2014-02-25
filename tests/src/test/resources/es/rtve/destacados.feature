#language: es

Característica: Contenido válido en los contenedores destacados
    El contenido de diversos contenedores han de ser correcto
    
    Esquema del escenario: Las respuestas a peticiones a una portada deben tener un contenido válido.

		Dado que navego a "http://www.rtve.es"		
		Cuando maximizo el navegador
		Entonces existe al menos un elemento con atributo "<attrib>" y valor "<name>"		
		Y ninguno está vacio
		Y contienen al menos un elemento con clase "<contenido_imperativo>"		
	
		
			Ejemplos:
			| attrib | name                    | contenido_imperativo | 
			| name   | tablaNoticiasDestacadas | news	              |  
#			| id     | moduloTVE               | news                 | 
#			| id	 | moduloRNE               | news                 |
#			| class  | Tiempo                  | news                 |