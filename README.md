# Mały Poszukiwacz Skarbów

Treasure format [grd]\d\d\w\w\w
e.g.: g01abc

 * 1st character - treasure type, g is for gold, r is for rubies and d is for diamonds
 
 * 2nd and 3rd character - the amount of treasure, should be from 01 to 99
 
 * 4th, 5th and 6th character - treausure identifier, each trasure should have a different one
 
Can be generated using: https://zxing.appspot.com/generator

GPLv3 (https://www.gnu.org/licenses/gpl-3.0.html)

## Rozgrywka 

1. Opiekun drukuje skarby (kody QR)
2. Opiekun ukrywa skarby, do każdego ukrytego skarbu zapisuje jego współrzędne i ewentualne wskazówki ułatwiające poszukiwania
3. Opiekun przekazuje współrzędne i wskazówki poszukiwaczom
4. Poszukiwacze szukają skarbów wspomagając się współrzędnym wyświetlanymi przez aplikację i materiałami przekazanymi przez opiekuna
5. Poszukiwacz po znalezieniu skarbu używa aplikacji do zeskanowania kodu QR i dzięki temu dowiaduje się co znalazł
