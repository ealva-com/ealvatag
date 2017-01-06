package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.jaudiotagger.tag.reference.Languages;
import org.jaudiotagger.tag.reference.PerformerHelper;

import java.io.File;
import java.util.Iterator;

/**
 * Support For Custom fields
 */
public class Issue298Test extends AbstractTestCase
{
    /**
     * Test writing Custom fields
     */
    public void  testWriteFieldsToMp3ID3v24()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v24Tag());
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.CUSTOM1,"custom1"));
            tag.setField(tag.createField(FieldKey.CUSTOM2,"custom2"));
            tag.setField(tag.createField(FieldKey.CUSTOM3,"custom3"));
            tag.setField(tag.createField(FieldKey.CUSTOM4,"custom4"));
            tag.setField(tag.createField(FieldKey.CUSTOM5,"custom5"));
            tag.setField(tag.createField(FieldKey.FBPM,"155.5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94e"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94f"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_ID,"0410c22a-0b2b-4793-9f18-5f1fab36338e"));
            tag.setField(tag.createField(FieldKey.OCCASION,"Occasion"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ALBUM,"original_album"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_LYRICIST,"original_lyricist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ARTIST,"original_artist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_YEAR,"2012"));
            tag.setField(tag.createField(FieldKey.QUALITY,"quality"));
            tag.setField(tag.createField(FieldKey.SCRIPT,"latin"));
            tag.setField(tag.createField(FieldKey.TAGS,"fred"));
            tag.setField(tag.createField(FieldKey.TEMPO,"Mellow"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD_AGGRESSIVE,"60"));
            tag.setField(tag.createField(FieldKey.MOOD_RELAXED,"61"));
            tag.setField(tag.createField(FieldKey.MOOD_SAD,"62"));
            tag.setField(tag.createField(FieldKey.MOOD_HAPPY,"63"));
            tag.setField(tag.createField(FieldKey.MOOD_PARTY,"64"));
            tag.setField(tag.createField(FieldKey.MOOD_DANCEABILITY,"65"));
            tag.setField(tag.createField(FieldKey.MOOD_VALENCE,"66"));
            tag.setField(tag.createField(FieldKey.MOOD_AROUSAL,"67"));
            tag.setField(tag.createField(FieldKey.MOOD_ACOUSTIC,"68"));
            tag.setField(tag.createField(FieldKey.MOOD_ELECTRONIC,"69"));
            tag.setField(tag.createField(FieldKey.MOOD_INSTRUMENTAL,"70"));
            tag.setField(tag.createField(FieldKey.TIMBRE,"71"));
            tag.setField(tag.createField(FieldKey.TONALITY,"72"));
            tag.setField(tag.createField(FieldKey.KEY,"Am"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA,"Orchestra"));
            tag.setField(tag.createField(FieldKey.PART,"Part"));
            tag.setField(tag.createField(FieldKey.PART_TYPE,"Composition Type"));
            tag.setField(tag.createField(FieldKey.WORK,"Work"));
            tag.setField(tag.createField(FieldKey.PERFORMER,"Performer"));
            tag.setField(tag.createField(FieldKey.WORK_TYPE,"WorkType"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94h"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94i"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94j"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94k"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94l"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION,"MusicBrainzWorkComposition"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1,"Level1"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE,"Level1Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2,"Level2"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE,"Level2Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3,"Level3"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE,"Level3Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4,"Level4"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE,"Level4Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5,"Level5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE,"Level5Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6,"Level6"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE,"Level6Type"));
            tag.setField(tag.createField(FieldKey.PART_NUMBER,"PartNumber"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.CONDUCTOR_SORT,"ConductorSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA_SORT,"OrchestraSort"));
            tag.setField(tag.createField(FieldKey.ARRANGER_SORT,"ArrangerSort"));
            tag.setField(tag.createField(FieldKey.OPUS,"Opus"));
            tag.setField(tag.createField(FieldKey.CHOIR,"Choir"));
            tag.setField(tag.createField(FieldKey.RANKING,"Ranking"));
            tag.setField(tag.createField(FieldKey.SINGLE_DISC_TRACK_NO,"SingleDiscTrackNo"));
            tag.setField(tag.createField(FieldKey.PERIOD,"Period"));
            tag.setField(tag.createField(FieldKey.IS_CLASSICAL,"true"));
            tag.setField(tag.createField(FieldKey.IS_SOUNDTRACK,"true"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE,"ensemble"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_CATALOG,"classicalcatalog"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_NICKNAME,"classicalnickname"));

            tag.setField(tag.createField(FieldKey.MOVEMENT,"Movement"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_NO,"1"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_TOTAL,"2"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            tag.setField(tag.createField(FieldKey.TITLE_MOVEMENT,"TitleMovement"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK,"MusicBrainzWork"));
            tag.setField(tag.createField(FieldKey.ITUNES_GROUPING,"ITUNESGROUPING"));



            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2",af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("Performer",af.getTag().getFirst(FieldKey.PERFORMER));
            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("Composition Type",af.getTag().getFirst(FieldKey.PART_TYPE));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("MusicBrainzWorkComposition",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("PartNumber",af.getTag().getFirst(FieldKey.PART_NUMBER));
            assertEquals("ArtistsSort",af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("ConductorSort",af.getTag().getFirst(FieldKey.CONDUCTOR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("OrchestraSort",af.getTag().getFirst(FieldKey.ORCHESTRA_SORT));
            assertEquals("ArrangerSort",af.getTag().getFirst(FieldKey.ARRANGER_SORT));
            assertEquals("Opus",af.getTag().getFirst(FieldKey.OPUS));
            assertEquals("Ranking",af.getTag().getFirst(FieldKey.RANKING));
            assertEquals("Choir",af.getTag().getFirst(FieldKey.CHOIR));
            assertEquals("SingleDiscTrackNo",af.getTag().getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            assertEquals("Period",af.getTag().getFirst(FieldKey.PERIOD));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_CLASSICAL));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_SOUNDTRACK));
            assertEquals("ChoirSort", af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("EnsembleSort", af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("PerformerName", af.getTag().getFirst(FieldKey.PERFORMER_NAME));
            assertEquals("PerformerNameSort", af.getTag().getFirst(FieldKey.PERFORMER_NAME_SORT));
            assertEquals("ensemble", af.getTag().getFirst(FieldKey.ENSEMBLE));
            assertEquals("classicalcatalog", af.getTag().getFirst(FieldKey.CLASSICAL_CATALOG));
            assertEquals("classicalnickname", af.getTag().getFirst(FieldKey.CLASSICAL_NICKNAME));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("TitleMovement", af.getTag().getFirst(FieldKey.TITLE_MOVEMENT));
            assertEquals("MusicBrainzWork", af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK));
            assertEquals("ITUNESGROUPING", af.getTag().getFirst(FieldKey.ITUNES_GROUPING));


            {
                TagField tagField = af.getTag().getFirstField(FieldKey.CUSTOM1);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
            }
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2",af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("Performer",af.getTag().getFirst(FieldKey.PERFORMER));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.CUSTOM1);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body =(FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                assertEquals(FrameBodyCOMM.MM_CUSTOM1,body.getDescription());
                assertEquals(Languages.MEDIA_MONKEY_ID,body.getLanguage());
            }
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.CUSTOM2);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body =(FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                assertEquals(FrameBodyCOMM.MM_CUSTOM2,body.getDescription());
                assertEquals(Languages.MEDIA_MONKEY_ID,body.getLanguage());
            }
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.CUSTOM3);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body =(FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                assertEquals(FrameBodyCOMM.MM_CUSTOM3,body.getDescription());
                assertEquals(Languages.MEDIA_MONKEY_ID,body.getLanguage());
            }
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.CUSTOM4);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body =(FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                assertEquals(FrameBodyCOMM.MM_CUSTOM4,body.getDescription());
                assertEquals(Languages.MEDIA_MONKEY_ID,body.getLanguage());
            }
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.CUSTOM5);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyCOMM);
                FrameBodyCOMM body =(FrameBodyCOMM)((ID3v24Frame)tagField).getBody();
                assertEquals(FrameBodyCOMM.MM_CUSTOM5,body.getDescription());
                assertEquals(Languages.MEDIA_MONKEY_ID,body.getLanguage());
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields
     */
    public void testWriteFieldsToMp3ID3v23()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.CUSTOM1,"custom1"));
            tag.setField(tag.createField(FieldKey.CUSTOM2,"custom2"));
            tag.setField(tag.createField(FieldKey.CUSTOM3,"custom3"));
            tag.setField(tag.createField(FieldKey.CUSTOM4,"custom4"));
            tag.setField(tag.createField(FieldKey.CUSTOM5,"custom5"));
            tag.setField(tag.createField(FieldKey.FBPM,"155.5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94e"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94f"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_ID,"0410c22a-0b2b-4793-9f18-5f1fab36338e"));
            tag.setField(tag.createField(FieldKey.OCCASION,"Occasion"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ALBUM,"original_album"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_LYRICIST,"original_lyricist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ARTIST,"original_artist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_YEAR,"2012"));
            tag.setField(tag.createField(FieldKey.QUALITY,"quality"));
            tag.setField(tag.createField(FieldKey.SCRIPT,"latin"));
            tag.setField(tag.createField(FieldKey.TAGS,"fred"));
            tag.setField(tag.createField(FieldKey.TEMPO,"Mellow"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD_AGGRESSIVE,"60"));
            tag.setField(tag.createField(FieldKey.MOOD_RELAXED,"61"));
            tag.setField(tag.createField(FieldKey.MOOD_SAD,"62"));
            tag.setField(tag.createField(FieldKey.MOOD_HAPPY,"63"));
            tag.setField(tag.createField(FieldKey.MOOD_PARTY,"64"));
            tag.setField(tag.createField(FieldKey.MOOD_DANCEABILITY,"65"));
            tag.setField(tag.createField(FieldKey.MOOD_VALENCE,"66"));
            tag.setField(tag.createField(FieldKey.MOOD_AROUSAL,"67"));
            tag.setField(tag.createField(FieldKey.MOOD_ACOUSTIC,"68"));
            tag.setField(tag.createField(FieldKey.MOOD_ELECTRONIC,"69"));
            tag.setField(tag.createField(FieldKey.MOOD_INSTRUMENTAL,"70"));
            tag.setField(tag.createField(FieldKey.TIMBRE,"71"));
            tag.setField(tag.createField(FieldKey.TONALITY,"72"));
            tag.setField(tag.createField(FieldKey.KEY,"Am"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA,"Orchestra"));
            tag.setField(tag.createField(FieldKey.PART,"Part"));
            tag.setField(tag.createField(FieldKey.WORK,"Work"));
            tag.setField(tag.createField(FieldKey.CHOIR,"Choir"));
            tag.setField(tag.createField(FieldKey.RANKING,"Ranking"));
            tag.setField(tag.createField(FieldKey.PERFORMER,"Performer"));
            tag.setField(tag.createField(FieldKey.WORK_TYPE,"WorkType"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94h"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94i"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94j"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94k"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94l"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION,"MusicBrainzWorkComposition"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1,"Level1"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE,"Level1Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2,"Level2"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE,"Level2Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3,"Level3"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE,"Level3Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4,"Level4"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE,"Level4Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5,"Level5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE,"Level5Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6,"Level6"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE,"Level6Type"));
            tag.setField(tag.createField(FieldKey.PART_NUMBER,"PartNumber"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.CONDUCTOR_SORT,"ConductorSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA_SORT,"OrchestraSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"Performer"));
            tag.setField(tag.createField(FieldKey.ARRANGER_SORT,"ArrangerSort"));
            tag.setField(tag.createField(FieldKey.OPUS,"Opus"));
            tag.setField(tag.createField(FieldKey.SINGLE_DISC_TRACK_NO,"SingleDiscTrackNo"));
            tag.setField(tag.createField(FieldKey.PERIOD,"Period"));
            tag.setField(tag.createField(FieldKey.PART_TYPE,"Composition Type"));
            tag.setField(tag.createField(FieldKey.IS_CLASSICAL,"true"));
            tag.setField(tag.createField(FieldKey.IS_SOUNDTRACK,"true"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            tag.setField(tag.createField(FieldKey.PRODUCER,"Producer"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE,"ensemble"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_CATALOG,"classicalcatalog"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_NICKNAME,"classicalnickname"));
            tag.setField(tag.createField(FieldKey.MOVEMENT,"Movement"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_NO,"1"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_TOTAL,"2"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            tag.setField(tag.createField(FieldKey.TITLE_MOVEMENT,"TitleMovement"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK,"MusicBrainzWork"));
            tag.setField(tag.createField(FieldKey.ITUNES_GROUPING,"ITUNESGROUPING"));

            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("ITUNESGROUPING", af.getTag().getFirst(FieldKey.ITUNES_GROUPING));

            Iterator<TagField> i =af.getTag().getFields();
            while(i.hasNext())
            {
                TagField tf = i.next();
                System.out.println(tf.getId()+":"+tf.toString());
            }

            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2", af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));

            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("Performer",af.getTag().getFirst(FieldKey.PERFORMER));
            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("Composition Type",af.getTag().getFirst(FieldKey.PART_TYPE));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("MusicBrainzWorkComposition",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("PartNumber",af.getTag().getFirst(FieldKey.PART_NUMBER));
            assertEquals("ArtistsSort",af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("ConductorSort",af.getTag().getFirst(FieldKey.CONDUCTOR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("OrchestraSort",af.getTag().getFirst(FieldKey.ORCHESTRA_SORT));
            assertEquals("ArrangerSort",af.getTag().getFirst(FieldKey.ARRANGER_SORT));
            assertEquals("Opus",af.getTag().getFirst(FieldKey.OPUS));
            assertEquals("Ranking",af.getTag().getFirst(FieldKey.RANKING));
            assertEquals("Choir",af.getTag().getFirst(FieldKey.CHOIR));

            assertEquals("SingleDiscTrackNo",af.getTag().getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            assertEquals("Period",af.getTag().getFirst(FieldKey.PERIOD));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_CLASSICAL));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_SOUNDTRACK));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("PerformerName",af.getTag().getFirst(FieldKey.PERFORMER_NAME));
            assertEquals("PerformerNameSort",af.getTag().getFirst(FieldKey.PERFORMER_NAME_SORT));
            assertEquals("Producer",af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("ensemble", af.getTag().getFirst(FieldKey.ENSEMBLE));
            assertEquals("classicalcatalog", af.getTag().getFirst(FieldKey.CLASSICAL_CATALOG));
            assertEquals("classicalnickname", af.getTag().getFirst(FieldKey.CLASSICAL_NICKNAME));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("TitleMovement", af.getTag().getFirst(FieldKey.TITLE_MOVEMENT));
            assertEquals("MusicBrainzWork", af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK));
            assertEquals("ITUNESGROUPING", af.getTag().getFirst(FieldKey.ITUNES_GROUPING));
            i =af.getTag().getFields();
            while(i.hasNext())
            {
                TagField tf = i.next();
                System.out.println(tf.getId()+":"+tf.toString());
            }
            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v22()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v22Tag());
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.CUSTOM1,"custom1"));
            tag.setField(tag.createField(FieldKey.CUSTOM2,"custom2"));
            tag.setField(tag.createField(FieldKey.CUSTOM3,"custom3"));
            tag.setField(tag.createField(FieldKey.CUSTOM4,"custom4"));
            tag.setField(tag.createField(FieldKey.CUSTOM5,"custom5"));
            tag.setField(tag.createField(FieldKey.FBPM,"155.5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94e"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94f"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_ID,"0410c22a-0b2b-4793-9f18-5f1fab36338e"));
            tag.setField(tag.createField(FieldKey.OCCASION,"Occasion"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ALBUM,"original_album"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_LYRICIST,"original_lyricist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ARTIST,"original_artist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_YEAR,"2012"));
            tag.setField(tag.createField(FieldKey.QUALITY,"quality"));
            tag.setField(tag.createField(FieldKey.SCRIPT,"latin"));
            tag.setField(tag.createField(FieldKey.TAGS,"fred"));
            tag.setField(tag.createField(FieldKey.TEMPO,"Mellow"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD_AGGRESSIVE,"60"));
            tag.setField(tag.createField(FieldKey.MOOD_RELAXED,"61"));
            tag.setField(tag.createField(FieldKey.MOOD_SAD,"62"));
            tag.setField(tag.createField(FieldKey.MOOD_HAPPY,"63"));
            tag.setField(tag.createField(FieldKey.MOOD_PARTY,"64"));
            tag.setField(tag.createField(FieldKey.MOOD_DANCEABILITY,"65"));
            tag.setField(tag.createField(FieldKey.MOOD_VALENCE,"66"));
            tag.setField(tag.createField(FieldKey.MOOD_AROUSAL,"67"));
            tag.setField(tag.createField(FieldKey.MOOD_ACOUSTIC,"68"));
            tag.setField(tag.createField(FieldKey.MOOD_ELECTRONIC,"69"));
            tag.setField(tag.createField(FieldKey.MOOD_INSTRUMENTAL,"70"));
            tag.setField(tag.createField(FieldKey.TIMBRE,"71"));
            tag.setField(tag.createField(FieldKey.TONALITY,"72"));
            tag.setField(tag.createField(FieldKey.KEY,"Am"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA,"Orchestra"));
            tag.setField(tag.createField(FieldKey.PART,"Part"));
            tag.setField(tag.createField(FieldKey.CHOIR,"Choir"));
            tag.setField(tag.createField(FieldKey.RANKING,"Ranking"));

            tag.setField(tag.createField(FieldKey.WORK,"Work"));
            tag.setField(tag.createField(FieldKey.PERFORMER,"Performer"));
            tag.setField(tag.createField(FieldKey.WORK_TYPE,"WorkType"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94h"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94i"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94j"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94k"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94l"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION,"MusicBrainzWorkComposition"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1,"Level1"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE,"Level1Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2,"Level2"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE,"Level2Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3,"Level3"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE,"Level3Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4,"Level4"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE,"Level4Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5,"Level5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE,"Level5Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6,"Level6"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE,"Level6Type"));
            tag.setField(tag.createField(FieldKey.PART_NUMBER,"PartNumber"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.CONDUCTOR_SORT,"ConductorSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA_SORT,"OrchestraSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"Performer"));
            tag.setField(tag.createField(FieldKey.ARRANGER_SORT,"ArrangerSort"));
            tag.setField(tag.createField(FieldKey.OPUS,"Opus"));
            tag.setField(tag.createField(FieldKey.SINGLE_DISC_TRACK_NO,"SingleDiscTrackNo"));
            tag.setField(tag.createField(FieldKey.PERIOD,"Period"));
            tag.setField(tag.createField(FieldKey.PART_TYPE,"Composition Type"));
            tag.setField(tag.createField(FieldKey.IS_CLASSICAL,"true"));
            tag.setField(tag.createField(FieldKey.IS_SOUNDTRACK,"true"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE,"ensemble"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_CATALOG,"classicalcatalog"));
            tag.setField(tag.createField(FieldKey.MOVEMENT,"Movement"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_NO,"1"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_TOTAL,"2"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_NICKNAME,"classicalnickname"));
            tag.setField(tag.createField(FieldKey.TITLE_MOVEMENT,"TitleMovement"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK,"MusicBrainzWork"));
            tag.setField(tag.createField(FieldKey.ITUNES_GROUPING,"ITUNESGROUPING"));


            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2", af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("Performer",af.getTag().getFirst(FieldKey.PERFORMER));
            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("MusicBrainzWorkComposition",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("PartNumber",af.getTag().getFirst(FieldKey.PART_NUMBER));
            assertEquals("ArtistsSort",af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("ConductorSort",af.getTag().getFirst(FieldKey.CONDUCTOR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("OrchestraSort",af.getTag().getFirst(FieldKey.ORCHESTRA_SORT));
            assertEquals("ArrangerSort",af.getTag().getFirst(FieldKey.ARRANGER_SORT));
            assertEquals("Opus",af.getTag().getFirst(FieldKey.OPUS));
            assertEquals("Ranking",af.getTag().getFirst(FieldKey.RANKING));
            assertEquals("Choir",af.getTag().getFirst(FieldKey.CHOIR));

            assertEquals("SingleDiscTrackNo",af.getTag().getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            assertEquals("Period",af.getTag().getFirst(FieldKey.PERIOD));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_CLASSICAL));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_SOUNDTRACK));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("PerformerName",af.getTag().getFirst(FieldKey.PERFORMER_NAME));
            assertEquals("PerformerNameSort",af.getTag().getFirst(FieldKey.PERFORMER_NAME_SORT));
            assertEquals("ensemble", af.getTag().getFirst(FieldKey.ENSEMBLE));
            assertEquals("classicalcatalog", af.getTag().getFirst(FieldKey.CLASSICAL_CATALOG));
            assertEquals("classicalnickname", af.getTag().getFirst(FieldKey.CLASSICAL_NICKNAME));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("TitleMovement", af.getTag().getFirst(FieldKey.TITLE_MOVEMENT));
            assertEquals("MusicBrainzWork", af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK));
            assertEquals("ITUNESGROUPING", af.getTag().getFirst(FieldKey.ITUNES_GROUPING));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields to Ogg Vorbis
     */
    public void testWriteFieldsToOggVorbis()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.CUSTOM1,"custom1"));
            tag.setField(tag.createField(FieldKey.CUSTOM2,"custom2"));
            tag.setField(tag.createField(FieldKey.CUSTOM3,"custom3"));
            tag.setField(tag.createField(FieldKey.CUSTOM4,"custom4"));
            tag.setField(tag.createField(FieldKey.CUSTOM5,"custom5"));
            tag.setField(tag.createField(FieldKey.FBPM,"155.5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94e"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94f"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_ID,"0410c22a-0b2b-4793-9f18-5f1fab36338e"));
            tag.setField(tag.createField(FieldKey.OCCASION,"Occasion"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ALBUM,"original_album"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_LYRICIST,"original_lyricist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ARTIST,"original_artist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_YEAR,"2012"));
            tag.setField(tag.createField(FieldKey.QUALITY,"quality"));
            tag.setField(tag.createField(FieldKey.SCRIPT,"latin"));
            tag.setField(tag.createField(FieldKey.TAGS,"fred"));
            tag.setField(tag.createField(FieldKey.TEMPO,"Mellow"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD_AGGRESSIVE,"60"));
            tag.setField(tag.createField(FieldKey.MOOD_RELAXED,"61"));
            tag.setField(tag.createField(FieldKey.MOOD_SAD,"62"));
            tag.setField(tag.createField(FieldKey.MOOD_HAPPY,"63"));
            tag.setField(tag.createField(FieldKey.MOOD_PARTY,"64"));
            tag.setField(tag.createField(FieldKey.MOOD_DANCEABILITY,"65"));
            tag.setField(tag.createField(FieldKey.MOOD_VALENCE,"66"));
            tag.setField(tag.createField(FieldKey.MOOD_AROUSAL,"67"));
            tag.setField(tag.createField(FieldKey.MOOD_ACOUSTIC,"68"));
            tag.setField(tag.createField(FieldKey.MOOD_ELECTRONIC,"69"));
            tag.setField(tag.createField(FieldKey.MOOD_INSTRUMENTAL,"70"));
            tag.setField(tag.createField(FieldKey.TIMBRE,"71"));
            tag.setField(tag.createField(FieldKey.TONALITY,"72"));
            tag.setField(tag.createField(FieldKey.KEY,"Am"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA,"Orchestra"));
            tag.setField(tag.createField(FieldKey.PART,"Part"));
            tag.setField(tag.createField(FieldKey.WORK,"Work"));
            tag.setField(tag.createField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist")));
            tag.setField(tag.createField(FieldKey.WORK_TYPE,"WorkType"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.PART_TYPE,"CompositionType"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94h"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94i"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94j"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94k"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94l"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION,"MusicBrainzWorkComposition"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1,"Level1"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE,"Level1Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2,"Level2"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE,"Level2Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3,"Level3"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE,"Level3Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4,"Level4"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE,"Level4Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5,"Level5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE,"Level5Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6,"Level6"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE,"Level6Type"));
            tag.setField(tag.createField(FieldKey.PART_NUMBER,"PartNumber"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.CONDUCTOR_SORT,"ConductorSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA_SORT,"OrchestraSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"Performer"));
            tag.setField(tag.createField(FieldKey.ARRANGER_SORT,"ArrangerSort"));
            tag.setField(tag.createField(FieldKey.OPUS,"Opus"));
            tag.setField(tag.createField(FieldKey.CHOIR,"Choir"));
            tag.setField(tag.createField(FieldKey.RANKING,"Ranking"));

            tag.setField(tag.createField(FieldKey.SINGLE_DISC_TRACK_NO,"SingleDiscTrackNo"));
            tag.setField(tag.createField(FieldKey.PERIOD,"Period"));
            tag.setField(tag.createField(FieldKey.IS_CLASSICAL,"true"));
            tag.setField(tag.createField(FieldKey.IS_SOUNDTRACK,"true"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE,"ensemble"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_CATALOG,"classicalcatalog"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_NICKNAME,"classicalnickname"));

            tag.setField(tag.createField(FieldKey.MOVEMENT,"Movement"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_NO,"1"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_TOTAL,"2"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            tag.setField(tag.createField(FieldKey.TITLE_MOVEMENT,"TitleMovement"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK,"MusicBrainzWork"));

            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2", af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("Nigel Kennedy (violinist)",af.getTag().getFirst(FieldKey.PERFORMER));
            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("MusicBrainzWorkComposition",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("PartNumber",af.getTag().getFirst(FieldKey.PART_NUMBER));
            assertEquals("ArtistsSort",af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("ConductorSort",af.getTag().getFirst(FieldKey.CONDUCTOR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("OrchestraSort",af.getTag().getFirst(FieldKey.ORCHESTRA_SORT));
            assertEquals("ArrangerSort",af.getTag().getFirst(FieldKey.ARRANGER_SORT));
            assertEquals("Opus",af.getTag().getFirst(FieldKey.OPUS));
            assertEquals("Ranking",af.getTag().getFirst(FieldKey.RANKING));
            assertEquals("Choir",af.getTag().getFirst(FieldKey.CHOIR));

            assertEquals("SingleDiscTrackNo",af.getTag().getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            assertEquals("Period",af.getTag().getFirst(FieldKey.PERIOD));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_CLASSICAL));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_SOUNDTRACK));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("PerformerName",af.getTag().getFirst(FieldKey.PERFORMER_NAME));
            assertEquals("PerformerNameSort",af.getTag().getFirst(FieldKey.PERFORMER_NAME_SORT));
            assertEquals("ensemble", af.getTag().getFirst(FieldKey.ENSEMBLE));
            assertEquals("classicalcatalog", af.getTag().getFirst(FieldKey.CLASSICAL_CATALOG));
            assertEquals("classicalnickname", af.getTag().getFirst(FieldKey.CLASSICAL_NICKNAME));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("TitleMovement", af.getTag().getFirst(FieldKey.TITLE_MOVEMENT));
            assertEquals("MusicBrainzWork", af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Flac
     */
    public void testWriteFieldsToFlac()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.CUSTOM1,"custom1"));
            tag.setField(tag.createField(FieldKey.CUSTOM2,"custom2"));
            tag.setField(tag.createField(FieldKey.CUSTOM3,"custom3"));
            tag.setField(tag.createField(FieldKey.CUSTOM4,"custom4"));
            tag.setField(tag.createField(FieldKey.CUSTOM5,"custom5"));
            tag.setField(tag.createField(FieldKey.FBPM,"155.5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94e"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94f"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_ID,"0410c22a-0b2b-4793-9f18-5f1fab36338e"));
            tag.setField(tag.createField(FieldKey.OCCASION,"Occasion"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ALBUM,"original_album"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_LYRICIST,"original_lyricist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ARTIST,"original_artist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_YEAR,"2012"));
            tag.setField(tag.createField(FieldKey.QUALITY,"quality"));
            tag.setField(tag.createField(FieldKey.SCRIPT,"latin"));
            tag.setField(tag.createField(FieldKey.TAGS,"fred"));
            tag.setField(tag.createField(FieldKey.TEMPO,"Mellow"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD_AGGRESSIVE,"60"));
            tag.setField(tag.createField(FieldKey.MOOD_RELAXED,"61"));
            tag.setField(tag.createField(FieldKey.MOOD_SAD,"62"));
            tag.setField(tag.createField(FieldKey.MOOD_HAPPY,"63"));
            tag.setField(tag.createField(FieldKey.MOOD_PARTY,"64"));
            tag.setField(tag.createField(FieldKey.MOOD_DANCEABILITY,"65"));
            tag.setField(tag.createField(FieldKey.MOOD_VALENCE,"66"));
            tag.setField(tag.createField(FieldKey.MOOD_AROUSAL,"67"));
            tag.setField(tag.createField(FieldKey.MOOD_ACOUSTIC,"68"));
            tag.setField(tag.createField(FieldKey.MOOD_ELECTRONIC,"69"));
            tag.setField(tag.createField(FieldKey.MOOD_INSTRUMENTAL,"70"));
            tag.setField(tag.createField(FieldKey.TIMBRE,"71"));
            tag.setField(tag.createField(FieldKey.TONALITY,"72"));
            tag.setField(tag.createField(FieldKey.KEY,"Am"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA,"Orchestra"));
            tag.setField(tag.createField(FieldKey.PART,"Part"));
            tag.setField(tag.createField(FieldKey.WORK,"Work"));
            tag.setField(tag.createField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist")));
            tag.setField(tag.createField(FieldKey.WORK_TYPE,"WorkType"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94h"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94i"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94j"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94k"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94l"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION,"MusicBrainzWorkComposition"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1,"Level1"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE,"Level1Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2,"Level2"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE,"Level2Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3,"Level3"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE,"Level3Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4,"Level4"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE,"Level4Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5,"Level5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE,"Level5Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6,"Level6"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE,"Level6Type"));
            tag.setField(tag.createField(FieldKey.PART_NUMBER,"PartNumber"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.CONDUCTOR_SORT,"ConductorSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA_SORT,"OrchestraSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"Performer"));
            tag.setField(tag.createField(FieldKey.ARRANGER_SORT,"ArrangerSort"));
            tag.setField(tag.createField(FieldKey.OPUS,"Opus"));
            tag.setField(tag.createField(FieldKey.CHOIR,"Choir"));
            tag.setField(tag.createField(FieldKey.RANKING,"Ranking"));
            tag.setField(tag.createField(FieldKey.MOVEMENT,"Movement"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_NO,"1"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_TOTAL,"2"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            tag.setField(tag.createField(FieldKey.SINGLE_DISC_TRACK_NO,"SingleDiscTrackNo"));
            tag.setField(tag.createField(FieldKey.PERIOD,"Period"));
            tag.setField(tag.createField(FieldKey.PART_TYPE,"CompositionType"));
            tag.setField(tag.createField(FieldKey.IS_CLASSICAL,"true"));
            tag.setField(tag.createField(FieldKey.IS_SOUNDTRACK,"true"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE,"ensemble"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_CATALOG,"classicalcatalog"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_NICKNAME,"classicalnickname"));
            tag.setField(tag.createField(FieldKey.TITLE_MOVEMENT,"TitleMovement"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK,"MusicBrainzWork"));

            af.commit();

            af = AudioFileIO.read(testFile);
            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2",af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));
            assertEquals("Nigel Kennedy (violinist)",af.getTag().getFirst(FieldKey.PERFORMER));

            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("MusicBrainzWorkComposition",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("CompositionType",af.getTag().getFirst(FieldKey.PART_TYPE));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("PartNumber",af.getTag().getFirst(FieldKey.PART_NUMBER));
            assertEquals("ArtistsSort",af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("ConductorSort",af.getTag().getFirst(FieldKey.CONDUCTOR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("OrchestraSort",af.getTag().getFirst(FieldKey.ORCHESTRA_SORT));
            assertEquals("ArrangerSort",af.getTag().getFirst(FieldKey.ARRANGER_SORT));
            assertEquals("Opus",af.getTag().getFirst(FieldKey.OPUS));
            assertEquals("Ranking",af.getTag().getFirst(FieldKey.RANKING));
            assertEquals("Choir",af.getTag().getFirst(FieldKey.CHOIR));

            assertEquals("SingleDiscTrackNo",af.getTag().getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            assertEquals("Period",af.getTag().getFirst(FieldKey.PERIOD));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_CLASSICAL));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_SOUNDTRACK));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("PerformerName",af.getTag().getFirst(FieldKey.PERFORMER_NAME));
            assertEquals("PerformerNameSort",af.getTag().getFirst(FieldKey.PERFORMER_NAME_SORT));
            assertEquals("ensemble", af.getTag().getFirst(FieldKey.ENSEMBLE));
            assertEquals("classicalcatalog", af.getTag().getFirst(FieldKey.CLASSICAL_CATALOG));
            assertEquals("classicalnickname", af.getTag().getFirst(FieldKey.CLASSICAL_NICKNAME));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("TitleMovement", af.getTag().getFirst(FieldKey.TITLE_MOVEMENT));
            assertEquals("MusicBrainzWork", af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }



    /**
     * Test writing Custom fields to Wma
     */
    public void testWriteFieldsToWma()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test1.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.CUSTOM1,"custom1"));
            tag.setField(tag.createField(FieldKey.CUSTOM2,"custom2"));
            tag.setField(tag.createField(FieldKey.CUSTOM3,"custom3"));
            tag.setField(tag.createField(FieldKey.CUSTOM4,"custom4"));
            tag.setField(tag.createField(FieldKey.CUSTOM5,"custom5"));
            tag.setField(tag.createField(FieldKey.FBPM,"155.5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94e"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94f"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_ID,"0410c22a-0b2b-4793-9f18-5f1fab36338e"));
            tag.setField(tag.createField(FieldKey.OCCASION,"Occasion"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ALBUM,"original_album"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_LYRICIST,"original_lyricist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ARTIST,"original_artist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_YEAR,"2012"));
            tag.setField(tag.createField(FieldKey.QUALITY,"quality"));
            tag.setField(tag.createField(FieldKey.SCRIPT,"latin"));
            tag.setField(tag.createField(FieldKey.TAGS,"fred"));
            tag.setField(tag.createField(FieldKey.TEMPO,"Mellow"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD_AGGRESSIVE,"60"));
            tag.setField(tag.createField(FieldKey.MOOD_RELAXED,"61"));
            tag.setField(tag.createField(FieldKey.MOOD_SAD,"62"));
            tag.setField(tag.createField(FieldKey.MOOD_HAPPY,"63"));
            tag.setField(tag.createField(FieldKey.MOOD_PARTY,"64"));
            tag.setField(tag.createField(FieldKey.MOOD_DANCEABILITY,"65"));
            tag.setField(tag.createField(FieldKey.MOOD_VALENCE,"66"));
            tag.setField(tag.createField(FieldKey.MOOD_AROUSAL,"67"));
            tag.setField(tag.createField(FieldKey.MOOD_ACOUSTIC,"68"));
            tag.setField(tag.createField(FieldKey.MOOD_ELECTRONIC,"69"));
            tag.setField(tag.createField(FieldKey.MOOD_INSTRUMENTAL,"70"));
            tag.setField(tag.createField(FieldKey.TIMBRE,"71"));
            tag.setField(tag.createField(FieldKey.TONALITY,"72"));
            tag.setField(tag.createField(FieldKey.KEY,"Am"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA,"Orchestra"));
            tag.setField(tag.createField(FieldKey.PART,"Part"));
            tag.setField(tag.createField(FieldKey.WORK,"Work"));
            tag.setField(tag.createField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist")));
            tag.setField(tag.createField(FieldKey.WORK_TYPE,"WorkType"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.PART_TYPE,"CompositionType"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94h"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94i"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94j"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94k"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94l"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION,"MusicBrainzWorkComposition"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1,"Level1"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE,"Level1Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2,"Level2"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE,"Level2Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3,"Level3"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE,"Level3Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4,"Level4"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE,"Level4Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5,"Level5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE,"Level5Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6,"Level6"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE,"Level6Type"));
            tag.setField(tag.createField(FieldKey.PART_NUMBER,"PartNumber"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.CONDUCTOR_SORT,"ConductorSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA_SORT,"OrchestraSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"Performer"));
            tag.setField(tag.createField(FieldKey.ARRANGER_SORT,"ArrangerSort"));
            tag.setField(tag.createField(FieldKey.OPUS,"Opus"));
            tag.setField(tag.createField(FieldKey.CHOIR,"Choir"));
            tag.setField(tag.createField(FieldKey.RANKING,"Ranking"));
            tag.setField(tag.createField(FieldKey.SINGLE_DISC_TRACK_NO,"SingleDiscTrackNo"));
            tag.setField(tag.createField(FieldKey.PERIOD,"Period"));
            tag.setField(tag.createField(FieldKey.IS_CLASSICAL,"true"));
            tag.setField(tag.createField(FieldKey.IS_SOUNDTRACK,"true"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE,"ensemble"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_CATALOG,"classicalcatalog"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_NICKNAME,"classicalnickname"));
            tag.setField(tag.createField(FieldKey.MOVEMENT,"Movement"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_NO,"1"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_TOTAL,"2"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            tag.setField(tag.createField(FieldKey.TITLE_MOVEMENT,"TitleMovement"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK,"MusicBrainzWork"));

            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2", af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("Nigel Kennedy (violinist)",af.getTag().getFirst(FieldKey.PERFORMER));
            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("MusicBrainzWorkComposition",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("PartNumber",af.getTag().getFirst(FieldKey.PART_NUMBER));
            assertEquals("ArtistsSort",af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("ConductorSort",af.getTag().getFirst(FieldKey.CONDUCTOR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("OrchestraSort",af.getTag().getFirst(FieldKey.ORCHESTRA_SORT));
            assertEquals("ArrangerSort",af.getTag().getFirst(FieldKey.ARRANGER_SORT));
            assertEquals("Opus",af.getTag().getFirst(FieldKey.OPUS));
            assertEquals("Ranking",af.getTag().getFirst(FieldKey.RANKING));
            assertEquals("Choir",af.getTag().getFirst(FieldKey.CHOIR));
            assertEquals("SingleDiscTrackNo",af.getTag().getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            assertEquals("Period",af.getTag().getFirst(FieldKey.PERIOD));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_CLASSICAL));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_SOUNDTRACK));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("PerformerName",af.getTag().getFirst(FieldKey.PERFORMER_NAME));
            assertEquals("PerformerNameSort",af.getTag().getFirst(FieldKey.PERFORMER_NAME_SORT));
            assertEquals("ensemble", af.getTag().getFirst(FieldKey.ENSEMBLE));
            assertEquals("classicalcatalog", af.getTag().getFirst(FieldKey.CLASSICAL_CATALOG));
            assertEquals("classicalnickname", af.getTag().getFirst(FieldKey.CLASSICAL_NICKNAME));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("TitleMovement", af.getTag().getFirst(FieldKey.TITLE_MOVEMENT));
            assertEquals("MusicBrainzWork", af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields to Mp4
     */
    public void testWriteFieldsToMp4()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.CUSTOM1,"custom1"));
            tag.setField(tag.createField(FieldKey.CUSTOM2,"custom2"));
            tag.setField(tag.createField(FieldKey.CUSTOM3,"custom3"));
            tag.setField(tag.createField(FieldKey.CUSTOM4,"custom4"));
            tag.setField(tag.createField(FieldKey.CUSTOM5,"custom5"));
            tag.setField(tag.createField(FieldKey.FBPM,"155.5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94e"));

            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94f"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_ID,"0410c22a-0b2b-4793-9f18-5f1fab36338e"));
            tag.setField(tag.createField(FieldKey.OCCASION,"Occasion"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ALBUM,"original_album"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_LYRICIST,"original_lyricist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_ARTIST,"original_artist"));
            tag.setField(tag.createField(FieldKey.ORIGINAL_YEAR,"2012"));
            tag.setField(tag.createField(FieldKey.QUALITY,"quality"));
            tag.setField(tag.createField(FieldKey.SCRIPT,"latin"));
            tag.setField(tag.createField(FieldKey.TAGS,"fred"));
            tag.setField(tag.createField(FieldKey.TEMPO,"Mellow"));
            tag.setField(tag.createField(FieldKey.MOOD,"Bad Mood"));
            tag.setField(tag.createField(FieldKey.MOOD_AGGRESSIVE,"60"));
            tag.setField(tag.createField(FieldKey.MOOD_RELAXED,"61"));
            tag.setField(tag.createField(FieldKey.MOOD_SAD,"62"));
            tag.setField(tag.createField(FieldKey.MOOD_HAPPY,"63"));
            tag.setField(tag.createField(FieldKey.MOOD_PARTY,"64"));
            tag.setField(tag.createField(FieldKey.MOOD_DANCEABILITY,"65"));
            tag.setField(tag.createField(FieldKey.MOOD_VALENCE,"66"));
            tag.setField(tag.createField(FieldKey.MOOD_AROUSAL,"67"));
            tag.setField(tag.createField(FieldKey.MOOD_ACOUSTIC,"68"));
            tag.setField(tag.createField(FieldKey.MOOD_ELECTRONIC,"69"));
            tag.setField(tag.createField(FieldKey.MOOD_INSTRUMENTAL,"70"));
            tag.setField(tag.createField(FieldKey.TIMBRE,"71"));
            tag.setField(tag.createField(FieldKey.TONALITY,"72"));
            tag.setField(tag.createField(FieldKey.KEY,"Am"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA,"Orchestra"));
            tag.setField(tag.createField(FieldKey.PART,"Part"));
            tag.setField(tag.createField(FieldKey.WORK,"Work"));
            tag.setField(tag.createField(FieldKey.PERFORMER, PerformerHelper.formatForNonId3("Nigel Kennedy", "violinist")));
            tag.setField(tag.createField(FieldKey.WORK_TYPE,"WorkType"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94g"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94h"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94i"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94j"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94k"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID,"c1f657ba-8177-3cbb-b84a-f62bc684a94l"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_COMPOSITION,"MusicBrainzWorkComposition"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1,"Level1"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE,"Level1Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2,"Level2"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE,"Level2Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3,"Level3"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE,"Level3Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4,"Level4"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE,"Level4Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5,"Level5"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE,"Level5Type"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6,"Level6"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE,"Level6Type"));
            tag.setField(tag.createField(FieldKey.PART_NUMBER,"PartNumber"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.CONDUCTOR_SORT,"ConductorSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.ORCHESTRA_SORT,"OrchestraSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"Performer"));
            tag.setField(tag.createField(FieldKey.ARRANGER_SORT,"ArrangerSort"));
            tag.setField(tag.createField(FieldKey.OPUS,"Opus"));
            tag.setField(tag.createField(FieldKey.CHOIR,"Choir"));
            tag.setField(tag.createField(FieldKey.RANKING,"Ranking"));

            tag.setField(tag.createField(FieldKey.SINGLE_DISC_TRACK_NO,"SingleDiscTrackNo"));
            tag.setField(tag.createField(FieldKey.PERIOD,"Period"));
            tag.setField(tag.createField(FieldKey.PART_TYPE,"Composition Type"));
            tag.setField(tag.createField(FieldKey.IS_CLASSICAL,"true"));
            tag.setField(tag.createField(FieldKey.IS_SOUNDTRACK,"true"));
            tag.setField(tag.createField(FieldKey.CHOIR_SORT,"ChoirSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE_SORT,"EnsembleSort"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME,"PerformerName"));
            tag.setField(tag.createField(FieldKey.PERFORMER_NAME_SORT,"PerformerNameSort"));
            tag.setField(tag.createField(FieldKey.ENSEMBLE,"ensemble"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_CATALOG,"classicalcatalog"));
            tag.setField(tag.createField(FieldKey.CLASSICAL_NICKNAME,"classicalnickname"));

            tag.setField(tag.createField(FieldKey.MOVEMENT,"Movement"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_NO,"1"));
            tag.setField(tag.createField(FieldKey.MOVEMENT_TOTAL,"2"));
            tag.setField(tag.createField(FieldKey.ARTISTS_SORT,"ArtistsSort"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS,"AlbumArtists"));
            tag.setField(tag.createField(FieldKey.ALBUM_ARTISTS_SORT,"AlbumArtistsSort"));
            tag.setField(tag.createField(FieldKey.TITLE_MOVEMENT,"TitleMovement"));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_WORK,"MusicBrainzWork"));

            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("custom1",af.getTag().getFirst(FieldKey.CUSTOM1));
            assertEquals("custom2", af.getTag().getFirst(FieldKey.CUSTOM2));
            assertEquals("custom3",af.getTag().getFirst(FieldKey.CUSTOM3));
            assertEquals("custom4",af.getTag().getFirst(FieldKey.CUSTOM4));
            assertEquals("custom5",af.getTag().getFirst(FieldKey.CUSTOM5));
            assertEquals("155.5",af.getTag().getFirst(FieldKey.FBPM));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94f", af.getTag().getFirst(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID));
            assertEquals("0410c22a-0b2b-4793-9f18-5f1fab36338e",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_ID));
            assertEquals("Occasion",af.getTag().getFirst(FieldKey.OCCASION));
            assertEquals("original_album",af.getTag().getFirst(FieldKey.ORIGINAL_ALBUM));
            assertEquals("original_lyricist",af.getTag().getFirst(FieldKey.ORIGINAL_LYRICIST));
            assertEquals("original_artist",af.getTag().getFirst(FieldKey.ORIGINAL_ARTIST));
            assertEquals("2012",af.getTag().getFirst(FieldKey.ORIGINAL_YEAR));
            assertEquals("quality",af.getTag().getFirst(FieldKey.QUALITY));
            assertEquals("latin",af.getTag().getFirst(FieldKey.SCRIPT));
            assertEquals("fred",af.getTag().getFirst(FieldKey.TAGS));
            assertEquals("Mellow",af.getTag().getFirst(FieldKey.TEMPO));
            assertEquals("Bad Mood",af.getTag().getFirst(FieldKey.MOOD));
            assertEquals("60",af.getTag().getFirst(FieldKey.MOOD_AGGRESSIVE));
            assertEquals("61",af.getTag().getFirst(FieldKey.MOOD_RELAXED));
            assertEquals("62",af.getTag().getFirst(FieldKey.MOOD_SAD));
            assertEquals("63",af.getTag().getFirst(FieldKey.MOOD_HAPPY));
            assertEquals("64",af.getTag().getFirst(FieldKey.MOOD_PARTY));
            assertEquals("65",af.getTag().getFirst(FieldKey.MOOD_DANCEABILITY));
            assertEquals("66",af.getTag().getFirst(FieldKey.MOOD_VALENCE));
            assertEquals("67",af.getTag().getFirst(FieldKey.MOOD_AROUSAL));
            assertEquals("68",af.getTag().getFirst(FieldKey.MOOD_ACOUSTIC));
            assertEquals("69",af.getTag().getFirst(FieldKey.MOOD_ELECTRONIC));
            assertEquals("70",af.getTag().getFirst(FieldKey.MOOD_INSTRUMENTAL));
            assertEquals("71",af.getTag().getFirst(FieldKey.TIMBRE));
            assertEquals("72",af.getTag().getFirst(FieldKey.TONALITY));
            assertEquals("Am",af.getTag().getFirst(FieldKey.KEY));
            assertEquals("Orchestra",af.getTag().getFirst(FieldKey.ORCHESTRA));
            assertEquals("Part",af.getTag().getFirst(FieldKey.PART));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("Nigel Kennedy (violinist)",af.getTag().getFirst(FieldKey.PERFORMER));
            assertEquals("WorkType",af.getTag().getFirst(FieldKey.WORK_TYPE));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID));
            assertEquals("Composition Type",af.getTag().getFirst(FieldKey.PART_TYPE));
            assertEquals("MusicBrainzWorkComposition",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_COMPOSITION));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94g",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94h",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94i",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94j",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94k",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID));
            assertEquals("c1f657ba-8177-3cbb-b84a-f62bc684a94l",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID));
            assertEquals("Level1",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1));
            assertEquals("Level2",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2));
            assertEquals("Level3",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3));
            assertEquals("Level4",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4));
            assertEquals("Level5",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5));
            assertEquals("Level6",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6));
            assertEquals("Level1Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE));
            assertEquals("Level2Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE));
            assertEquals("Level3Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE));
            assertEquals("Level4Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE));
            assertEquals("Level5Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE));
            assertEquals("Level6Type",af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE));
            assertEquals("Work",af.getTag().getFirst(FieldKey.WORK));
            assertEquals("PartNumber",af.getTag().getFirst(FieldKey.PART_NUMBER));
            assertEquals("ArtistsSort",af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("ConductorSort",af.getTag().getFirst(FieldKey.CONDUCTOR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("OrchestraSort",af.getTag().getFirst(FieldKey.ORCHESTRA_SORT));
            assertEquals("ArrangerSort",af.getTag().getFirst(FieldKey.ARRANGER_SORT));
            assertEquals("Opus",af.getTag().getFirst(FieldKey.OPUS));
            assertEquals("Ranking",af.getTag().getFirst(FieldKey.RANKING));
            assertEquals("Choir",af.getTag().getFirst(FieldKey.CHOIR));

            assertEquals("SingleDiscTrackNo",af.getTag().getFirst(FieldKey.SINGLE_DISC_TRACK_NO));
            assertEquals("Period",af.getTag().getFirst(FieldKey.PERIOD));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_CLASSICAL));
            assertEquals("true",af.getTag().getFirst(FieldKey.IS_SOUNDTRACK));
            assertEquals("ChoirSort",af.getTag().getFirst(FieldKey.CHOIR_SORT));
            assertEquals("EnsembleSort",af.getTag().getFirst(FieldKey.ENSEMBLE_SORT));
            assertEquals("PerformerName",af.getTag().getFirst(FieldKey.PERFORMER_NAME));
            assertEquals("PerformerNameSort",af.getTag().getFirst(FieldKey.PERFORMER_NAME_SORT));
            assertEquals("ensemble", af.getTag().getFirst(FieldKey.ENSEMBLE));
            assertEquals("classicalcatalog", af.getTag().getFirst(FieldKey.CLASSICAL_CATALOG));
            assertEquals("classicalnickname", af.getTag().getFirst(FieldKey.CLASSICAL_NICKNAME));
            assertEquals("Movement", af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1", af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("2", af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            assertEquals("ArtistsSort", af.getTag().getFirst(FieldKey.ARTISTS_SORT));
            assertEquals("AlbumArtists", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS));
            assertEquals("AlbumArtistsSort", af.getTag().getFirst(FieldKey.ALBUM_ARTISTS_SORT));
            assertEquals("TitleMovement", af.getTag().getFirst(FieldKey.TITLE_MOVEMENT));
            assertEquals("MusicBrainzWork", af.getTag().getFirst(FieldKey.MUSICBRAINZ_WORK));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }
}