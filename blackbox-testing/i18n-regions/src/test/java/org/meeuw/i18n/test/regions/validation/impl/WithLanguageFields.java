package org.meeuw.i18n.test.regions.validation.impl;

import org.meeuw.i18n.languages.validation.Language;
import org.meeuw.i18n.regions.validation.ValidRegion;

/**
 * @author Michiel Meeuwissen
 */
public class WithLanguageFields {
    @Language
    public String language;

    @Language(
        forXml = false
    )
    @ValidRegion(types = {})
    public String lenientCountry;

    @Language(
        forXml = false,
        mayContainVariant = true
    )
    public String notForXml;

    @Language(mayContainCountry = false)
    public String languageNoCountry;

    @Language(mayContainVariant = true)
    public String languageWithVariant;

    @Language
    public Object object;
}
