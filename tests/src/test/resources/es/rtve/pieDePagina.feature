#language: es

Característica: Pié de página estable 
    El contenido del pié de página no debe variar
    
	Esquema del escenario: El generador de piés de página siempre deve devolver el mismo valor.

		Dado que navego a http://www.rtve.es
		Cuando maximizo el navegador (si es posible)
		Y hago click en la pestaña "<pestaña>" 
		Entonces el pié de página debe ser el establecido "<pestaña>"
		
			Ejemplos:
			| pestaña     |
			| Noticias    |
			| Televisión  |
#			| Radio       |
#			| Deportes    |			
#			| El Tiempo   |		