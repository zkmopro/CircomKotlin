package uniffi.mopro

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import uniffi.mopro.generateCircomProof
import uniffi.mopro.ProofLib
import java.io.File


@RunWith(AndroidJUnit4::class)
class MoproTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.github.zkmopro.test", appContext.packageName)
    }

    // Add more test methods here for testing Mopro functionality
    // Example:
    @Test
    fun testMoproFunctionality() {
        val input_str: String = "{\"b\":[\"5\"],\"a\":[\"3\"]}"

        val appContext = InstrumentationRegistry.getInstrumentation().context

        // Read from assets and copy to temporary file
        val zkeyFile = File(appContext.cacheDir, "multiplier2_final.zkey")
        appContext.assets.open("multiplier2_final.zkey").use { input ->
            zkeyFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val graphFile = File(appContext.cacheDir, "multiplier2.bin")
        appContext.assets.open("multiplier2.bin").use { input ->
            graphFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        
        val zkeyPath = zkeyFile.absolutePath
        val graphPath = graphFile.absolutePath
        
        try {
            val res = circomProve(graphPath, input_str, zkeyPath)
            val valid = verifyCircomProof(zkeyPath, res, ProofLib.ARKWORKS)
            assertEquals(valid, true)
        } finally {
            // Clean up the temporary file
            zkeyFile.delete()
            graphFile.delete()
        }
    }
    
} 