package org.meeuw.i18n;

import com.neovisionaries.i18n.LanguageCode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.bind.jaxb.Code;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The region interface represents a certain geographical region. E.g. a {@link Country}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@XmlJavaTypeAdapter(Code.class)
public interface Region extends Serializable {

    /**
     * The code for the region. For countries: <a href="https://en.wikipedia.org/wiki/ISO_3166>ISO 3166</a>.
     */
    String getCode();

    /**
     * The locale associated with the region.
     */
    default Locale toLocale() {
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

    default String getBundle() {
        return getClass().getSimpleName();
    }

    /**
     * The name of the region in the given {@link Locale}. The default implementation uses the {@link #getBundle()} resource bundle.
     */
    default String getName(@NonNull Locale locale) {
        try {
            return ResourceBundle.getBundle(getBundle(), locale).getString(getCode());
        } catch (MissingResourceException mse){
            return getName();
        }
    }

    default String getName(@NonNull LanguageCode languageCode) {
        return getName(languageCode.toLocale());
    }

    /**
     * Return the name of the region in the locale of the language {@link #toLocale()}
     */
    default String getLocalName() {
        return getName(toLocale());
    }

    /**
     *
     *
     */
    default void toStringBuilder(@NonNull StringBuilder builder, @NonNull Locale language) {
        builder.append(getName(language));

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
        SUBDIVISION

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



}