package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v11Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Test when write multiple strings using UTF16 with BOM that it writes the BOM
 * for all strings not just the first one
 */
public class Issue327Test extends AbstractTestCase
{
    public void testUTF16BOMMultipleFieldSeperators() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File f = (MP3File)AudioFileIO.read(testFile);
        f.setID3v2Tag(new ID3v23Tag());
        f.getID3v2Tag().addField(FieldKey.MOOD,"For Checking End");
        f.getID3v2Tag().addField(FieldKey.ALBUM_ARTIST,"Ϟ");
        f.getID3v2Tag().addField(FieldKey.ALBUM_ARTIST,"Ϟ");
        f.commit();
        ByteBuffer bb = ByteBuffer.allocate(40);
        FileChannel fc = new RandomAccessFile(testFile,"r").getChannel();
        fc.read(bb);
        assertEquals('T',bb.get(10) & 0xFF);
        assertEquals('P',bb.get(11) & 0xFF);
        assertEquals('E',bb.get(12) & 0xFF);
        assertEquals('2',bb.get(13) & 0xFF);

        //BOM for first String
        assertEquals(0xFF,bb.get(21) & 0xFF);
        assertEquals(0xFE,bb.get(22) & 0xFF);
        //First String char
        assertEquals(0xDE,bb.get(23) & 0xFF);
        assertEquals(0x03,bb.get(24) & 0xFF);
        //Null Separator (2 bytes because of encoding
        assertEquals(0x00,bb.get(25) & 0xFF);
        assertEquals(0x00,bb.get(26) & 0xFF);

        //BOM for second String
        assertEquals(0xFF,bb.get(27) & 0xFF);
        assertEquals(0xFE,bb.get(28) & 0xFF);
        //Second String char
        assertEquals(0xDE,bb.get(29) & 0xFF);
        assertEquals(0x03,bb.get(30) & 0xFF);

         //Next Frame
        assertEquals(0x54,bb.get(31) & 0xFF);
        assertEquals(0x58,bb.get(32) & 0xFF);
        assertEquals(0x58,bb.get(33) & 0xFF);
        assertEquals(0x58,bb.get(34) & 0xFF);

        fc.close();

        //What does jaudiotagger read the values back as
        f = (MP3File)AudioFileIO.read(testFile);
        assertEquals("Ϟ",f.getTag().getFirst(FieldKey.ALBUM_ARTIST));
        assertEquals("Ϟ",f.getTag().getValue(FieldKey.ALBUM_ARTIST,0));
        assertEquals("Ϟ",f.getTag().getValue(FieldKey.ALBUM_ARTIST,1));

    }
}