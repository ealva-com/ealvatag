package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.framebody.FrameBodyTPOS;
import ealvatag.tag.id3.framebody.FrameBodyTRCK;
import ealvatag.tag.id3.framebody.ID3v23FrameBody;
import ealvatag.tag.options.PadNumberOption;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Trackno/DiscNo Position
 */
public class Issue431Test {
    @Before
    public void setup() throws Exception {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testSetTrackNo() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag)tag).getFrame("TRCK");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTRCK frameBodyTrck = (FrameBodyTRCK)frameBody;
        Assert.assertEquals(frameBodyTrck.getText(), "1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'R');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'C');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'K');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '1');
    }


    @Test public void testSetTrackNoWithPaddingAndLengthOne() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'R');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'C');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'K');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '0');
        Assert.assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("01", tag.getFirst(FieldKey.TRACK));
    }

    @Test public void testSetTrackNoWithNoPaddingThenSetPaddingAndLengthOne() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        //Track isnt padded
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag)tag).getFrame("TRCK");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTRCK frameBodyTrck = (FrameBodyTRCK)frameBody;
        Assert.assertEquals(frameBodyTrck.getText(), "1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'R');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'C');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'K');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '1');

        //But if you set padding option from that point on display padded values
        //although not saved to the file as padded
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        Assert.assertEquals("01", tag.getFirst(FieldKey.TRACK));


        //Check the underlying frame
        frame = (ID3v23Frame)((ID3v23Tag)tag).getFrame("TRCK");
        frameBody = (ID3v23FrameBody)frame.getBody();
        frameBodyTrck = (FrameBodyTRCK)frameBody;
        Assert.assertEquals(frameBodyTrck.getText(), "01");

    }

    @Test public void testSetTrackNoWithPaddingAndLengthTwo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("001", tag.getFirst(FieldKey.TRACK));
    }

    @Test public void testSetTrackNoWithPaddingAndLengthThree() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("0001", tag.getFirst(FieldKey.TRACK));
    }

    @Test public void testSetTrackNoWithPaddingAndLengthThreeLargerNumber() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "112");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("0112", tag.getFirst(FieldKey.TRACK));
    }

    @Test public void testSetTrackNoWithNoPaddingAndLengthTwo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
    }


    @Test public void testSetTrackNoAndTotalWithPaddingLengthOne() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        tag.setField(FieldKey.TRACK_TOTAL, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("01", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals("01", tag.getFirst(FieldKey.TRACK_TOTAL));

    }

    @Test public void testSetTrackNoAndTotalWithPaddingLengthTwo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        tag.setField(FieldKey.TRACK_TOTAL, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("001", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals("001", tag.getFirst(FieldKey.TRACK_TOTAL));

    }

    @Test public void testSetTrackNoFlac() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
    }

    @Test public void testSetTrackNoFlacWithPadding() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.flac");
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
    }

    @Test public void testSetPrePaddedTrackNo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "01");
        Assert.assertEquals("01", tag.getFirst(FieldKey.TRACK));
        f.save();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'R');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'C');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'K');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '0');
        Assert.assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("01", tag.getFirst(FieldKey.TRACK));
    }


    @Test public void testSetDiscNo() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag)tag).getFrame("TPOS");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTPOS frameBodyTpos = (FrameBodyTPOS)frameBody;
        Assert.assertEquals(frameBodyTpos.getText(), "1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'O');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'S');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '1');
    }


    @Test public void testSetDiscNoWithPaddingAndLengthOne() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'O');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'S');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '0');
        Assert.assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
    }

    @Test public void testSetDiscNoWithNoPaddingThenSetPaddingAndLengthOne() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        //Track isnt padded
        Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag)tag).getFrame("TPOS");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTPOS frameBodyTpos = (FrameBodyTPOS)frameBody;
        Assert.assertEquals(frameBodyTpos.getText(), "1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'O');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'S');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '1');

        //But if you set padding option from that point on display padded values
        //although not saved to the file as padded
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        Assert.assertEquals("01", tag.getFirst(FieldKey.DISC_NO));


        //Check the underlying frame
        frame = (ID3v23Frame)((ID3v23Tag)tag).getFrame("TPOS");
        frameBody = (ID3v23FrameBody)frame.getBody();
        frameBodyTpos = (FrameBodyTPOS)frameBody;
        Assert.assertEquals(frameBodyTpos.getText(), "01");

    }

    @Test public void testSetDiscNoWithPaddingAndLengthTwo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("001", tag.getFirst(FieldKey.DISC_NO));
    }

    @Test public void testSetDiscNoWithPaddingAndLengthThree() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("0001", tag.getFirst(FieldKey.DISC_NO));
    }

    @Test public void testSetDiscNoWithPaddingAndLengthThreeLargerNumber() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "112");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("0112", tag.getFirst(FieldKey.DISC_NO));
    }

    @Test public void testSetDiscNoWithNoPaddingAndLengthTwo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
    }


    @Test public void testSetDiscNoAndTotalWithPaddingLengthOne() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        tag.setField(FieldKey.DISC_TOTAL, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
        Assert.assertEquals("01", tag.getFirst(FieldKey.DISC_TOTAL));

    }

    @Test public void testSetDiscNoAndTotalWithPaddingLengthTwo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        tag.setField(FieldKey.DISC_TOTAL, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("001", tag.getFirst(FieldKey.DISC_NO));
        Assert.assertEquals("001", tag.getFirst(FieldKey.DISC_TOTAL));

    }

    @Test public void testSetDiscNoFlac() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
    }

    @Test public void testSetDiscNoFlacWithPadding() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.flac");
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
    }

    @Test public void testSetPrePaddedDiscNo() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "01");
        Assert.assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
        f.save();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'O');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'S');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '0');
        Assert.assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
    }

    @Test public void testSetPrePaddedDiscAndDiscTotal() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_NO, "01");
        tag.setField(FieldKey.DISC_TOTAL, "08");
        Assert.assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
        f.save();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'O');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'S');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '0');
        Assert.assertTrue((buffer.get(22) & 0xff) == '1');
        Assert.assertTrue((buffer.get(23) & 0xff) == '/');
        Assert.assertTrue((buffer.get(24) & 0xff) == '0');
        Assert.assertTrue((buffer.get(25) & 0xff) == '8');


        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
    }

    @Test public void testSetPrePaddedDiscTotal() throws Exception {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_TOTAL, "08");
        Assert.assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
        f.save();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'O');
        Assert.assertTrue((buffer.get(13) & 0xff) == 'S');
        Assert.assertTrue((buffer.get(20) & 0xff) == 0);
        Assert.assertTrue((buffer.get(21) & 0xff) == '0');
        Assert.assertTrue((buffer.get(22) & 0xff) == '/');
        Assert.assertTrue((buffer.get(23) & 0xff) == '0');
        Assert.assertTrue((buffer.get(24) & 0xff) == '8');


        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
    }
}
