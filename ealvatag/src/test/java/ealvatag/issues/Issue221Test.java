package ealvatag.issues;

import ealvatag.tag.FieldKey;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.mp4.Mp4Tag;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import org.junit.Test;

import java.io.FileOutputStream;

/**
 * Test Creating Null fields
 */
public class Issue221Test {

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullMp4FrameTitle() throws Exception {
    Mp4Tag tag = Mp4Tag.makeEmpty();
    tag.setField(FieldKey.TITLE, (String)null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullOggVorbisFrameTitle() throws Exception {
    VorbisCommentTag tag = VorbisCommentTag.createNewTag();
    tag.setField(FieldKey.TITLE, (String)null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v23FrameTitle() throws Exception {
    ID3v23Tag tag = new ID3v23Tag();
    tag.setField(FieldKey.TITLE, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v23FrameAlbum() throws Exception {
    ID3v23Tag tag = new ID3v23Tag();
    tag.setField(FieldKey.ALBUM, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v23FrameArtist() throws Exception {
    ID3v23Tag tag = new ID3v23Tag();
    tag.setField(FieldKey.ARTIST, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v23FrameComment() throws Exception {
    ID3v23Tag tag = new ID3v23Tag();
    tag.setField(FieldKey.COMMENT, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v23FrameGenre() throws Exception {
    ID3v23Tag tag = new ID3v23Tag();
    tag.setField(FieldKey.GENRE, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v23FrameTrack() throws Exception {
    ID3v23Tag tag = new ID3v23Tag();
    tag.setField(FieldKey.TRACK, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v24Frame() throws Exception {
    ID3v24Tag tag = new ID3v24Tag();
    tag.setField(FieldKey.TITLE, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v24.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullID3v22Frame() throws Exception {
    ID3v22Tag tag = new ID3v22Tag();
    tag.setField(FieldKey.TITLE, (String)null);
    FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v24.mp3");
    tag.write(os.getChannel(), 0);
    os.close();
  }
}
