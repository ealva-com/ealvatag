package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.id3.framebody.FrameBodyTRDA;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * TDRA - TDRC
 */
public class Issue435Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testConvertV23TRDAToV24TRDC() {
        Throwable e = null;
        try {
            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TRDA);
            FrameBodyTRDA fb = new FrameBodyTRDA((byte)0, "2008");
            frame.setBody(fb);

            File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setToDefault();
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.getTagOrSetNewDefault();
            ((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).setFrame(frame);
            af.save();

            af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setToDefault();
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            af.getConvertedTagOrSetNewDefault();
            af.save();
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v24Tag);
            Assert.assertTrue(((ID3v24Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC") instanceof AbstractID3v2Frame);

            TagOptionSingleton.getInstance().setToDefault();
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }
}
