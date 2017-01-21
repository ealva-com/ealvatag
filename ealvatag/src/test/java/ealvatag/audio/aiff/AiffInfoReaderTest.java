package ealvatag.audio.aiff;

import ealvatag.audio.GenericAudioHeader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * AiffInfoReader Test.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class AiffInfoReaderTest {

    @Test public void testWithSomeLocalChunks() throws Exception {

        final String author = "AUTH4567";
        final String copyright = "(c) 4567";
        final String annotation1 = "ANNO1_67890123456789";
        final String annotation2 = "ANNO2_67890123456789";
        final String name = "NAME456789";
        final PseudoChunk[] pseudoChunks = {
                new PseudoChunk("NAME", name),
                new PseudoChunk("ANNO", annotation1),
                new PseudoChunk("ANNO", annotation2),
                new PseudoChunk("(c) ", copyright),
                new PseudoChunk("AUTH", author)
        };
        final File aiff = createAIFF("FORM", "AIFF", pseudoChunks);

        final AiffInfoReader aiffInfoReader = new AiffInfoReader();
        try (FileChannel fc = new RandomAccessFile(aiff, "rw").getChannel()) {
            final GenericAudioHeader audioHeader = aiffInfoReader.read(fc, aiff.getAbsolutePath());
            Assert.assertTrue(audioHeader instanceof AiffAudioHeader);
            final AiffAudioHeader aiffAudioHeader = (AiffAudioHeader)audioHeader;
            Assert.assertEquals(author, aiffAudioHeader.getAuthor());
            Assert.assertEquals(name, aiffAudioHeader.getName());
            Assert.assertEquals(copyright, aiffAudioHeader.getCopyright());
            Assert.assertEquals(annotation1, aiffAudioHeader.getAnnotations().get(0));
            Assert.assertEquals(annotation2, aiffAudioHeader.getAnnotations().get(1));
        }
        //noinspection ResultOfMethodCallIgnored
        aiff.delete();
    }

    @Test public void testWithUnknownChunk() throws Exception {

        final String author = "AUTH4567";
        final File aiff = createAIFF("FORM", "AIFF", new PseudoChunk("XYZ0", "SOME_STUFF"), new PseudoChunk("AUTH", author));

        final AiffInfoReader aiffInfoReader = new AiffInfoReader();
        try (FileChannel fc = new RandomAccessFile(aiff, "rw").getChannel()) {
            final GenericAudioHeader audioHeader = aiffInfoReader.read(fc, aiff.getAbsolutePath());
            Assert.assertTrue(audioHeader instanceof AiffAudioHeader);
            final AiffAudioHeader aiffAudioHeader = (AiffAudioHeader)audioHeader;
            Assert.assertEquals(author, aiffAudioHeader.getAuthor());
        }
        //noinspection ResultOfMethodCallIgnored
        aiff.delete();
    }

    private static class PseudoChunk {

        private String chunkType;
        private String text;

        PseudoChunk(final String chunkType, final String text) {
            this.chunkType = chunkType;
            this.text = text;
        }

        String getChunkType() {
            return chunkType;
        }

        public String getText() {
            return text;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static File createAIFF(final String form, final String formType, final PseudoChunk... chunks) throws IOException {
        final File tempFile = File.createTempFile(AiffFileHeaderTest.class.getSimpleName(), ".aif");
        tempFile.deleteOnExit();

        try (final DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile))) {

            // create some chunks
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final DataOutputStream dataOut = new DataOutputStream(bout);

            for (final PseudoChunk chunk : chunks) {
                dataOut.write(chunk.getChunkType().getBytes(StandardCharsets.US_ASCII));
                dataOut.writeInt(chunk.getText().length());
                dataOut.write(chunk.getText().getBytes(StandardCharsets.US_ASCII));
            }
            dataOut.flush();

            // write header

            // write format
            out.write(form.getBytes(StandardCharsets.US_ASCII));
            // write size
            out.writeInt(bout.size() + 4);
            // write format type
            out.write(formType.getBytes(StandardCharsets.US_ASCII));
            out.write(bout.toByteArray());
        }
        return tempFile;
    }

}
