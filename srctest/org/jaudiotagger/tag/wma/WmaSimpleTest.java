package org.jaudiotagger.tag.wma;

import org.jaudiotagger.tag.TagField;

import org.jaudiotagger.audio.asf.tag.AsfTagTextField;

import org.jaudiotagger.tag.TagFieldKey;

import org.jaudiotagger.tag.Tag;

import org.jaudiotagger.audio.asf.tag.AsfFieldKey;

import org.jaudiotagger.audio.asf.tag.AsfTag;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;
import java.util.List;

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


            assertTrue(f.getTag() instanceof AsfTag);
            AsfTag tag = (AsfTag) f.getTag();

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
            tag.setCopyRight("copyright");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (AsfTag) f.getTag();

            assertEquals("artist2", tag.getFirstArtist());
            assertEquals("album2", tag.getFirstAlbum());
            assertEquals("tracktitle2", tag.getFirstTitle());
            assertEquals("comments2", tag.getFirstComment());
            assertEquals("1972", tag.getFirstYear());
            assertEquals("4", tag.getFirstTrack());
            assertEquals("genre2", tag.getFirstGenre());
            assertEquals("copyright", tag.getFirst(AsfFieldKey.COPYRIGHT.toString()));
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
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            for (TagFieldKey key : TagFieldKey.values())
            {
                tag.add(AsfTag.createTextField(key.name(), key.name() + "_value"));
            }
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            for (TagFieldKey key : TagFieldKey.values())
            {
                /*
                 * Test value retrieval, using multiple access methods.
                 */
                String value = key.name() + "_value";
                assertEquals("Key under test: " + key.name(), value, tag.getFirst(key.name()));
                assertEquals("Key under test: " + key.name(), value, tag.getFirst(key));
                AsfTagTextField atf = (AsfTagTextField)tag.get(key.name()).get(0);
                assertEquals("Key under test: " + key.name(), value, atf.getContent());
                atf = (AsfTagTextField)tag.get(key).get(0);
                assertEquals("Key under test: " + key.name(), value, atf.getContent());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    //    public void testMultiValueWrite()
    //    {
    //        Exception exceptionCaught = null;
    //        TagFieldKey lastKey = TagFieldKey.ALBUM;
    //        try
    //        {
    //            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
    //            AudioFile f = AudioFileIO.read(testFile);
    //            Tag tag = f.getTag();
    //            for (TagFieldKey key : TagFieldKey.values())
    //            {
    //                // reverse order, because non multivalue fields will overwrite previous values.
    //                tag.add(AsfTag.createTextField(key.name(), key.name() + "_value3"));
    //                tag.add(AsfTag.createTextField(key.name(), key.name() + "_value2"));
    //                tag.add(AsfTag.createTextField(key.name(), key.name() + "_value"));
    //            }
    //            f.commit();
    //            f = AudioFileIO.read(testFile);
    //            tag = f.getTag();
    //            for (TagFieldKey key : TagFieldKey.values())
    //            {
    //                lastKey = key;
    //                /*
    //                 * Test value retrieval, using multiple access methods.
    //                 */
    //                String value = key.name() + "_value";
    //                String value2 = key.name() + "_value2";
    //                String value3 = key.name() + "_value3";
    //                List<TagField> list = tag.get(key);
    //                assertFalse(list.isEmpty());
    //                assertTrue(list.get(0) instanceof AsfTagTextField);
    //                assertEquals(value, ((AsfTagTextField) list.get(0)).getContent());
    //                AsfFieldKey fieldKey = AsfFieldKey.getAsfFieldKey(key.name());
    //                if (fieldKey == null || fieldKey.isMultiValued())
    //                {
    //                    assertEquals("Key:" + key.name(), 3, list.size());
    //                    assertTrue(list.get(1) instanceof AsfTagTextField);
    //                    assertEquals(value2, ((AsfTagTextField) list.get(1)).getContent());
    //                    assertTrue(list.get(2) instanceof AsfTagTextField);
    //                    assertEquals(value3, ((AsfTagTextField) list.get(2)).getContent());
    //                }
    //            }
    //        } catch (Exception e)
    //        {
    //            System.err.println("Key under test: " + lastKey.name());
    //            e.printStackTrace();
    //            exceptionCaught = e;
    //        }
    //        assertNull("Key: " + lastKey.name(), exceptionCaught);
    //    }
    //    
    //    public static void main(String[] args)
    //    {
    //        WmaSimpleTest t = new WmaSimpleTest();
    //        t.testMultiValueWrite();
    //    }
}
