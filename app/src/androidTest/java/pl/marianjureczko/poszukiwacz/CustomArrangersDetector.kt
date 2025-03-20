package pl.marianjureczko.poszukiwacz

import androidx.test.platform.app.InstrumentationRegistry
import dalvik.system.DexFile
import org.junit.Test


class CustomArrangersDetector {

    @Test
    fun findArrangerConstructors() {
        val testClasses = getTestApkClasses()
        val customArrangers = convertToComaSeparatedCustomArrangers(testClasses)
        println("### $customArrangers ###")
    }

    private fun convertToComaSeparatedCustomArrangers(classes: List<String>): String {
        return classes
            .filter { c -> c.endsWith("Arranger") && !c.endsWith(".Arranger") && !c.endsWith(".CustomArranger") }
            .joinToString(separator = ",")
    }

    private fun getTestApkClasses(): List<String> {
        try {
            val testApkPath: String = InstrumentationRegistry.getInstrumentation()
                .getContext()
                .getApplicationInfo()
                .sourceDir
            val dexFile = DexFile(testApkPath)
            return dexFile.entries().asSequence().toList()
        } catch (e: Exception) {
            throw RuntimeException("Failed to retrieve test APK classes", e)
        }
    }
}