package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;

/**
 * Testing that adding large artwork doesn't overwrite mp3 audio data
 */
public class Issue374Test extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        File testdatadir = new File("testdata");
        int count =0;
        for(File next:testdatadir.listFiles(new MP3FileFilter()))
        {
            count++;
            System.out.println("Checking:"+next.getName());
            Exception caught = null;
            try
            {
                File orig = new File("testdata", next.getName());
                if (!orig.isFile())
                {
                    System.err.println("Unable to test file - not available");
                    return;
                }

                File testFile = AbstractTestCase.copyAudioToTmp(next.getName());


                AudioFile af = AudioFileIO.read(testFile);
                String s1 = af.getAudioHeader().getBitRate();
                String s2 = String.valueOf(af.getAudioHeader().getTrackLength());
                String s3 = String.valueOf(af.getAudioHeader().isVariableBitRate());

                Tag tag = af.getTag();
                if(tag==null|| tag instanceof ID3v1Tag)
                {
                    tag = new ID3v23Tag();
                    af.setTag(tag);
                }
                tag.addField(ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart_large.jpg")));
                af.commit();
                System.out.println("Checking:"+testFile);
                af = AudioFileIO.read(testFile);
                String s11 = af.getAudioHeader().getBitRate();
                String s22 = String.valueOf(af.getAudioHeader().getTrackLength());
                String s33 = String.valueOf(af.getAudioHeader().isVariableBitRate());

                assertEquals(s1,s11);
                assertEquals(s2,s22);
                assertEquals(s3,s33);
                assertTrue(af.getTag().getFields(FieldKey.COVER_ART).size() > 0);

            }
            catch(Exception e)
            {
                caught=e;
                e.printStackTrace();
                assertNull(caught);
            }
        }
        System.out.println("Checked "+count+" files");

    }

    final class MP3FileFilter implements java.io.FileFilter
    {

        /**
         * allows Directories
         */
        private final boolean allowDirectories;

        /**
         * Create a default MP3FileFilter.  The allowDirectories field will
         * default to false.
         */
        public MP3FileFilter()
        {
            this(false);
        }

        /**
         * Create an MP3FileFilter.  If allowDirectories is true, then this filter
         * will accept directories as well as mp3 files.  If it is false then
         * only mp3 files will be accepted.
         *
         * @param allowDirectories whether or not to accept directories
         */
        private MP3FileFilter(final boolean allowDirectories)
        {
            this.allowDirectories = allowDirectories;
        }

        /**
         * Determines whether or not the file is an mp3 file.  If the file is
         * a directory, whether or not is accepted depends upon the
         * allowDirectories flag passed to the constructor.
         *
         * @param file the file to test
         * @return true if this file or directory should be accepted
         */
        public final boolean accept(final File file)
        {
            if(file.getName().equals("corrupt.mp3")||
               file.getName().equals("Issue79.mp3")||
               file.getName().equals("test22.mp3")||
               file.getName().equals("test92.mp3")||
               file.getName().equals("issue52.mp3")||
               file.getName().equals("Issue81.mp3"))
            {
                return false;
            }

            return (((file.getName()).toLowerCase().endsWith(".mp3")) || (file.isDirectory() && (this.allowDirectories)));
        }

    }

}