package org.meeuw.i18n.languages;

public enum Scope {

    /**
     * Individual languages as defined by ISO 639-3
     */
    I("Individual"),
    /**
     * MacroLanguage
     */
    M("Macrolanguage"),
    /**
     * Special
     */
    S("Special language");

    private final String string;

    Scope(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

}
