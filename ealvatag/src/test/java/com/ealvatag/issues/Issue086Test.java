package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.audio.wav.WavOptions;
import com.ealvatag.audio.wav.WavSaveOptions;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;
import com.ealvatag.tag.TagOptionSingleton;
import com.ealvatag.tag.wav.WavTag;

import java.io.File;

/**
 * Test
 */
public class Issue086Test extends AbstractTestCase
{


    public void testEnsureWritingID3SkipBytesWhenChunkNotEven()
    {

        Exception exceptionCaught = null;
        try
        {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
                File testFile = AbstractTestCase.copyAudioToTmp("test126.wav", new File("test126ID3WriteSyncByte.wav"));
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                tag.setField(FieldKey.ARTIST, "fred");
                ((WavTag)tag).syncToInfoFromId3IfEmpty();
                f.commit();
                f = AudioFileIO.read(testFile);
                tag = f.getTag();
                assertEquals("fred", ((WavTag)tag).getID3Tag().getFirst(FieldKey.ARTIST));
                assertTrue(((WavTag)tag).isExistingInfoTag());
                assertTrue(((WavTag)tag).isExistingId3Tag());
                assertEquals("fred", ((WavTag)tag).getInfoTag().getFirst(FieldKey.ARTIST));
                assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

}
