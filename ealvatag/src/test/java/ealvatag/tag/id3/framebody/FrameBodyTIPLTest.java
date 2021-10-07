package ealvatag.tag.id3.framebody;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test TIPL
 */
public class FrameBodyTIPLTest {
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

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        Assert.assertEquals("producer", fb.getKeyAtIndex(0));
        Assert.assertEquals("eno,lanois", fb.getValueAtIndex(0));

    }

    @Test public void testCreateFrameBodyodd() {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE_ODD);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        Assert.assertEquals("producer", fb.getKeyAtIndex(0));
        Assert.assertEquals("eno,lanois", fb.getValueAtIndex(0));

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());

    }

    @Test public void testCreateFromIPLS() {
        Exception exceptionCaught = null;
        FrameBodyIPLS fbv3 = FrameBodyIPLSTest.getInitialisedBody();
        FrameBodyTIPL fb = null;
        try {
            fb = new FrameBodyTIPL(fbv3);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("*" + fb.getText() + "*", "*" + FrameBodyIPLSTest.INVOLVED_PEOPLE + "*");
        Assert.assertEquals(2, fb.getNumberOfPairs());
        Assert.assertEquals("producer", fb.getKeyAtIndex(0));
        Assert.assertEquals("eno,lanois", fb.getValueAtIndex(0));

    }

    /**
     * Uses TMCL frame
     *
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test public void testMultiArrangerIDv24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteArrangerv24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        Assert.assertNull(f.getTag().orNull());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARRANGER, "Arranger1");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ARRANGER, "Arranger2");
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        Assert.assertEquals("Arranger1", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
        Assert.assertEquals("Arranger1", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ARRANGER, 0));
        Assert.assertEquals("Arranger2", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ARRANGER, 1));

        f.save();
        f = AudioFileIO.read(testFile);
        Assert.assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ARRANGER).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }


}
