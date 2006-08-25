/**
 * @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * <p/>
 * Jaudiotagger Copyright (C)2004,2005
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License ainteger with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * <p/>
 * Description:
 */
package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;

public class GenreTypes extends AbstractIntStringValuePair
{
    private static GenreTypes genreTypes;

    public static GenreTypes getInstanceOf()
    {
        if (genreTypes == null)
        {
            genreTypes = new GenreTypes();
        }
        return genreTypes;
    }


    private GenreTypes()
    {
        idToValue.put(new Integer(0), "Blues");
        idToValue.put(new Integer(1), "Classic Rock");
        idToValue.put(new Integer(2), "Country");
        idToValue.put(new Integer(3), "Dance");
        idToValue.put(new Integer(4), "Disco");
        idToValue.put(new Integer(5), "Funk");
        idToValue.put(new Integer(6), "Grunge");
        idToValue.put(new Integer(7), "Hip-Hop");
        idToValue.put(new Integer(8), "Jazz");
        idToValue.put(new Integer(9), "Metal");
        idToValue.put(new Integer(10), "New Age");
        idToValue.put(new Integer(11), "Oldies");
        idToValue.put(new Integer(12), "Other");
        idToValue.put(new Integer(13), "Pop");
        idToValue.put(new Integer(14), "R&B");
        idToValue.put(new Integer(15), "Rap");
        idToValue.put(new Integer(16), "Reggae");
        idToValue.put(new Integer(17), "Rock");
        idToValue.put(new Integer(18), "Techno");
        idToValue.put(new Integer(19), "Industrial");
        idToValue.put(new Integer(20), "Alternative");
        idToValue.put(new Integer(21), "Ska");
        idToValue.put(new Integer(22), "Death Metal");
        idToValue.put(new Integer(23), "Pranks");
        idToValue.put(new Integer(24), "Soundtrack");
        idToValue.put(new Integer(25), "Euro-Techno");
        idToValue.put(new Integer(26), "Ambient");
        idToValue.put(new Integer(27), "Trip-Hop");
        idToValue.put(new Integer(28), "Vocal");
        idToValue.put(new Integer(29), "Jazz+Funk");
        idToValue.put(new Integer(30), "Fusion");
        idToValue.put(new Integer(31), "Trance");
        idToValue.put(new Integer(32), "Classical");
        idToValue.put(new Integer(33), "Instrumental");
        idToValue.put(new Integer(34), "Acid");
        idToValue.put(new Integer(35), "House");
        idToValue.put(new Integer(36), "Game");
        idToValue.put(new Integer(37), "Sound Clip");
        idToValue.put(new Integer(38), "Gospel");
        idToValue.put(new Integer(39), "Noise");
        idToValue.put(new Integer(40), "AlternRock");
        idToValue.put(new Integer(41), "Bass");
        idToValue.put(new Integer(42), "Soul");
        idToValue.put(new Integer(43), "Punk");
        idToValue.put(new Integer(44), "Space");
        idToValue.put(new Integer(45), "Meditative");
        idToValue.put(new Integer(46), "Instrumental Pop");
        idToValue.put(new Integer(47), "Instrumental Rock");
        idToValue.put(new Integer(48), "Ethnic");
        idToValue.put(new Integer(49), "Gothic");
        idToValue.put(new Integer(50), "Darkwave");
        idToValue.put(new Integer(51), "Techno-Industrial");
        idToValue.put(new Integer(52), "Electronic");
        idToValue.put(new Integer(53), "Pop-Folk");
        idToValue.put(new Integer(54), "Eurodance");
        idToValue.put(new Integer(55), "Dream");
        idToValue.put(new Integer(56), "Southern Rock");
        idToValue.put(new Integer(57), "Comedy");
        idToValue.put(new Integer(58), "Cult");
        idToValue.put(new Integer(59), "Gangsta");
        idToValue.put(new Integer(60), "Top 40");
        idToValue.put(new Integer(61), "Christian Rap");
        idToValue.put(new Integer(62), "Pop/Funk");
        idToValue.put(new Integer(63), "Jungle");
        idToValue.put(new Integer(64), "Native American");
        idToValue.put(new Integer(65), "Cabaret");
        idToValue.put(new Integer(66), "New Wave");
        idToValue.put(new Integer(67), "Psychadelic");
        idToValue.put(new Integer(68), "Rave");
        idToValue.put(new Integer(69), "Showtunes");
        idToValue.put(new Integer(70), "Trailer");
        idToValue.put(new Integer(71), "Lo-Fi");
        idToValue.put(new Integer(72), "Tribal");
        idToValue.put(new Integer(73), "Acid Punk");
        idToValue.put(new Integer(74), "Acid Jazz");
        idToValue.put(new Integer(75), "Polka");
        idToValue.put(new Integer(76), "Retro");
        idToValue.put(new Integer(77), "Musical");
        idToValue.put(new Integer(78), "Rock & Roll");
        idToValue.put(new Integer(79), "Hard Rock");
        idToValue.put(new Integer(80), "Folk");
        idToValue.put(new Integer(81), "Folk-Rock");
        idToValue.put(new Integer(82), "National Folk");
        idToValue.put(new Integer(83), "Swing");
        idToValue.put(new Integer(84), "Fast Fusion");
        idToValue.put(new Integer(85), "Bebob");
        idToValue.put(new Integer(86), "Latin");
        idToValue.put(new Integer(87), "Revival");
        idToValue.put(new Integer(88), "Celtic");
        idToValue.put(new Integer(89), "Bluegrass");
        idToValue.put(new Integer(90), "Avantgarde");
        idToValue.put(new Integer(91), "Gothic Rock");
        idToValue.put(new Integer(92), "Progressive Rock");
        idToValue.put(new Integer(93), "Psychedelic Rock");
        idToValue.put(new Integer(94), "Symphonic Rock");
        idToValue.put(new Integer(95), "Slow Rock");
        idToValue.put(new Integer(96), "Big Band");
        idToValue.put(new Integer(97), "Chorus");
        idToValue.put(new Integer(98), "Easy Listening");
        idToValue.put(new Integer(99), "Acoustic");
        idToValue.put(new Integer(100), "Humour");
        idToValue.put(new Integer(101), "Speech");
        idToValue.put(new Integer(102), "Chanson");
        idToValue.put(new Integer(103), "Opera");
        idToValue.put(new Integer(104), "Chamber Music");
        idToValue.put(new Integer(105), "Sonata");
        idToValue.put(new Integer(106), "Symphony");
        idToValue.put(new Integer(107), "Booty Bass");
        idToValue.put(new Integer(108), "Primus");
        idToValue.put(new Integer(109), "Porn Groove");
        idToValue.put(new Integer(110), "Satire");
        idToValue.put(new Integer(111), "Slow Jam");
        idToValue.put(new Integer(112), "Club");
        idToValue.put(new Integer(113), "Tango");
        idToValue.put(new Integer(114), "Samba");
        idToValue.put(new Integer(115), "Folklore");
        idToValue.put(new Integer(116), "Ballad");
        idToValue.put(new Integer(117), "Power Ballad");
        idToValue.put(new Integer(118), "Rhythmic Soul");
        idToValue.put(new Integer(119), "Freestyle");
        idToValue.put(new Integer(120), "Duet");
        idToValue.put(new Integer(121), "Punk Rock");
        idToValue.put(new Integer(122), "Drum Solo");
        idToValue.put(new Integer(123), "Acapella");
        idToValue.put(new Integer(124), "Euro-House");
        idToValue.put(new Integer(125), "Dance Hall");
        idToValue.put(new Integer(126), "Goa");
        idToValue.put(new Integer(127), "Drum&Bass");
        idToValue.put(new Integer(128), "Club-House");
        idToValue.put(new Integer(129), "Hardcore");
        idToValue.put(new Integer(130), "Terror");
        idToValue.put(new Integer(131), "Indie");
        idToValue.put(new Integer(132), "BritPop");
        idToValue.put(new Integer(133), "Negerpunk");
        idToValue.put(new Integer(134), "PolskPunk");
        idToValue.put(new Integer(135), "Beat");
        idToValue.put(new Integer(136), "ChristianGangstaRap");
        idToValue.put(new Integer(137), "HeavyMetal");
        idToValue.put(new Integer(138), "BlackMetal");
        idToValue.put(new Integer(139), "Crossover");
        idToValue.put(new Integer(140), "ContemporaryChristian");
        idToValue.put(new Integer(141), "ChristianRock");
        idToValue.put(new Integer(142), "Merengue");
        idToValue.put(new Integer(143), "Salsa");
        idToValue.put(new Integer(144), "ThrashMetal");
        idToValue.put(new Integer(145), "Anime");
        idToValue.put(new Integer(146), "JPop");
        idToValue.put(new Integer(147), "SynthPop");

        createMaps();
    }


}
