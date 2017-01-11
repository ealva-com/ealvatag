package ealvatag.audio.generic;

import ealvatag.audio.AudioFile;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.exceptions.NoWritePermissionsException;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by Paul on 28/01/2016.
 */
public abstract class AudioFileWriter2 extends AudioFileWriter
{
    private static final Logger LOG = LoggerFactory.getLogger(AudioFileWriter2.class);

    /**
     * Delete the tag (if any) present in the given file
     *
     * @param af The file to process
     *
     * @throws CannotWriteException if anything went wrong
     * @throws ealvatag.audio.exceptions.CannotReadException
     */
    @Override
    public void delete(AudioFile af) throws CannotReadException, CannotWriteException
    {
        final File file = af.getFile();
        checkCanWriteAndSize(af, file);
        try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel())
        {
            deleteTag(af.getTag(), channel, file.getAbsolutePath());
        }
        catch (FileNotFoundException e)
        {
            LOG.warn(ErrorMessage.GENERAL_DELETE_FAILED.getMsg(file));
            throw new CannotWriteException(ErrorMessage.GENERAL_DELETE_FAILED.getMsg(file));
        }
        catch (IOException e)
        {
            LOG.warn(ErrorMessage.GENERAL_DELETE_FAILED.getMsg(file, e.getMessage()));
            throw new CannotWriteException(ErrorMessage.GENERAL_DELETE_FAILED.getMsg(file, e.getMessage()));
        }
    }

    private void checkCanWriteAndSize(final AudioFile af, final File file) throws CannotWriteException {
        if (TagOptionSingleton.getInstance().isCheckIsWritable() && !file.canWrite())
        {
            LOG.error(ErrorMessage.NO_PERMISSIONS_TO_WRITE_TO_FILE.getMsg(file));
            throw new CannotWriteException(ErrorMessage.GENERAL_DELETE_FAILED
                    .getMsg(file));
        }

        if (af.getFile().length() <= MINIMUM_FILESIZE)
        {
            throw new CannotWriteException(ErrorMessage.GENERAL_DELETE_FAILED_BECAUSE_FILE_IS_TOO_SMALL
                    .getMsg(file));
        }
    }

    /**
     * Replace with new tag
     *
     * @param af The file we want to process
     * @throws CannotWriteException
     */
    @Override
    public void write(AudioFile af) throws CannotWriteException
    {
        final File file = af.getFile();
        checkCanWriteAndSize(af, file);
        try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel())
        {
            writeTag(af.getTag(), channel, file.getAbsolutePath());
        }
        catch (FileNotFoundException e)
        {
            if (file.exists()) {
                // file exists, permission error
                LOG.warn(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file));
                throw new NoWritePermissionsException(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file));
            } else {
                LOG.warn(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(file));
                throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(file), e);
            }
        }
        catch (IOException e)
        {
            LOG.warn(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE.getMsg(file, e.getMessage()));
            throw new CannotWriteException(e);
        }
    }

    /**
     * Must be implemented by each audio format
     *
     * @param tag
     * @param channel
     * @param fileName
     * @throws CannotReadException
     * @throws CannotWriteException
     */
    protected abstract void deleteTag(Tag tag, FileChannel channel, final String fileName) throws CannotReadException, CannotWriteException;


    public void deleteTag(Tag tag, RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotReadException, CannotWriteException, IOException
    {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }

    /**
     * Must be implemented by each audio format
     *
     * @param tag
     * @param channel
     * @param fileName
     * @throws CannotWriteException
     */
    protected abstract void writeTag(Tag tag, FileChannel channel, final String fileName) throws CannotWriteException;

    protected   void writeTag(AudioFile audioFile, Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException
    {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }
}
