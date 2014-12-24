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
Reference platform:
`Windows 7 SP1 x64
AMD Phenom II X4 3.2GHz 8GB RAM`
* 10 threads sampling: 2ms avg response time, 2934 req/s
* 50 threads sampling: 13ms avg response time, 3150 req/s
* 100 threads sampling: 30ms avg response time, 3081 req/s
* 200 threads sampling: 61ms avg response time, 3122 req/s

Horizontal scaling is theoretically linear (if load balancer is not a bottleneck). To be confirmed by testing. 

 * Performance can be tested using maven: `mvn jmeter:jmeter`. The service should be started before the command.
 * There are several tests defined (in src/test/jmeter) for different number of simultaneous client threads.
 * Results can be viewed in console or as files in target\jmeter\results\


## Usage and configuration
### Configuration file:
`{
   "port": 8080,
   "groupWeights": {
     "a": 30,
     "b": 20,
     "c": 50
   }
 }`
### Usage
`mvn package` builds an executable jar ab-splitter-service.jar
 
To start the service:
`java -jar ab-splitter-service.jar <config file path>`


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