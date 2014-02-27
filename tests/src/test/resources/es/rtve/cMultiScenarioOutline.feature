#language: es

Característica: Tiempo de carga aceptable
    El tiempo de carga de la una página debe estar dentro de los umbrales definidos
    
    Esquema del escenario: Las peticiones a una página deben ser terminadas en un tiempo definido.

		Dado que navego a "<página>"				
		Entonces el tiempo en segundos transcurrido ha de ser menor a "<tiempo_esperado>"		
	
			Ejemplos:
			| página                      | tiempo_esperado | 
			| http://www.rtve.es/         | 10              |  			
			| http://www.rtve.es/radio    | 10              |

    Esquema del escenario: Las peticiones a una página deben ser terminadas en un tiempo definido.

		Dado que navego a "<página>"				
		Entonces el tiempo en segundos transcurrido ha de ser menor a "<tiempo_esperado>"		
	
			Ejemplos:
			| página                      | tiempo_esperado | 
			| http://www.rtve.es/eltiempo | 5               |
			| http://www.rtve.es/infantil | 5               |
