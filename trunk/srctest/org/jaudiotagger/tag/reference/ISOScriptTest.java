package org.jaudiotagger.tag.reference;

import org.jaudiotagger.AbstractTestCase;

/**
 * Testing of ISOScript
 */
public class  ISOScriptTest extends AbstractTestCase
{
    /**
     * This tests lower case genre names identifications
     */
    public void testScriptMatches()
    {
        //Find by Code
        assertEquals(ISOScript.Script.LATIN, ISOScript.getScriptByCode("Latn"));
        assertNull(ISOScript.getScriptByCode("latn"));

        //Find by Description - case sensitive
        assertEquals(ISOScript.Script.LATIN, ISOScript.getScriptByDescription("Latin"));
        assertNull(ISOScript.getScriptByDescription("latin"));

        //All values can be found
        for (ISOScript.Script script : ISOScript.Script.values())
        {
            assertNotNull(ISOScript.getScriptByCode(script.getCode()));
            assertNotNull(ISOScript.getScriptByDescription(script.getDescription()));
        }
    }
}
