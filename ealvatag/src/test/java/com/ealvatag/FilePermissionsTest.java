package com.ealvatag;

import java.io.File;
import java.io.IOException;

import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.audio.exceptions.CannotReadException;
import com.ealvatag.audio.exceptions.CannotWriteException;
import com.ealvatag.audio.exceptions.InvalidAudioFrameException;
import com.ealvatag.audio.exceptions.NoWritePermissionsException;
import com.ealvatag.audio.exceptions.ReadOnlyFileException;
import com.ealvatag.tag.FieldDataInvalidException;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;
import com.ealvatag.tag.TagException;
import com.ealvatag.tag.TagOptionSingleton;

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
