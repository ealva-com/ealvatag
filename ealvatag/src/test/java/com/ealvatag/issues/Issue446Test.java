package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.mp3.MP3File;
import com.ealvatag.tag.id3.AbstractID3v2Frame;
import com.ealvatag.tag.id3.ID3v23Frame;
import com.ealvatag.tag.id3.ID3v23Tag;
import com.ealvatag.tag.id3.ID3v24Tag;
import com.ealvatag.tag.id3.framebody.FrameBodyTXXX;

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

    public void testWriteReplayGain() throws Exception
    {
        File orig = new File("testdata", "test110.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test110.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();

        AbstractID3v2Frame frame = new ID3v23Frame();
        byte b = 1; //If ISO-8859-1 is used this byte should be $00, if Unicode is used it should be $01.
        FrameBodyTXXX fb;
        fb = new FrameBodyTXXX(b, "REPLAYGAIN_TRACK_GAIN", "+67.89 dB");
        frame.setBody(fb);
        v2Tag.setFrame(frame);
    }

}
