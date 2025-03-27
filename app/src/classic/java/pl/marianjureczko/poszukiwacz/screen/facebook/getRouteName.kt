package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

fun getRouteName(context: Context, progress: TreasuresProgress): String {
    return progress.routeName
}