package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test
 */
public class Issue452Test extends AbstractTestCase
{
    public void testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test110.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test110.mp3");
        MP3File mp3File = new MP3File(testFile);
        System.out.println("AudioHeaderBefore"+mp3File.getMP3AudioHeader());
        System.out.println("AlbumField:"+mp3File.getTag().getFirst(FieldKey.ALBUM));
        assertEquals(0x544bc, mp3File.getMP3AudioHeader().getMp3StartByte());

        mp3File.getTag().setField(FieldKey.ALBUM,"newalbum");
        mp3File.commit();
        mp3File = new MP3File(testFile);
        assertEquals(0x544bc, mp3File.getMP3AudioHeader().getMp3StartByte());
        System.out.println("AudioHeaderAfter"+mp3File.getMP3AudioHeader());
        System.out.println("AlbumField"+mp3File.getTag().getFirst(FieldKey.ALBUM));
    }


}
