package org.jaudiotagger.audio.generic;

import junit.framework.TestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.flac.FlacTag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;

/**
 * AudioFileWriterTest.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class AudioFileWriterTest extends TestCase {

    private AudioFile audioFile;

    @Override
    protected void setUp() throws IOException {
        final File file = File.createTempFile("AudioFileWriterTest", ".bin");
        try (final FileOutputStream out = new FileOutputStream(file)) {
            for (int i=0; i<100; i++) out.write("Some random stuff\n".getBytes(StandardCharsets.US_ASCII));
        }
        this.audioFile = new AudioFile(file, null, new FlacTag());
        TagOptionSingleton.getInstance().setToDefault();
    }

    @Override
    protected void tearDown() throws Exception {
        if (audioFile.getFile().exists()) {
            audioFile.getFile().delete();
        }
    }

    public void testSizeHasIncreased() throws CannotWriteException, IOException, InterruptedException {
        sizeHasChanged(200);
    }

    public void testSizeHasDecreased() throws CannotWriteException, IOException, InterruptedException {
        sizeHasChanged(-200);
    }

    public void testSizeHasIncreasedWithFileIdentityPreserved() throws CannotWriteException, IOException, InterruptedException {
        TagOptionSingleton.getInstance().setPreserveFileIdentity(true);
        sizeHasChanged(200);
    }

    public void testSizeHasDecreasedWithFileIdentityPreserved() throws CannotWriteException, IOException, InterruptedException {
        TagOptionSingleton.getInstance().setPreserveFileIdentity(true);
        sizeHasChanged(-200);
    }

    private void sizeHasChanged(final int fileSizeDelta) throws CannotWriteException {
        final long originalFileSize = audioFile.getFile().length();
        final AudioFileWriter audioFileWriter = new MockAudioFileWriter(fileSizeDelta);
        audioFileWriter.write(this.audioFile);
        final long fileSize = audioFile.getFile().length();
        assertEquals("File size is not correct", originalFileSize + fileSizeDelta, fileSize);
    }

    public void testCreationTime() throws CannotWriteException, IOException, InterruptedException {
        if (!System.getProperty("os.name").toLowerCase().startsWith("win")) {
            // known to fail under OS X, because of a JDK bug
            // other Unix file systems don't necessarily support creationTime at all
            System.out.println("Skipped testCreationTime(), because we're not on Windows.");
            return;
        }
        creationTime();
    }

    public void testCreationTimeWithFileIdentityPreserved() throws CannotWriteException, IOException, InterruptedException {
        TagOptionSingleton.getInstance().setPreserveFileIdentity(true);
        creationTime();
    }

    public void testFileIdentity() throws Exception {
        try
        {
            if (System.getProperty("os.name").toLowerCase().startsWith("win"))
            {
                System.out.println("Skipped testFileIdentity(), because we're on Windows.");
                return;
            }
            TagOptionSingleton.getInstance().setPreserveFileIdentity(true);
            final Path path = audioFile.getFile().toPath();
            final Long originalInode = (Long) Files.getAttribute(path, "unix:ino");
            final AudioFileWriter audioFileWriter = new MockAudioFileWriter();
            audioFileWriter.write(this.audioFile);
            final Long inode = (Long) Files.getAttribute(path, "unix:ino");
            assertEquals("Inodes do not match", originalInode, inode);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }
    }

    private void creationTime() throws IOException, InterruptedException, CannotWriteException {
        try {
            final Path path = audioFile.getFile().toPath();
            final FileTime originalCreationTime = (FileTime) Files.getAttribute(path, "creationTime");
            // make sure we don't have the same time, in case resolution is only one second
            Thread.sleep(1500);
            final AudioFileWriter audioFileWriter = new MockAudioFileWriter();
            audioFileWriter.write(this.audioFile);
            final FileTime creationTime = (FileTime)Files.getAttribute(path, "creationTime");
            System.out.println("Creation Time:"+originalCreationTime+":"+creationTime);
            assertTrue("Creation time not within 3000 milliseconds of original time",(creationTime.toMillis() - originalCreationTime.toMillis()) < 3000);
        } catch (UnsupportedOperationException e) {
            System.out.println("Accessing creationTime is not supported.");
            e.printStackTrace(System.out);
        }
    }

    private static class MockAudioFileWriter extends AudioFileWriter {

        private final int fileSizeDelta;

        public MockAudioFileWriter() {
            this(0);
        }

        public MockAudioFileWriter(final int fileSizeDelta) {
            this.fileSizeDelta = fileSizeDelta;
        }

        @Override
        protected void deleteTag(final Tag tag, final RandomAccessFile raf, final RandomAccessFile tempRaf) throws CannotReadException, CannotWriteException, IOException {
            // not implemented
        }

        @Override
        protected void writeTag(final AudioFile audioFile, final Tag tag, final RandomAccessFile raf, final RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException {
            // dummy code, just copy from raf to rafTemp
            final long length = raf.length();
            raf.getChannel().transferTo(0, length, rafTemp.getChannel());
            if (fileSizeDelta != 0) {
                // now adjust the size
                rafTemp.setLength(length + fileSizeDelta);
                if (fileSizeDelta > 0) {
                    rafTemp.seek(length);
                    // fill extra with 6es.
                    final byte[] buf = new byte[fileSizeDelta];
                    Arrays.fill(buf, (byte) 6);
                    rafTemp.write(buf);
                }
            }
        }
    }
}
