package org.meeuw.i18n;

import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.meeuw.i18n.bind.jaxb.Code;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.CurrentCountry;
import com.neovisionaries.i18n.LanguageCode;

/**
 * The region interface represents a certain geographical region. E.g. a {@link Country}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@XmlJavaTypeAdapter(Code.class)
public interface Region extends Serializable {

    String BUNDLE = "Regions";

    /**
     * The code for the region. For countries: <a href="https://en.wikipedia.org/wiki/ISO_3166>ISO 3166</a>.
     */
    String getCode();

    /**
     * The locale associated with the region.
     */
    Locale toLocale();


    /**
     * What 'type' this region is. E.g. a {@link Type#COUNTRY}, or a {@link Type#SUBDIVISION}.
     */
    Type getType();

    /**
     * The official name (in english) of the region
     */

    String getName();


    /**
     * The name of the region in the given {@link Locale}. The default implementation uses the {@link #BUNDLE} resource bundle. For {@link CurrentCountry} also {@code code.toLocale().getDisplayCountry(locale)} is used.
     */
    default String getName(@Nonnull Locale locale) {
        try {
            return ResourceBundle.getBundle(BUNDLE, locale).getString(getCode());
        } catch (MissingResourceException mse){
            return getName();
        }
    }

    default String getName(@Nonnull LanguageCode languageCode) {
        return getName(languageCode.toLocale());
    }

    /**
     * Return the name of the region in the locale of the language {@link #toLocale()}
     */
    default String getLocalName() {
        return getName(toLocale());
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
