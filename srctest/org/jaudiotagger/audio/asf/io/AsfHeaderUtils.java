package org.jaudiotagger.audio.asf.io;

import junit.framework.TestCase;
import org.jaudiotagger.audio.asf.data.ContainerType;

import org.jaudiotagger.audio.asf.data.MetadataContainer;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ChunkContainer;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * 
 * @author Christian Laireiter
 */
public final class AsfHeaderUtils extends TestCase
{

    public final static int BINARY_PRINT_COLUMNS = 20;

    public static String binary2ByteArrayString(final byte[] data) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; data != null && i < data.length; i++) {
            if (i > 0 && i % BINARY_PRINT_COLUMNS == 0) {
                result.append(Utils.LINE_SEPARATOR);
            }
            result.append("0x");
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            result.append(hex);
            if (i < data.length - 1) {
                result.append(',');
            }
        }
        return result.toString();
    }

    public static Chunk findChunk(Collection<Chunk> chunk, GUID chunkGUID) {
        Chunk result = null;
        for (Chunk curr : chunk) {
            if (curr instanceof ChunkContainer) {
                result = findChunk(((ChunkContainer) curr).getChunks(),
                        chunkGUID);
                if (result != null) {
                    break;
                }
            } else {
                if (curr.getGuid().equals(chunkGUID)) {
                    result = curr;
                    break;
                }
            }
        }
        return result;
    }

    public static byte[] getFirstChunk(File file, GUID chunkGUID)
            throws IOException {
        RandomAccessFile asfFile = null;
        try
        {
            asfFile = new RandomAccessFile(file,"r");
            byte[] result = new byte[0];
            AsfHeader readHeader = AsfHeaderReader.readHeader(asfFile);
            Chunk found = findChunk(readHeader.getChunks(), chunkGUID);
            if (found != null) {
                byte[] tmp = new byte[(int) found.getChunkLength().longValue()];
                asfFile.seek(found.getPosition());
                asfFile.readFully(tmp);
                result = tmp;                
            }
            return result;
        }
        finally
        {
            asfFile.close();
        }
    }

    public static MetadataContainer readContainer(File file, ContainerType type)
            throws IOException {
        AsfHeader readHeader = AsfHeaderReader.readHeader(file);
        return readHeader.findMetadataContainer(type);
    }

    /**
     * Test date conversion
     */
    //TODO we dont know this is correct because need an independent way of checking our figures with an ASF file,
    //the previous calculation appeard incorrect.
    public void testDateHeaderConversion()
    {
        Calendar cal = org.jaudiotagger.audio.asf.util.Utils.getDateOf(BigInteger.valueOf(1964448000));
        System.out.println(cal.getTime());
        assertEquals(-11644273555200l,cal.getTimeInMillis());
    }

    /**
     * Test to show the calculation done to derive the DIFF_BETWEEN_ASF_DATE_AND_JAVA_DATE constant
     */
    public void testConversionDateConstant()
    {
        Date date1 = new Date((1601-1900),0,1);
        Date date2 = new Date((1970-1900),0,1);
        assertEquals(11644470000000l,date2.getTime() - date1.getTime());
    }

}
