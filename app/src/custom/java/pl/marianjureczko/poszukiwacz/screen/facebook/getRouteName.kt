package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

fun getRouteName(context: Context, progress: TreasuresProgress): String {
    return context.getString(R.string.pretty_route_name)
}