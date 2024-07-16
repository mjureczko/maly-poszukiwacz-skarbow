//package pl.marianjureczko.poszukiwacz.activity.bluetooth
//
//import android.bluetooth.BluetoothSocket
//import android.content.Context
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.shared.StorageHelper
//import java.io.InputStream
//
//class ReaderThread(
//    socket: BluetoothSocket,
//    private val context: Context,
//    private val memoConsole: MemoConsole
//) : Thread() {
//
//    private val TAG = javaClass.simpleName
//
//    private val inStream: InputStream = socket.inputStream
//    private val storageHelper = StorageHelper(context)
//    override fun run() {
//        val progressPrinter = ProgressPrinter(memoConsole, context)
//        storageHelper.extractZipStream(inStream, progressPrinter)
//        memoConsole.print(context.getString(R.string.extracted_everything))
//    }
//}