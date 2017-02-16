package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.wav.WavOptions;
import ealvatag.audio.wav.WavSaveOptions;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.wav.WavTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue086Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testEnsureWritingID3SkipBytesWhenChunkNotEven() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
                File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126ID3WriteSyncByte.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                tag.setField(FieldKey.ARTIST, "fred");
                ((WavTag)tag).syncToInfoFromId3IfEmpty();
                f.save();
                f = AudioFileIO.read(testFile);
                tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getValue(FieldKey.ARTIST).or(""));
                Assert.assertTrue(((WavTag)tag).isExistingInfoTag());
                Assert.assertTrue(((WavTag)tag).isExistingId3Tag());
                Assert.assertEquals("fred", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred", ((WavTag)tag).getInfoTag().getValue(FieldKey.ARTIST).or(""));
                Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred", tag.getValue(FieldKey.ARTIST).or(""));

            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

}
