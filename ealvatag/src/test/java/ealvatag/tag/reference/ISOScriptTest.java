package ealvatag.tag.reference;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing of ISOScript
 */
public class ISOScriptTest {
    /**
     * This tests lower case genre names identifications
     */
    @Test public void testScriptMatches() {
        //Find by Code
        Assert.assertEquals(ISOScript.Script.LATIN, ISOScript.getScriptByCode("Latn"));
        Assert.assertNull(ISOScript.getScriptByCode("latn"));

        //Find by Description - case sensitive
        Assert.assertEquals(ISOScript.Script.LATIN, ISOScript.getScriptByDescription("Latin"));
        Assert.assertNull(ISOScript.getScriptByDescription("latin"));

        //All values can be found
        for (ISOScript.Script script : ISOScript.Script.values()) {
            Assert.assertNotNull(ISOScript.getScriptByCode(script.getCode()));
            Assert.assertNotNull(ISOScript.getScriptByDescription(script.getDescription()));
        }
    }
}
