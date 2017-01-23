package ealvatag.tag.wma;

import ealvatag.tag.NullTag;
import junit.framework.TestCase;
import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.asf.util.Utils;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;

/**
 * Base class for WMA test cases.<br>
 *
 * @author Christian Laireiter
 */
public abstract class WmaTestCase {
    /**
     * Stores the audio file instance of {@link #testFile}.<br>
     */
    private AudioFile audioFile;

    /**
     * The file name of the source file, from which a copy will be created.
     */
    private String sourceTestFile;

    /**
     * The file on which tests should be performed.<br>
     */
    private File testFile;

    /**
     * Returns the audio file to perform the tests on.<br>
     * @return audio file to perform the tests on.<br>
     * @throws Exception Upon IO errors, or ASF parsing faults.
     */
    public AudioFile getAudioFile() throws Exception
    {
        if (this.audioFile == null)
        {
            this.audioFile = AudioFileIO.read(this.testFile);
        }
        return this.audioFile;
    }

    /**
     * Returns the tag of the {@linkplain #audioFile}.<br>
     * @return the tag of the {@linkplain #audioFile}.<br>
     * @throws Exception from call of  {@link #getAudioFile()}.
     */
    public Tag getTag() throws Exception
    {
        return getAudioFile().getTag().or(NullTag.INSTANCE);
    }

    /**
     * Returns a file for testing purposes.
     * @return file for testing.
     */
    public File prepareTestFile(String fileName)
    {
        Assert.assertNotNull(sourceTestFile);
        File result = null;
        if (!Utils.isBlank(fileName)) {
            result = AbstractTestCase.copyAudioToTmp(sourceTestFile, new File(fileName));
        } else {
            result = AbstractTestCase.copyAudioToTmp(sourceTestFile);
        }
        return result;
    }

    /**
     * Creates the file copy.
     */
    @Before public void setUp() throws Exception
    {
        sourceTestFile = getTestFile();
        Assert.assertNotNull(sourceTestFile);
        this.testFile = prepareTestFile(null);
    }

    abstract String getTestFile();


    /**
     * Deletes the copy.
     */
    @After public void tearDown() throws Exception
    {
        //        this.testFile.deleteField();
    }


}
