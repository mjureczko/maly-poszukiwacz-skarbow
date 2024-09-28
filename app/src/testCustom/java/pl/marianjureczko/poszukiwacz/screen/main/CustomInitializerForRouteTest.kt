package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.AssetManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.times
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.XmlHelper
import java.io.File

@ExtendWith(MockitoExtension::class)
class CustomInitializerForRouteTest {

    @Mock
    lateinit var storage: StorageHelper

    @Mock
    lateinit var assetManager: AssetManager

    private val pathToDestination = "."
    private val destinationRoute = "$pathToDestination/custom.xml"
    private val treasure = TreasureDescription(
        1,
        50.501055,
        18.180191,
        "k01001",
        "tip_01.m4a",
        "tip_01.jpg",
        "kalinowice_01.mp4",
        "en_01.srt"
    )
    private val route = Route("custom", mutableListOf(treasure.copy()))

    @AfterEach
    fun removeFiles() {
        tryToRemove(destinationRoute)
        tryToRemove(treasure.photoFileName)
        tryToRemove(treasure.tipFileName)
        tryToRemove(treasure.movieFileName)
        tryToRemove(treasure.subtitlesFileName)
        tryToRemove("${pathToDestination}/copied_marker.txt")
    }

    @Test
    fun copyRouteToLocalStorage() {
        //given
        given(storage.pathToRoutesDir()).willReturn(pathToDestination)
        given(storage.getRouteFile(CustomInitializerForRoute.routeName)).willReturn(File(destinationRoute))
        given(assetManager.open("${CustomInitializerForRoute.routeName}.xml"))
            .willReturn(XmlHelper().writeToString(route).byteInputStream())
        given(storage.loadRoute(CustomInitializerForRoute.routeName)).willReturn(route)
        given(assetManager.open(treasure.photoFileName!!)).willReturn(treasure.photoFileName!!.byteInputStream())
        given(assetManager.open(treasure.tipFileName!!)).willReturn(treasure.tipFileName!!.byteInputStream())
        given(assetManager.open(treasure.movieFileName!!)).willReturn(treasure.movieFileName!!.byteInputStream())
        given(assetManager.open(treasure.subtitlesFileName!!)).willReturn(treasure.subtitlesFileName!!.byteInputStream())
        val initializer = CustomInitializerForRoute(storage, assetManager)

        //when
        initializer.copyRouteToLocalStorage()

        //then
        then(storage).should(times(1)).save(route)
        //all paths in the saved route should be prefixed with pathToDestination
        val actualTreasure = route.treasures[0]
        val prefix = pathToDestination + "/"
        assertThat(actualTreasure.photoFileName).isEqualTo(prefix + treasure.photoFileName)
        assertThat(actualTreasure.tipFileName).isEqualTo(prefix + treasure.tipFileName)
        assertThat(actualTreasure.movieFileName).isEqualTo(prefix + treasure.movieFileName)
        assertThat(actualTreasure.subtitlesFileName).isEqualTo(prefix + treasure.subtitlesFileName)

        assertThat(File(treasure.photoFileName!!)).hasContent(treasure.photoFileName)
        assertThat(File(treasure.tipFileName!!)).hasContent(treasure.tipFileName)
        assertThat(File(treasure.movieFileName!!)).hasContent(treasure.movieFileName)
        assertThat(File(treasure.subtitlesFileName!!)).hasContent(treasure.subtitlesFileName)
        assertThat(initializer.isAlreadyCopied()).isTrue()
    }

    private fun tryToRemove(path: String?) {
        try {
            File(path).delete()
        } catch (ex: Exception) {
            System.err.println(ex);
        }
    }

}