package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.framebody.FrameBodyCOMM;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * COMM Frames with non-standard lang values
 */
public class Issue273Test {

    @Before
    public void setup() throws Exception {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test Read/Write Comment with funny lang
     */
    @Test public void testCommentWithLanguage() {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        Exception exceptionCaught = null;
        try {
            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.save();

            af = AudioFileIO.read(testFile);
            ID3v23Tag tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            tag.addField(FieldKey.COMMENT, "COMMENTVALUE");
            af.save();

            af = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("COMMENTVALUE", tag.getFirst(FieldKey.COMMENT));
            AbstractID3v2Frame frame = (AbstractID3v2Frame)tag.getFirstField("COMM").orNull();
            Assert.assertNotNull(frame);
            FrameBodyCOMM fb = (FrameBodyCOMM)frame.getBody();
            Assert.assertEquals("eng", fb.getLanguage());
            af.save();


            fb.setLanguage("XXX");
            Assert.assertEquals("XXX", fb.getLanguage());
            af.save();

            af = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("COMMENTVALUE", tag.getFirst(FieldKey.COMMENT));
            frame = (AbstractID3v2Frame)tag.getFirstField("COMM").orNull();
            Assert.assertNotNull(frame);
            fb = (FrameBodyCOMM)frame.getBody();
            Assert.assertEquals("XXX", fb.getLanguage());
            af.save();

            fb.setLanguage("\0\0\0");
            af.save();

            af = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("COMMENTVALUE", tag.getFirst(FieldKey.COMMENT));
            frame = (AbstractID3v2Frame)tag.getFirstField("COMM").orNull();
            Assert.assertNotNull(frame);
            fb = (FrameBodyCOMM)frame.getBody();
            Assert.assertEquals("\0\0\0", fb.getLanguage());
            af.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

    }

    @Test public void testCommentSetGet() {
        FrameBodyCOMM comm = new FrameBodyCOMM();
        comm.setLanguage("XXX");
        Assert.assertEquals("XXX", comm.getLanguage());

        comm = new FrameBodyCOMM();
        comm.setLanguage("\0\0\0");
        Assert.assertEquals("\0\0\0", comm.getLanguage());
    }
}
