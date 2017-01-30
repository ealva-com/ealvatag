package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.datatype.DataTypes;
import ealvatag.tag.id3.ID3v22Frame;
import ealvatag.tag.id3.ID3v22Frames;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.ID3v24Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Set;


/**
 * Test Writing to mp3 always writes the fields in a sensible order  to minimize problems with iTunes and other
 * players.
 */
public class Issue100Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testID3v24WriteFieldsInPreferredOrder() {

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");


            //Create tag
            ID3v24Tag tag = new ID3v24Tag();
            {
                ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_PRIVATE);
                tag.setFrame(frame);
            }
            {
                ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_UNIQUE_FILE_ID);
                tag.setFrame(frame);
            }
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            mp3File.setID3v2Tag(tag);

            //PRIV is listed first because added first
            Set<String> keys = mp3File.getID3v2Tag().frameMap.keySet();
            Iterator<String> iter = keys.iterator();
            Assert.assertEquals("PRIV", iter.next());
            Assert.assertEquals("UFID", iter.next());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(2, mp3File.getID3v2Tag().getFieldCount());

            //After save UFID first because in order of written and UFID should be first
            keys = mp3File.getID3v2Tag().frameMap.keySet();
            iter = keys.iterator();
            Assert.assertEquals("UFID", iter.next());
            Assert.assertEquals("PRIV", iter.next());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testID3v23WriteFieldsInPreferredOrder() {

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");


            //Create tag
            ID3v23Tag tag = new ID3v23Tag();
            {
                ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_PRIVATE);
                tag.setFrame(frame);
            }
            {
                ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_UNIQUE_FILE_ID);
                tag.setFrame(frame);
            }
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            mp3File.setID3v2Tag(tag);

            //PRIV is listed first because added first
            Set<String> keys = mp3File.getID3v2Tag().frameMap.keySet();
            Iterator<String> iter = keys.iterator();
            Assert.assertEquals("PRIV", iter.next());
            Assert.assertEquals("UFID", iter.next());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(2, mp3File.getID3v2Tag().getFieldCount());

            //After save UFID first because in order of written and UFID should be first
            keys = mp3File.getID3v2Tag().frameMap.keySet();
            iter = keys.iterator();
            Assert.assertEquals("UFID", iter.next());
            Assert.assertEquals("PRIV", iter.next());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testID3v22WriteFieldsInPreferredOrder() {

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");


            //Create tag
            ID3v22Tag tag = new ID3v22Tag();
            {
                ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE);
                frame.getBody().setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
                tag.setFrame(frame);
            }
            {
                ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_UNIQUE_FILE_ID);
                tag.setFrame(frame);
            }
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            mp3File.setID3v2Tag(tag);

            //PRIV is listed first because added first
            Set<String> keys = mp3File.getID3v2Tag().frameMap.keySet();
            Iterator<String> iter = keys.iterator();
            Assert.assertEquals("PIC", iter.next());
            Assert.assertEquals("UFI", iter.next());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(2, mp3File.getID3v2Tag().getFieldCount());

            //After save UFID first because in order of written and UFID should be first
            keys = mp3File.getID3v2Tag().frameMap.keySet();
            iter = keys.iterator();
            Assert.assertEquals("UFI", iter.next());
            Assert.assertEquals("PIC", iter.next());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
