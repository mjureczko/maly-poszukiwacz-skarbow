package pl.marianjureczko.poszukiwacz.screen

import pl.marianjureczko.poszukiwacz.screen.bluetooth.Mode
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper

object Screens {

    object TreasureEditor {
        const val PARAMETER_ROUTE_NAME = "route_name"
        private const val PATH = "treasureeditor"
        const val ROUTE = "$PATH/{$PARAMETER_ROUTE_NAME}"

        fun doRoute(routeName: String): String = "$PATH/$routeName"
    }

    object Searching {
        const val PARAMETER_ROUTE_NAME = "route_name"
        private const val PATH = "searching"
        const val ROUTE = "$PATH/{${PARAMETER_ROUTE_NAME}}"

        fun doRoute(routeName: String): String = "${PATH}/$routeName"
    }

    object Results {
        const val PARAMETER_ROUTE_NAME = "route_name"
        const val PARAMETER_RESULT_TYPE = "result_type"
        const val PARAMETER_TREASURE_ID = "treasure_id"
        const val PARAMETER_TREASURE_AMOUNT = "treasure_amount"
        const val PATH = "result"
        const val ROUTE =
            "$PATH/{${PARAMETER_ROUTE_NAME}}/{$PARAMETER_RESULT_TYPE}/{$PARAMETER_TREASURE_ID}/{$PARAMETER_TREASURE_AMOUNT}"

        fun doRoute(routeName: String, resultType: ResultType, treasureId: Int?, treasureAmount: Int?): String {
            return "$PATH/$routeName/$resultType/$treasureId/$treasureAmount"
        }
    }

    object TipPhoto {
        const val PARAMETER_TIP_PHOTO = "tip_photo"
        const val PARAMETER_ROUTE_NAME = "route_name"
        private const val PATH = "tipPhoto"
        const val ROUTE = "$PATH/{$PARAMETER_TIP_PHOTO}/{$PARAMETER_ROUTE_NAME}"

        fun doRoute(tipPhoto: String, routeName: String): String {
            return "$PATH/$tipPhoto/$routeName"
        }
    }

    object Map {
        const val PARAMETER_ROUTE_NAME = "route_name"
        private const val PATH = "map"
        const val ROUTE = "$PATH/{$PARAMETER_ROUTE_NAME}"

        fun doRoute(routeName: String) = "$PATH/$routeName"
    }

    object Selector {
        const val PARAMETER_JUST_FOUND_TREASURE = "just_found_treasure_id"
        private const val PATH = "selector"
        val ROUTE = "$PATH/{$PARAMETER_JUST_FOUND_TREASURE}"

        fun doRoute(justFoundTreasureId: Int) = "$PATH/$justFoundTreasureId"
    }

    object Commemorative {
        const val PARAMETER_TREASURE_DESCRIPTION_ID = "treasure_description_id"
        const val PARAMETER_PHOTO_PATH = "photo_path"
        private const val PATH = "commemorative"
        const val ROUTE = "$PATH/{$PARAMETER_TREASURE_DESCRIPTION_ID}/{$PARAMETER_PHOTO_PATH}"

        fun doRoute(treasureDescriptionId: Int, photoPath: String?): String {
            return "$PATH/$treasureDescriptionId/${PhotoHelper.encodePhotoPath(photoPath)}"
        }
    }

    object Facebook {
        const val PARAMETER_ROUTE_NAME = "route_name"
        private const val PATH = "facebook"
        const val ROUTE = "$PATH/{${PARAMETER_ROUTE_NAME}}"

        fun doRoute(routeName: String) = "$PATH/$routeName"
    }

    object Bluetooth {
        const val PARAMETER_MODE = "mode"
        const val PARAMETER_ROUTE_TO_SENT = "routeName"
        private const val PATH = "bluetooth"
        const val ROUTE = "$PATH/{$PARAMETER_MODE}/{$PARAMETER_ROUTE_TO_SENT}"

        fun doRoute(mode: Mode, routeToSent: String) = "$PATH/$mode/$routeToSent"

    }
}