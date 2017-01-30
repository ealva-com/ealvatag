package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.NullTagField;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.framebody.FrameBodyCOMM;
import ealvatag.tag.reference.ID3V2Version;
import ealvatag.tag.reference.Languages;
import ealvatag.tag.reference.PerformerHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

/**
 * Support For Custom fields
 */
public class Issue298Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test writing Custom fields
     */
    @Test public void testWriteFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.CUSTOM1, "custom1");
            tag.setField(FieldKey.CUSTOM2, "custom2");
            tag.setField(FieldKey.CUSTOM3, "custom3");
            tag.setField(FieldKey.CUSTOM4, "custom4");
            tag.setField(FieldKey.CUSTOM5, "custom5");
            tag.setField(FieldKey.FBPM, "155.5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94e");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94f");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_ID, "0410c22a-0b2b-4793-9f18-5f1fab36338e");
            tag.setField(FieldKey.OCCASION, "Occasion");
            tag.setField(FieldKey.ORIGINAL_ALBUM, "original_album");
            tag.setField(FieldKey.ORIGINAL_LYRICIST, "original_lyricist");
            tag.setField(FieldKey.ORIGINAL_ARTIST, "original_artist");
            tag.setField(FieldKey.ORIGINAL_YEAR, "2012");
            tag.setField(FieldKey.QUALITY, "quality");
            tag.setField(FieldKey.SCRIPT, "latin");
            tag.setField(FieldKey.TAGS, "fred");
            tag.setField(FieldKey.TEMPO, "Mellow");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD_AGGRESSIVE, "60");
            tag.setField(FieldKey.MOOD_RELAXED, "61");
            tag.setField(FieldKey.MOOD_SAD, "62");
            tag.setField(FieldKey.MOOD_HAPPY, "63");
            tag.setField(FieldKey.MOOD_PARTY, "64");
            tag.setField(FieldKey.MOOD_DANCEABILITY, "65");
            tag.setField(FieldKey.MOOD_VALENCE, "66");
            tag.setField(FieldKey.MOOD_AROUSAL, "67");
            tag.setField(FieldKey.MOOD_ACOUSTIC, "68");
            tag.setField(FieldKey.MOOD_ELECTRONIC, "69");
            tag.setField(FieldKey.MOOD_INSTRUMENTAL, "70");
            tag.setField(FieldKey.TIMBRE, "71");
            tag.setField(FieldKey.TONALITY, "72");
            tag.setField(FieldKey.KEY, "Am");
            tag.setField(FieldKey.ORCHESTRA, "Orchestra");
            tag.setField(FieldKey.PART, "Part");
            tag.setField(FieldKey.PART_TYPE, "Composition Type");
            tag.setField(FieldKey.WORK, "Work");
            tag.setField(FieldKey.PERFORMER, "Performer");
            tag.setField(FieldKey.WORK_TYPE, "WorkType");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94h");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94i");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94j");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94k");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94l");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "MusicBrainzWorkComposition");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, "Level1");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, "Level1Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, "Level2");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, "Level2Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, "Level3");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, "Level3Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, "Level4");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, "Level4Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, "Level5");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, "Level5Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, "Level6");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, "Level6Type");
            tag.setField(FieldKey.PART_NUMBER, "PartNumber");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.CONDUCTOR_SORT, "ConductorSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.ORCHESTRA_SORT, "OrchestraSort");
            tag.setField(FieldKey.ARRANGER_SORT, "ArrangerSort");
            tag.setField(FieldKey.OPUS, "Opus");
            tag.setField(FieldKey.CHOIR, "Choir");
            tag.setField(FieldKey.RANKING, "Ranking");
            tag.setField(FieldKey.SINGLE_DISC_TRACK_NO, "SingleDiscTrackNo");
            tag.setField(FieldKey.PERIOD, "Period");
            tag.setField(FieldKey.IS_CLASSICAL, "true");
            tag.setField(FieldKey.IS_SOUNDTRACK, "true");
            tag.setField(FieldKey.ENSEMBLE, "ensemble");
            tag.setField(FieldKey.CLASSICAL_CATALOG, "classicalcatalog");
            tag.setField(FieldKey.CLASSICAL_NICKNAME, "classicalnickname");

            tag.setField(FieldKey.MOVEMENT, "Movement");
            tag.setField(FieldKey.MOVEMENT_NO, "1");
            tag.setField(FieldKey.MOVEMENT_TOTAL, "2");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            tag.setField(FieldKey.TITLE_MOVEMENT, "TitleMovement");
            tag.setField(FieldKey.MUSICBRAINZ_WORK, "MusicBrainzWork");
            tag.setField(FieldKey.ITUNES_GROUPING, "ITUNESGROUPING");


            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("Performer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("Composition Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_TYPE));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("MusicBrainzWorkComposition",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("PartNumber", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_NUMBER));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("ConductorSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CONDUCTOR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("OrchestraSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA_SORT));
            Assert.assertEquals("ArrangerSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER_SORT));
            Assert.assertEquals("Opus", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OPUS));
            Assert.assertEquals("Ranking", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RANKING));
            Assert.assertEquals("Choir", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR));
            Assert.assertEquals("SingleDiscTrackNo", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            Assert.assertEquals("Period", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERIOD));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_CLASSICAL));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_SOUNDTRACK));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("PerformerName", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME));
            Assert.assertEquals("PerformerNameSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME_SORT));
            Assert.assertEquals("ensemble", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE));
            Assert.assertEquals("classicalcatalog", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_CATALOG));
            Assert.assertEquals("classicalnickname", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_NICKNAME));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("TitleMovement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE_MOVEMENT));
            Assert.assertEquals("MusicBrainzWork", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK));
            Assert.assertEquals("ITUNESGROUPING", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ITUNES_GROUPING));


            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.CUSTOM1).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
            }
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("Performer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.CUSTOM1).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body = (FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                Assert.assertEquals(FrameBodyCOMM.MM_CUSTOM1, body.getDescription());
                Assert.assertEquals(Languages.MEDIA_MONKEY_ID, body.getLanguage());
            }
            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.CUSTOM2).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body = (FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                Assert.assertEquals(FrameBodyCOMM.MM_CUSTOM2, body.getDescription());
                Assert.assertEquals(Languages.MEDIA_MONKEY_ID, body.getLanguage());
            }
            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.CUSTOM3).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body = (FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                Assert.assertEquals(FrameBodyCOMM.MM_CUSTOM3, body.getDescription());
                Assert.assertEquals(Languages.MEDIA_MONKEY_ID, body.getLanguage());
            }
            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.CUSTOM4).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body = (FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                Assert.assertEquals(FrameBodyCOMM.MM_CUSTOM4, body.getDescription());
                Assert.assertEquals(Languages.MEDIA_MONKEY_ID, body.getLanguage());
            }
            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.CUSTOM5).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body = (FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                Assert.assertEquals(FrameBodyCOMM.MM_CUSTOM5, body.getDescription());
                Assert.assertEquals(Languages.MEDIA_MONKEY_ID, body.getLanguage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields
     */
    @Test public void testWriteFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.CUSTOM1, "custom1");
            tag.setField(FieldKey.CUSTOM2, "custom2");
            tag.setField(FieldKey.CUSTOM3, "custom3");
            tag.setField(FieldKey.CUSTOM4, "custom4");
            tag.setField(FieldKey.CUSTOM5, "custom5");
            tag.setField(FieldKey.FBPM, "155.5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94e");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94f");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_ID, "0410c22a-0b2b-4793-9f18-5f1fab36338e");
            tag.setField(FieldKey.OCCASION, "Occasion");
            tag.setField(FieldKey.ORIGINAL_ALBUM, "original_album");
            tag.setField(FieldKey.ORIGINAL_LYRICIST, "original_lyricist");
            tag.setField(FieldKey.ORIGINAL_ARTIST, "original_artist");
            tag.setField(FieldKey.ORIGINAL_YEAR, "2012");
            tag.setField(FieldKey.QUALITY, "quality");
            tag.setField(FieldKey.SCRIPT, "latin");
            tag.setField(FieldKey.TAGS, "fred");
            tag.setField(FieldKey.TEMPO, "Mellow");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD_AGGRESSIVE, "60");
            tag.setField(FieldKey.MOOD_RELAXED, "61");
            tag.setField(FieldKey.MOOD_SAD, "62");
            tag.setField(FieldKey.MOOD_HAPPY, "63");
            tag.setField(FieldKey.MOOD_PARTY, "64");
            tag.setField(FieldKey.MOOD_DANCEABILITY, "65");
            tag.setField(FieldKey.MOOD_VALENCE, "66");
            tag.setField(FieldKey.MOOD_AROUSAL, "67");
            tag.setField(FieldKey.MOOD_ACOUSTIC, "68");
            tag.setField(FieldKey.MOOD_ELECTRONIC, "69");
            tag.setField(FieldKey.MOOD_INSTRUMENTAL, "70");
            tag.setField(FieldKey.TIMBRE, "71");
            tag.setField(FieldKey.TONALITY, "72");
            tag.setField(FieldKey.KEY, "Am");
            tag.setField(FieldKey.ORCHESTRA, "Orchestra");
            tag.setField(FieldKey.PART, "Part");
            tag.setField(FieldKey.WORK, "Work");
            tag.setField(FieldKey.CHOIR, "Choir");
            tag.setField(FieldKey.RANKING, "Ranking");
            tag.setField(FieldKey.PERFORMER, "Performer");
            tag.setField(FieldKey.WORK_TYPE, "WorkType");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94h");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94i");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94j");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94k");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94l");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "MusicBrainzWorkComposition");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, "Level1");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, "Level1Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, "Level2");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, "Level2Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, "Level3");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, "Level3Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, "Level4");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, "Level4Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, "Level5");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, "Level5Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, "Level6");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, "Level6Type");
            tag.setField(FieldKey.PART_NUMBER, "PartNumber");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.CONDUCTOR_SORT, "ConductorSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.ORCHESTRA_SORT, "OrchestraSort");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "Performer");
            tag.setField(FieldKey.ARRANGER_SORT, "ArrangerSort");
            tag.setField(FieldKey.OPUS, "Opus");
            tag.setField(FieldKey.SINGLE_DISC_TRACK_NO, "SingleDiscTrackNo");
            tag.setField(FieldKey.PERIOD, "Period");
            tag.setField(FieldKey.PART_TYPE, "Composition Type");
            tag.setField(FieldKey.IS_CLASSICAL, "true");
            tag.setField(FieldKey.IS_SOUNDTRACK, "true");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            tag.setField(FieldKey.PRODUCER, "Producer");
            tag.setField(FieldKey.ENSEMBLE, "ensemble");
            tag.setField(FieldKey.CLASSICAL_CATALOG, "classicalcatalog");
            tag.setField(FieldKey.CLASSICAL_NICKNAME, "classicalnickname");
            tag.setField(FieldKey.MOVEMENT, "Movement");
            tag.setField(FieldKey.MOVEMENT_NO, "1");
            tag.setField(FieldKey.MOVEMENT_TOTAL, "2");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            tag.setField(FieldKey.TITLE_MOVEMENT, "TitleMovement");
            tag.setField(FieldKey.MUSICBRAINZ_WORK, "MusicBrainzWork");
            tag.setField(FieldKey.ITUNES_GROUPING, "ITUNESGROUPING");

            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("ITUNESGROUPING", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ITUNES_GROUPING));

            Iterator<TagField> i = af.getTag().or(NullTag.INSTANCE).getFields();
            while (i.hasNext()) {
                TagField tf = i.next();
                System.out.println(tf.getId() + ":" + tf.toString());
            }

            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));

            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("Performer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("Composition Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_TYPE));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("MusicBrainzWorkComposition",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("PartNumber", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_NUMBER));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("ConductorSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CONDUCTOR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("OrchestraSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA_SORT));
            Assert.assertEquals("ArrangerSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER_SORT));
            Assert.assertEquals("Opus", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OPUS));
            Assert.assertEquals("Ranking", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RANKING));
            Assert.assertEquals("Choir", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR));

            Assert.assertEquals("SingleDiscTrackNo", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            Assert.assertEquals("Period", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERIOD));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_CLASSICAL));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_SOUNDTRACK));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("PerformerName", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME));
            Assert.assertEquals("PerformerNameSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME_SORT));
            Assert.assertEquals("Producer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("ensemble", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE));
            Assert.assertEquals("classicalcatalog", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_CATALOG));
            Assert.assertEquals("classicalnickname", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_NICKNAME));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("TitleMovement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE_MOVEMENT));
            Assert.assertEquals("MusicBrainzWork", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK));
            Assert.assertEquals("ITUNESGROUPING", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ITUNES_GROUPING));
            i = af.getTag().or(NullTag.INSTANCE).getFields();
            while (i.hasNext()) {
                TagField tf = i.next();
                System.out.println(tf.getId() + ":" + tf.toString());
            }
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Mp3 ID3v23
     */
    @Test public void testWriteFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.CUSTOM1, "custom1");
            tag.setField(FieldKey.CUSTOM2, "custom2");
            tag.setField(FieldKey.CUSTOM3, "custom3");
            tag.setField(FieldKey.CUSTOM4, "custom4");
            tag.setField(FieldKey.CUSTOM5, "custom5");
            tag.setField(FieldKey.FBPM, "155.5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94e");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94f");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_ID, "0410c22a-0b2b-4793-9f18-5f1fab36338e");
            tag.setField(FieldKey.OCCASION, "Occasion");
            tag.setField(FieldKey.ORIGINAL_ALBUM, "original_album");
            tag.setField(FieldKey.ORIGINAL_LYRICIST, "original_lyricist");
            tag.setField(FieldKey.ORIGINAL_ARTIST, "original_artist");
            tag.setField(FieldKey.ORIGINAL_YEAR, "2012");
            tag.setField(FieldKey.QUALITY, "quality");
            tag.setField(FieldKey.SCRIPT, "latin");
            tag.setField(FieldKey.TAGS, "fred");
            tag.setField(FieldKey.TEMPO, "Mellow");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD_AGGRESSIVE, "60");
            tag.setField(FieldKey.MOOD_RELAXED, "61");
            tag.setField(FieldKey.MOOD_SAD, "62");
            tag.setField(FieldKey.MOOD_HAPPY, "63");
            tag.setField(FieldKey.MOOD_PARTY, "64");
            tag.setField(FieldKey.MOOD_DANCEABILITY, "65");
            tag.setField(FieldKey.MOOD_VALENCE, "66");
            tag.setField(FieldKey.MOOD_AROUSAL, "67");
            tag.setField(FieldKey.MOOD_ACOUSTIC, "68");
            tag.setField(FieldKey.MOOD_ELECTRONIC, "69");
            tag.setField(FieldKey.MOOD_INSTRUMENTAL, "70");
            tag.setField(FieldKey.TIMBRE, "71");
            tag.setField(FieldKey.TONALITY, "72");
            tag.setField(FieldKey.KEY, "Am");
            tag.setField(FieldKey.ORCHESTRA, "Orchestra");
            tag.setField(FieldKey.PART, "Part");
            tag.setField(FieldKey.CHOIR, "Choir");
            tag.setField(FieldKey.RANKING, "Ranking");

            tag.setField(FieldKey.WORK, "Work");
            tag.setField(FieldKey.PERFORMER, "Performer");
            tag.setField(FieldKey.WORK_TYPE, "WorkType");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94h");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94i");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94j");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94k");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94l");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "MusicBrainzWorkComposition");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, "Level1");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, "Level1Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, "Level2");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, "Level2Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, "Level3");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, "Level3Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, "Level4");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, "Level4Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, "Level5");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, "Level5Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, "Level6");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, "Level6Type");
            tag.setField(FieldKey.PART_NUMBER, "PartNumber");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.CONDUCTOR_SORT, "ConductorSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.ORCHESTRA_SORT, "OrchestraSort");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "Performer");
            tag.setField(FieldKey.ARRANGER_SORT, "ArrangerSort");
            tag.setField(FieldKey.OPUS, "Opus");
            tag.setField(FieldKey.SINGLE_DISC_TRACK_NO, "SingleDiscTrackNo");
            tag.setField(FieldKey.PERIOD, "Period");
            tag.setField(FieldKey.PART_TYPE, "Composition Type");
            tag.setField(FieldKey.IS_CLASSICAL, "true");
            tag.setField(FieldKey.IS_SOUNDTRACK, "true");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            tag.setField(FieldKey.ENSEMBLE, "ensemble");
            tag.setField(FieldKey.CLASSICAL_CATALOG, "classicalcatalog");
            tag.setField(FieldKey.MOVEMENT, "Movement");
            tag.setField(FieldKey.MOVEMENT_NO, "1");
            tag.setField(FieldKey.MOVEMENT_TOTAL, "2");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            tag.setField(FieldKey.CLASSICAL_NICKNAME, "classicalnickname");
            tag.setField(FieldKey.TITLE_MOVEMENT, "TitleMovement");
            tag.setField(FieldKey.MUSICBRAINZ_WORK, "MusicBrainzWork");
            tag.setField(FieldKey.ITUNES_GROUPING, "ITUNESGROUPING");


            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("Performer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("MusicBrainzWorkComposition",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("PartNumber", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_NUMBER));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("ConductorSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CONDUCTOR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("OrchestraSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA_SORT));
            Assert.assertEquals("ArrangerSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER_SORT));
            Assert.assertEquals("Opus", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OPUS));
            Assert.assertEquals("Ranking", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RANKING));
            Assert.assertEquals("Choir", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR));

            Assert.assertEquals("SingleDiscTrackNo", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            Assert.assertEquals("Period", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERIOD));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_CLASSICAL));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_SOUNDTRACK));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("PerformerName", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME));
            Assert.assertEquals("PerformerNameSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME_SORT));
            Assert.assertEquals("ensemble", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE));
            Assert.assertEquals("classicalcatalog", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_CATALOG));
            Assert.assertEquals("classicalnickname", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_NICKNAME));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("TitleMovement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE_MOVEMENT));
            Assert.assertEquals("MusicBrainzWork", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK));
            Assert.assertEquals("ITUNESGROUPING", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ITUNES_GROUPING));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Ogg Vorbis
     */
    @Test public void testWriteFieldsToOggVorbis() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.CUSTOM1, "custom1");
            tag.setField(FieldKey.CUSTOM2, "custom2");
            tag.setField(FieldKey.CUSTOM3, "custom3");
            tag.setField(FieldKey.CUSTOM4, "custom4");
            tag.setField(FieldKey.CUSTOM5, "custom5");
            tag.setField(FieldKey.FBPM, "155.5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94e");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94f");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_ID, "0410c22a-0b2b-4793-9f18-5f1fab36338e");
            tag.setField(FieldKey.OCCASION, "Occasion");
            tag.setField(FieldKey.ORIGINAL_ALBUM, "original_album");
            tag.setField(FieldKey.ORIGINAL_LYRICIST, "original_lyricist");
            tag.setField(FieldKey.ORIGINAL_ARTIST, "original_artist");
            tag.setField(FieldKey.ORIGINAL_YEAR, "2012");
            tag.setField(FieldKey.QUALITY, "quality");
            tag.setField(FieldKey.SCRIPT, "latin");
            tag.setField(FieldKey.TAGS, "fred");
            tag.setField(FieldKey.TEMPO, "Mellow");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD_AGGRESSIVE, "60");
            tag.setField(FieldKey.MOOD_RELAXED, "61");
            tag.setField(FieldKey.MOOD_SAD, "62");
            tag.setField(FieldKey.MOOD_HAPPY, "63");
            tag.setField(FieldKey.MOOD_PARTY, "64");
            tag.setField(FieldKey.MOOD_DANCEABILITY, "65");
            tag.setField(FieldKey.MOOD_VALENCE, "66");
            tag.setField(FieldKey.MOOD_AROUSAL, "67");
            tag.setField(FieldKey.MOOD_ACOUSTIC, "68");
            tag.setField(FieldKey.MOOD_ELECTRONIC, "69");
            tag.setField(FieldKey.MOOD_INSTRUMENTAL, "70");
            tag.setField(FieldKey.TIMBRE, "71");
            tag.setField(FieldKey.TONALITY, "72");
            tag.setField(FieldKey.KEY, "Am");
            tag.setField(FieldKey.ORCHESTRA, "Orchestra");
            tag.setField(FieldKey.PART, "Part");
            tag.setField(FieldKey.WORK, "Work");
            tag.setField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist"));
            tag.setField(FieldKey.WORK_TYPE, "WorkType");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.PART_TYPE, "CompositionType");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94h");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94i");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94j");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94k");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94l");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "MusicBrainzWorkComposition");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, "Level1");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, "Level1Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, "Level2");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, "Level2Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, "Level3");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, "Level3Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, "Level4");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, "Level4Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, "Level5");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, "Level5Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, "Level6");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, "Level6Type");
            tag.setField(FieldKey.PART_NUMBER, "PartNumber");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.CONDUCTOR_SORT, "ConductorSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.ORCHESTRA_SORT, "OrchestraSort");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "Performer");
            tag.setField(FieldKey.ARRANGER_SORT, "ArrangerSort");
            tag.setField(FieldKey.OPUS, "Opus");
            tag.setField(FieldKey.CHOIR, "Choir");
            tag.setField(FieldKey.RANKING, "Ranking");

            tag.setField(FieldKey.SINGLE_DISC_TRACK_NO, "SingleDiscTrackNo");
            tag.setField(FieldKey.PERIOD, "Period");
            tag.setField(FieldKey.IS_CLASSICAL, "true");
            tag.setField(FieldKey.IS_SOUNDTRACK, "true");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            tag.setField(FieldKey.ENSEMBLE, "ensemble");
            tag.setField(FieldKey.CLASSICAL_CATALOG, "classicalcatalog");
            tag.setField(FieldKey.CLASSICAL_NICKNAME, "classicalnickname");

            tag.setField(FieldKey.MOVEMENT, "Movement");
            tag.setField(FieldKey.MOVEMENT_NO, "1");
            tag.setField(FieldKey.MOVEMENT_TOTAL, "2");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            tag.setField(FieldKey.TITLE_MOVEMENT, "TitleMovement");
            tag.setField(FieldKey.MUSICBRAINZ_WORK, "MusicBrainzWork");

            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("Nigel Kennedy (violinist)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("MusicBrainzWorkComposition",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("PartNumber", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_NUMBER));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("ConductorSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CONDUCTOR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("OrchestraSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA_SORT));
            Assert.assertEquals("ArrangerSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER_SORT));
            Assert.assertEquals("Opus", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OPUS));
            Assert.assertEquals("Ranking", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RANKING));
            Assert.assertEquals("Choir", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR));

            Assert.assertEquals("SingleDiscTrackNo", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            Assert.assertEquals("Period", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERIOD));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_CLASSICAL));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_SOUNDTRACK));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("PerformerName", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME));
            Assert.assertEquals("PerformerNameSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME_SORT));
            Assert.assertEquals("ensemble", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE));
            Assert.assertEquals("classicalcatalog", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_CATALOG));
            Assert.assertEquals("classicalnickname", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_NICKNAME));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("TitleMovement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE_MOVEMENT));
            Assert.assertEquals("MusicBrainzWork", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Flac
     */
    @Test public void testWriteFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.CUSTOM1, "custom1");
            tag.setField(FieldKey.CUSTOM2, "custom2");
            tag.setField(FieldKey.CUSTOM3, "custom3");
            tag.setField(FieldKey.CUSTOM4, "custom4");
            tag.setField(FieldKey.CUSTOM5, "custom5");
            tag.setField(FieldKey.FBPM, "155.5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94e");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94f");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_ID, "0410c22a-0b2b-4793-9f18-5f1fab36338e");
            tag.setField(FieldKey.OCCASION, "Occasion");
            tag.setField(FieldKey.ORIGINAL_ALBUM, "original_album");
            tag.setField(FieldKey.ORIGINAL_LYRICIST, "original_lyricist");
            tag.setField(FieldKey.ORIGINAL_ARTIST, "original_artist");
            tag.setField(FieldKey.ORIGINAL_YEAR, "2012");
            tag.setField(FieldKey.QUALITY, "quality");
            tag.setField(FieldKey.SCRIPT, "latin");
            tag.setField(FieldKey.TAGS, "fred");
            tag.setField(FieldKey.TEMPO, "Mellow");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD_AGGRESSIVE, "60");
            tag.setField(FieldKey.MOOD_RELAXED, "61");
            tag.setField(FieldKey.MOOD_SAD, "62");
            tag.setField(FieldKey.MOOD_HAPPY, "63");
            tag.setField(FieldKey.MOOD_PARTY, "64");
            tag.setField(FieldKey.MOOD_DANCEABILITY, "65");
            tag.setField(FieldKey.MOOD_VALENCE, "66");
            tag.setField(FieldKey.MOOD_AROUSAL, "67");
            tag.setField(FieldKey.MOOD_ACOUSTIC, "68");
            tag.setField(FieldKey.MOOD_ELECTRONIC, "69");
            tag.setField(FieldKey.MOOD_INSTRUMENTAL, "70");
            tag.setField(FieldKey.TIMBRE, "71");
            tag.setField(FieldKey.TONALITY, "72");
            tag.setField(FieldKey.KEY, "Am");
            tag.setField(FieldKey.ORCHESTRA, "Orchestra");
            tag.setField(FieldKey.PART, "Part");
            tag.setField(FieldKey.WORK, "Work");
            tag.setField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist"));
            tag.setField(FieldKey.WORK_TYPE, "WorkType");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94h");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94i");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94j");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94k");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94l");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "MusicBrainzWorkComposition");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, "Level1");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, "Level1Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, "Level2");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, "Level2Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, "Level3");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, "Level3Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, "Level4");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, "Level4Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, "Level5");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, "Level5Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, "Level6");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, "Level6Type");
            tag.setField(FieldKey.PART_NUMBER, "PartNumber");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.CONDUCTOR_SORT, "ConductorSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.ORCHESTRA_SORT, "OrchestraSort");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "Performer");
            tag.setField(FieldKey.ARRANGER_SORT, "ArrangerSort");
            tag.setField(FieldKey.OPUS, "Opus");
            tag.setField(FieldKey.CHOIR, "Choir");
            tag.setField(FieldKey.RANKING, "Ranking");
            tag.setField(FieldKey.MOVEMENT, "Movement");
            tag.setField(FieldKey.MOVEMENT_NO, "1");
            tag.setField(FieldKey.MOVEMENT_TOTAL, "2");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            tag.setField(FieldKey.SINGLE_DISC_TRACK_NO, "SingleDiscTrackNo");
            tag.setField(FieldKey.PERIOD, "Period");
            tag.setField(FieldKey.PART_TYPE, "CompositionType");
            tag.setField(FieldKey.IS_CLASSICAL, "true");
            tag.setField(FieldKey.IS_SOUNDTRACK, "true");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            tag.setField(FieldKey.ENSEMBLE, "ensemble");
            tag.setField(FieldKey.CLASSICAL_CATALOG, "classicalcatalog");
            tag.setField(FieldKey.CLASSICAL_NICKNAME, "classicalnickname");
            tag.setField(FieldKey.TITLE_MOVEMENT, "TitleMovement");
            tag.setField(FieldKey.MUSICBRAINZ_WORK, "MusicBrainzWork");

            af.save();

            af = AudioFileIO.read(testFile);
            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));
            Assert.assertEquals("Nigel Kennedy (violinist)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));

            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("MusicBrainzWorkComposition",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("CompositionType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_TYPE));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("PartNumber", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_NUMBER));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("ConductorSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CONDUCTOR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("OrchestraSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA_SORT));
            Assert.assertEquals("ArrangerSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER_SORT));
            Assert.assertEquals("Opus", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OPUS));
            Assert.assertEquals("Ranking", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RANKING));
            Assert.assertEquals("Choir", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR));

            Assert.assertEquals("SingleDiscTrackNo", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            Assert.assertEquals("Period", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERIOD));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_CLASSICAL));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_SOUNDTRACK));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("PerformerName", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME));
            Assert.assertEquals("PerformerNameSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME_SORT));
            Assert.assertEquals("ensemble", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE));
            Assert.assertEquals("classicalcatalog", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_CATALOG));
            Assert.assertEquals("classicalnickname", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_NICKNAME));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("TitleMovement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE_MOVEMENT));
            Assert.assertEquals("MusicBrainzWork", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test writing Custom fields to Wma
     */
    @Test public void testWriteFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test1.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.CUSTOM1, "custom1");
            tag.setField(FieldKey.CUSTOM2, "custom2");
            tag.setField(FieldKey.CUSTOM3, "custom3");
            tag.setField(FieldKey.CUSTOM4, "custom4");
            tag.setField(FieldKey.CUSTOM5, "custom5");
            tag.setField(FieldKey.FBPM, "155.5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94e");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94f");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_ID, "0410c22a-0b2b-4793-9f18-5f1fab36338e");
            tag.setField(FieldKey.OCCASION, "Occasion");
            tag.setField(FieldKey.ORIGINAL_ALBUM, "original_album");
            tag.setField(FieldKey.ORIGINAL_LYRICIST, "original_lyricist");
            tag.setField(FieldKey.ORIGINAL_ARTIST, "original_artist");
            tag.setField(FieldKey.ORIGINAL_YEAR, "2012");
            tag.setField(FieldKey.QUALITY, "quality");
            tag.setField(FieldKey.SCRIPT, "latin");
            tag.setField(FieldKey.TAGS, "fred");
            tag.setField(FieldKey.TEMPO, "Mellow");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD_AGGRESSIVE, "60");
            tag.setField(FieldKey.MOOD_RELAXED, "61");
            tag.setField(FieldKey.MOOD_SAD, "62");
            tag.setField(FieldKey.MOOD_HAPPY, "63");
            tag.setField(FieldKey.MOOD_PARTY, "64");
            tag.setField(FieldKey.MOOD_DANCEABILITY, "65");
            tag.setField(FieldKey.MOOD_VALENCE, "66");
            tag.setField(FieldKey.MOOD_AROUSAL, "67");
            tag.setField(FieldKey.MOOD_ACOUSTIC, "68");
            tag.setField(FieldKey.MOOD_ELECTRONIC, "69");
            tag.setField(FieldKey.MOOD_INSTRUMENTAL, "70");
            tag.setField(FieldKey.TIMBRE, "71");
            tag.setField(FieldKey.TONALITY, "72");
            tag.setField(FieldKey.KEY, "Am");
            tag.setField(FieldKey.ORCHESTRA, "Orchestra");
            tag.setField(FieldKey.PART, "Part");
            tag.setField(FieldKey.WORK, "Work");
            tag.setField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist"));
            tag.setField(FieldKey.WORK_TYPE, "WorkType");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.PART_TYPE, "CompositionType");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94h");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94i");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94j");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94k");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94l");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "MusicBrainzWorkComposition");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, "Level1");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, "Level1Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, "Level2");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, "Level2Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, "Level3");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, "Level3Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, "Level4");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, "Level4Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, "Level5");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, "Level5Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, "Level6");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, "Level6Type");
            tag.setField(FieldKey.PART_NUMBER, "PartNumber");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.CONDUCTOR_SORT, "ConductorSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.ORCHESTRA_SORT, "OrchestraSort");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "Performer");
            tag.setField(FieldKey.ARRANGER_SORT, "ArrangerSort");
            tag.setField(FieldKey.OPUS, "Opus");
            tag.setField(FieldKey.CHOIR, "Choir");
            tag.setField(FieldKey.RANKING, "Ranking");
            tag.setField(FieldKey.SINGLE_DISC_TRACK_NO, "SingleDiscTrackNo");
            tag.setField(FieldKey.PERIOD, "Period");
            tag.setField(FieldKey.IS_CLASSICAL, "true");
            tag.setField(FieldKey.IS_SOUNDTRACK, "true");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            tag.setField(FieldKey.ENSEMBLE, "ensemble");
            tag.setField(FieldKey.CLASSICAL_CATALOG, "classicalcatalog");
            tag.setField(FieldKey.CLASSICAL_NICKNAME, "classicalnickname");
            tag.setField(FieldKey.MOVEMENT, "Movement");
            tag.setField(FieldKey.MOVEMENT_NO, "1");
            tag.setField(FieldKey.MOVEMENT_TOTAL, "2");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            tag.setField(FieldKey.TITLE_MOVEMENT, "TitleMovement");
            tag.setField(FieldKey.MUSICBRAINZ_WORK, "MusicBrainzWork");

            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("Nigel Kennedy (violinist)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("MusicBrainzWorkComposition",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("PartNumber", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_NUMBER));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("ConductorSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CONDUCTOR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("OrchestraSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA_SORT));
            Assert.assertEquals("ArrangerSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER_SORT));
            Assert.assertEquals("Opus", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OPUS));
            Assert.assertEquals("Ranking", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RANKING));
            Assert.assertEquals("Choir", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR));
            Assert.assertEquals("SingleDiscTrackNo", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            Assert.assertEquals("Period", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERIOD));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_CLASSICAL));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_SOUNDTRACK));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("PerformerName", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME));
            Assert.assertEquals("PerformerNameSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME_SORT));
            Assert.assertEquals("ensemble", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE));
            Assert.assertEquals("classicalcatalog", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_CATALOG));
            Assert.assertEquals("classicalnickname", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_NICKNAME));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("TitleMovement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE_MOVEMENT));
            Assert.assertEquals("MusicBrainzWork", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Mp4
     */
    @Test public void testWriteFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.CUSTOM1, "custom1");
            tag.setField(FieldKey.CUSTOM2, "custom2");
            tag.setField(FieldKey.CUSTOM3, "custom3");
            tag.setField(FieldKey.CUSTOM4, "custom4");
            tag.setField(FieldKey.CUSTOM5, "custom5");
            tag.setField(FieldKey.FBPM, "155.5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94e");

            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94f");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_ID, "0410c22a-0b2b-4793-9f18-5f1fab36338e");
            tag.setField(FieldKey.OCCASION, "Occasion");
            tag.setField(FieldKey.ORIGINAL_ALBUM, "original_album");
            tag.setField(FieldKey.ORIGINAL_LYRICIST, "original_lyricist");
            tag.setField(FieldKey.ORIGINAL_ARTIST, "original_artist");
            tag.setField(FieldKey.ORIGINAL_YEAR, "2012");
            tag.setField(FieldKey.QUALITY, "quality");
            tag.setField(FieldKey.SCRIPT, "latin");
            tag.setField(FieldKey.TAGS, "fred");
            tag.setField(FieldKey.TEMPO, "Mellow");
            tag.setField(FieldKey.MOOD, "Bad Mood");
            tag.setField(FieldKey.MOOD_AGGRESSIVE, "60");
            tag.setField(FieldKey.MOOD_RELAXED, "61");
            tag.setField(FieldKey.MOOD_SAD, "62");
            tag.setField(FieldKey.MOOD_HAPPY, "63");
            tag.setField(FieldKey.MOOD_PARTY, "64");
            tag.setField(FieldKey.MOOD_DANCEABILITY, "65");
            tag.setField(FieldKey.MOOD_VALENCE, "66");
            tag.setField(FieldKey.MOOD_AROUSAL, "67");
            tag.setField(FieldKey.MOOD_ACOUSTIC, "68");
            tag.setField(FieldKey.MOOD_ELECTRONIC, "69");
            tag.setField(FieldKey.MOOD_INSTRUMENTAL, "70");
            tag.setField(FieldKey.TIMBRE, "71");
            tag.setField(FieldKey.TONALITY, "72");
            tag.setField(FieldKey.KEY, "Am");
            tag.setField(FieldKey.ORCHESTRA, "Orchestra");
            tag.setField(FieldKey.PART, "Part");
            tag.setField(FieldKey.WORK, "Work");
            tag.setField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist"));
            tag.setField(FieldKey.WORK_TYPE, "WorkType");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94g");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94h");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94i");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94j");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94k");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "c1f657ba-8177-3cbb-b84a-f62bc684a94l");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "MusicBrainzWorkComposition");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, "Level1");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, "Level1Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, "Level2");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, "Level2Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, "Level3");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, "Level3Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, "Level4");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, "Level4Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, "Level5");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, "Level5Type");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, "Level6");
            tag.setField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, "Level6Type");
            tag.setField(FieldKey.PART_NUMBER, "PartNumber");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.CONDUCTOR_SORT, "ConductorSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.ORCHESTRA_SORT, "OrchestraSort");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "Performer");
            tag.setField(FieldKey.ARRANGER_SORT, "ArrangerSort");
            tag.setField(FieldKey.OPUS, "Opus");
            tag.setField(FieldKey.CHOIR, "Choir");
            tag.setField(FieldKey.RANKING, "Ranking");

            tag.setField(FieldKey.SINGLE_DISC_TRACK_NO, "SingleDiscTrackNo");
            tag.setField(FieldKey.PERIOD, "Period");
            tag.setField(FieldKey.PART_TYPE, "Composition Type");
            tag.setField(FieldKey.IS_CLASSICAL, "true");
            tag.setField(FieldKey.IS_SOUNDTRACK, "true");
            tag.setField(FieldKey.CHOIR_SORT, "ChoirSort");
            tag.setField(FieldKey.ENSEMBLE_SORT, "EnsembleSort");
            tag.setField(FieldKey.PERFORMER_NAME, "PerformerName");
            tag.setField(FieldKey.PERFORMER_NAME_SORT, "PerformerNameSort");
            tag.setField(FieldKey.ENSEMBLE, "ensemble");
            tag.setField(FieldKey.CLASSICAL_CATALOG, "classicalcatalog");
            tag.setField(FieldKey.CLASSICAL_NICKNAME, "classicalnickname");

            tag.setField(FieldKey.MOVEMENT, "Movement");
            tag.setField(FieldKey.MOVEMENT_NO, "1");
            tag.setField(FieldKey.MOVEMENT_TOTAL, "2");
            tag.setField(FieldKey.ARTISTS_SORT, "ArtistsSort");
            tag.setField(FieldKey.ALBUM_ARTISTS, "AlbumArtists");
            tag.setField(FieldKey.ALBUM_ARTISTS_SORT, "AlbumArtistsSort");
            tag.setField(FieldKey.TITLE_MOVEMENT, "TitleMovement");
            tag.setField(FieldKey.MUSICBRAINZ_WORK, "MusicBrainzWork");

            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("custom1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1));
            Assert.assertEquals("custom2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM2));
            Assert.assertEquals("custom3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM3));
            Assert.assertEquals("custom4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM4));
            Assert.assertEquals("custom5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM5));
            Assert.assertEquals("155.5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.FBPM));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            Assert.assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            Assert.assertEquals("Occasion", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OCCASION));
            Assert.assertEquals("original_album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ALBUM));
            Assert.assertEquals("original_lyricist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_LYRICIST));
            Assert.assertEquals("original_artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("2012", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORIGINAL_YEAR));
            Assert.assertEquals("quality", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.QUALITY));
            Assert.assertEquals("latin", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SCRIPT));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TAGS));
            Assert.assertEquals("Mellow", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TEMPO));
            Assert.assertEquals("Bad Mood", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD));
            Assert.assertEquals("60", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AGGRESSIVE));
            Assert.assertEquals("61", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_RELAXED));
            Assert.assertEquals("62", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_SAD));
            Assert.assertEquals("63", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_HAPPY));
            Assert.assertEquals("64", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_PARTY));
            Assert.assertEquals("65", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_DANCEABILITY));
            Assert.assertEquals("66", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_VALENCE));
            Assert.assertEquals("67", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_AROUSAL));
            Assert.assertEquals("68", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ACOUSTIC));
            Assert.assertEquals("69", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_ELECTRONIC));
            Assert.assertEquals("70", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOOD_INSTRUMENTAL));
            Assert.assertEquals("71", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TIMBRE));
            Assert.assertEquals("72", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TONALITY));
            Assert.assertEquals("Am", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("Orchestra", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA));
            Assert.assertEquals("Part", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("Nigel Kennedy (violinist)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
            Assert.assertEquals("WorkType", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK_TYPE));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            Assert.assertEquals("Composition Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_TYPE));
            Assert.assertEquals("MusicBrainzWorkComposition",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            Assert.assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            Assert.assertEquals("Level1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            Assert.assertEquals("Level2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            Assert.assertEquals("Level3", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            Assert.assertEquals("Level4", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            Assert.assertEquals("Level5", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            Assert.assertEquals("Level6", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            Assert.assertEquals("Level1Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            Assert.assertEquals("Level2Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            Assert.assertEquals("Level3Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            Assert.assertEquals("Level4Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            Assert.assertEquals("Level5Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            Assert.assertEquals("Level6Type", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            Assert.assertEquals("Work", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.WORK));
            Assert.assertEquals("PartNumber", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PART_NUMBER));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("ConductorSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CONDUCTOR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("OrchestraSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ORCHESTRA_SORT));
            Assert.assertEquals("ArrangerSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER_SORT));
            Assert.assertEquals("Opus", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.OPUS));
            Assert.assertEquals("Ranking", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RANKING));
            Assert.assertEquals("Choir", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR));

            Assert.assertEquals("SingleDiscTrackNo", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            Assert.assertEquals("Period", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERIOD));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_CLASSICAL));
            Assert.assertEquals("true", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_SOUNDTRACK));
            Assert.assertEquals("ChoirSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CHOIR_SORT));
            Assert.assertEquals("EnsembleSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE_SORT));
            Assert.assertEquals("PerformerName", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME));
            Assert.assertEquals("PerformerNameSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER_NAME_SORT));
            Assert.assertEquals("ensemble", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENSEMBLE));
            Assert.assertEquals("classicalcatalog", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_CATALOG));
            Assert.assertEquals("classicalnickname", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CLASSICAL_NICKNAME));
            Assert.assertEquals("Movement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            Assert.assertEquals("ArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTISTS_SORT));
            Assert.assertEquals("AlbumArtists", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS));
            Assert.assertEquals("AlbumArtistsSort", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            Assert.assertEquals("TitleMovement", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE_MOVEMENT));
            Assert.assertEquals("MusicBrainzWork", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICBRAINZ_WORK));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }
}
