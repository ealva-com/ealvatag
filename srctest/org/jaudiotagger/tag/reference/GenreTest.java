package org.jaudiotagger.tag.reference;

import org.jaudiotagger.AbstractTestCase;

/**
 * Testing of Genres
 */
public class GenreTest extends AbstractTestCase
{
    /**
     * This tests lower case genre names identifications
     */
    public void testLowercaseGenrematch()
    {
        //Case sensitive
        assertNull(GenreTypes.getInstanceOf().getIdForValue("rock"));
        assertEquals(17, (int) GenreTypes.getInstanceOf().getIdForValue("Rock"));

        //Case insensitive
        assertEquals(17, (int) GenreTypes.getInstanceOf().getIdForName("Rock"));
        assertEquals(17, (int) GenreTypes.getInstanceOf().getIdForName("rock"));

        //Doesnt exist
        assertNull(GenreTypes.getInstanceOf().getIdForValue("rocky"));
        assertNull(GenreTypes.getInstanceOf().getIdForName("rocky"));

        //All values can be found
        for (String value : GenreTypes.getInstanceOf().getAlphabeticalValueList())
        {
            assertNotNull(GenreTypes.getInstanceOf().getIdForName(value));
            assertNotNull(GenreTypes.getInstanceOf().getIdForName(value.toLowerCase()));
        }
    }
}
