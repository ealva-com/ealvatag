package ealvatag.tag.reference;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing of ISCountries
 */
public class ISOCountryTest {
    /**
     * This tests lower case genre names identifications
     */
    @Test public void testCountryMatches() {
        //Find by Code
        Assert.assertEquals(ISOCountry.Country.UNITED_KINGDOM, ISOCountry.getCountryByCode("GB"));

        //Find by Description - case senstive
        Assert.assertEquals(ISOCountry.Country.UNITED_KINGDOM, ISOCountry.getCountryByDescription("United Kingdom"));
        Assert.assertNull(ISOCountry.getCountryByDescription("united kingdom"));

        //Doesnt exist
        Assert.assertNull(ISOCountry.getCountryByCode("GBE"));
        Assert.assertNull(ISOCountry.getCountryByDescription("england"));

        //All values can be found
        for (ISOCountry.Country country : ISOCountry.Country.values()) {
            Assert.assertNotNull(ISOCountry.getCountryByCode(country.getCode()));
            Assert.assertNotNull(ISOCountry.getCountryByDescription(country.getDescription()));
        }
    }
}
