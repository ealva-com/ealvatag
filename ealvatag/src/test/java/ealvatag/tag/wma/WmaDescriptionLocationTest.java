package ealvatag.tag.wma;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioFileImpl;
import ealvatag.audio.asf.data.AsfHeader;
import ealvatag.audio.asf.data.MetadataContainer;
import ealvatag.audio.asf.io.AsfExtHeaderModifier;
import ealvatag.audio.asf.io.AsfHeaderReader;
import ealvatag.audio.asf.io.AsfStreamer;
import ealvatag.audio.asf.io.ChunkModifier;
import ealvatag.audio.asf.io.WriteableChunkModifer;
import ealvatag.audio.asf.util.TagConverter;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.asf.AsfFieldKey;
import ealvatag.tag.asf.AsfTag;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This testcase tests the ability to read the content description and extended content description
 * from the ASF header object and the ASF header extension object.
 *
 * @author Christian Laireiter
 */
public class WmaDescriptionLocationTest extends WmaTestCase
{

    /**
     * Test file to use as source.
     */
    private final static String TEST_FILE = "test1.wma"; //$NON-NLS-1$

    /**
     * Will hold a tag instance for writing some values.
     */
    private AsfTag testTag;


    /**
     * Applies {@link #testTag} to the given audio file, and allows to specify at which location the
     * content description and extended content description are to be added.<br>
     * @param testFile The file to work with.
     * @param hcd <code>true</code> if the content description should be placed into the header object. if <code>false</code>
     *            it will be placed in the header extension object.
     * @param hecd <code>true</code> if the extended content description should be placed into the header object. if <code>false</code>
     *            it will be placed in the header extension object.
     * @throws Exception on I/O Errors.
     */
    private void applyTag(File testFile, boolean hcd, boolean hecd) throws Exception
    {
        // getFields an audio file instance
        AudioFile read = AudioFileIO.read(testFile);
        // deleteField all managed data
        read.deleteFileTag();
        // Create chunks
        MetadataContainer[] distributeMetadata = TagConverter.distributeMetadata(this.testTag);
        // createField creator for the content description object (chunk)
        WriteableChunkModifer cdCreator = new WriteableChunkModifer(distributeMetadata[0]);
        MetadataContainer ecd = distributeMetadata[2];
        // createField creator for the extended content description object (chunk)
        WriteableChunkModifer ecdCreator = new WriteableChunkModifer(ecd);
        // createField the modifier lists
        List<ChunkModifier> headerMods = new ArrayList<ChunkModifier>();
        List<ChunkModifier> extHeaderMods = new ArrayList<ChunkModifier>();
        if (hcd)
        {
            headerMods.add(cdCreator);
        }
        else
        {
            extHeaderMods.add(cdCreator);
        }
        if (hecd)
        {
            headerMods.add(ecdCreator);
        }
        else
        {
            extHeaderMods.add(ecdCreator);
        }
        headerMods.add(new AsfExtHeaderModifier(extHeaderMods));
        File destination = prepareTestFile("chunkloc.wma");
        new AsfStreamer().createModifiedCopy(new FileInputStream(testFile), new FileOutputStream(destination), headerMods);
        checkExpectations(destination, hcd, hecd, !hcd, !hecd);

    }

    /**
     * Tests whether the audio file contains artist and variable bitrate as specified in the
     * tag, and if a content description object as well
     * as an extended content description is available.
     * @param testFile file to test
     * @param hcd <code>true</code> if a content description is expected in the ASF header.
     * @param hecd <code>true</code> if an extended content description is expected in the ASF header.
     * @param ehcd <code>true</code> if a content description is expected in the ASF header extension.
     * @param ehecd <code>true</code> if an extended content description is expected in the ASF header extension.
     * @throws Exception on I/O Errors
     */
    private void checkExpectations(File testFile, boolean hcd, boolean hecd, boolean ehcd, boolean ehecd) throws Exception
    {
        AudioFile read = AudioFileIO.read(testFile);
        assertTrue(read.getAudioHeader().isVariableBitRate());
        assertEquals("TheArtist", read.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
        AsfHeader readHeader = AsfHeaderReader.readHeader(testFile);
        assertNotNull(readHeader.findContentDescription());
        assertNotNull(readHeader.findExtendedContentDescription());
        assertEquals(hcd, readHeader.getContentDescription() != null);
        assertEquals(hecd, readHeader.getExtendedContentDescription() != null);
        assertEquals(ehcd, readHeader.getExtendedHeader() != null && readHeader.getExtendedHeader()
                        .getContentDescription() != null);
        assertEquals(ehecd, readHeader.getExtendedHeader() != null && readHeader.getExtendedHeader()
                        .getExtendedContentDescription() != null);
    }

    /**
     * Tests the locations of the metadata descriptor object and the extended metadata descriptor object, upon
     * some deep ASF manipulations.
     *
     * @throws Exception On I/O Errors
     */
    @Test
    public void testChunkLocations() throws Exception
    {
        File testFile = prepareTestFile(null);
        AudioFileImpl read = (AudioFileImpl)AudioFileIO.read(testFile);
        read.deleteFileTag();
        testTag = (AsfTag)read.setNewDefaultTag();
        this.testTag.setField(FieldKey.ARTIST,"TheArtist");
        this.testTag.setField(this.testTag.createField(AsfFieldKey.ISVBR, Boolean.TRUE.toString()));
        testTag.setCopyFields(true);
        read.save();
        checkExpectations(testFile, true, true, false, false);

        applyTag(testFile, false, false);
        applyTag(testFile, false, true);
        applyTag(testFile, true, false);
        applyTag(testFile, true, true);
    }

    @Override String getTestFile() {
        return TEST_FILE;
    }
}
