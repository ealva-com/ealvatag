package org.jaudiotagger.tag.mp4;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.audio.mp4.atom.Mp4StcoBox;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.utils.tree.DefaultMutableTreeNode;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

/**
 * Mp4StemWriteTagTest.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class Mp4StemWriteTagTest extends TestCase {

    @Override
    public void setUp()
    {
        TagOptionSingleton.getInstance().setToDefault();
    }

    /**
     * Test to write tag data, new tagdata is a larger size than existing data, and too
     * large to fit into the space already allocated to {@code meta} ({@code ilst} + {@code free} atom).
     */
    public void testWriteFileALotLargerSize() throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException, CannotWriteException {
        final File testFile = AbstractTestCase.copyAudioToTmp("test.stem.mp4", new File("testWriteFileALot.stem.mp4"));

        final Mp4AtomTree treeBefore = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        final List<Mp4StcoBox> beforeStcos = treeBefore.getStcos();
        System.out.println("Chunk Offsets before (stco atoms): " + beforeStcos.size());
        // verify that all five tracks were recognized
        assertEquals(5, beforeStcos.size());
        int freeSpace = 0;
        for (final DefaultMutableTreeNode node : treeBefore.getFreeNodes()) {
            freeSpace += ((Mp4BoxHeader)node.getUserObject()).getDataLength();
        }
        System.out.println("Available free space: " + freeSpace);

        // fill up free space
        final AudioFile audioFile = AudioFileIO.read(testFile);
        final char[] chars = new char[freeSpace * 2]; // twice the size of the total available free space
        Arrays.fill(chars, 'C');
        audioFile.getTag().setField(FieldKey.TITLE, new String(chars));
        audioFile.commit();

        final Mp4AtomTree treeAfter = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        final List<Mp4StcoBox> afterStcos = treeAfter.getStcos();
        System.out.println("Chunk Offsets after (stco atoms): " + afterStcos.size());
        assertEquals(beforeStcos.size(), afterStcos.size());

        // verify constant shift
        int shift = -1;
        for (int i=0; i<beforeStcos.size(); i++) {
            final Mp4StcoBox before = beforeStcos.get(i);
            final Mp4StcoBox after = afterStcos.get(i);
            if (shift == -1) shift = getOffsetShift(before, after);
            assertFalse(0 == shift);
            assertEquals(shift, getOffsetShift(before, after));
        }
    }

    private static int getOffsetShift(final Mp4StcoBox before, final Mp4StcoBox after) {
        return before.getFirstOffSet() - after.getFirstOffSet();
    }
}
