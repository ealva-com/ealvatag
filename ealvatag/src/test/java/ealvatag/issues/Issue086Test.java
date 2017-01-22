package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.wav.WavOptions;
import ealvatag.audio.wav.WavSaveOptions;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.wav.WavTag;

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
                f.save();
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
