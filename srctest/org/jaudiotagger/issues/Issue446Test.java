package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;

import java.io.File;
import java.util.Iterator;

/**
 * Test
 */
public class Issue446Test extends AbstractTestCase
{
    public void testReadReplayGain() throws Exception
    {
        boolean isMatchedPeak=false;
        boolean isMatchedGain=false;

        Exception ex=null;
        File orig = new File("testdata", "test110.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test110.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = mp3File.getID3v2TagAsv24();

        Iterator<AbstractID3v2Frame> i = v24Tag.getFrameOfType("TXXX");
        while(i.hasNext())
        {
            FrameBodyTXXX fb = (FrameBodyTXXX)((AbstractID3v2Frame)i.next()).getBody();
            if(fb.getDescription().equals("replaygain_track_peak"))
            {
                System.out.println(fb.getText());
                assertEquals("0,999969",fb.getText());
                isMatchedPeak=true;
            }
            else if(fb.getDescription().equals("replaygain_track_gain"))
            {
                System.out.println(fb.getText());
                assertEquals("-6,81 dB",fb.getText());
                isMatchedGain=true;
            }
        }
        assertTrue(isMatchedPeak);
        assertTrue(isMatchedGain);



    }


}
