/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ealvatag.tag.id3;

import ealvatag.tag.NullTag;
import ealvatag.tag.id3.framebody.FrameBodyCOMM;
import ealvatag.tag.id3.framebody.FrameBodyTALB;
import ealvatag.tag.id3.framebody.FrameBodyTCON;
import ealvatag.tag.id3.framebody.FrameBodyTDRL;
import ealvatag.tag.id3.framebody.FrameBodyTIT2;
import ealvatag.tag.id3.framebody.FrameBodyTPE1;
import ealvatag.tag.id3.framebody.FrameBodyTRCK;
import ealvatag.tag.id3.framebody.FrameBodyTYER;
import ealvatag.tag.id3.framebody.FrameBodyUnsupported;
import ealvatag.tag.reference.ID3V2Version;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ealvatag.AbstractTestCase;
import ealvatag.FilePermissionsTest;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.datatype.DataTypes;

import java.io.File;
import java.util.List;

/**
 *
 */
public class ID3v23TagTest extends TestCase
{
    /**
     * Constructor
     *
     * @param arg0
     */
    public ID3v23TagTest(String arg0)
    {
        super(arg0);
    }

    /**
     * Command line entrance.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    /////////////////////////////////////////////////////////////////////////
    // TestCase classes to override
    /////////////////////////////////////////////////////////////////////////

    /**
        *
        */
       protected void setUp()
       {
           TagOptionSingleton.getInstance().setToDefault();
       }

       /**
        *
        */
       protected void tearDown()
       {
       }


    /**
     *
     */
//    protected void runTest()
//    {
//    }

    /**
     * Builds the Test Suite.
     *
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(ID3v23TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////

    public void testReadID3v1ID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

    public void testReadID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

    public void testReadPaddedID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2pad.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

    public void testDeleteID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());

        mp3File.setID3v1Tag(null);
        mp3File.setID3v2Tag(null);
        try
        {
            mp3File.saveMp3();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNull(mp3File.getID3v2Tag());
    }

    public void testCreateIDv23Tag()
    {
        ID3v23Tag v2Tag = new ID3v23Tag();
        assertEquals((byte) 2, v2Tag.getRelease());
        assertEquals((byte) 3, v2Tag.getMajorVersion());
        assertEquals((byte) 0, v2Tag.getRevision());
    }

    public void testCreateID3v23FromID3v11()
    {
        ID3v11Tag v11Tag = ID3v11TagTest.getInitialisedTag();
        ID3v23Tag v2Tag = new ID3v23Tag(v11Tag);
        assertNotNull(v11Tag);
        assertNotNull(v2Tag);
        assertEquals(ID3v11TagTest.ARTIST, ((FrameBodyTPE1) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST)).getBody()).getText());
        assertEquals(ID3v11TagTest.ALBUM, ((FrameBodyTALB) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ALBUM)).getBody()).getText());
        assertEquals(ID3v11TagTest.COMMENT, ((FrameBodyCOMM) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_COMMENT)).getBody()).getText());
        assertEquals(ID3v11TagTest.TITLE, ((FrameBodyTIT2) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TITLE)).getBody()).getText());
        assertEquals(ID3v11TagTest.TRACK_VALUE, String.valueOf(((FrameBodyTRCK) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TRACK)).getBody()).getTrackNo()));
        assertTrue(((FrameBodyTCON) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_GENRE)).getBody()).getText().endsWith(ID3v11TagTest.GENRE_VAL));
        assertEquals(ID3v11TagTest.YEAR, ((FrameBodyTYER) ((ID3v23Frame) v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER)).getBody()).getText());

        assertEquals((byte) 2, v2Tag.getRelease());
        assertEquals((byte) 3, v2Tag.getMajorVersion());
        assertEquals((byte) 0, v2Tag.getRevision());

    }

    /**
     * Test converting a v24 tag to a v23 tag, the v24 tag contains:
     * <p/>
     * A frame which is known in v24 and v23
     *
     * @throws Exception
     */
    public void testCreateID3v23FromID3v24knownInV3() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = null;
        mp3File = new MP3File(testFile);

        //Add v24 tag to mp3 with single tdrl frame (which is only supported in v24)
        ID3v24Tag tag = new ID3v24Tag();
        FrameBodyTIT2 framebodyTdrl = new FrameBodyTIT2();
        framebodyTdrl.setText("title");
        ID3v24Frame tdrlFrame = new ID3v24Frame("TIT2");
        tdrlFrame.setBody(framebodyTdrl);
        tag.setFrame(tdrlFrame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame converted and marked as unsupported
        ID3v23Tag v23tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        ID3v23Frame v23frame = (ID3v23Frame) v23tag.getFrame("TIT2");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyTIT2);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.saveMp3();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame should still exist
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TIT2");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyTIT2);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.saveMp3();

