package ealvatag.audio;

import ealvatag.audio.real.RealTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.aiff.AiffTag;
import ealvatag.tag.asf.AsfTag;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.mp4.Mp4Tag;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import ealvatag.tag.wav.WavTag;
import org.junit.Assert;
import org.junit.Test;

import static ealvatag.audio.SupportedFileFormat.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link SupportedFileFormat} as it adds functionality
 * <p>
 * Created by eric on 1/7/17.
 */
public class SupportedFileFormatTest {
    @Test public void testFromExtension() throws Exception {
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
        for (String extension : expectedMap.keySet()) {
            Assert.assertSame(expectedMap.get(extension), SupportedFileFormat.fromExtension(extension));
        }
    }

    @Test public void testCreateDefaultTag() {
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
        Assert.assertTrue("Did not test all formats. " + formatSet, formatSet.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void unknownCannotCreateDefault() {
        UNKNOWN.makeDefaultTag();
        Assert.fail("UNKNOWN format can't create a tag");
    }

    private void defaultTagIsInstanceOf(EnumSet<SupportedFileFormat> formatSet, SupportedFileFormat format, Class<?> tagClass) {
        formatSet.remove(format);
        final Tag defaultTag = format.makeDefaultTag();
        Assert.assertTrue("Expected:" + tagClass + " Actual:" + defaultTag.getClass(), tagClass.isInstance(defaultTag));
    }

}
