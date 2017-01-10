package ealvatag.tag.wma;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.asf.data.AsfHeader;
import ealvatag.audio.asf.data.ContentDescription;
import ealvatag.audio.asf.io.AsfHeaderReader;
import ealvatag.audio.asf.util.TagConverter;
import ealvatag.tag.asf.AsfFieldKey;
import ealvatag.tag.asf.AsfTag;

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
        for (String curr : ContentDescription.ALLOWED)
        {
            tag.deleteField(AsfFieldKey.getAsfFieldKey(curr));
        }
        file.commit();
        AsfHeader header = AsfHeaderReader.readHeader(file.getFile());
        assertNull(header.getContentDescription());
        for (String currKey : ContentDescription.ALLOWED)
        {
            AsfFieldKey curr = AsfFieldKey.getAsfFieldKey(currKey);
            AudioFileIO.delete(file);
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNull(header.getContentDescription());
            assertNull(header.getExtendedContentDescription());
            file = AudioFileIO.read(file.getFile());
            tag = (AsfTag) file.getTag();
            tag.addField(tag.createField(curr, curr.getFieldName()));
            file.commit();
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNotNull(header.getContentDescription());
            assertNull("Key: " + curr.getFieldName(), header.getExtendedContentDescription());
            assertEquals(curr.getFieldName(), TagConverter.createTagOf(header).getFirst(curr.getFieldName()));
        }
    }

}
