package pl.marianjureczko.poszukiwacz.shared.port.external

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.marianjureczko.poszukiwacz.usecase.badges.Achievements
import pl.marianjureczko.poszukiwacz.usecase.badges.AchievementsStoragePort
import java.io.File
import java.io.FileOutputStream

class ExternalStoragePort(val context: Context) : AchievementsStoragePort {
    private val fileName: String = "DO_NOT_DELETE_little_treasure_hunter.json"
    private val dirName: String = "DO_NOT_DELETE_little_treasure_hunter/"
    private val TAG = javaClass.simpleName

    override fun load(): Achievements? {
        try {
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                loadFromNewDevice()
            } else {
                loadFromOldDevice()
            }
            return data?.let {
                Json.decodeFromString<AchievementsJson>(String(it, Charsets.UTF_8)).toDomain()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not load achievements", e)
            return null
        }
    }

    override fun save(achievements: Achievements) {
        val dto = AchievementsJson.from(achievements)
        val json = Json.encodeToString(dto)
        val dataToSave = json.toByteArray(charset = Charsets.UTF_8)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveOnNewDevice(dataToSave)
            } else {
                saveOnOldDevice(dataToSave)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not save achievements", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadFromNewDevice(): ByteArray? {
        val resolver = context.contentResolver
        val uri = getUriToFile(resolver)
        return uri?.let { uri2 ->
            resolver.openInputStream(uri2)?.use { it.readBytes() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getUriToFile(resolver: ContentResolver): Uri? {
        val projection = arrayOf(MediaStore.Downloads._ID)
        val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ? AND ${MediaStore.Downloads.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(fileName, Environment.DIRECTORY_DOWNLOADS + "/" + dirName)

        val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        resolver.query(contentUri, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
                return ContentUris.withAppendedId(contentUri, id)
            }
        }
        return null
    }

    private fun loadFromOldDevice(): ByteArray? {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val myAppDir = File(downloadsDir, dirName)
            val file = File(myAppDir, fileName)
            if (file.exists()) {
                return file.readBytes()
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveOnNewDevice(data: ByteArray) {
        val resolver = context.contentResolver
        val uriOfExistingFile = getUriToFile(resolver)
        if (uriOfExistingFile == null) {
            saveToNewFile(resolver, data)
        } else {
            overrideExistingFile(resolver, uriOfExistingFile, data)
        }

    }

    private fun overrideExistingFile(resolver: ContentResolver, uriOfExistingFile: Uri, data: ByteArray) {
        resolver.openOutputStream(uriOfExistingFile, "wt")?.use { stream ->
            stream.write(data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToNewFile(resolver: ContentResolver, data: ByteArray) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + dirName)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { it.write(data) }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }
    }

    private fun saveOnOldDevice(data: ByteArray) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val myAppDir = File(downloadsDir, dirName).apply { mkdirs() }
            val file = File(myAppDir, fileName)
            FileOutputStream(file).use { it.write(data) }
        }
    }
}