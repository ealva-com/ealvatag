package ealvatag.tag.id3.framebody;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;

/**
 * Test TIPL
 */
public class FrameBodyTIPLTest extends AbstractTestCase {
    public static final String INVOLVED_PEOPLE = "producer\0eno,lanois";
    public static final String INVOLVED_PEOPLE_ODD = "producer\0eno,lanois\0engineer";

    public static FrameBodyTIPL getInitialisedBodyOdd() {
        FrameBodyTIPL fb = new FrameBodyTIPL();
        fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE_ODD);
        return fb;
    }

    public static FrameBodyTIPL getInitialisedBody() {
        FrameBodyTIPL fb = new FrameBodyTIPL();
        fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        return fb;
    }

    public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer", fb.getKeyAtIndex(0));
        assertEquals("eno,lanois", fb.getValueAtIndex(0));

    }

    public void testCreateFrameBodyodd() {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE_ODD);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer", fb.getKeyAtIndex(0));
        assertEquals("eno,lanois", fb.getValueAtIndex(0));

    }

    public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());

    }

    public void testCreateFromIPLS() {
        Exception exceptionCaught = null;
        FrameBodyIPLS fbv3 = FrameBodyIPLSTest.getInitialisedBody();
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL(fbv3);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("*" + fb.getText() + "*", "*" + FrameBodyIPLSTest.INVOLVED_PEOPLE + "*");
        assertEquals(2, fb.getNumberOfPairs());
        assertEquals("producer", fb.getKeyAtIndex(0));
        assertEquals("eno,lanois", fb.getValueAtIndex(0));

    }

    /**
     * Uses TMCL frame
     *
     * @throws Exception
     */
    public void testMultiArrangerIDv24() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteArrangerv24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARRANGER, "Arranger1");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ARRANGER, "Arranger2");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("Arranger1", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
        assertEquals("Arranger1", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ARRANGER, 0));
        assertEquals("Arranger2", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ARRANGER, 1));

        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ARRANGER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }


}
