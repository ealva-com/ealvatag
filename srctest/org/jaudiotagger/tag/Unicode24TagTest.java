package org.jaudiotagger.tag;

import java.io.IOException;

import junit.textui.TestRunner;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.InvalidAudioFrameException;
import org.jaudiotagger.audio.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;

public class Unicode24TagTest extends AbstractTestCase {

    public void testUTF8WithNullTerminator () throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, RuntimeException {
        MP3File mp3File  = new MP3File (copyAudioToTmp("testV24-comments-utf8.mp3"));
        AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
        assertNotNull (id3v2Tag);
        AbstractID3v2Frame frame  = (AbstractID3v2Frame) id3v2Tag.getFrame("COMM");
        assertNotNull (frame);
        AbstractTagFrameBody frameBody = frame.getBody();
        assertTrue (frameBody instanceof FrameBodyCOMM);
        FrameBodyCOMM commFrameBody = (FrameBodyCOMM) frameBody;

        //String borodin = "\u0411\u043e\u0440\u043e\u0434\u0438\u043d";
        byte UTF8_ENCODING = (byte) TextEncoding.UTF_8;
        String language = "eng";
        String comment = "some comment here";
        String description = "cc";
        assertEquals (UTF8_ENCODING,commFrameBody.getTextEncoding());
        assertEquals (language,commFrameBody.getLanguage ());
        assertEquals (description,commFrameBody.getDescription () );
        assertEquals (comment,commFrameBody.getText () );

        ID3v24Frame newFrame = new ID3v24Frame(ID3v24Frames.FRAME_ID_COMMENT);
        FrameBodyCOMM targetFrameBody  = ( FrameBodyCOMM)newFrame.getBody();
        targetFrameBody.setTextEncoding(UTF8_ENCODING);
        targetFrameBody.setLanguage(language);
        targetFrameBody.setDescription(description);
        targetFrameBody.setText(comment);
        assertEquals (UTF8_ENCODING,targetFrameBody.getTextEncoding() );
        assertEquals (language,targetFrameBody.getLanguage ());
        assertEquals (description,targetFrameBody.getDescription () );
        assertEquals (comment,targetFrameBody.getText());

        assertEquals (targetFrameBody,commFrameBody );

    }

    public static void main (String [] args) throws Exception {
        TestRunner.run(Unicode24TagTest.class);
    }
}
