package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

/**
 * Test
 */
public class Issue146Test extends AbstractTestCase
{
    public void testIssue146() throws Exception
    {
        File orig = new File("testdata", "test158.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File file = AbstractTestCase.copyAudioToTmp("test158.mp3");

        if (file.exists())
        {
            AudioFile afile = AudioFileIO.read(file);
            Tag tag = afile.getTagOrCreateDefault();

            System.out.println(tag);
            if (tag == null)
            {
                System.out.println("Tag is null");
                tag = afile.createDefaultTag();
                System.out.println(tag);
                afile.setTag(tag);
            }
            tag.setField(FieldKey.TITLE,"好好学习");
            afile.commit();
            System.out.println(tag.getValue(FieldKey.TITLE, 0)+tag.getValue(FieldKey.TITLE, 0).getBytes().length);
            tag = AudioFileIO.read(file).getTag();
            System.out.println(tag.getValue(FieldKey.TITLE, 0)+tag.getValue(FieldKey.TITLE, 0).getBytes().length);
        }
    }
}
