package ealvatag;

import java.io.File;
import java.io.IOException;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.audio.exceptions.NoWritePermissionsException;
import ealvatag.audio.exceptions.ReadOnlyFileException;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.TagException;
import ealvatag.tag.TagOptionSingleton;

import static junit.framework.TestCase.*;

public class FilePermissionsTest {

	public static void runWriteWriteProtectedFileWithCheckDisabled(String sourceFile) throws Exception {
		File testFile = createFile(sourceFile);
		try {
			testFile.setWritable(false);
			boolean threwException = false;
			try {
				setFieldAndCommit(testFile, false);
			} catch (NoWritePermissionsException success) {
				threwException = true;
			}
			assertTrue("Expected to throw " + NoWritePermissionsException.class.getSimpleName() + " but didn't",
					threwException);
		} finally {
			testFile.delete();
		}
	}

	public static void runWriteWriteProtectedFileWithCheckEnabled(String sourceFile) throws Exception {
		File testFile = createFile(sourceFile);
		try {
	        testFile.setWritable(false);
	        boolean threwException = false;
	        try {
	        	setFieldAndCommit(testFile, true);
	        } catch(CannotWriteException success) {
	        	threwException=true;
	        }
	        assertTrue("Expected to throw " + CannotWriteException.class.getSimpleName() + " but didn't", threwException);
		} finally {
			testFile.delete();
		}
	}

	public static void runWriteReadOnlyFileWithCheckDisabled(String sourceFile) throws Exception {
		File testFile = createFile(sourceFile);
		assertTrue("Test file must exist", testFile.exists());
		try {
	        testFile.setReadOnly();
	        boolean threwException = false;
	        try {
	        	setFieldAndCommit(testFile, false);
	        } catch(NoWritePermissionsException success) {
	        	threwException=true;
	        }
	        assertTrue("Expected to throw " + NoWritePermissionsException.class.getSimpleName() + " but didn't", threwException);
		} finally {
			testFile.delete();
		}
	}

	private static File createFile(String sourceFile) {
		String[] baseNameAndExt = sourceFile.split("\\.(?=[^\\.]+$)");
		File testFile = AbstractTestCase
				.copyAudioToTmp(sourceFile, new File(baseNameAndExt[0] + "WriteProtected." + baseNameAndExt[1]));
		return testFile;
	}

	private static void setFieldAndCommit(File testFile, boolean performPreCheck) throws CannotReadException,
			IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, FieldDataInvalidException,
			CannotWriteException {
		TagOptionSingleton.getInstance().setCheckIsWritable(performPreCheck);
		try {
			AudioFile aFile = AudioFileIO.read(testFile);
			Tag tag = aFile.getTag();
			tag.setField(FieldKey.ALBUM, "album");
			aFile.commit();
		} finally {
			testFile.setWritable(true);
			TagOptionSingleton.getInstance().setToDefault();
		}
	}


}
