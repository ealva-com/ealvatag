package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Test when write multiple strings using UTF16 with BOM that it writes the BOM
 * for all strings not just the first one
 */
public class Issue327Test {
    @SuppressWarnings("deprecation")
    @Test public void testUTF16BOMMultipleFieldSeperators() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testUTF16BOMMultipleFieldSeperators.mp3"));
        MP3File f = (MP3File)AudioFileIO.read(testFile);
        f.setID3v2Tag(new ID3v23Tag());
        f.getID3v2Tag().addField(FieldKey.MOOD, "For Checking End");
        f.getID3v2Tag().addField(FieldKey.ALBUM_ARTIST, "Ϟ");
        f.getID3v2Tag().addField(FieldKey.ALBUM_ARTIST, "Ϟ");
        f.save();
        ByteBuffer bb = ByteBuffer.allocate(40);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(testFile, "r")) {
			FileChannel fc = randomAccessFile.getChannel();
			fc.read(bb);
			Assert.assertEquals('T', bb.get(10) & 0xFF);
			Assert.assertEquals('P', bb.get(11) & 0xFF);
			Assert.assertEquals('E', bb.get(12) & 0xFF);
			Assert.assertEquals('2', bb.get(13) & 0xFF);

			//BOM for first String
			Assert.assertEquals(0xFF, bb.get(21) & 0xFF);
			Assert.assertEquals(0xFE, bb.get(22) & 0xFF);
			//First String char
			Assert.assertEquals(0xDE, bb.get(23) & 0xFF);
			Assert.assertEquals(0x03, bb.get(24) & 0xFF);
			//Null Separator (2 bytes because of encoding
			Assert.assertEquals(0x00, bb.get(25) & 0xFF);
			Assert.assertEquals(0x00, bb.get(26) & 0xFF);

			//BOM for second String
			Assert.assertEquals(0xFF, bb.get(27) & 0xFF);
			Assert.assertEquals(0xFE, bb.get(28) & 0xFF);
			//Second String char
			Assert.assertEquals(0xDE, bb.get(29) & 0xFF);
			Assert.assertEquals(0x03, bb.get(30) & 0xFF);

			//Next Frame
			Assert.assertEquals(0x54, bb.get(31) & 0xFF);
			Assert.assertEquals(0x58, bb.get(32) & 0xFF);
			Assert.assertEquals(0x58, bb.get(33) & 0xFF);
			Assert.assertEquals(0x58, bb.get(34) & 0xFF);

			fc.close();
		}
		//What does ealvatag read the values back as
        f = (MP3File)AudioFileIO.read(testFile);
        Assert.assertEquals("Ϟ", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST));
        Assert.assertEquals("Ϟ", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ALBUM_ARTIST, 0));
        Assert.assertEquals("Ϟ", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ALBUM_ARTIST, 1));

    }
}
