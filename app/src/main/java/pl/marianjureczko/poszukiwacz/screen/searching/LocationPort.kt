package pl.marianjureczko.poszukiwacz.screen.searching

import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

fun interface UpdateLocationCallback {
    operator fun invoke(location: AndroidLocation)
}