package org.jaudiotagger.audio.aiff.chunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Known compression types that can be used with AIFF, taken from https://en.wikipedia.org/wiki/Audio_Interchange_File_Format
 *
 * Note SOWT is not a compression format but it uses AIFF-C to allow it store music data in Little-Endian order, itr only affects audfio data
 * not the other chunks such as metadata.
 *
 */
public enum AiffCompressionType
{
    NONE("NONE", "not compressed","big-endian", "Apple", true),
    RAW("raw ", "PCM 8-bit","offset-binary", "Apple", false),
    TWOS("twos", "PCM 16-bit","twos-complement little-endian", "Apple", false),
    SOWT("sowt", "not compressed","little-endian", "Apple", true),
    fl32("fl32", "PCM 32-bit","floating point,", "Apple", false),
    ll64("fl64", "PCM 64-bit","floating point", "Apple", false),
    IN24("in24", "PCM 24-bit","integer", "Apple", false),
    IN32("in32", "PCM 32-bit","integer", "Apple", false),
    alaw("alaw","ALaw 2:1", "8-bit ITU-T G.711 A-law", "Apple", false),
    ulaw("ulaw","µLaw 2:1","8-bit ITU-T G.711 µ-law","Apple", false),
    MAC3("MAC3", "MACE 3-to-1","", "Apple", false),
    MAC6("MAC6", "MACE 6-to-1","", "Apple", false),
    ALAW("ALAW","CCITT G.711 A-law", "8-bit ITU-T G.711 A-law (64 kbit/s)", "SGI", false),
    ULAW("ULAW","CCITT G.711 u-law","8-bit ITU-T G.711 A-law (64 kbit/s)","SGI", false),
    FL32("FL32", "Float 32","IEEE 32-bit float", "SoundHack & Csound", false),
    rt24("rt24", "RT24 50:1","", "Voxware", false),
    rt29("rt29", "RT29 50:1","", "Voxware", false),
       ;

    // Reverse-lookup map for getting a compression type from code
    private static final Map<String, AiffCompressionType> lookup = new HashMap<String, AiffCompressionType>();

    static
    {
        for (AiffCompressionType d : AiffCompressionType.values())
        {
            lookup.put(d.getCode(), d);
        }
    }


    private String code;
    private String compression;
    private String dataType;
    private String provider;
    private boolean isLossless;

    AiffCompressionType(String code, String compression, String dataType, String provider, boolean isLossless)
    {
        this.code = code;
        this.compression = compression;
        this.dataType = dataType;
        this.provider=provider;
        this.isLossless=isLossless;
    }

    public String getCode()
    {
        return code;
    }

    public String getCompression()
    {
        return compression;
    }

    public boolean isLossless()
    {
        return isLossless;
    }

    public String getDataType()
    {
        return dataType;
    }

    public String getProvider()
    {
        return provider;
    }

    public static AiffCompressionType getByCode(String code)
    {
        return lookup.get(code);
    }

}