        //Check when converted to v24 has value been maintained
        assertEquals("title", ((FrameBodyTIT2) ((ID3v24Frame) mp3File.getID3v2TagAsv24().getFrame("TIT2")).getBody()).getText());
    }

    /**
     * Test converting a v24 tag to a v23 tag, the v24 tag contains:
     * <p/>
     * A frame which is known in v24 but not v23
     *
     * @throws Exception
     */
    public void testCreateID3v23FromID3v24UnknownInV3() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = null;
        mp3File = new MP3File(testFile);

        //Add v24 tag to mp3 with single tdrl frame (which is only supported in v24)
        ID3v24Tag tag = new ID3v24Tag();
        FrameBodyTDRL framebodyTdrl = new FrameBodyTDRL();
        framebodyTdrl.setText("2008");
        ID3v24Frame tdrlFrame = new ID3v24Frame("TDRL");
        tdrlFrame.setBody(framebodyTdrl);
        tag.setFrame(tdrlFrame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame converted and marked as unsupported
        ID3v23Tag v23tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        ID3v23Frame v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.saveMp3();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame should still exist
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.saveMp3();

        //Check value maintained, can only see as bytes
        FrameBodyUnsupported v23FrameBody = (FrameBodyUnsupported) v23frame.getBody();
        assertEquals((byte) '2', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[1]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[2]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[3]);
        assertEquals((byte) '8', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[4]);

        //Reread from File
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Check value maintained, can only see as bytes
        v23FrameBody = (FrameBodyUnsupported) v23frame.getBody();
        assertEquals((byte) '2', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[1]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[2]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[3]);
        assertEquals((byte) '8', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[4]);

        //Convert V24 representation to V23, and then save this
        v23tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        mp3File.setID3v2TagOnly(v23tag);
        mp3File.setID3v2Tag(v23tag);
        mp3File.saveMp3();

        //Reread from File
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23tag.getFrame("TDRL");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Check value maintained, can only see as bytes
        v23FrameBody = (FrameBodyUnsupported) v23frame.getBody();
        assertEquals((byte) '2', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[1]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[2]);
        assertEquals((byte) '0', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[3]);
        assertEquals((byte) '8', ((byte[]) v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[4]);

    }

    /**
     * Test converting a v24 tag to a v23 tag, the v24 tag contains:
     *
     * A frame which is unknown in v24 and v23
     *
     * @throws Exception
     */
    /*
    public void testCreateID3v23FromID3v24UnknownInV3AndV4()throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = null;
        mp3File = new MP3File(testFile);

        //Add v24 tag to mp3 with single user defined frame
        byte[] byteData = new byte[1];
        byteData[0] = 'X';

        ID3v24Tag tag = new ID3v24Tag();
        FrameBodyUnsupported framebodyUnsupported = new  FrameBodyUnsupported("FREW",byteData);
        ID3v24Frame frame = new ID3v24Frame();
        frame.setBody(framebodyUnsupported);
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame converted and marked as unsupported
        ID3v23Tag v23tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        ID3v23Frame v23frame = (ID3v23Frame)v23tag.getFrame("FREW");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.save();

        //Reread from File
        mp3File = new MP3File(testFile);

        //Convert to v23 ,frame should still exist
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame)v23tag.getFrame("FREW");
        assertNotNull(v23frame);
        assertTrue(v23frame.getBody() instanceof FrameBodyUnsupported);

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File.setID3v2Tag(v23tag);
        mp3File.save();

        //Check value maintained, can only see as bytes
        FrameBodyUnsupported v23FrameBody = (FrameBodyUnsupported)v23frame.getBody();
        assertEquals((byte)'X',((byte[])v23FrameBody.getObjectValue(DataTypes.OBJ_DATA))[0]);
    }
    */

     public void testDeleteFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v2Tag = new ID3v23Tag();
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        //Delete using generic key
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST_SORT);
        f.save();

        //Delete using flac id
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        f.getTag().or(NullTag.INSTANCE).deleteField("TSO2");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.save();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
    }

    public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {

         FilePermissionsTest.runWriteWriteProtectedFileWithCheckDisabled("testV1Cbr128ID3v2.mp3");
 	}

    public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {

     	FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("testV1Cbr128ID3v2.mp3");
 	}

    public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {

     	FilePermissionsTest.runWriteReadOnlyFileWithCheckDisabled("testV1Cbr128ID3v2.mp3");
 	}

    public void testWriteMultipleGenresToID3v23TagUsingDefault() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        assertNull(file.getID3v1Tag());
        assertNotNull(file.getID3v2Tag());
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE,"Genre1");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE,"Genre2");
        file.save();
        file = new MP3File(testFile);
        assertEquals("Genre1",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Genre1",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Genre2",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE,"Death Metal");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE,"(23)");
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE,"Death Metal");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE,"23");
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
    }

    public void testWriteMultipleGenresToID3v23TagUsingCreateField() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        assertNull(file.getID3v1Tag());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        file.setNewDefaultTag();
        assertNotNull(file.getTag().or(NullTag.INSTANCE));
        ID3v23Tag v23Tag = (ID3v23Tag)file.getTag().or(NullTag.INSTANCE);
        TagField genreField = v23Tag.createField(FieldKey.GENRE,"Genre1");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(FieldKey.GENRE,"Genre2");
        v23Tag.addField(genreField);
        file.save();
        file = new MP3File(testFile);
        assertEquals("Genre1",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Genre1",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Genre2",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v23Tag.createField(FieldKey.GENRE,"Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(FieldKey.GENRE,"(23)");
        v23Tag.addField(genreField);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v23Tag.createField(FieldKey.GENRE,"Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(FieldKey.GENRE,"23");
        v23Tag.addField(genreField);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
    }

    public void testWriteMultipleGenresToID3v23TagUsingV23CreateField() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        assertNull(file.getID3v1Tag());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        file.setNewDefaultTag();
        assertNotNull(file.getTag().or(NullTag.INSTANCE));
        ID3v23Tag v23Tag = (ID3v23Tag)file.getTag().or(NullTag.INSTANCE);
        TagField genreField = v23Tag.createField(ID3v23FieldKey.GENRE,"Genre1");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE,"Genre2");
        v23Tag.addField(genreField);
        file.save();
        file = new MP3File(testFile);
        assertEquals("Genre1",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Genre1",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Genre2",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE,"Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE,"(23)");
        v23Tag.addField(genreField);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v23Tag = (ID3v23Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE,"Death Metal");
        v23Tag.addField(genreField);
        genreField = v23Tag.createField(ID3v23FieldKey.GENRE,"23");
        v23Tag.addField(genreField);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("Death Metal",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        assertEquals("Pranks",file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
    }
}
