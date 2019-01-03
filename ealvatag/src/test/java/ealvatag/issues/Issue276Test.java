/*
 * Copyright (c) 2017 Eric A. Snell
 *
 * This file is part of eAlvaTag.
 *
 * eAlvaTag is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * eAlvaTag is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaTag.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

//package ealvatag.issues;
//
//import ealvatag.audio.AudioFile;
//import ealvatag.audio.AudioFileIO;
//import ealvatag.tag.FieldKey;
//import ealvatag.tag.Tag;
//import ealvatag.tag.TagOptionSingleton;
//import ealvatag.tag.id3.AbstractID3v2Tag;
//import ealvatag.tag.id3.ID3v23Tag;
//import ealvatag.tag.reference.ID3V2Version;
//
//import static junit.framework.TestCase.assertNotNull;
//import static junit.framework.TestCase.assertNull;
//import static junit.framework.TestCase.assertTrue;
//
//import java.io.File;
//
///**
// * Test Problem saving to Mp3 caused by duplicate CHAP frames
// */
//public class Issue276Test extends AbstractTestCase
//{
//  public void testCHAPV24ToV23() throws Exception
//  {
//    File orig = new File("testdata", "test536.mp3");
//    if (!orig.isFile())
//    {
//      System.err.println("Unable to test file - not available");
//      return;
//    }
//
//    Exception ex=null;
//    try
//    {
//      TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
//      File testFile = AbstractTestCase.copyAudioToTmp("test536.mp3");
//      AudioFile af = AudioFileIO.read(testFile);
//      assertNotNull(af.getTag());
//      System.out.println(af.getTag());
//      Tag tag=af.getTagAndConvertOrCreateAndSetDefault();
//      tag.setField(FieldKey.YEAR, "year");
//      af.commit();
//      assertTrue((AbstractID3v2Tag)af.getTag() instanceof ID3v23Tag);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      ex=e;
//    }
//    assertNull(ex);
//  }
//}
