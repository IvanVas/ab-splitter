# A/B splitter microservice
Used for determining user's group for A/B testing

## API

## Configuration



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