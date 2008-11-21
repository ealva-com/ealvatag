package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.exceptions.CannotReadException;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Map;
import java.util.HashMap;

/**
 * HdlrBox ( Handler box),
 * <p/>
 * Describes the type of metadata in the following ilst or minf atom
 */
public class Mp4HdlrBox extends AbstractMp4Box
{
    public static final int VERSION_FLAG_LENGTH = 1;
    public static final int OTHER_FLAG_LENGTH = 3;
    public static final int RESERVED_FLAG_LENGTH = 4;
    public static final int HANDLER_LENGTH = 4;

    private int reserved;       //32 bit
    private String handlerType;    //4 bytes;
    private String name;           //Variable length
    private MediaDataType mediaDataType;

    private static Map<String, MediaDataType> mediaDataTypeMap;

    static
    {
        //Create maps to speed up lookup from raw value to enum
        mediaDataTypeMap = new HashMap<String, MediaDataType>();
        for (MediaDataType next : MediaDataType.values())
        {
            mediaDataTypeMap.put(next.getId(), next);
        }
    }
    /**
     * DataBuffer must start from from the start of the body
     *
     * @param header     header info
     * @param dataBuffer data of box (doesnt include header data)
     */
    public Mp4HdlrBox(Mp4BoxHeader header, ByteBuffer dataBuffer)
    {
        this.header = header;
        this.dataBuffer = dataBuffer;
    }

    public void processData() throws CannotReadException
    {
        //Skip other flags
        dataBuffer.position(dataBuffer.position() + VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH + RESERVED_FLAG_LENGTH);


        CharsetDecoder decoder = Charset.forName("ISO-8859-1").newDecoder();
        try
        {
            handlerType = decoder.decode((ByteBuffer) dataBuffer.slice().limit(HANDLER_LENGTH)).toString();
        }
        catch (CharacterCodingException cee)
        {
            //Ignore

        }

        //To get huma readble name
        mediaDataType = mediaDataTypeMap.get( handlerType);
    }

    public String gethandlerType()
    {
        return handlerType;
    }

    public MediaDataType getMedaiDataType()
    {
        return mediaDataType;
    }

    public String toString()
    {
        String s = "handlerType:" + handlerType + ":human readable:"+mediaDataType.getDescription();
        return s;
    }

    public static enum MediaDataType
    {
        ODSM("odsm", "ObjectDescriptorStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        CRSM("crsm", "ClockReferenceStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        SDSM("sdsm", "SceneDescriptionStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        M7SM("m7sm", "MPEG7Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        OCSM("ocsm", "ObjectContentInfoStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        IPSM("ipsm", "IPMP Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        MJSM("mjsm", "MPEG-J Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        MDIR("mdir", "Apple Meta Data iTunes Reader"),
        MP7B("mp7b", "MPEG-7 binary XML"),
        MP7T("mp7t", "MPEG-7 XML"),
        VIDE("vide", "Video Track"),
        SOUN("soun", "Sound Track"),
        HINT("hint", "Hint Track"),
        APPL("appl", "Apple specific"),
        META("meta", "Timed Metadata track - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),;

        private String id;
        private String description;


        MediaDataType(String id, String description)
        {
            this.id = id;
            this.description=description;
        }

        public String getId()
        {
            return id;
        }

        public String getDescription()
        {
            return description;
        }
    }

}
