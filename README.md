# Mały Poszukiwacz Skarbów

## [English](README_en.md)

GPLv3 (https://www.gnu.org/licenses/gpl-3.0.html)

## Rozgrywka

1. Do gry potrzebne są wydrukowane kody QR, które posłużą jako skarby.
Można użyć tych:
![picture](sample_treasures/diamond03.png)
![picture](sample_treasures/diamond11.png)
![picture](sample_treasures/diamond22.png)
![picture](sample_treasures/gold01.png)
![picture](sample_treasures/gold19.png)
![picture](sample_treasures/gold27.png)
![picture](sample_treasures/gold32.png)
![picture](sample_treasures/gold37.png)
![picture](sample_treasures/ruby02.png)
![picture](sample_treasures/ruby14.png)
![picture](sample_treasures/ruby26.png)
Można również wygenerować własne na podstawie instrukcji [poniżej](#markdown-header-skarby).
2. Przygotowanie do poszukiwań wymaga ukrycia skarbów:
	* Należy wybierać `NOWA TRASA DLA POSZUKIWACZY` i wprowadzić jej nazwę, dowolną.
    * Trzeba ukryć skarby, czyli wydrukowane wcześniej kody QR.
    * W miejscu ukrycia skarbu należy wcisnąć przycisk z wizerunkiem skrzyni
![picture](/app/src/main/res/drawable/chest_small.png)
Dzięki temu gra zapamięta współrzędne skarbu.
    * Dodatkowo można nagrać podpowiedź dla poszukiwaczy po naciśnięciu przycisku z symbolem mikrofonu.
3. Gdy już wszystkie skarby są ukryte należy wycofać się do głównego ekranu gry, wybrać właśnie utworzoną trasę i przekazać telefon poszukiwaczom.
4. Poszukiwacze szukają na podstawie w informacji o odległości wyrażonej w liczbą kroków do skarbu i kompasu.
Kompas wskazuje kierunek którym trzeba podążać aby dotrzeć do skarbu.
Jest to kierunek świata, poszukiwacz musi samodzielnie ustalić gdzie jest północ żeby pójść we właściwą stronę.
5. Podczas poszukiwań można skorzystać z podpowiedzi nagranej wcześniej  przez ukrywającego skarby naciskając przycisk
![picture](/app/src/main/res/drawable/megaphone.png){height=10 width=10}
6. Poszukiwacz po znalezieniu skarbu używa aplikacji do zeskanowania kodu QR i dzięki temu dowiaduje się co znalazł.
7. Po znalezieniu skarbu można wybrać do poszukiwań kolejny przy pomocy przycisku
![picture](/app/src/main/res/drawable/change_chest.png){height=10% width=10%}

## Skarby

Treasure format [grd]\d\d\w\w\w
e.g.: g01abc

 * 1st character - treasure type, g is for gold, r is for rubies and d is for diamonds

 * 2nd and 3rd character - the amount of treasure, should be from 01 to 99

 * 4th, 5th and 6th character - treausure identifier, each trasure should have a different one

Can be generated using: https://zxing.appspot.com/generator