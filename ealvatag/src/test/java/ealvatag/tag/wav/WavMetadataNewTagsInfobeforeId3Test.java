package ealvatag.tag.wav;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.Utils;
import ealvatag.audio.wav.WavOptions;
import ealvatag.audio.wav.WavSaveOptions;
import ealvatag.audio.wav.WavSaveOrder;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v24Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavMetadataNewTagsInfobeforeId3Test {

    @Before
    public void setup() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveBoth() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveBothNew.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);


            Assert.assertTrue(tag.isExistingInfoTag());

            assertThat(tag.getInfoTag().getStartLocationInFile(), is(926264L));
            assertThat(tag.getInfoTag().getEndLocationInFile(), is(926560L));
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertThat(tag.getInfoTag().getStartLocationInFile(), is(926264L));
            assertThat(tag.getInfoTag().getEndLocationInFile(), is(926552L));
            Assert.assertEquals(280L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(10L,
                                tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertThat(tag.getStartLocationInFileOfId3Chunk(), is(926552L));
            Assert.assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileWithMoreMetadataSaveBoth() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataNew.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(926264L, ((WavTag)tag).getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, ((WavTag)tag).getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, ((WavTag)tag).getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, ((WavTag)tag).getSizeOfID3TagOnly());
            Assert.assertEquals(0L, ((WavTag)tag).getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, ((WavTag)tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(((WavTag)tag).getInfoTag());
            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ALBUM_ARTIST));

            Assert.assertEquals(926264L, ((WavTag)tag).getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926700L, ((WavTag)tag).getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(428L, ((WavTag)tag).getInfoTag().getSizeOfTag());
            Assert.assertEquals(10L,
                                ((WavTag)tag).getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            Assert.assertEquals(926700L, ((WavTag)tag).getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(18L, ((WavTag)tag).getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveExistingActiveId3Info() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActiveId3New.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(tag.isExistingInfoTag());

            Assert.assertEquals(926264L, ((WavTag)tag).getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, ((WavTag)tag).getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, ((WavTag)tag).getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, ((WavTag)tag).getSizeOfID3TagOnly());
            Assert.assertEquals(0L, ((WavTag)tag).getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, ((WavTag)tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(((WavTag)tag).getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertEquals(926264L, ((WavTag)tag).getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, ((WavTag)tag).getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, ((WavTag)tag).getInfoTag().getSizeOfTag());
            Assert.assertEquals(26L, ((WavTag)tag).getSizeOfID3TagOnly());
            Assert.assertEquals(926560L, ((WavTag)tag).getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(34L, ((WavTag)tag).getSizeOfID3TagIncludingChunkHeader());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveActiveId3() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActiveId3New.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(tag.isExistingInfoTag());

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(((WavTag)tag).getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertTrue(tag.isExistingId3Tag());

            Assert.assertNull(((WavTag)tag).getInfoTag().getStartLocationInFile());
            Assert.assertNull(((WavTag)tag).getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0L, ((WavTag)tag).getInfoTag().getSizeOfTag());
            Assert.assertEquals(26L, ((WavTag)tag).getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, ((WavTag)tag).getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(34L, ((WavTag)tag).getSizeOfID3TagIncludingChunkHeader());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveActiveId32() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126ModifyMetadataSaveActiveId3New.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertFalse(tag.isExistingInfoTag());

            Assert.assertNull(((WavTag)tag).getInfoTag().getStartLocationInFile());
            Assert.assertNull(((WavTag)tag).getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0L, ((WavTag)tag).getInfoTag().getSizeOfTag());
            Assert.assertEquals(25L, ((WavTag)tag).getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, ((WavTag)tag).getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(33L, ((WavTag)tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(((WavTag)tag).getInfoTag());

            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertTrue(tag.isExistingId3Tag());

            Assert.assertNull(((WavTag)tag).getInfoTag().getStartLocationInFile());
            Assert.assertNull(((WavTag)tag).getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0L, ((WavTag)tag).getInfoTag().getSizeOfTag());
            Assert.assertEquals(26L, ((WavTag)tag).getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, ((WavTag)tag).getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(34L, ((WavTag)tag).getSizeOfID3TagIncludingChunkHeader());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Starts of with Id3chunk which is odd but doesnt have padding byte but at end of file
     * so can still read, then we write to it padding bit added and when read/write again we
     * correctly work out ID3chunk is still at end of file.
     */
    @Test public void testFileDeleteWithInfoAndOddLengthData() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test129.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        File testFile = TestUtil.copyAudioToTmp("test129.wav", new File("test128OddData.wav"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.deleteFileTag();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }
}
