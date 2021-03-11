# Little Treasure Hunter

GPLv3 (https://www.gnu.org/licenses/gpl-3.0.html)

## The game

1. To play the game you need some printed out QR codes that will be used as treasures.

2. To prepare for the game you need to stash the treasures:
    * You need to select `NOWA TRASA DLA POSZUKIWACZY` and enter a name for the route.
    * You need to stash the treasures, that is the printed QR codes.
    * When being in the place where the treasure is stashed you need to press the chest button ![picture](/app/src/main/res/drawable/chest_small.png).
    The game will save treasure location coordinates.
    * Additionally, you can record a tip about the treasure location using the button with a microphone.

3. When all treasures are stashed you need to go back to the game main screen and select the just created route.
When everything is ready you can hand over the mobile to the treasures hunters.

4. The treasures hunters use the distance to treasure expressed using the number of steps and the compass as a guide.
The compass shows the direction towards the treasure.
However, they still need to find out where the north is to use the compass correctly.

5. During the search, treasures hunters can use the recorded tip by pressing ![picture](/app/src/main/res/drawable/megaphone_small.png).

6. When a treasure is found, the treasure hunter should press ![picture](/app/src/main/res/drawable/chest_small2.png) to scan the code and find out what exactly that is.

7. The treasure hunter can select the next treasure for searching using ![picture](/app/src/main/res/drawable/change_chest_small.png).

## Treasures

The treasures are represented using QR codes.
The code should correspond with this pattern TDDWWW (regular expression [grd]\d\d\w\w\w), e.g. g01abc.

 * T - treasure type, g is for gold, r is for rubies and d is for diamonds

 * DD - the amount of treasure, should be from 01 to 99

 * WWW - treasure identifier, each treasure should have a different one

The treasures can be generated using (https://zxing.appspot.com/generator). You can also use on of the following:
 * * *
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
 * * *