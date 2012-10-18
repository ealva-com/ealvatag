package org.jaudiotagger.audio.aiff;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class AiffFileReader extends AudioFileReader {

    private enum FileType {
        AIFFTYPE,
        AIFCTYPE
    }
    
    /* Fixed value for first 4 bytes */
    private static final int[] sigByte =
       { 0X46, 0X4F, 0X52, 0X4D };

    /* Type of AIFF file */
    private FileType fileType;
    
    /* InputStream that reads the file sequentially */
//    private DataInputStream inStream;
    
    public AiffFileReader () {
    }
    
    
    public AiffFileReader (RandomAccessFile raf) {
//        inStream= new DataInputStream (new AiffInputStream (raf));
    }



    @Override
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf)
            throws CannotReadException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Tag getTag(RandomAccessFile raf) throws CannotReadException,
            IOException {
        logger.info("getTag called");
        byte sigBuf[] = new byte[4];
        raf.read(sigBuf);
        for (int i = 0; i < 4; i++) {
            if (sigBuf[i] != sigByte[i]) {
                throw new CannotReadException ("Not an AIFF file: incorrect signature");
            }
        }
        long bytesRemaining = AiffUtil.readUINT32(raf);
        
        // Read the file type.
        if (!readFileType (raf)) {
            throw new CannotReadException ("Invalid AIFF file: Incorrect file type info");
        }
        bytesRemaining -= 4;
        
        AiffTag tag = new AiffTag();
        while (bytesRemaining > 0) {
            if (!readChunk (raf, tag, bytesRemaining)) {
                break;
            }
        }
        return tag;
        
    }
    
    /*  Reads the file type.   
     *  Broken out from parse().
     *  If it is not a valid file type, returns false.
     */
    private boolean readFileType (RandomAccessFile raf) throws IOException
    {
        String typ = AiffUtil.read4Chars (raf);
        if ("AIFF".equals (typ)) {
            fileType = FileType.AIFFTYPE;
            return true;
        }
        else if ("AIFC".equals (typ)) {
            fileType = FileType.AIFCTYPE;
            return true;
        }
        else {
            return false;
        }
    }

    /** Reads an AIFF Chunk.
     * 
     */
     protected boolean readChunk 
           (RandomAccessFile raf, AiffTag tag, long bytesRemaining) 
             throws IOException
     {
        Chunk chunk = null;
        ChunkHeader chunkh = new ChunkHeader ();
        if (!chunkh.readHeader(raf)) {
            return false;
        }
        int chunkSize = (int) chunkh.getSize ();
        bytesRemaining -= chunkSize + 8;
        
        String id = chunkh.getID ();
        if ("FVER".equals (id)) {
            chunk = new FormatVersionChunk (chunkh, raf, tag);
        }
//        else if ("APPL".equals (id)) {
//            chunk = new ApplicationChunk (this, chunkh, _dstream);
//            // Any number of application chunks is ok
//        }
//        else if ("COMM".equals (id)) {
//            if (commonChunkSeen) {
//                dupChunkError (info, "Common");
//            }
//            chunk = new CommonChunk (this, chunkh, _dstream);
//            commonChunkSeen = true;
//        }
//        else if ("SSND".equals (id)) {
//            // Watch for multiple sound chunks
//            if (soundChunkSeen) {
//                dupChunkError (info, "Sound");
//            }
//            else {
//                chunk = new SoundDataChunk (this, chunkh, _dstream);
//                soundChunkSeen = true;
//            }
//        }
//        else if ("COMT".equals (id)) {
//            if (commentsChunkSeen) {
//                dupChunkError (info, "Comments");
//            }
//            chunk = new CommentsChunk (this, chunkh, _dstream);
//            commentsChunkSeen = true;
//        }
//        else if ("INST".equals (id)) {
//            if (instrumentChunkSeen) {
//                dupChunkError (info, "Instrument");
//            }
//            chunk = new InstrumentChunk (this, chunkh, _dstream);
//            instrumentChunkSeen = true;
//        }
//        else if ("MARK".equals (id)) {
//            if (markerChunkSeen) {
//                dupChunkError (info, "Marker");
//            }
//            else {
//                chunk = new MarkerChunk (this, chunkh, _dstream);
//                markerChunkSeen = true;
//            }
//        }
//        else if ("MIDI".equals (id)) {
//            chunk = new MidiChunk (this, chunkh, _dstream);
//            // Any number of MIDI chunks are allowed
//        }
//        else if ("NAME".equals (id)) {
//            if (nameChunkSeen) {
//                dupChunkError (info, "Name");
//            }
//            else {
//                chunk = new NameChunk (this, chunkh, _dstream);
//                nameChunkSeen = true;
//            }
//        }
//        else if ("AUTH".equals (id)) {
//            if (authorChunkSeen) {
//                dupChunkError (info, "Author");
//            }
//            else {
//                chunk = new AuthorChunk (this, chunkh, _dstream);
//                authorChunkSeen = true;
//            }
//        }
//        else if ("(c) ".equals (id)) {
//            if (copyrightChunkSeen) {
//                dupChunkError (info, "Copyright");
//            }
//            else {
//                chunk = new CopyrightChunk (this, chunkh, _dstream);
//                copyrightChunkSeen = true;
//            }
//        }
//        else if ("AESD".equals (id)) {
//            if (audioRecChunkSeen) {
//                dupChunkError (info, "Audio Recording");
//            }
//            else {
//                chunk = new AudioRecChunk (this, chunkh, _dstream);
//                audioRecChunkSeen = true;
//            }
//        }
//        else if ("SAXL".equals (id)) {
//            chunk = new SaxelChunk (this, chunkh, _dstream);
//            // Multiple saxel chunks are ok 
//        }
//        else if ("ANNO".equals (id)) {
//            chunk = new AnnotationChunk (this, chunkh, _dstream);
//            // Multiple annotations are OK
//        }

        if (chunk != null) {
            if (!chunk.readChunk ()) {
                return false;
            }
        }
        else {
            // Other chunk types are legal, just skip over them
            raf.skipBytes (chunkSize);
        }
        if ((chunkSize & 1) != 0) {
            // Must come out to an even byte boundary
            raf.skipBytes(1);
            --bytesRemaining;
        }
        return true;   
     }

}
