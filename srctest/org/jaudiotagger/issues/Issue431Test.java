package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPOS;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRCK;
import org.jaudiotagger.tag.id3.framebody.ID3v23FrameBody;
import org.jaudiotagger.tag.options.PadNumberOption;
import org.jaudiotagger.tag.reference.ID3V2Version;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Trackno/DiscNo Position
 */
public class Issue431Test extends AbstractTestCase
{
    public void testSetTrackNo() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.commit();

        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.TRACK));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag) tag).getFrame("TRCK");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTRCK frameBodyTrck = (FrameBodyTRCK)frameBody;
        assertEquals(frameBodyTrck.getText(),"1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'R');
        assertTrue((buffer.get(12) & 0xff) == 'C');
        assertTrue((buffer.get(13) & 0xff) == 'K');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '1');
    }


    public void testSetTrackNoWithPaddingAndLengthOne() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"1");
        f.commit();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'R');
        assertTrue((buffer.get(12) & 0xff) == 'C');
        assertTrue((buffer.get(13) & 0xff) == 'K');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '0');
        assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("01", tag.getFirst(FieldKey.TRACK));
    }

    public void testSetTrackNoWithNoPaddingThenSetPaddingAndLengthOne() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        //Track isnt padded
        assertEquals("1", tag.getFirst(FieldKey.TRACK));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag) tag).getFrame("TRCK");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTRCK frameBodyTrck = (FrameBodyTRCK)frameBody;
        assertEquals(frameBodyTrck.getText(),"1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'R');
        assertTrue((buffer.get(12) & 0xff) == 'C');
        assertTrue((buffer.get(13) & 0xff) == 'K');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '1');

        //But if you set padding option from that point on display padded values
        //although not saved to the file as padded 
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        assertEquals("01", tag.getFirst(FieldKey.TRACK));


        //Check the underlying frame
        frame = (ID3v23Frame)((ID3v23Tag) tag).getFrame("TRCK");
        frameBody = (ID3v23FrameBody)frame.getBody();
        frameBodyTrck = (FrameBodyTRCK)frameBody;
        assertEquals(frameBodyTrck.getText(),"01");

    }

    public void testSetTrackNoWithPaddingAndLengthTwo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("001", tag.getFirst(FieldKey.TRACK));
    }

    public void testSetTrackNoWithPaddingAndLengthThree() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("0001", tag.getFirst(FieldKey.TRACK));
    }

    public void testSetTrackNoWithPaddingAndLengthThreeLargerNumber() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"112");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("0112", tag.getFirst(FieldKey.TRACK));
    }

    public void testSetTrackNoWithNoPaddingAndLengthTwo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.TRACK));
    }


    public void testSetTrackNoAndTotalWithPaddingLengthOne() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"1");
        tag.setField(FieldKey.TRACK_TOTAL,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("01", tag.getFirst(FieldKey.TRACK));
        assertEquals("01", tag.getFirst(FieldKey.TRACK_TOTAL));

    }

    public void testSetTrackNoAndTotalWithPaddingLengthTwo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"1");
        tag.setField(FieldKey.TRACK_TOTAL,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("001", tag.getFirst(FieldKey.TRACK));
        assertEquals("001", tag.getFirst(FieldKey.TRACK_TOTAL));

    }

    public void testSetTrackNoFlac() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.TRACK));
    }

    public void testSetTrackNoFlacWithPadding() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK, "1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.TRACK));
    }

    public void testSetPrePaddedTrackNo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.TRACK,"01");
        assertEquals("01", tag.getFirst(FieldKey.TRACK));
        f.commit();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'R');
        assertTrue((buffer.get(12) & 0xff) == 'C');
        assertTrue((buffer.get(13) & 0xff) == 'K');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '0');
        assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("01", tag.getFirst(FieldKey.TRACK));
    }


    public void testSetDiscNo() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.commit();

        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag) tag).getFrame("TPOS");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTPOS frameBodyTpos = (FrameBodyTPOS)frameBody;
        assertEquals(frameBodyTpos.getText(),"1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'O');
        assertTrue((buffer.get(13) & 0xff) == 'S');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '1');
    }


    public void testSetDiscNoWithPaddingAndLengthOne() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"1");
        f.commit();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'O');
        assertTrue((buffer.get(13) & 0xff) == 'S');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '0');
        assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
    }

    public void testSetDiscNoWithNoPaddingThenSetPaddingAndLengthOne() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        //Track isnt padded
        assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

        //Check the underlying frame
        ID3v23Frame frame = (ID3v23Frame)((ID3v23Tag) tag).getFrame("TPOS");
        ID3v23FrameBody frameBody = (ID3v23FrameBody)frame.getBody();
        FrameBodyTPOS frameBodyTpos = (FrameBodyTPOS)frameBody;
        assertEquals(frameBodyTpos.getText(),"1");

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'O');
        assertTrue((buffer.get(13) & 0xff) == 'S');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '1');

        //But if you set padding option from that point on display padded values
        //although not saved to the file as padded 
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        assertEquals("01", tag.getFirst(FieldKey.DISC_NO));


        //Check the underlying frame
        frame = (ID3v23Frame)((ID3v23Tag) tag).getFrame("TPOS");
        frameBody = (ID3v23FrameBody)frame.getBody();
        frameBodyTpos = (FrameBodyTPOS)frameBody;
        assertEquals(frameBodyTpos.getText(),"01");

    }

    public void testSetDiscNoWithPaddingAndLengthTwo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("001", tag.getFirst(FieldKey.DISC_NO));
    }

    public void testSetDiscNoWithPaddingAndLengthThree() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("0001", tag.getFirst(FieldKey.DISC_NO));
    }

    public void testSetDiscNoWithPaddingAndLengthThreeLargerNumber() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_THREE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"112");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("0112", tag.getFirst(FieldKey.DISC_NO));
    }

    public void testSetDiscNoWithNoPaddingAndLengthTwo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
    }


    public void testSetDiscNoAndTotalWithPaddingLengthOne() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"1");
        tag.setField(FieldKey.DISC_TOTAL,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
        assertEquals("01", tag.getFirst(FieldKey.DISC_TOTAL));

    }

    public void testSetDiscNoAndTotalWithPaddingLengthTwo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_TWO_ZERO);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"1");
        tag.setField(FieldKey.DISC_TOTAL,"1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("001", tag.getFirst(FieldKey.DISC_NO));
        assertEquals("001", tag.getFirst(FieldKey.DISC_TOTAL));

    }

    public void testSetDiscNoFlac() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
    }

    public void testSetDiscNoFlacWithPadding() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
        TagOptionSingleton.getInstance().setPadNumbers(true);
        TagOptionSingleton.getInstance().setPadNumberTotalLength(PadNumberOption.PAD_ONE_ZERO);

        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO, "1");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
    }

    public void testSetPrePaddedDiscNo() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"01");
        assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
        f.commit();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'O');
        assertTrue((buffer.get(13) & 0xff) == 'S');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '0');
        assertTrue((buffer.get(22) & 0xff) == '1');


        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
    }

    public void testSetPrePaddedDiscAndDiscTotal() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_NO,"01");
        tag.setField(FieldKey.DISC_TOTAL,"08");
        assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
        f.commit();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'O');
        assertTrue((buffer.get(13) & 0xff) == 'S');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '0');
        assertTrue((buffer.get(22) & 0xff) == '1');
        assertTrue((buffer.get(23) & 0xff) == '/');
        assertTrue((buffer.get(24) & 0xff) == '0');
        assertTrue((buffer.get(25) & 0xff) == '8');


        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
    }

    public void testSetPrePaddedDiscTotal() throws Exception
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.DISC_TOTAL,"08");
        assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
        f.commit();

        //Frame Header
        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'O');
        assertTrue((buffer.get(13) & 0xff) == 'S');
        assertTrue((buffer.get(20) & 0xff) == 0);
        assertTrue((buffer.get(21) & 0xff) == '0');
        assertTrue((buffer.get(22) & 0xff) == '/');
        assertTrue((buffer.get(23) & 0xff) == '0');
        assertTrue((buffer.get(24) & 0xff) == '8');


        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertEquals("08", tag.getFirst(FieldKey.DISC_TOTAL));
    }
}
