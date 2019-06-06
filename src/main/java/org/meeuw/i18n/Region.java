package org.meeuw.i18n;

import java.util.Locale;

/**
 * The region interface represents a certain geographical region. E.g. a {@link Country}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface Region {

	/**
	 * The <a href="https://en.wikipedia.org/wiki/ISO_3166>ISO 3166</a> code for the region
	 */
	String getISOCode();

	/**
	 * The locale associated with the region.
	 */
	Locale toLocale();


	Type getType();

	/**
	 * The official name (in english) of the region
	 */

	String getName();

	enum Type {
		/**
		 * A country or former country
		 */
		COUNTRY,

		/**
		 * A subdivision of a country, of which the type is otherwise unknown
		 */
		SUBDIVISION,

		PROVINCE,
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
		NATION
	}



}
