package org.meeuw.i18n;

import java.util.Locale;

/**
 * The region interface represents a certain geographical region. E.g. a {@link Country}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface Region {

	String getISOCode();

	Locale toLocale();

	Type getType();

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
