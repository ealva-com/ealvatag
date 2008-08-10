package org.jaudiotagger.tag.wma;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.io.AsfHeaderReader;
import org.jaudiotagger.audio.asf.tag.AsfFieldKey;
import org.jaudiotagger.audio.asf.tag.AsfTag;
import org.jaudiotagger.audio.asf.util.TagConverter;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;

/**
 * This testcase tests the handling of the content description chunk.
 * 
 * @author Christian Laireiter
 */
public class WmaContentDescriptionTest extends WmaTestCase
{

    /**
     * Testfile to use as source.
     */
    public final static String TEST_FILE = "test1.wma"; //$NON-NLS-1$

    /**
     * Creates an instance.
     */
    public WmaContentDescriptionTest()
    {
        super(TEST_FILE);
    }

    /**
     * tests whether the content description chunk will disappear if not
     * necessary.<br>
     * @throws Exception On I/O Errors
     */
    public void testContentDescriptionRemoval() throws Exception
    {
        AudioFile file = getAudioFile();
        AsfTag tag = (AsfTag) file.getTag();
        for (AsfFieldKey curr : ContentDescription.DESCRIPTION_FIELDS)
        {
            tag.deleteTagField(curr);
        }
        file.commit();
        AsfHeader header = AsfHeaderReader.readHeader(file.getFile());
        assertNull(header.getContentDescription());
        for (AsfFieldKey curr : ContentDescription.DESCRIPTION_FIELDS)
        {
            AudioFileIO.delete(file);
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNull(header.getContentDescription());
            assertNull(header.getExtendedContentDescription());
            file = AudioFileIO.read(file.getFile());
            tag = (AsfTag) file.getTag();
            tag.add(AsfTag.createTextField(curr.getPublicFieldId(), curr.getPublicFieldId()));
            file.commit();
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNotNull(header.getContentDescription());
            assertNull("Key: " + curr.name(), header.getExtendedContentDescription());
            assertEquals(curr.getPublicFieldId(), TagConverter.createTagOf(header).getFirst(curr.getPublicFieldId()));
        }
    }

}
