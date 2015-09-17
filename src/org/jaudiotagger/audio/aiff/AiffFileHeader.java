package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.logging.Hex;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Aiff File Header definitions
 */
public class AiffFileHeader
{
    public static int SIGNATURE_LENGTH = 4;
    public static int SIZE_LENGTH = 4;
    public static int TYPE_LENGTH = 4;
    public static int HEADER_LENGTH = SIGNATURE_LENGTH + SIZE_LENGTH + TYPE_LENGTH;

    public static final String AIFF_SIGNATURE = "FORM";

    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff.AudioFileHeader");

    public AiffFileHeader()
    {

    }

    public long readHeader(RandomAccessFile raf, AiffAudioHeader aiffAudioHeader) throws IOException, CannotReadException
    {
        ByteBuffer headerData = ByteBuffer.allocate(HEADER_LENGTH);
        int bytesRead = raf.getChannel().read(headerData);
        headerData.position(0);

        if (bytesRead < HEADER_LENGTH)
        {
            throw new IOException("AIFF:Unable to read required number of databytes read:" + bytesRead + ":required:" + HEADER_LENGTH);
        }

        if(Utils.readFourBytesAsChars(headerData).equals(AIFF_SIGNATURE))
        {
            //Read Size
            long bytesRemaining  = headerData.getInt();
            logger.severe("Reading AIFF header size:" + bytesRemaining + " (" + Hex.asHex(bytesRemaining)+ ")"  );

            //Read FileType
            if (!readFileType(headerData, aiffAudioHeader))
            {
                throw new CannotReadException("Invalid AIFF file: Incorrect file type info");
            }
            bytesRemaining -= TYPE_LENGTH;
            return bytesRemaining;
        }
        else
        {
            throw new CannotReadException("Not an AIFF file: incorrect signature");
        }
    }

    /*  Reads the file type.
 *  If it is not a valid file type, returns false.
 */
    private boolean readFileType(ByteBuffer bytes, AiffAudioHeader aiffAudioHeader ) throws IOException
    {
        String type = Utils.readFourBytesAsChars(bytes);
        if (AiffType.AIFF.getCode().equals(type))
        {
            aiffAudioHeader.setFileType(AiffType.AIFF);
            return true;
        }
        else if (AiffType.AIFC.getCode().equals(type))
        {
            aiffAudioHeader.setFileType(AiffType.AIFC);
            return true;
        }
        else
        {
            return false;
        }
    }
}
