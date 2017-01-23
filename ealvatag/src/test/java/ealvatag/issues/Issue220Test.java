package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue220Test extends AbstractTestCase
{
    /**
     * Test read mp4 ok without any udta atom (but does have meta atom under trak)
     */
    public void testReadMp4WithoutUdta()
    {
        File orig = new File("testdata", "test41.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test41.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test write mp4 ok without any udta atom (but does have meta atom under trak)
     */
    public void testWriteMp4WithoutUdta()
    {
        File orig = new File("testdata", "test41.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test41.m4a");

            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());

            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST,"FREDDYCOUGAR");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM,"album");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "title");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE,"genre");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR,"year");
            af.save();

            //Read file again okay

            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            af = AudioFileIO.read(testFile);
            assertEquals("FREDDYCOUGAR",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            assertEquals("album",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            assertEquals("title",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            assertEquals("genre",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            assertEquals("year",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test read mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta atom
     * before mvhd atom (and still has the meta atom under trak)
     */
    public void testReadMp4WithUdtaAndMetaHierachy()
    {
        File orig = new File("testdata", "test42.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test42.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            assertEquals("album",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            assertEquals("test42",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

        /**
        * Test write mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta atom
        * before mvhd atom (and still has the meta atom under trak)
        */
       public void testWriteMp4WithUdtaAndMetaHierachy()
       {
           File orig = new File("testdata", "test42.m4a");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }

           File testFile = null;
           Exception exceptionCaught = null;
           try
           {
               testFile = AbstractTestCase.copyAudioToTmp("test42.m4a");

               //Read File okay
               AudioFile af = AudioFileIO.read(testFile);
               af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM,"KARENTAYLORALBUM");
               af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE,"KARENTAYLORTITLE");
               af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE,"KARENTAYLORGENRE");
               af.getTag().or(NullTag.INSTANCE).setField(FieldKey.AMAZON_ID,"12345678");

               af.save();
               System.out.println("All is going well");
               af = AudioFileIO.read(testFile);
               assertEquals("KARENTAYLORALBUM",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
               assertEquals("KARENTAYLORTITLE",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
               assertEquals("KARENTAYLORGENRE",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
               assertEquals("12345678",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.AMAZON_ID));

           }
           catch(Exception e)
           {
               e.printStackTrace();
               exceptionCaught=e;
           }

           assertNull(exceptionCaught);
       }

    /**
             * Test write mp4 ok without any udta atom (but does have meta atom under trak)
             */
            public void testWriteMp4WithUdtaAfterTrackSmaller()
            {
                File orig = new File("testdata", "test44.m4a");
                if (!orig.isFile())
                {
                    System.err.println("Unable to test file - not available");
                    return;
                }

                File testFile = null;
                Exception exceptionCaught = null;
                try
                {
                    testFile = AbstractTestCase.copyAudioToTmp("test44.m4a");

                    //Read File okay
                    AudioFile af = AudioFileIO.read(testFile);


                    //Write file
                    af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE,"ti");
                    af.save();

                    //Read file again okay
                    af = AudioFileIO.read(testFile);
                    assertEquals("ti",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    exceptionCaught=e;
                }

                assertNull(exceptionCaught);
            }


        /**
             * Test write mp4 ok without any udta atom (but does have meta atom under trak)
             */
            public void testWriteMp4WithUdtaAfterTrack()
            {
                File orig = new File("testdata", "test44.m4a");
                if (!orig.isFile())
                {
                    System.err.println("Unable to test file - not available");
                    return;
                }

                File testFile = null;
                Exception exceptionCaught = null;
                try
                {
                    testFile = AbstractTestCase.copyAudioToTmp("test44.m4a");

                    //Read File okay
                    AudioFile af = AudioFileIO.read(testFile);


                    //Write file
                    af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST,"FREDDYCOUGAR");
                    af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM,"album");
                    af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE,"title");
                    af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE,"genre");
                    af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR,"year");
                    af.save();

                    //Read file again okay
                    af = AudioFileIO.read(testFile);
                    assertEquals("FREDDYCOUGAR",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
                    assertEquals("album",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
                    assertEquals("title",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
                    assertEquals("genre",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
                    assertEquals("year",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    exceptionCaught=e;
                }

                assertNull(exceptionCaught);
            }

}
