# A/B splitter microservice
Used to determine user's group for A/B testing

## Requirements
Java 8 is required to run the service.

## API
### User's group route
Get user's testing group:
`GET /v1/route/:user`
#### Parameters

#### Response
Status: 200 OK
`{"group":"c"}`

#### Error response
Status: 400 or 500
`{"group":"","errorMessage":"Wrong API version","errorCode":"400"}`

#### HTTP Status Code Summary
200 OK - Everything worked as expected.
400 Bad Request - Wrong API version.
500 - something went wrong on service's end.



## Performance


 Performance can be testing using maven: `mvn jmeter:jmeter`
 Service should be started before the command.
 There are several tests defined (in src/test/jmeter) for different number of simultaneous client threads
 Results can be viewed in console or as files in target\jmeter\results\

## Usage and configuration


## Requirements

Wymagania:
* Konfiguracja usługi to lista par (“grupa testowa”, waga)
np. ((“grupa A”, 2), (“grupa B”, 3), (“grupa C”, 5)), co rozkłada 2/10 do A, 3/10 do B oraz 5/10 do C
Konfiguracja jest dostępna statycznie w np. pliku konfiguracyjnym
* W parametrze żądania mamy id użytkownika (alfanumeryczny)
np. /route?id=abc453def
Liczba użytkowników jest rzędu miliona
* Odpowiedzią jest nazwa grupy do której użytkownik jest przekierowany
Ważne: Dany użytkownik trafia zawsze do tej samej grupy testowej
Należy udowodnić wydajność stworzonej usługi (ilość jednocześnie obsługiwanych req / s oraz mean time odpowiedzi) wskazując referencyjną platformę.