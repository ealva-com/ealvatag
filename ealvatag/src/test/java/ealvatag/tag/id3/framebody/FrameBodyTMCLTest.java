package ealvatag.tag.id3.framebody;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.reference.ID3V2Version;
import ealvatag.tag.reference.PerformerHelper;

import java.io.File;


public class FrameBodyTMCLTest extends AbstractTestCase {
    /**
     * Uses TMCL frame
     *
     * @throws Exception
     */
    public void testWritePerformersIDv24() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, "violinist", "Nigel Kennedy");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist", "Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWritePerformersAndDeleteIDv24() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersAndDeletev24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, "violinist", "Nigel Kennedy");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist", "Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());

        f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.PERFORMER);
        assertEquals(0, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(0, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(0, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(0, f.getTag().or(NullTag.INSTANCE).getFieldCount());

    }

    public void testWritePerformersIDv23() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv23.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, "violinist", "Nigel Kennedy");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist", "Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWritePerformersIDv22() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, "violinist", "Nigel Kennedy");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist", "Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    /**
     * Uses TMCL frame
     *
     * @throws Exception
     */
    public void testWritePerformersIDv24v2() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, "violinist\0Nigel Kennedy");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist\0Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    /**
     * Uses TMCL frame
     *
     * @throws Exception
     */
    public void testWritePerformersIDv24v3() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, PerformerHelper.formatForId3("Nigel Kennedy", "violinist"));
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, PerformerHelper.formatForId3("Gloria Divosky", "harpist"));
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWritePerformersIDv23v2() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv23.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, "violinist\0Nigel Kennedy");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist\0Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWritePerformersIDv23v3() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv23.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, PerformerHelper.formatForId3("Nigel Kennedy", "violinist"));
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, PerformerHelper.formatForId3("Gloria Divosky", "harpist"));
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWritePerformersIDv22v2() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, "violinist\0Nigel Kennedy");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist\0Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWritePerformersIDv22v3() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWritePerformersv22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, PerformerHelper.formatForId3("Nigel Kennedy", "violinist"));
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PERFORMER, PerformerHelper.formatForId3("Gloria Divosky", "harpist"));
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PERFORMER));
        assertEquals("violinist\0Nigel Kennedy", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 1));
        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());

    }

    public void testWriteMultiplePeopleIDv24() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMultiplePeoplev24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PRODUCER, "steve lilllywhite");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist", "Gloria Divosky");
        assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PRODUCER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));

        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFieldCount());

        f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.PERFORMER);
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PRODUCER, 0));


        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(0, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWriteMultiplePeopleIDv23() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMultiplePeoplev23.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PRODUCER, "steve lilllywhite");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist", "Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PRODUCER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));


        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());

        f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.PERFORMER);
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PRODUCER, 0));


        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }

    public void testWriteMultiplePeopleIDv22() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMultiplePeoplev22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag().orNull());

        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        f.setNewDefaultTag();
        f.getTag().or(NullTag.INSTANCE).setField(FieldKey.PRODUCER, "steve lilllywhite");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.PERFORMER, "harpist", "Gloria Divosky");
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PRODUCER, 0));
        assertEquals("harpist\0Gloria Divosky", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PERFORMER, 0));


        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());

        f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.PERFORMER);
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
        assertEquals("steve lilllywhite", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.PRODUCER, 0));

        f.save();
        f = AudioFileIO.read(testFile);
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.PERFORMER).size());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
    }
}
