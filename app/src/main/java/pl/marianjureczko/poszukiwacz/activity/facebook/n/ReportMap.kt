package pl.marianjureczko.poszukiwacz.activity.facebook.n

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapSnapshotInterface
import com.mapbox.maps.MapSnapshotOptions
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.Size
import com.mapbox.maps.Snapshotter
import com.mapbox.maps.Style
import com.mapbox.maps.bitmap
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.LocationHelper


class ReportMap(
    private val model: FacebookReportModel
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

    fun draw(context: Context, reportCanvas: Canvas, currentTop: Float, publishReport: () -> Unit) {
        if (mapSelected()) {
            var snapshotter = configureSnapshotter(context, model.route)
            snapshotter.start { snapshot ->
                snapshot?.bitmap()?.let { bitmap ->
                    val mapCanvas = Canvas(bitmap)
                    drawChests(context.resources, model.route, snapshot, model.progress, mapCanvas)
                    if (showRouteSelected()) {
                        drawRoute(snapshot, mapCanvas)
                    }
                    drawMapOnReportCanvas(reportCanvas, bitmap, currentTop)
                    publishReport()
                }
            }
        }
    }

    private fun drawMapOnReportCanvas(reportCanvas: Canvas, bitmap: Bitmap, currentTop: Float) {
        val marginX = (reportCanvas.width - MAP_WIDTH) / 2
        reportCanvas.drawBitmap(bitmap, marginX, currentTop + MAP_MARGIN, Paint())
    }

    private fun drawRoute(snapshot: MapSnapshotInterface, mapCanvas: Canvas) {
        model.hunterPath?.let {
            val locations = it.pathAsCoordinates().toList()
            if (locations.size > 1) {
                var previousXY = snapshot.screenCoordinate(Point.fromLngLat(locations[0].longitude, locations[0].latitude))
                locations.asSequence()
                    .drop(1)
                    .forEach {
                        val xy = snapshot.screenCoordinate(Point.fromLngLat(it.longitude, it.latitude))
                        mapCanvas.drawLine(
                            previousXY.x.toFloat(), previousXY.y.toFloat(), xy.x.toFloat(), xy.y.toFloat(),
                            Paint().apply { color = Color.RED }
                        )
                        previousXY = xy
                    }
            }
        }
    }

    private fun mapSelected() = model.getMap()?.isSelected == true

    private fun showRouteSelected() = model.getMapRoute()?.isSelected == true

    private fun configureSnapshotter(context: Context, route: Route): Snapshotter {
        var snapshotter = Snapshotter(
            context, MapSnapshotOptions.Builder()
                .size(Size(MAP_WIDTH, MAP_HEIGHT))
                .resourceOptions(
                    ResourceOptions.Builder()
                        .accessToken(context.getString(R.string.mapbox_access_token))
                        .build()
                )
                .build()
        )
        snapshotter.setStyleUri(Style.OUTDOORS)
        val cameraPosition = CameraOptions.Builder()
            .center(LocationHelper(route).center())
            .build()
        snapshotter.setCamera(cameraPosition)
        //TODO: different padding on x and y in order to adjust to route shape
        val padding = 50.0
        val locationHelper = LocationHelper(route)
        val cameraCoordinates = snapshotter.cameraForCoordinates(
            listOf(locationHelper.northeast(), locationHelper.southwest()),
            EdgeInsets(padding, padding, padding, padding),
            0.0,
            0.0
        )
        snapshotter.setCamera(cameraCoordinates)
        return snapshotter
    }

    private fun drawChests(resources: Resources, route: Route, snapshot: MapSnapshotInterface, progress: TreasuresProgress, canvas: Canvas) {
        val closedChest = IconHelper.loadIcon(resources, R.drawable.chest_closed_small, 41)
        val openedChest = IconHelper.loadIcon(resources, R.drawable.chest_very_small, 41)
        route.treasures.forEach { t ->
            val xy = snapshot.screenCoordinate(Point.fromLngLat(t.longitude, t.latitude))
            val chestToDraw = if (progress.collectedTreasuresDescriptionId.contains(t.id)) {
                openedChest
            } else {
                closedChest
            }
            canvas.drawBitmap(chestToDraw, xy.x.toFloat(), xy.y.toFloat(), null)
        }
    }
}