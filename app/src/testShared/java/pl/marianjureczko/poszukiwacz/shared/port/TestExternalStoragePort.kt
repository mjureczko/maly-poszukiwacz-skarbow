package pl.marianjureczko.poszukiwacz.shared.port

import android.graphics.Bitmap
import pl.marianjureczko.poszukiwacz.screen.facebook.ReportStoragePort
import pl.marianjureczko.poszukiwacz.usecase.badges.Achievements
import pl.marianjureczko.poszukiwacz.usecase.badges.AchievementsStoragePort

class TestExternalStoragePort : AchievementsStoragePort, ReportStoragePort {
    private var achievements: Achievements? = null
    var savedBitmap: Bitmap? = null
    var savedFileName: String? = null

    override fun loadAchievements(): Achievements? {
        return achievements
    }

    override fun save(achievements: Achievements) {
        this.achievements = achievements
    }

    override fun save(bitmap: Bitmap, fileName: String): Boolean {
        savedBitmap = bitmap
        savedFileName = fileName
        return true
    }
}