#language: es

Característica: Contenido válido en los contenedores destacados
	El contenido de diversos contenedores han de ser correcto
    
	Esquema del escenario: Las respuestas a peticiones a una portada deben tener un contenido válido.

		Dado que navego a http://www.rtve.es		
		Entonces existe al menos un elemento "div" con nombre "<contenedor>"
		Y el primero de ellos contiene únicamente elementos con clase "<contenido_valido>"
		Pero si existe mas de uno, "<warn>" habrá que generar un aviso		
		
			Ejemplos:
			| contenedor              | warn | contenido_valido
			| tablaNoticiasDestacadas | si   | news, video, audio, encuesta
			| moduloTVE               | no   | news, video, audio			