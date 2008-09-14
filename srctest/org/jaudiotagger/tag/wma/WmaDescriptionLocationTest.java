package org.jaudiotagger.tag.wma;

import org.jaudiotagger.audio.asf.data.AsfHeader;

import org.jaudiotagger.audio.asf.io.AsfHeaderReader;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.asf.tag.AsfFieldKey;
import org.jaudiotagger.audio.asf.tag.AsfTag;

import java.io.File;

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
    public final static String TEST_FILE = "test1.wma"; //$NON-NLS-1$

    /**
     * Will hold a tag instance for writing some values.
     */
    public final AsfTag testTag;

    /**
     * Creates an instance.
     */
    public WmaDescriptionLocationTest()
    {
        super(TEST_FILE);
        this.testTag = new AsfTag(true);
        this.testTag.setArtist("TheArtist");
        this.testTag.set(AsfTag.createTextField(AsfFieldKey.ISVBR.getPublicFieldId(), Boolean.TRUE.toString()));
    }


    /**
     * Tests whether the audio file contains artist and variable bitrate as specified in the
     * {@linkplain #WmaDescriptionLocationTest() constructor}, and if a content description object as well
     * as an extended content description is available.
     * @param testFile file to test
     * @param hcd <code>true</code> if a content description is expected in the ASF header.
     * @param hecd <code>true</code> if an extended content description is expected in the ASF header.
     * @param ehcd <code>true</code> if a content description is expected in the ASF header extension.
     * @param ehecd <code>true</code> if an extended content description is expected in the ASF header extension.
     * @throws Exception on I/O Errors
     */
    public void checkExcpectations(File testFile, boolean hcd, boolean hecd, boolean ehcd, boolean ehecd) throws Exception
    {
        AudioFile read = AudioFileIO.read(testFile);
        assertTrue(read.getAudioHeader().isVariableBitRate());
        assertEquals("TheArtist", read.getTag().getFirstArtist());
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
     * Tests the locations of the content descriptor object and the extended content descriptor object, upon
     * some deep ASF manipulations.
     * 
     * @throws Exception On I/O Errors
     */
    public void testChunkLocations() throws Exception
    {
        File testFile = prepareTestFile(null);
        AudioFile read = AudioFileIO.read(testFile);
        AudioFileIO.delete(read);
        read.setTag(testTag);
        read.commit();
        checkExcpectations(testFile, true, true, false, false);
    }

}
