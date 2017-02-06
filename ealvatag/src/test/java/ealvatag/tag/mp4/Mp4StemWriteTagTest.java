package ealvatag.tag.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.audio.exceptions.ReadOnlyFileException;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import ealvatag.audio.mp4.atom.Mp4StcoBox;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagException;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.mp4.field.Mp4FieldType;
import ealvatag.tag.mp4.field.Mp4TagCoverField;
import ealvatag.tag.mp4.field.Mp4TagTextNumberField;
import ealvatag.utils.tree.DefaultMutableTreeNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Mp4StemWriteTagTest.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class Mp4StemWriteTagTest {

    private static final int TEST_FILE1_SIZE = 1450197;

    @Before public void setUp() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    /**
     * Test to write tag data, new tagdata is a larger size than existing data, and too
     * large to fit into the space already allocated to {@code meta} ({@code ilst} + {@code free} atom).
     */
    @Test public void testWriteOneFieldALotLargerSize()
            throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException, CannotWriteException {

        final File testFile = TestUtil.copyAudioToTmp("test.stem.mp4", new File("testWriteOneFieldALotLarger.stem.mp4"));

        final Mp4AtomTree treeBefore = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        final List<Mp4StcoBox> beforeStcos = treeBefore.getStcos();
        System.out.println("Chunk Offsets before (stco atoms): " + beforeStcos.size());
        // verify that all five tracks were recognized
        Assert.assertEquals(5, beforeStcos.size());
        int freeSpace = 0;
        for (final DefaultMutableTreeNode node : treeBefore.getFreeNodes()) {
            freeSpace += ((Mp4BoxHeader)node.getUserObject()).getDataLength();
        }
        System.out.println("Available free space: " + freeSpace);

        // fill up free space
        final AudioFile audioFile = AudioFileIO.read(testFile);
        final char[] chars = new char[freeSpace * 2]; // twice the size of the total available free space
        Arrays.fill(chars, 'C');
        audioFile.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, new String(chars));
        audioFile.save();

        final Mp4AtomTree treeAfter = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        final List<Mp4StcoBox> afterStcos = treeAfter.getStcos();
        System.out.println("Chunk Offsets after (stco atoms): " + afterStcos.size());
        Assert.assertEquals(beforeStcos.size(), afterStcos.size());

        // verify constant shift
        int shift = -1;
        for (int i = 0; i < beforeStcos.size(); i++) {
            final Mp4StcoBox before = beforeStcos.get(i);
            final Mp4StcoBox after = afterStcos.get(i);
            if (shift == -1) {
                shift = getOffsetShift(before, after);
            }
            Assert.assertFalse(0 == shift);
            Assert.assertEquals(shift, getOffsetShift(before, after));
        }
    }

    private static int getOffsetShift(final Mp4StcoBox before, final Mp4StcoBox after) {
        return before.getFirstOffSet() - after.getFirstOffSet();
    }

    /**
     * Test to write tag data, new tagdata is a larger size than existing data, and too
     * large to fit into the space already allocated to meta (ilst + free atom), but can fit into
     * the second free atom.
     */
    @Test public void testWriteFileAlotLargerSize() {
        Exception exceptionCaught = null;
        try {
            final File testFile = TestUtil.copyAudioToTmp("test.stem.mp4", new File("testWriteFileALot.stem.mp4"));

            //Starting filesize
            Assert.assertEquals(TEST_FILE1_SIZE, testFile.length());

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(tag.createArtworkField(imagedata));

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            //Total FileSize must be larger, as the free atom in meta is only 844 big
            Assert.assertEquals(1450641, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(13, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());

            //Ease of use methods for common fields
            Assert.assertEquals("tagtraum", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("stem_test_track", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK_TOTAL));

            //Lookup by mp4 key
            Assert.assertEquals("tagtraum", tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("stem_test_track", tag.getFirst(Mp4FieldKey.TITLE));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/1", tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            List coverart = tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());

            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0x47, coverArtField.getData()[3] & 0xff);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
