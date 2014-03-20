#language: es

Característica: Reemplazo de players PF mobile emu
    XX

      Escenario: Video players.
   
        Dado que tengo la cookie 'r_ckplc', con valor 'v1', para el dominio '.rtve.es'
# 		Dado que tengo la cookie 'r_sdocu', con valor 'v2', para el dominio '.rtve.es'
    	Y que navego a 'http://www.rtve.es/api/gestores.json'    
    	Entonces existe '1' elemento, con atributo 'id' y valor 'non-existant'
#    	Entonces existen '1' elementos o mas, con atributo 'class' y valor 'VideoContainer'
#		Y tienen '1' elemento hijo, con tag 'meta', atributo 'itemprop' y valor 'thumbnail'
#		Cuando hago click en esos elementos, con esperas de '5' segundos
#		Y espero '10' segundos
#		Entonces existen '1' elementos o mas, con atributo 'class' y valor 'VideoContainer'
#		Entonces tienen '1' elementos hijo, con tag 'video', atributo 'id' y valor 'html5id'
##		Y tienen '1' elementos hijo, con tag 'param', atributo 'value' y valor '.*?assetID=\\d+.*?'

    Escenario: Audio players.

        Dado que tengo la cookie 'r_ckplc', con valor 'v1', para el dominio '.rtve.es'
# 		Dado que tengo la cookie 'r_sdocu', con valor 'v2', para el dominio '.rtve.es'
#    	Y que navego a 'http://rtve.es'
#    	Entonces existen '1' elementos o mas, con atributo 'class' y valor 'AudioContainer'
#		Y tienen '1' elemento hijo, con tag 'img', atributo 'src' y valor 'http://img.irtve.es/css/i/player-radio-dummy.png'
#		Cuando hago click en esos elementos, con esperas de '5' segundos
#		Y espero '5' segundos
#		Entonces existen '1' elementos o mas, con atributo 'class' y valor 'AudioContainer'
#		Entonces tienen '1' elementos hijo, con tag 'audio', atributo 'preload' y valor 'metadata'
	