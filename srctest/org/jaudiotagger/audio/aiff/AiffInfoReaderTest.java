package org.jaudiotagger.audio.aiff;

import junit.framework.TestCase;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * AiffInfoReader Test.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class AiffInfoReaderTest extends TestCase {

    public void testWithSomeLocalChunks() throws IOException, CannotReadException {

        final String author = "AUTH4567";
        final String copyright = "(c) 4567";
        final String annotation1 = "ANNO1_67890123456789";
        final String annotation2 = "ANNO2_67890123456789";
        final String name = "NAME456789";
        final PseudoChunk[] pseudoChunks = {new PseudoChunk("NAME", name),
                new PseudoChunk("ANNO", annotation1),
                new PseudoChunk("ANNO", annotation2),
                new PseudoChunk("(c) ", copyright),
                new PseudoChunk("AUTH", author)};
        final File aiff = createAIFF("FORM", "AIFF", pseudoChunks);

        final AiffInfoReader aiffInfoReader = new AiffInfoReader();
        try(FileChannel fc = FileChannel.open(aiff.toPath())) {
            final GenericAudioHeader audioHeader = aiffInfoReader.read(aiff.toPath());
            assertTrue(audioHeader instanceof AiffAudioHeader);
            final AiffAudioHeader aiffAudioHeader = (AiffAudioHeader) audioHeader;
            assertEquals(author, aiffAudioHeader.getAuthor());
            assertEquals(name, aiffAudioHeader.getName());
            assertEquals(copyright, aiffAudioHeader.getCopyright());
            assertEquals(annotation1, aiffAudioHeader.getAnnotations().get(0));
            assertEquals(annotation2, aiffAudioHeader.getAnnotations().get(1));
        }
        aiff.delete();
    }

    public void testWithUnknownChunk() throws IOException, CannotReadException {

        final String author = "AUTH4567";
        final File aiff = createAIFF("FORM", "AIFF", new PseudoChunk("XYZ0", "SOME_STUFF"), new PseudoChunk("AUTH", author));

        final AiffInfoReader aiffInfoReader = new AiffInfoReader();
        try(FileChannel fc = FileChannel.open(aiff.toPath())) {
            final GenericAudioHeader audioHeader = aiffInfoReader.read(aiff.toPath());
            assertTrue(audioHeader instanceof AiffAudioHeader);
            final AiffAudioHeader aiffAudioHeader = (AiffAudioHeader) audioHeader;
            assertEquals(author, aiffAudioHeader.getAuthor());
        }
        aiff.delete();
    }

    private static class PseudoChunk {

        private String chunkType;
        private String text;

        public PseudoChunk(final String chunkType, final String text) {
            this.chunkType = chunkType;
            this.text = text;
        }

        public String getChunkType() {
            return chunkType;
        }

        public String getText() {
            return text;
        }
    }

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
