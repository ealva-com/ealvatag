package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.id3.AbstractID3v2Frame;
import com.ealvatag.tag.id3.ID3v23Tag;
import com.ealvatag.tag.id3.framebody.FrameBodyCOMM;

import java.io.File;

/** COMM Frames with non-standard lang values
 */
public class Issue273Test extends AbstractTestCase
{
    /**
     * Test Read/Write Comment with funny lang
     */
    public void testCommentWithLanguage()
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

        Exception exceptionCaught = null;
        try
        {
            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            af.commit();

            af = AudioFileIO.read(testFile);
            ID3v23Tag tag = (ID3v23Tag)af.getTag();
            tag.addField(FieldKey.COMMENT,"COMMENTVALUE");
            af.commit();

            af = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)af.getTag();
            assertEquals("COMMENTVALUE",tag.getFirst(FieldKey.COMMENT));
            AbstractID3v2Frame frame = tag.getFirstField("COMM");
            FrameBodyCOMM fb = (FrameBodyCOMM)frame.getBody();
            assertEquals("eng",fb.getLanguage());
            af.commit();



            fb.setLanguage("XXX");
            assertEquals("XXX",fb.getLanguage());
            af.commit();

            af = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)af.getTag();
            assertEquals("COMMENTVALUE",tag.getFirst(FieldKey.COMMENT));
            frame = tag.getFirstField("COMM");
            fb = (FrameBodyCOMM)frame.getBody();
            assertEquals("XXX",fb.getLanguage());
            af.commit();

            fb.setLanguage("\0\0\0");
            af.commit();

            af = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)af.getTag();
            assertEquals("COMMENTVALUE",tag.getFirst(FieldKey.COMMENT));
            frame = tag.getFirstField("COMM");
            fb = (FrameBodyCOMM)frame.getBody();
            assertEquals("\0\0\0",fb.getLanguage());
            af.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);

    }

    public void testCommentSetGet()
    {
        FrameBodyCOMM comm = new FrameBodyCOMM();
        comm.setLanguage("XXX");
        assertEquals("XXX",comm.getLanguage());

        comm = new FrameBodyCOMM();
        comm.setLanguage("\0\0\0");
        assertEquals("\0\0\0",comm.getLanguage());
    }
}
