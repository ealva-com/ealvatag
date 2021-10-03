package ealvatag.audio.real;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.AudioFileReader;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.Utils;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.TagFieldContainer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Real Media File Format: Major Chunks: .RMF PROP MDPR CONT DATA INDX
 */
public class RealFileReader extends AudioFileReader
{
    @Override
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        final GenericAudioHeader rv = new GenericAudioHeader();
        final RealChunk prop = findPropChunk(raf);
        final DataInputStream dis = prop.getDataInputStream();
        final int objVersion = Utils.readUint16(dis);
        if (objVersion == 0)
        {
            final long maxBitRate = Utils.readUint32(dis) / 1000;
            final long avgBitRate = Utils.readUint32(dis) / 1000;
            Utils.readUint32(dis);
            Utils.readUint32(dis);
            Utils.readUint32(dis);
            final int duration = (int)Utils.readUint32(dis) / 1000;
            Utils.readUint32(dis);
            Utils.readUint32(dis);
            Utils.readUint32(dis);
            Utils.readUint16(dis);
            Utils.readUint16(dis);
            rv.setBitRate((int) avgBitRate);
            rv.setPreciseLength(duration);
            rv.setVariableBitRate(maxBitRate != avgBitRate);
        }
        return rv;
    }

    private RealChunk findPropChunk(RandomAccessFile raf) throws IOException, CannotReadException
    {
        RealChunk.readChunk(raf);
        final RealChunk prop = RealChunk.readChunk(raf);
        return prop;
    }

    private RealChunk findContChunk(RandomAccessFile raf) throws IOException, CannotReadException
    {
        RealChunk.readChunk(raf);
        RealChunk.readChunk(raf);
        RealChunk rv = RealChunk.readChunk(raf);
        while (!rv.isCONT()) rv = RealChunk.readChunk(raf);
        return rv;
    }

    @Override
    protected TagFieldContainer getTag(RandomAccessFile raf, final boolean ignoreArtwork) throws CannotReadException, IOException
    {
        final RealChunk cont = findContChunk(raf);
        final DataInputStream dis = cont.getDataInputStream();
        final String title = Utils.readString(dis, Utils.readUint16(dis));
        final String author = Utils.readString(dis, Utils.readUint16(dis));
        final String copyright = Utils.readString(dis, Utils.readUint16(dis));
        final String comment = Utils.readString(dis, Utils.readUint16(dis));
        final RealTag rv = new RealTag();
        // NOTE: frequently these fields are off-by-one, thus the crazy
        // logic below...
        try
        {
            rv.addField(FieldKey.TITLE,(title.length() == 0 ? author : title));
            rv.addField(FieldKey.ARTIST, title.length() == 0 ? copyright : author);
            rv.addField(FieldKey.COMMENT,comment);
        }
        catch(FieldDataInvalidException fdie)
        {
            throw new RuntimeException(fdie);
        }
        return rv;
    }

}

