package org.meeuw.i18n.regions;

import java.io.Serializable;
import java.net.URI;
import java.util.*;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.i18n.regions.bind.jaxb.Code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.neovisionaries.i18n.LanguageCode;

/**
 * The region interface represents a certain geographical region. E.g. a Country.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@XmlJavaTypeAdapter(Code.class)
public interface Region extends Serializable {

    /**
     * The code for the region. For countries: <a href="https://en.wikipedia.org/wiki/ISO_3166">ISO 3166</a>.
     */
    String getCode();

    /**
     * The locale associated with the region.
     */
    default @Nullable Locale toLocale() {
        return null;
    }


    /**
     * What 'type' this region is. E.g. a {@link Type#COUNTRY}, or a {@link Type#SUBDIVISION}.
     */
    Type getType();

    /**
     * The official name (in english) of the region
     */
    String getName();

    /**
     * The resource bundle associated with this region, to look up its name in other languages.
     * This is used in the default implementaton of {@link #getName(Locale)}
     * @see #getName(Locale)
     */
    default String getBundle() {
        return getClass().getSimpleName();
    }

    /**
     * The name of the region in the given {@link Locale}. The default implementation uses the {@link #getBundle()} resource bundle.
     */
    default String getName(@NonNull Locale locale) {
        String bundle = getBundle();
        if (bundle == null) {
            return getName();
        }
        try {
            return ResourceBundle.getBundle(bundle, locale).getString(getCode());
        } catch (MissingResourceException mse){
            return getName();
        }
    }

    /**
     * Defaulting version of {@link #getName(Locale)}, the name of the region only considering the
     * language, with considering any country or other variants of the language.
     */
    default String getName(@NonNull LanguageCode languageCode) {
        return getName(languageCode.toLocale());
    }

    /**
     * Return the name of the region in the locale of the region itself {@link #toLocale()}
     *
     * E.g. Germany will  be 'Deutschland', China will be '中国'.
     */
    default @Nullable String getLocalName() {
        Locale locale = toLocale();
        return locale == null ? null : getName(locale);
    }

    /**
     * Writes a string representation of a region to a string builder. This is meant to be a string for end users, which would make it unambiguously clear to them what is meant. E.g. in case of a subdivision of country, it could include the name of the country itself.
     */
    default void toStringBuilder(
        @NonNull StringBuilder builder,
        @NonNull Locale language) {
        builder.append(getName(language));
    }

    /**
     * To a region optionally an 'icon' may be associated. This is an URI, representing an URl to a picture of a flag or so. The URI may not be absolute, in which case it may e.g. refer to a webjars, and may have to be prefixed by the web application's context 'context' to be useable.
     */

    default Optional<URI> getIcon() {
        return Optional.empty();
    }

    /**
     * Type of regions. For now this is small list, we may add all known 'subdivision' of countries.
     */
    enum Type {
        /**
         * A country or former country
         */
        COUNTRY,

        /**
         * Continents
         */
        CONTINENT,

        /**
         * A subdivision of a country, of which the type is otherwise unknown
         */
        SUBDIVISION,

        UNDEFINED

      /*  PROVINCE,
        STATE,
        PARISH,
        EMIRATE,
        DEPENDENCY,
        CITY,
        REGION,
        TERRITORY,
        MUNICIPALITY,
        SPECIAL_MUNICIPALITY,
        RAYON,
        ENTITY,
        DIVISION,
        DISTRICT,
        GOVERNORATE,
        DEPARTMENT,
        FEDERAL_DISTRICT,
        TOWN,
        OBLAST,
        COMMUNUNE,
        PREFECTURE,
        CANTON,
        LAND,
        ISLAND,
        NATION*/
    }



    @JsonCreator
    static Region of(String code) {
        if (code == null || code.isEmpty()) {
            // be lenient about this too.
            // we'd perhaps like to access ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, but in that case we propbable need custom deserializer?
            return null;
        }
        return RegionService.getInstance().getByCode(code, true).orElseThrow(IllegalArgumentException::new);
    }


}
