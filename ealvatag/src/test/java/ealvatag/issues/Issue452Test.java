package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue452Test {
    @Test public void testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test110.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File
                testFile =
                TestUtil.copyAudioToTmp("test110.mp3", new File("testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding.mp3"));
        MP3File mp3File = new MP3File(testFile);
        System.out.println("AudioHeaderBefore" + mp3File.getMP3AudioHeader());
        System.out.println("AlbumField:" + mp3File.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
        Assert.assertEquals(0x54adf, mp3File.getMP3AudioHeader().getMp3StartByte());

        mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "newalbum");
        mp3File.save();
        mp3File = new MP3File(testFile);
        Assert.assertEquals(0x54adf, mp3File.getMP3AudioHeader().getMp3StartByte());
        System.out.println("AudioHeaderAfter" + mp3File.getMP3AudioHeader());
        System.out.println("AlbumField" + mp3File.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }


}
