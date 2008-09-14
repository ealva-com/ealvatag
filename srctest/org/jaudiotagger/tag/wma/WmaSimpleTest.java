package org.jaudiotagger.tag.wma;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.asf.tag.AsfFieldKey;
import org.jaudiotagger.audio.asf.tag.AsfTag;
import org.jaudiotagger.audio.asf.tag.AsfTagTextField;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagFieldKey;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WmaSimpleTest extends AbstractTestCase
{
    public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("32", f.getAudioHeader().getBitRate());
            assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("32000", f.getAudioHeader().getSampleRate());
            assertFalse(f.getAudioHeader().isVariableBitRate());

            assertTrue(f.getTag() instanceof AsfTag);
            AsfTag tag = (AsfTag) f.getTag();
            System.out.println(tag);

            //Ease of use methods for common fields
            assertEquals("artist", tag.getFirstArtist());
            assertEquals("album", tag.getFirstAlbum());
            assertEquals("tracktitle", tag.getFirstTitle());
            assertEquals("comments", tag.getFirstComment());
            assertEquals("1971", tag.getFirstYear());
            assertEquals("3", tag.getFirstTrack());
            assertEquals("genre", tag.getFirstGenre());
        } catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("32", f.getAudioHeader().getBitRate());
            assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("32000", f.getAudioHeader().getSampleRate());
            assertFalse(f.getAudioHeader().isVariableBitRate());

            assertTrue(f.getTag() instanceof AsfTag);
            AsfTag tag = (AsfTag) f.getTag();

            //Write some new values and save
            tag.setArtist("artist2");
            tag.setAlbum("album2");
            tag.setTitle("tracktitle2");
            tag.setComment("comments2");
            tag.setYear("1972");
            tag.setGenre("genre2");
            tag.setTrack("4");
            tag.setCopyright("copyright");
            tag.setRating("rating");
            // set the IsVbr value (can be modified for now)
            tag.set(AsfTag.createTextField(AsfFieldKey.ISVBR.getPublicFieldId(), Boolean.TRUE.toString()));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (AsfTag) f.getTag();

            assertTrue(f.getAudioHeader().isVariableBitRate());
            
            assertEquals("artist2", tag.getFirstArtist());
            assertEquals("album2", tag.getFirstAlbum());
            assertEquals("tracktitle2", tag.getFirstTitle());
            assertEquals("comments2", tag.getFirstComment());
            assertEquals("1972", tag.getFirstYear());
            assertEquals("4", tag.getFirstTrack());
            assertEquals("genre2", tag.getFirstGenre());
            assertEquals("copyright", tag.getFirstCopyright());
            assertEquals("rating", tag.getFirstRating());

            AudioFileIO.delete(f);
            f = AudioFileIO.read(testFile);
            tag = (AsfTag) f.getTag();

            assertFalse(f.getAudioHeader().isVariableBitRate());
            assertTrue(tag.isEmpty());

        } catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testTagFieldKeyWrite()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            // Tests multiple iterations on same file
            for (int i = 0; i < 2; i++)
            {
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                for (TagFieldKey key : TagFieldKey.values())
                {
                    tag.add(AsfTag.createTextField(key.name(), key.name() + "_value_" + i));
                }
                f.commit();
                f = AudioFileIO.read(testFile);
                tag = f.getTag();
                for (TagFieldKey key : TagFieldKey.values())
                {
                    /*
                     * Test value retrieval, using multiple access methods.
                     */
                    String value = key.name() + "_value_" + i;
                    assertEquals("Key under test: " + key.name(), value, tag.getFirst(key.name()));
                    assertEquals("Key under test: " + key.name(), value, tag.getFirst(key));
                    AsfTagTextField atf = (AsfTagTextField) tag.get(key.name()).get(0);
                    assertEquals("Key under test: " + key.name(), value, atf.getContent());
                    atf = (AsfTagTextField) tag.get(key).get(0);
                    assertEquals("Key under test: " + key.name(), value, atf.getContent());
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

}
