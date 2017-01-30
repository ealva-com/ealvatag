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

public class Issue084Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testSyncToId3HasInfoOnly() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123Synced.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("artistName\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("albumName\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ALBUM));
                Assert.assertEquals("", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("", tag.getFirst(FieldKey.ALBUM));
                ((WavTag)tag).syncToId3FromInfoIfEmpty();
                Assert.assertEquals("artistName", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("albumName", tag.getFirst(FieldKey.ALBUM));
                f.save();
                f = AudioFileIO.read(testFile);
                tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("artistName\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertTrue(((WavTag)tag).isExistingInfoTag());
                Assert.assertTrue(((WavTag)tag).isExistingId3Tag());
                Assert.assertEquals("artistName", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("artistName", tag.getFirst(FieldKey.ARTIST));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testSyncToInfoHasId3Only() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
                File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126Synced.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                tag.setField(FieldKey.ARTIST, "fred");
                Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
                ((WavTag)tag).syncToInfoFromId3IfEmpty();
                Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
                f.save();
                f = AudioFileIO.read(testFile);
                tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertTrue(((WavTag)tag).isExistingInfoTag());
                Assert.assertTrue(((WavTag)tag).isExistingId3Tag());
                Assert.assertEquals("fred", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    @Test public void testSyncBeforeReadAfterWriteId3Only() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
                File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126SyncedAfterRead.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("", tag.getFirst(FieldKey.ARTIST));
                ((WavTag)tag).syncTagsAfterRead();
                Assert.assertEquals("fred\0", tag.getFirst(FieldKey.ARTIST));
                tag.setField(FieldKey.ARTIST, "jimmy");
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("jimmy", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("jimmy", tag.getFirst(FieldKey.ARTIST));
                ((WavTag)tag).syncTagBeforeWrite();
                Assert.assertEquals("jimmy", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("jimmy", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("jimmy", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                f.save();

            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testAutoSyncBeforeReadId3Only() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY_AND_SYNC);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
                File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126SyncedAfterRead.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred\0", tag.getFirst(FieldKey.ARTIST));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testAutoSyncBeforeReadInfoOnly() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY_AND_SYNC);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
                File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123AutoSyncedAfterRead.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("artistName", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("artistName\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("artistName", tag.getFirst(FieldKey.ARTIST));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testAutoSyncAfterWriteInfoOnly() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY_AND_SYNC);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
                File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123AutoSyncedAfterReadBeforeWrite.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("artistName", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("artistName\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("artistName", tag.getFirst(FieldKey.ARTIST));
                tag.setField(FieldKey.ARTIST, "fred");
                f.save();
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                f = AudioFileIO.read(testFile);
                tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testAutoSyncAfterWriteId3Only() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY_AND_SYNC);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
                File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126AutoSyncedAfterReadBeforeWrite.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred\0", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("fred\0", tag.getFirst(FieldKey.ARTIST));
                tag.setField(FieldKey.ARTIST, "tim");
                f.save();
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                f = AudioFileIO.read(testFile);
                tag = f.getTag().or(NullTag.INSTANCE);
                Assert.assertEquals("tim", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("tim", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                Assert.assertEquals("tim", tag.getFirst(FieldKey.ARTIST));

            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
