package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24FieldKey;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.exceptions.CannotReadException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.RandomAccessFile;
import java.awt.image.BufferedImage;

/**
 * Test Creating Temp file when filename < 3
 */
public class Issue258Test extends AbstractTestCase
{
    /**
     * Test write of mp3 with very short filename
     */
    public void testWriteToShortMp3File()
    {
        File orig = new File("testdata", "01.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("01.mp3");

            //Read File, and write tag cause padding to be adjusted and temp file created
            AudioFile af = AudioFileIO.read(testFile);
            Tag t = af.getTagOrCreateAndSetDefault();
            t.setArtist("fred");
            af.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test write to mp4 with very short file name
     */
    public void testWriteToShortMp4File()
    {
        File orig = new File("testdata", "01.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("01.m4a");   

            //Read File
            AudioFile af = AudioFileIO.read(testFile);            
            Tag t = af.getTagOrCreateAndSetDefault();
            t.setArtist("fred");
            af.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }
}
