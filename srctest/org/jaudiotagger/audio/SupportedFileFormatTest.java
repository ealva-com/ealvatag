package org.jaudiotagger.audio;

import junit.framework.TestCase;
import org.jaudiotagger.audio.real.RealTag;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.wav.WavTag;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static org.jaudiotagger.audio.SupportedFileFormat.*;

/**
 * Test for {@link SupportedFileFormat} as it adds functionality
 *
 * Created by eric on 1/7/17.
 */
public class SupportedFileFormatTest extends TestCase
{
    public void testFromExtension() throws Exception
    {
        Map<String, SupportedFileFormat> expectedMap = new HashMap<>();
        expectedMap.put("ogg", OGG);
        expectedMap.put("mp3", MP3);
        expectedMap.put("flac", FLAC);
        expectedMap.put("mp4", MP4);
        expectedMap.put("m4a", M4A);
        expectedMap.put("m4p", M4P);
        expectedMap.put("wma", WMA);
        expectedMap.put("wav", WAV);
        expectedMap.put("ra", RA);
        expectedMap.put("rm", RM);
        expectedMap.put("m4b", M4B);
        expectedMap.put("aif", AIF);
        expectedMap.put("aiff", AIFF);
        expectedMap.put("aifc", AIFC);
        expectedMap.put("dsf", DSF);
        expectedMap.put("OGG", OGG);
        expectedMap.put("MP3", MP3);
        expectedMap.put("FLAC", FLAC);
        expectedMap.put("MP4", MP4);
        expectedMap.put("M4A", M4A);
        expectedMap.put("M4P", M4P);
        expectedMap.put("WMA", WMA);
        expectedMap.put("WAV", WAV);
        expectedMap.put("RA", RA);
        expectedMap.put("RM", RM);
        expectedMap.put("M4B", M4B);
        expectedMap.put("AIF", AIF);
        expectedMap.put("AIFF", AIFF);
        expectedMap.put("AIFC", AIFC);
        expectedMap.put("DSF", DSF);
        expectedMap.put("", UNKNOWN);
        for (String extension : expectedMap.keySet())
        {
            assertSame(expectedMap.get(extension), SupportedFileFormat.fromExtension(extension));
        }
    }

    public void testCreateDefaultTag()
    {
        EnumSet<SupportedFileFormat> formatSet = EnumSet.allOf(SupportedFileFormat.class); // ensure we test all
        defaultTagIsInstanceOf(formatSet, OGG, VorbisCommentTag.class);
        defaultTagIsInstanceOf(formatSet, MP3, TagOptionSingleton.createDefaultID3Tag().getClass());
        defaultTagIsInstanceOf(formatSet, FLAC, FlacTag.class);
        defaultTagIsInstanceOf(formatSet, MP4, Mp4Tag.class);
        defaultTagIsInstanceOf(formatSet, M4A, Mp4Tag.class);
        defaultTagIsInstanceOf(formatSet, M4P, Mp4Tag.class);
        defaultTagIsInstanceOf(formatSet, WMA, AsfTag.class);
        defaultTagIsInstanceOf(formatSet, WAV, WavTag.class);
        defaultTagIsInstanceOf(formatSet, RA, RealTag.class);
        defaultTagIsInstanceOf(formatSet, RM, RealTag.class);
        defaultTagIsInstanceOf(formatSet, M4B, Mp4Tag.class);
        defaultTagIsInstanceOf(formatSet, AIF, AiffTag.class);
        defaultTagIsInstanceOf(formatSet, AIFF, AiffTag.class);
        defaultTagIsInstanceOf(formatSet, AIFC, AiffTag.class);
        defaultTagIsInstanceOf(formatSet, DSF, TagOptionSingleton.createDefaultID3Tag().getClass());

        formatSet.remove(UNKNOWN);
        try
        {
            UNKNOWN.createDefaultTag();
            fail("UNKNOWN format can't create a tag");
        }
        catch (RuntimeException ignored)
        {
            // expected
        }

        assertTrue("Did not test all formats. " + formatSet, formatSet.isEmpty());
    }

    private void defaultTagIsInstanceOf(EnumSet<SupportedFileFormat> formatSet, SupportedFileFormat format, Class<?> tagClass)
    {
        formatSet.remove(format);
        final Tag defaultTag = format.createDefaultTag();
        assertTrue("Expected:" + tagClass + " Actual:" + defaultTag.getClass(), tagClass.isInstance(defaultTag));
    }

}
