# Mały Poszukiwacz Skarbów

GPLv3 (https://www.gnu.org/licenses/gpl-3.0.html)

## Available apps:

- [Little Treasure Hunter](https://play.google.com/store/apps/details?id=pl.marianjureczko.poszukiwacz&hl=en)
- [Kalinowice](https://play.google.com/store/apps/details?id=pl.marianjureczko.poszukiwacz.kalinowice&hl=en)
- [Pęgów](https://play.google.com/store/apps/details?id=pl.marianjureczko.poszukiwacz.pegow)

prompt: In the previous section you extracted
@/app/src/main/java/pl/marianjureczko/poszukiwacz/screen/searching/Compass.kt and
@/app/src/main/java/pl/marianjureczko/poszukiwacz/screen/searching/Steps.kt to
@/app/src/main/java/pl/marianjureczko/poszukiwacz/screen/searching/CompassAndSteps.kt . The old versions are commented
out in @/app/src/main/java/pl/marianjureczko/poszukiwacz/screen/searching/_Steps.kt and in
@/app/src/main/java/pl/marianjureczko/poszukiwacz/screen/searching/SearchingScreen.kt . After the change the view
changed. The Compass and Steps widgets are smaller and below them there is way bigger empty space separating it from "
bottom" widgets. Correct the view so it will look like before the refactor.