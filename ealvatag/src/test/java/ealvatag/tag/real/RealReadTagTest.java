package ealvatag.tag.real;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class RealReadTagTest {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void test01() throws Exception {
        checkRealTag("test01.ra", "Temptation Rag", "Prince's Military Band", "1910 [Columbia A854]");
    }

    private void checkRealTag(String filename, String title, String artist, String comment) throws Exception {
        File testFile = TestUtil.copyAudioToTmp(filename);
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals(3,
                            tag.getFieldCount()); // If this line fails we need to update our test as the RealMedia tag parser has been augmented
        Assert.assertEquals(title, tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(artist, tag.getFirst(FieldKey.ARTIST));
        Assert.assertEquals(comment, tag.getFirst(FieldKey.COMMENT));
    }

    @Test public void test02() throws Exception {
        checkRealTag("test02.ra", "Dixieland Jass Band One Step", "Original Dixieland 'Jass' Band", "1917 [Victor 18255-A]");
    }

    @Test public void test03() throws Exception {
        checkRealTag("test03.ra", "Here Comes My Daddy Now", "Collins and Harlan", "1913 [Oxford 38528]");
    }

    @Test public void test04() throws Exception {
        checkRealTag("test04.ra", "A Cat-Astrophe", "Columbia Orchestra", "1919 [Columbia A2855]");
    }

    @Test public void test05ra() throws Exception {
        checkRealTag("test05.ra",
                     "It Makes My Love Come Down",
                     "Bessie Smith, vocal; James P. Johnson, piano",
                     "1929 (Columbia 14464-D mx148904)");
    }

    @Test public void test05rm() throws Exception {
        checkRealTag("test05.rm",
                     "It Makes My Love Come Down",
                     "Bessie Smith, vocal; James P. Johnson, piano",
                     "1929 (Columbia 14464-D mx148904)");
    }

    @Test public void test06() throws Exception {
        checkRealTag("test06.rm", "No Trouble But You", "Ben Bernie & His Hotel Roosevelt Orchestra", "1926 (Brunswick 3171-A)");
    }

    @Test public void test07() throws Exception {
        checkRealTag("test07.rm",
                     "Is There A Place Up There For Me?",
                     "Paul Tremaine & His Orchestra",
                     "circa 1931 (Columbia Tele-Focal Radio Series 91957)");
    }

    @Test public void test08() throws Exception {
        checkRealTag("test08.rm",
                     "Let's Say Good Night Till The Morning",
                     " Jack Buchanan and Elsie Randolph",
                     "1926 (Columbia (British) 9147)");
    }

    @Test public void test09() throws Exception {
        checkRealTag("test09.rm", "Until Today", "Fletcher Henderson and his Orchestra", "1936 (Victor 25373-B)");
    }

    @Test public void test10() throws Exception {
        checkRealTag("test10.rm", "Nobody Cares If I'm Blue", "Annette Hanshaw", "1930 (Harmony 1196-H)");
    }

}
