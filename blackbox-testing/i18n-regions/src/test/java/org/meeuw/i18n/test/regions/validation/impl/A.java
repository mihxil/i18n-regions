package org.meeuw.i18n.test.regions.validation.impl;

import org.meeuw.i18n.regions.validation.Language;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class A {
    @Language
    public String language;

    @Language(mayContainCountry = false)
    public String languageNoCountry;

    @Language(mayContainVariant = true)
    public String languageWithVariant;

    @Language
    public Object object;
}
