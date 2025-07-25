package pl.marianjureczko.poszukiwacz.screen.bluetooth

import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.port.storage.ExtractionProgress

class ProgressPrinter(private val printer: Printer) : ExtractionProgress {

    override fun routeExtracted(routeName: String) =
        print(routeName.trim(), R.string.extracted_route)

    override fun fileExtracted(fileName: String) {
        print(fileName.trim(), R.string.extracted_file)
    }

    private fun print(routeName: String, stringId: Int) {
        printer.print(stringId, routeName)
        printer.print(R.string.bluetooth_next_file)
    }
}