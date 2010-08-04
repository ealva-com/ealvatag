package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRCK;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRCKTest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;

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

}