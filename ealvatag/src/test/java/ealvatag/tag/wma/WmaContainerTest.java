package ealvatag.tag.wma;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.asf.data.Chunk;
import ealvatag.audio.asf.data.GUID;
import ealvatag.audio.asf.data.MetadataContainer;
import ealvatag.audio.asf.data.MetadataContainerUtils;
import ealvatag.audio.asf.io.AsfHeaderUtils;
import ealvatag.audio.asf.io.MetadataReader;
import ealvatag.audio.asf.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Christian Laireiter
 *
 */
public class WmaContainerTest extends WmaTestCase {

    public final static String TEST_FILE = "test6.wma";

    public WmaContainerTest() {
        super(TEST_FILE);
    }

    public void testExtContentAfterWrite() throws Exception {
        File prepareTestFile = prepareTestFile(null);
        AudioFile read = AudioFileIO.read(prepareTestFile);
        read.save(); // Normalize Text file
        byte[] ext = AsfHeaderUtils.getFirstChunk(read.getFile(), GUID.GUID_EXTENDED_CONTENT_DESCRIPTION);
        read.save();
        byte[] ext2 = AsfHeaderUtils.getFirstChunk(read.getFile(), GUID.GUID_EXTENDED_CONTENT_DESCRIPTION);
        assertTrue(Arrays.equals(ext, ext2));
//        assertEquals(ext, ext2);
    }

    public void testReadWriteEquality() throws IOException {
        File prepareTestFile = prepareTestFile(null);
        byte[] tmp = AsfHeaderUtils.getFirstChunk(prepareTestFile, GUID.GUID_EXTENDED_CONTENT_DESCRIPTION);

        MetadataReader reader = new MetadataReader();
        ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
        GUID readGUID = Utils.readGUID(bis);
        assertEquals(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, readGUID);
        Chunk read1 = reader.read(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, bis,
                0);
        System.out.println(read1);
        assertTrue(read1 instanceof MetadataContainer);
        assertEquals(tmp.length, ((MetadataContainer) read1)
                .getCurrentAsfChunkSize());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ((MetadataContainer) read1).writeInto(bos);
        assertEquals(tmp.length, bos.toByteArray().length);
        bis = new ByteArrayInputStream(bos.toByteArray());
        readGUID = Utils.readGUID(bis);
        assertEquals(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, readGUID);
        Chunk read2 = reader.read(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, bis,
                0);
        System.out.println(MetadataContainerUtils.equals(
                (MetadataContainer) read1, (MetadataContainer) read2));
    }

}
