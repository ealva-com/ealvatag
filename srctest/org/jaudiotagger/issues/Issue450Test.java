package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyUrlLink;
import org.jaudiotagger.tag.id3.framebody.FrameBodyWOAR;

import java.io.File;

/**
 * Test
 */
public class Issue450Test extends AbstractTestCase
{
    public void testSaveUrl() throws Exception
    {
        File orig = new File("testdata", "test108.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }
        File testFile = AbstractTestCase.copyAudioToTmp("test108.mp3");
        MP3File mp3file = (MP3File)AudioFileIO.read(testFile);
        AbstractID3v2Frame frame = (AbstractID3v2Frame)mp3file.getID3v2TagAsv24().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        assertNotNull(frame);

        frame = (AbstractID3v2Frame)mp3file.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        assertNotNull(frame);
        assertEquals(FrameBodyWOAR.class,frame.getBody().getClass());
        FrameBodyWOAR fb = (FrameBodyWOAR)frame.getBody();
        System.out.println(fb.getUrlLink());

        mp3file.setID3v2Tag(new ID3v23Tag(mp3file.getID3v2TagAsv24()));
        frame = (AbstractID3v2Frame)mp3file.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        assertNotNull(frame);
        assertEquals(FrameBodyWOAR.class,frame.getBody().getClass());
        fb = (FrameBodyWOAR)frame.getBody();
        System.out.println(fb.getUrlLink());
        mp3file.commit();




    }

}
