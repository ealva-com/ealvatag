package org.jaudiotagger.tag.real;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class RealReadTagTest extends AbstractTestCase
{

    public void test01() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test01.ra", "Temptation Rag", "Prince's Military Band", "1910 [Columbia A854]");
    }

    public void test02() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test02.ra", "Dixieland Jass Band One Step", "Original Dixieland 'Jass' Band", "1917 [Victor 18255-A]");
    }

    public void test03() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test03.ra", "Here Comes My Daddy Now", "Collins and Harlan", "1913 [Oxford 38528]");
    }

    public void test04() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test04.ra", "A Cat-Astrophe", "Columbia Orchestra", "1919 [Columbia A2855]");
    }

    public void test05ra() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test05.ra", "It Makes My Love Come Down", "Bessie Smith, vocal; James P. Johnson, piano", "1929 (Columbia 14464-D mx148904)");
    }

    public void test05rm() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test05.rm", "It Makes My Love Come Down", "Bessie Smith, vocal; James P. Johnson, piano", "1929 (Columbia 14464-D mx148904)");
    }

    public void test06() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test06.rm", "No Trouble But You", "Ben Bernie & His Hotel Roosevelt Orchestra", "1926 (Brunswick 3171-A)");
    }

    public void test07() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test07.rm", "Is There A Place Up There For Me?", "Paul Tremaine & His Orchestra", "circa 1931 (Columbia Tele-Focal Radio Series 91957)");
    }

    public void test08() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test08.rm", "Let's Say Good Night Till The Morning", " Jack Buchanan and Elsie Randolph", "1926 (Columbia (British) 9147)");
    }

    public void test09() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test09.rm", "Until Today", "Fletcher Henderson and his Orchestra", "1936 (Victor 25373-B)");
    }

    public void test10() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        checkRealTag("test10.rm", "Nobody Cares If I'm Blue", "Annette Hanshaw", "1930 (Harmony 1196-H)");
    }

    public void checkRealTag(String filename, String title, String artist, String comment) throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException
    {
        File testFile = AbstractTestCase.copyAudioToTmp(filename);
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals(3, tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        assertEquals(title, tag.getFirst(FieldKey.TITLE));
        assertEquals(artist, tag.getFirst(FieldKey.ARTIST));
        assertEquals(comment, tag.getFirst(FieldKey.COMMENT));
    }

}
