package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.content.Context
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.ExtractionProgress

class ProgressPrinter(
    private val memoConsole: MemoConsole,
    private val context: Context
) : ExtractionProgress {

    override fun routeExtracted(routeName: String) =
        print(routeName.trim(), R.string.extracted_route)

    override fun fileExtracted(fileName: String) {
        print(fileName.trim(), R.string.extracted_file)
    }

    private fun print(routeName: String, stringId: Int) {
        memoConsole.print(context.getString(stringId, routeName))
        memoConsole.print(context.getString(R.string.bluetooth_next_file))
    }
}