#language: es

Característica: Coloreado de las pestañas de cada portada 
    El contenido dinámico de cada portada debe estar bien formado
    
	Escenario: Las respuestas a peticiones a una portada deben tener un contenido válido.
	
		Dado que navego a "http://www.rtve.es"
		Cuando maximizo el navegador
		Y hago click en la pestaña "Noticias" 
		Entonces la pestaña "Noticias" debe tener un color distinto de gris
		Y la etiqueta "body" debe tener un id "Noticias", en minusculas, sin acentos
		