package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;

/**
 * Test TIPL
 */
public class FrameBodyTIPLTest extends AbstractTestCase
{
    public static final String INVOLVED_PEOPLE = "producer\0eno,lanois";
    public static final String INVOLVED_PEOPLE_ODD = "producer\0eno,lanois\0engineer";

    public static FrameBodyTIPL getInitialisedBodyOdd()
    {
        FrameBodyTIPL fb = new FrameBodyTIPL();
        fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE_ODD);
        return fb;
    }

    public static FrameBodyTIPL getInitialisedBody()
    {
        FrameBodyTIPL fb = new FrameBodyTIPL();
        fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));

    }

    public void testCreateFrameBodyodd()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE_ODD);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));

    }
    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());

    }

     public void testCreateFromIPLS()
    {
        Exception exceptionCaught = null;
        FrameBodyIPLS fbv3 = FrameBodyIPLSTest.getInitialisedBody();
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL(fbv3);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("*"+fb.getText()+"*","*"+FrameBodyIPLSTest.INVOLVED_PEOPLE+"*");
        assertEquals(2,fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));

    }

    /**
     * Uses TMCL frame
     * @throws Exception
     */
    public void testMultiArrangerIDv24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteArrangerv24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());

        f.setTag(new ID3v24Tag());
        ((ID3v24Tag)f.getTag()).setField(FieldKey.ARRANGER, "Arranger1");
        ((ID3v24Tag)f.getTag()).addField(FieldKey.ARRANGER, "Arranger2");
        assertEquals(1, f.getTag().getFieldCount());
        assertEquals("Arranger1", f.getTag().getFirst(FieldKey.ARRANGER));
        assertEquals("Arranger1", f.getTag().getValue(FieldKey.ARRANGER,0));
        assertEquals("Arranger2", f.getTag().getValue(FieldKey.ARRANGER,1));

        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(2,f.getTag().getFields(FieldKey.ARRANGER).size());
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(1, f.getTag().getFieldCount());
    }


}