#language: es

Característica: Contenido válido en los contenedores destacados
    El contenido de diversos contenedores han de ser correcto
    
    Esquema del escenario: Las respuestas a peticiones a una portada deben tener un contenido válido.

		Dado que navego a "http://www.rtve.es"		
		Cuando maximizo el navegador
		Entonces existe al menos un elemento con "<tag>" "<contenedor>"
		Y el primero de ellos no contiene elementos con clase "<contenido_invalido>"
		Pero si existe mas de uno, "<warn>" habrá que generar un aviso			
		
			Ejemplos:
			| tag  | contenedor              | warn | contenido_invalido |
			| name | tablaNoticiasDestacadas | si   | newsie 			 |	
#			| id   | moduloTVE               | no   | news, encuesta	 |