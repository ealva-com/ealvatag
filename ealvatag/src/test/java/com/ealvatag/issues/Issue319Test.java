package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.audio.mp3.MP3File;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.KeyNotFoundException;
import com.ealvatag.tag.Tag;

import java.io.File;

/**
  Test tag Equality (specifically PartOfSet)
*/
public class Issue319Test extends AbstractTestCase
{
    /*
     * Test File Equality
     * @throws Exception
     */
    public void testTagEquality() throws Exception
    {
        File orig = new File("testdata", "test26.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File file1 = new File("testdata", "test26.mp3");


        MP3File audioFile = (MP3File)AudioFileIO.read(file1);
        Tag tag = audioFile.getTag();

        FieldKey key = FieldKey.DISC_NO;
        try {
          String fieldValue = tag.getFirst(key);
            System.out.println("Fieldvalue is"+fieldValue);
        } catch (KeyNotFoundException e) {
          e.printStackTrace();
        }



    }
}
