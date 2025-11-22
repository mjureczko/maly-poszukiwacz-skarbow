package pl.marianjureczko.poszukiwacz.screen.treasureselector

import pl.marianjureczko.poszukiwacz.R

fun moreApsData(): List<MoreApsEntry> {
    return listOf(
        MoreApsEntry(R.string.well_done_custom_app, R.string.well_done_kalinowice, R.string.kalinowice_app_link),
        MoreApsEntry(R.string.well_done_custom_app, R.string.well_done_pegow, R.string.pegow_app_link)
    )
}

