package org.jaudiotagger.tag.aiff;


import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.FilePermissionsTest;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.chunk.AiffChunkType;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.iff.IffHeaderChunk;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.audio.wav.WavSaveOptions;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v22Tag;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class AiffAudioTagTest extends TestCase {


    public void testReadAiff1() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test119.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif", new File("test119ReadAiffWithoutTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertTrue(tag instanceof AiffTag);
            assertTrue(((AiffTag) tag).getID3Tag() instanceof ID3v22Tag);
            assertFalse(((AiffTag) tag).isExistingId3Tag());
            assertEquals(0L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(0L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testReadAiff2() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test120.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test120.aif", new File("test120ReadAiffWithTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertNotNull(tag);
            assertTrue(tag instanceof AiffTag);
            assertTrue(((AiffTag) tag).isExistingId3Tag());
            assertTrue(tag.getFieldCount() == 10);
            assertEquals("Gary McGath", tag.getFirst(FieldKey.ARTIST));
            assertEquals("None", tag.getFirst(FieldKey.ALBUM));
            assertTrue(tag.getFirst(FieldKey.TITLE).indexOf("Short sample") == 0);
            assertEquals("This is actually a comment.", tag.getFirst(FieldKey.COMMENT));
            assertEquals("2012", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals(2254L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(2246L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(148230L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testReadAiff3() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test121.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif", new File("test121ReadAiffWithoutItunesTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(ah);
            System.out.println(ah.getBitRate());
            assertEquals("2",ah.getChannels());
            System.out.println(ah.getEncodingType());
            assertEquals("44100",ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertEquals(5.0d,((AiffAudioHeader) ah).getPreciseTrackLength());

            System.out.println(tag);
            assertNotNull(tag);
            assertTrue(((AiffTag) tag).isExistingId3Tag());
            assertTrue(tag instanceof AiffTag);
            assertTrue(tag.getFieldCount() == 6);
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            assertEquals("A Rush Of Blood To The Head", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Politik", tag.getFirst(FieldKey.TITLE));
            assertEquals("2002", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals(10274L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(10266L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(882054L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testReadAiff4() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test124.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test124.aif", new File("test124ReadAiffWithoutItunesTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(ah);
            System.out.println(ah.getBitRate());
            assertEquals("2",ah.getChannels());
            System.out.println(ah.getEncodingType());
            assertEquals("44100",ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertEquals(5.0d,((AiffAudioHeader) ah).getPreciseTrackLength());

            System.out.println(tag);
            assertNotNull(tag);
            assertTrue(((AiffTag) tag).isExistingId3Tag());
            assertTrue(tag instanceof AiffTag);
            assertTrue(tag.getFieldCount() == 6);
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            assertEquals("A Rush Of Blood To The Head", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Politik", tag.getFirst(FieldKey.TITLE));
            assertEquals("2002", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals(10274L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(10266L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(12L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteAiff3() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test121.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif", new File("test121WriteAiffWithTagAddPadding.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            assertNotNull(tag);
            assertEquals(10274L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(10266L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(882054L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());

            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "Warmplay");
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            assertEquals(10274L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(10266L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(882054L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());
            System.out.println(f.getTag());

            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "Warmplayer");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getTag());
            assertEquals(10274L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(10266L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(882054L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals("Warmplayer", tag.getFirst(FieldKey.ARTIST));


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteAiffWithoutTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test119.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif", new File("test119WriteAiffWithoutTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            assertNotNull(tag);
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "Warmplay");
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testDeleteAiff3() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test121.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif", new File("test121DeleteTag.aif"));
        try
        {
            final int oldSize = readAIFFFormSize(testFile);
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertNotNull(tag);
            assertNotNull(((AiffTag) tag).getID3Tag());
            assertFalse(tag.isEmpty());
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            AudioFileIO.delete(f);

            f = null;
            final int newSize = readAIFFFormSize(testFile);
            AudioFile f2 = AudioFileIO.read(testFile);
            Tag tag2 = f2.getTag();
            System.out.println(tag2);
            assertNotNull(tag2);
            assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
            assertFalse("FORM chunk size should have changed, but hasn't.", oldSize == newSize);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testDeleteAiff4() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test124.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        // test124.aif is special in that the ID3 chunk is right at the beginning, not the end.
        File testFile = AbstractTestCase.copyAudioToTmp("test124.aif", new File("test124DeleteTag.aif"));

        try
        {
            final List<String> oldChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.TAG.getCode(), oldChunkIds.get(0));
            assertEquals(AiffChunkType.COMMON.getCode(), oldChunkIds.get(1));
            assertEquals(AiffChunkType.SOUND.getCode(), oldChunkIds.get(2));
            assertTrue(oldChunkIds.size() == 3);

            final int oldSize = readAIFFFormSize(testFile);
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertNotNull(tag);
            assertNotNull(((AiffTag)tag).getID3Tag());
            assertFalse(tag.isEmpty());
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            AudioFileIO.delete(f);

            f = null;
            final int newSize = readAIFFFormSize(testFile);
            AudioFile f2 = AudioFileIO.read(testFile);
            Tag tag2 = f2.getTag();
            System.out.println(tag2);
            assertNotNull(tag2);
            assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
            assertFalse("FORM chunk size should have changed, but hasn't.", oldSize == newSize);

            final List<String> newChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.COMMON.getCode(), newChunkIds.get(0));
            assertEquals(AiffChunkType.SOUND.getCode(), newChunkIds.get(1));
            assertTrue(newChunkIds.size() == 2);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteNotLastChunk() {
        Exception exceptionCaught = null;

        // test124.aif is special in that the ID3 chunk is right at the beginning, not the end.
        File orig = new File("testdata", "test124.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test124.aif", new File("test124WriteAiffWithChunkNotAtEnd.aif"));
        try
        {
            final List<String> oldChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.TAG.getCode(), oldChunkIds.get(0));
            assertEquals(AiffChunkType.COMMON.getCode(), oldChunkIds.get(1));
            assertEquals(AiffChunkType.SOUND.getCode(), oldChunkIds.get(2));
            assertTrue(oldChunkIds.size() == 3);

            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            assertEquals(10274L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(10266L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(12L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());
            assertNotNull(tag);
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "Warmplay");
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getTag());

            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "Warmplayer");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getTag());
            assertEquals("Warmplayer", tag.getFirst(FieldKey.ARTIST));
            assertEquals(10274L,((AiffTag) tag).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(10266L,((AiffTag) tag).getSizeOfID3TagOnly());
            assertEquals(882054L,((AiffTag) tag).getStartLocationInFileOfId3Chunk());

            final List<String> newChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.COMMON.getCode(), newChunkIds.get(0));
            assertEquals(AiffChunkType.SOUND.getCode(), newChunkIds.get(1));
            // ID3 TAG should be at end
            assertEquals(AiffChunkType.TAG.getCode(), newChunkIds.get(2));
            assertTrue(newChunkIds.size() == 3);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    private static int readAIFFFormSize(final File file) throws IOException {
        try (final RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(IffHeaderChunk.SIGNATURE_LENGTH);
            return raf.readInt();
        }
    }

    private static List<String> readChunkIds(final File file) throws IOException {
        final List<String> chunkIds = new ArrayList<>();
        try (final RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(IffHeaderChunk.HEADER_LENGTH);
            final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);

            while (raf.getFilePointer() != raf.length()) {
                if (chunkHeader.readHeader(raf)) {
                    chunkIds.add(chunkHeader.getID());
                }
                raf.skipBytes((int) chunkHeader.getSize());
            }
        }
        return chunkIds;
    }

    public void testWriteMetadataAifcWhenSSNDBeforeCOMMChunk() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test135.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test136.aif", new File("test135SSNDBeforeCOMMChunk.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);

            f.getTagOrCreateAndSetDefault().setField(FieldKey.ALBUM, "album");
            f.commit();

            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            assertEquals("album", f.getTag().getFirst(FieldKey.ALBUM));
        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }
    public void testWriteMetadataAifcWithUnknonwExtraChunk() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test136.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test136.aif", new File("test136WriteMetadataWithUnknownExtraChunk.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());

            f.getTagOrCreateAndSetDefault().setField(FieldKey.ALBUM, "album");
            f.commit();

            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());
            assertEquals("album", f.getTag().getFirst(FieldKey.ALBUM));
        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    /** TODO this file had bad zzzz chunk so when we write back unable to read data back because cant find
     * start of ID3 chunk
      */
    public void testWriteMetadataAifcWithJunk() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test137.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test137.aif", new File("test137WriteMetadataWithJunkAtEnd.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);

            f.getTagOrCreateAndSetDefault().setField(FieldKey.ALBUM, "album");
            f.commit();

            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
//            assertEquals("album", f.getTag().getFirst(FieldKey.ALBUM));


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testWriteMetadataAiffWithNameAndAuthorChunks() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test138.aiff");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test138.aiff", new File("test138WriteMetadataWithNameAuthorChunks.aiff"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);

            f.getTagOrCreateAndSetDefault().setField(FieldKey.ALBUM, "album");
            f.commit();

            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            assertEquals("album", f.getTag().getFirst(FieldKey.ALBUM));
        }
        catch (Exception e) {
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
    public void testOddLengthID3ChunkFile() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test144.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY_AND_SYNC);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
        File testFile = AbstractTestCase.copyAudioToTmp("test144.aif", new File("test144Odd.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().deleteField(FieldKey.ACOUSTID_ID);
            f.getTag().deleteField(FieldKey.ACOUSTID_FINGERPRINT);
            f.commit();
            f = AudioFileIO.read(testFile);
            f.getTag().setField(FieldKey.ARTIST, "freddy");
            f.commit();
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag());
            assertEquals(f.getTag().getFirst(FieldKey.ARTIST), "freddy");

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testDeleteOddLengthID3ChunkFile() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test144.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY_AND_SYNC);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
        File testFile = AbstractTestCase.copyAudioToTmp("test144.aif", new File("test144OddDelete.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().deleteField(FieldKey.ACOUSTID_ID);
            f.getTag().deleteField(FieldKey.ACOUSTID_FINGERPRINT);
            f.commit();
            AudioFileIO.delete(f);
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag());
            assertEquals(0,((AiffTag)f.getTag()).getStartLocationInFileOfId3Chunk());
            assertEquals(0,((AiffTag)f.getTag()).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(0,((AiffTag)f.getTag()).getSizeOfID3TagOnly());


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testDeleteAiff4Odd() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test124.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        // test124.aif is special in that the ID3 chunk is right at the beginning, not the end.
        File testFile = AbstractTestCase.copyAudioToTmp("test124.aif", new File("test124DeleteOddTag.aif"));

        try
        {
            final List<String> oldChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.TAG.getCode(), oldChunkIds.get(0));
            assertEquals(AiffChunkType.COMMON.getCode(), oldChunkIds.get(1));
            assertEquals(AiffChunkType.SOUND.getCode(), oldChunkIds.get(2));
            assertTrue(oldChunkIds.size() == 3);

            final int oldSize = readAIFFFormSize(testFile);
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertNotNull(tag);
            assertNotNull(((AiffTag)tag).getID3Tag());
            assertFalse(tag.isEmpty());
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            AudioFileIO.delete(f);

            f = null;
            final int newSize = readAIFFFormSize(testFile);
            AudioFile f2 = AudioFileIO.read(testFile);
            Tag tag2 = f2.getTag();
            System.out.println(tag2);
            assertNotNull(tag2);
            assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
            assertFalse("FORM chunk size should have changed, but hasn't.", oldSize == newSize);

            final List<String> newChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.COMMON.getCode(), newChunkIds.get(0));
            assertEquals(AiffChunkType.SOUND.getCode(), newChunkIds.get(1));
            assertTrue(newChunkIds.size() == 2);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteMetadataAifcWithUnknonwExtraChunkID3DatSizeOdd() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test136.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test136.aif", new File("test136WriteMetadataWithUnknownExtraChunkID3DatSizeOdd.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());

            f.getTagOrCreateAndSetDefault().setField(FieldKey.ALBUM, "albums");
            f.commit();
            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());
            assertEquals("albums", f.getTag().getFirst(FieldKey.ALBUM));
            assertEquals(122, ((AiffTag)f.getTag()).getStartLocationInFileOfId3Chunk());
            assertEquals(154, ((AiffTag)f.getTag()).getEndLocationInFileOfId3Chunk());


            f.getTagOrCreateAndSetDefault().setField(FieldKey.ALBUM, "albuks");
            f.commit();
            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());
            assertEquals("albuks", f.getTag().getFirst(FieldKey.ALBUM));
            assertEquals(122, ((AiffTag)f.getTag()).getStartLocationInFileOfId3Chunk());
            assertEquals(154, ((AiffTag)f.getTag()).getEndLocationInFileOfId3Chunk());

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {
    	
        FilePermissionsTest.runWriteWriteProtectedFileWithCheckDisabled("test121.aif");
	}

    public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {
    	
    	FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("test121.aif");
	}

    public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {
    	
    	FilePermissionsTest.runWriteReadOnlyFileWithCheckDisabled("test121.aif");
	}


}
