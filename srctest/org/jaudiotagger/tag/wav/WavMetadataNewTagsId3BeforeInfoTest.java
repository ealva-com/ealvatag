package org.jaudiotagger.tag.wav;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.audio.wav.WavSaveOptions;
import org.jaudiotagger.audio.wav.WavSaveOrder;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavMetadataNewTagsId3BeforeInfoTest extends AbstractTestCase
{




    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveBoth()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveBothNew.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();


            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag  id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926282L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926570L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(280L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(10L, ((WavTag) tag).getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(18L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileWithMoreMetadataSaveBoth()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataNew.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag  id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", tag.getFirst(FieldKey.ARTIST));
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", tag.getFirst(FieldKey.ALBUM_ARTIST));

            assertEquals(926282L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926718L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(428L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(10L, ((WavTag) tag).getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(18L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }



    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveExistingActiveId3Info()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActiveId3New.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag  id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926298L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926594L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(26L, ((WavTag) tag).getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothing added to ID3 save empty ID3tag
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(34L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActiveId3()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActiveId3New.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag  id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull( ((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(26L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(34L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActiveId32()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test126.wav", new File("test126ModifyMetadataSaveActiveId3New.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertFalse(tag.isExistingInfoTag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull(((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(25L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(33L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            //Replace Id3tag
            ID3v24Tag  id3tag = new ID3v24Tag();
            tag.setID3Tag(id3tag);

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());

            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull( ((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(26L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(34L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Starts of with Id3chunk which is odd but doesnt have padding byte but at end of file
     * so can still read, then we write to it padding bit added and when read/write again we
     * correctly work out ID3chunk is still at end of file.
     */
    public void testFileDeleteWithInfoAndOddLengthData()
    {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test129.wav");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        File testFile = AbstractTestCase.copyAudioToTmp("test129.wav", new File("test128OddData.wav"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.delete();

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }
}
