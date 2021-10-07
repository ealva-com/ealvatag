package ealvatag.audio.aiff;

import ealvatag.audio.exceptions.CannotReadException;
import org.junit.Assert;
import org.junit.Test;

import static ealvatag.audio.aiff.AiffType.AIFC;
import static ealvatag.audio.aiff.AiffType.AIFF;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class AiffFileHeaderTest {

    @Test public void testValidAIFF() throws IOException, CannotReadException {
        final int size = 1234;
        final File aiffFile = createAIFF("FORM", "AIFF", size);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(aiffFile, "rw")) {
			FileChannel fc = randomAccessFile.getChannel();
            final AiffFileHeader header = new AiffFileHeader();
            final AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
            final long remainingBytes = header.readHeader(fc, aiffAudioHeader, aiffFile.toString());
            Assert.assertEquals((long)(size - 4), remainingBytes);
            Assert.assertEquals(AIFF, aiffAudioHeader.getFileType());
        }

        //noinspection ResultOfMethodCallIgnored
        aiffFile.delete();
    }

    @Test public void testInvalidFormatType() throws IOException, CannotReadException {
        final int size = 5762;
        final File aiffFile = createAIFF("FORM", "COOL", size);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(aiffFile, "rw")) {
        	FileChannel fc = randomAccessFile.getChannel();
            new AiffFileHeader().readHeader(fc, new AiffAudioHeader(), aiffFile.toString());
            Assert.fail("Expected " + CannotReadException.class.getSimpleName());
        } catch (CannotReadException e) {
            // expected this
        }

        //noinspection ResultOfMethodCallIgnored
        aiffFile.delete();
    }

    @Test public void testInvalidFormat1() throws IOException, CannotReadException {
        final int size = 34242;
        final File aiffFile = createAIFF("FURM", "AIFF", size);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(aiffFile, "rw")) {
        	FileChannel fc = randomAccessFile.getChannel();
            new AiffFileHeader().readHeader(fc, new AiffAudioHeader(), aiffFile.toString());
            Assert.fail("Expected " + CannotReadException.class.getSimpleName());
        } catch (CannotReadException e) {
            // expected this
        }

        //noinspection ResultOfMethodCallIgnored
        aiffFile.delete();
    }


    @Test public void testInvalidFormat2() throws IOException, CannotReadException {
        final int size = 34234;
        final File aiffFile = createAIFF("FORMA", "AIFF", size);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(aiffFile, "rw")) {
			FileChannel fc = randomAccessFile.getChannel();
            new AiffFileHeader().readHeader(fc, new AiffAudioHeader(), aiffFile.toString());
            Assert.fail("Expected " + CannotReadException.class.getSimpleName());
        } catch (CannotReadException e) {
            // expected this
        }

        //noinspection ResultOfMethodCallIgnored
        aiffFile.delete();
    }

    @Test public void testValidAIFC() throws IOException, CannotReadException {
        final int size = 3452;
        final File aiffFile = createAIFF("FORM", "AIFC", size);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(aiffFile, "rw")) {
			FileChannel fc = randomAccessFile.getChannel();
            final AiffFileHeader header = new AiffFileHeader();
            final AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
            final long remainingBytes = header.readHeader(fc, aiffAudioHeader, aiffFile.toString());
            Assert.assertEquals((long)(size - 4), remainingBytes);
            Assert.assertEquals(AIFC, aiffAudioHeader.getFileType());
        }

        //noinspection ResultOfMethodCallIgnored
        aiffFile.delete();
    }

    private static File createAIFF(final String form, final String formType, final int size) throws IOException {
        final File tempFile = File.createTempFile(AiffFileHeaderTest.class.getSimpleName(), ".aif");
        tempFile.deleteOnExit();

        try (final DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile))) {
            // write format
            out.write(form.getBytes(StandardCharsets.US_ASCII));
            // write size
            out.writeInt(size);
            // write format type
            out.write(formType.getBytes(StandardCharsets.US_ASCII));
            // write remaining random data
            final byte[] remainingData = new byte[size - formType.length()];
            final Random random = new Random();
            random.nextBytes(remainingData);
            out.write(remainingData);
        }
        return tempFile;
    }


}
