package pl.marianjureczko.poszukiwacz.screen.treasureselector

import pl.marianjureczko.poszukiwacz.R

fun moreApsData(): List<MoreApsEntry> {
    return listOf(
        MoreApsEntry(
            R.string.well_done_classic_app,
            R.string.well_done_little_treasure_hunter,
            R.string.classic_app_link
        ),
        MoreApsEntry(R.string.well_done_custom_app, R.string.well_done_kalinowice, R.string.kalinowice_app_link)
    )
}

