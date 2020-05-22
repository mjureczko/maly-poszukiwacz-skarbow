package pl.marianjureczko.poszukiwacz

import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayOutputStream

class StorageHelperTest {

    @Test
    fun xml() {
        //given
        val someTreasures = TreasuresList("name", ArrayList(listOf(TreasureDescription(1.1, 1.2))))
        val serializer: Serializer = Persister()
        val out = ByteArrayOutputStream()

        //when
        serializer.write(someTreasures, out)
        val actual = out.toString("UTF-8")

        //then
        print(actual)
        Assert.assertNotNull(actual)
        val deserialized = serializer.read(TreasuresList::class.java, actual)
        assertEquals(someTreasures, deserialized)
    }

    @Test
    fun xml2() {
        val xml = "<treasuresList>\n" +
                "   <name>test1</name>\n" +
                "   <tresures class=\"java.util.Arrays\$ArrayList\">\n" +
                "      <treasureDescription>\n" +
                "         <latitude>1.1</latitude>\n" +
                "         <longitude>1.2</longitude>\n" +
                "      </treasureDescription>\n" +
                "      <treasureDescription>\n" +
                "         <latitude>2.1</latitude>\n" +
                "         <longitude>2.2</longitude>\n" +
                "      </treasureDescription>\n" +
                "   </tresures>\n" +
                "</treasuresList>"
    }
    @Test
    fun save() {
        //given
//        val helper = StorageHelper()
        val someTreasures = TreasuresList("name", ArrayList(listOf(TreasureDescription(1.1, 1.2))))

        //when
//        helper.save(someTreasures)

        //then
    }
}