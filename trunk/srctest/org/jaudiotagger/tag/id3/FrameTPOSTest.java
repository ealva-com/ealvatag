package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRCK;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRCKTest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;
import java.util.Iterator;

/**
 * Test TPOSFrame
 */
public class FrameTPOSTest extends AbstractTestCase
{
    public void testMergingMultipleFrames() throws Exception
    {
        ID3v24Tag tag = new ID3v24Tag();
        tag.setField(tag.createField(FieldKey.DISC_NO,"1"));
        tag.setField(tag.createField(FieldKey.DISC_TOTAL,"10"));
        assertEquals("1",tag.getFirst(FieldKey.DISC_NO));
        assertEquals("10",tag.getFirst(FieldKey.DISC_TOTAL));
        assertTrue(tag.getFrame("TPOS") instanceof AbstractID3v2Frame);
    }

    public void testDiscNo()
    {
        Exception exceptionCaught=null;
        File orig = new File("testdata", "test82.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        try
        {
            AudioFile af = AudioFileIO.read(orig);
            Tag newTags = (Tag)af.getTag();
            Iterator<TagField> i = newTags.getFields();
            while(i.hasNext())
            {
                System.out.println(i.next().getId());
            }
            //Integer discNo = Integer.parseInt(newTags.get("Disc Number"));
            //tag.setField(FieldKey.DISC_NO,discNo.toString())
        }
        catch(Exception e)
        {
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }
}