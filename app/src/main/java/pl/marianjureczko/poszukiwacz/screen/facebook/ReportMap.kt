package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapSnapshotOptions
import com.mapbox.maps.Size
import com.mapbox.maps.SnapshotOverlay
import com.mapbox.maps.Snapshotter
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.LocationHelper
import pl.marianjureczko.poszukiwacz.shared.MapHelper.DEFAULT_STYLE

class ReportMap(
    private val model: FacebookReportState
) : ReportPart {

    companion object {
        private const val MAP_WIDTH = 950f
        private const val MAP_HEIGHT = 450f
        private const val MAP_MARGIN = 10f
    }

    override fun height(): Float {
        return if (mapSelected()) {
            MAP_HEIGHT + 2 * MAP_MARGIN
        } else {
            0f
        }
    }

    /**
     * @return true if report publication was scheduled; false if the map was not selected and the report needs to be published elsewhere
     */
    fun draw(context: Context, reportCanvas: Canvas, currentTop: Float, publishReport: () -> Unit): Boolean {
        if (mapSelected()) {
            val snapshotter = configureSnapshotter(context, model.route)
            snapshotter.start(
                overlayCallback = { overlay ->
                    if (showRouteSelected()) {
                        drawRoute(overlay)
                    }
                    drawChests(context.resources, model.route, overlay, model.progress)
                }
            ) { bitmap, errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(context, R.string.error_on_exporting_map, Toast.LENGTH_LONG).show()
                } else if (bitmap != null) {
                    drawMapOnReportCanvas(reportCanvas, bitmap, currentTop)
                    publishReport()
                }
            }
        }
        return mapSelected()
    }

    private fun drawMapOnReportCanvas(reportCanvas: Canvas, bitmap: Bitmap, currentTop: Float) {
        val marginX = (reportCanvas.width - MAP_WIDTH) / 2
        reportCanvas.drawBitmap(bitmap, marginX, currentTop + MAP_MARGIN, Paint())
    }

    private fun drawRoute(overlay: SnapshotOverlay) {
        model.hunterPath?.let {
            val mapCanvas = overlay.canvas
            val locations = it.path().toList()
            if (locations.size > 1) {
                var previousXY =
                    overlay.screenCoordinate(Point.fromLngLat(locations[0].longitude, locations[0].latitude))
                locations.asSequence()
                    .drop(1)
                    .forEach {
                        val xy = overlay.screenCoordinate(Point.fromLngLat(it.longitude, it.latitude))
                        mapCanvas.drawLine(
                            previousXY.x.toFloat(), previousXY.y.toFloat(), xy.x.toFloat(), xy.y.toFloat(),
                            Paint().apply {
                                color = Color.RED
                                strokeWidth = 3.0f
                            }
                        )
                        previousXY = xy
                    }
            }
        }
    }

    private fun mapSelected() = model.getMap()?.isSelected == true

    private fun showRouteSelected() = model.getMapRoute()?.isSelected == true

    private fun configureSnapshotter(context: Context, route: Route): Snapshotter {
        val snapshotter = Snapshotter(
            context, MapSnapshotOptions.Builder()
                .size(Size(MAP_WIDTH, MAP_HEIGHT))
                .build()
        )
        snapshotter.setStyleUri(DEFAULT_STYLE)
        val cameraPosition = CameraOptions.Builder()
            .center(LocationHelper(route.treasures).center())
            .build()
        snapshotter.setCamera(cameraPosition)
        val padding = 50.0
        val locationHelper = LocationHelper(route.treasures)
        val cameraCoordinates = snapshotter.cameraForCoordinates(
            establishBoundingBoxCoordinates(locationHelper),
            EdgeInsets(padding, padding, padding, padding),
            0.0,
            0.0
        )
        snapshotter.setCamera(cameraCoordinates)
        return snapshotter
    }

    private fun establishBoundingBoxCoordinates(locationHelper: LocationHelper): List<Point> {
        var ne = locationHelper.northeast()
        var sw = locationHelper.southwest()
        if (ne.coordinates() == sw.coordinates()) {
            val marginForSingleTreasure = 0.005
            ne = Point.fromLngLat(ne.longitude() + marginForSingleTreasure, ne.latitude() + marginForSingleTreasure)
            sw = Point.fromLngLat(sw.longitude() - marginForSingleTreasure, sw.latitude() - marginForSingleTreasure)
        }
        return listOf(ne, sw)
    }

    private fun drawChests(resources: Resources, route: Route, snapshot: SnapshotOverlay, progress: TreasuresProgress) {
        val closedChest = IconHelper.loadIcon(resources, R.drawable.chest_closed_small, 41)
        val openedChest = IconHelper.loadIcon(resources, R.drawable.chest_very_small, 41)
        route.treasures.forEach { t ->
            val xy = snapshot.screenCoordinate(Point.fromLngLat(t.longitude, t.latitude))
            val chestToDraw = if (progress.collectedTreasuresDescriptionId.contains(t.id)) {
                openedChest
            } else {
                closedChest
            }
            snapshot.canvas.drawBitmap(chestToDraw, xy.x.toFloat(), xy.y.toFloat(), null)
        }
    }
}