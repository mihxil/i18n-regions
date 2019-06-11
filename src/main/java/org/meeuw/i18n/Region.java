package org.meeuw.i18n;

import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.meeuw.i18n.bind.jaxb.Code;

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
    Locale toLocale();


    Type getType();

    /**
     * The official name (in english) of the region
     */

    String getName();

    default String getName(Locale locale) {
        try {
            return ResourceBundle.getBundle("Regions", locale).getString(getCode());
        } catch (MissingResourceException mse){
            return getName();
        }
    }


    default String getLocalName() {
        return getName(toLocale());
    }
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
