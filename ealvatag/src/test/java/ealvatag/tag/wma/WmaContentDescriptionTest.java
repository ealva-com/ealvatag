package ealvatag.tag.wma;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.asf.data.AsfHeader;
import ealvatag.audio.asf.data.ContentDescription;
import ealvatag.audio.asf.io.AsfHeaderReader;
import ealvatag.audio.asf.util.TagConverter;
import ealvatag.tag.NullTag;
import ealvatag.tag.asf.AsfFieldKey;
import ealvatag.tag.asf.AsfTag;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 * This testcase tests the handling of the content description chunk.
 *
 * @author Christian Laireiter
 */
public class WmaContentDescriptionTest extends WmaTestCase
{

    private final static String TEST_FILE = "test1.wma"; //$NON-NLS-1$

    /**
     * tests whether the content description chunk will disappear if not
     * necessary.<br>
     * @throws Exception On I/O Errors
     */
    @Test public void testContentDescriptionRemoval() throws Exception
    {
        AudioFile file = getAudioFile();
        AsfTag tag = (AsfTag) file.getTag().or(NullTag.INSTANCE);
        for (String curr : ContentDescription.ALLOWED)
        {
            tag.deleteField(AsfFieldKey.getAsfFieldKey(curr));
        }
        file.save();
        AsfHeader header = AsfHeaderReader.readHeader(file.getFile());

        assertNull(header.getContentDescription());
        for (String currKey : ContentDescription.ALLOWED)
        {
            AsfFieldKey curr = AsfFieldKey.getAsfFieldKey(currKey);
            file.deleteFileTag();
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNull(header.getContentDescription());
            assertNull(header.getExtendedContentDescription());
            file = AudioFileIO.read(file.getFile());
            tag = (AsfTag) file.getTag().or(NullTag.INSTANCE);
            tag.addField(tag.createField(curr, curr.getFieldName()));
            file.save();
            header = AsfHeaderReader.readHeader(file.getFile());
            assertNotNull(header.getContentDescription());
            assertNull("Key: " + curr.getFieldName(), header.getExtendedContentDescription());
            assertEquals(curr.getFieldName(), TagConverter.createTagOf(header).getFirst(curr.getFieldName()));
        }
    }

    @Override String getTestFile() {
        return TEST_FILE;
    }
}
