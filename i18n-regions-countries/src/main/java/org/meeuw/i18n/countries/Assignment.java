package org.meeuw.i18n.countries;

/**
 * Code assignment state in <a href="http://en.wikipedia.org/wiki/ISO_3166-1"
 * >ISO 3166-1</a>.
 *
 * @see <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Decoding_table"
 * >Decoding table of ISO 3166-1 alpha-2 codes</a>
 * @since 3.2
 */
public enum Assignment {
    /**
     * <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Officially_assigned_code_elements"
     * >Officially assigned</a>.
     * <p>
     * Assigned to a country, territory, or area of geographical interest.
     */
    OFFICIALLY_ASSIGNED,

    /**
     * <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#User-assigned_code_elements"
     * >User assigned</a>.
     * <p>
     * Free for assignment at the disposal of users.
     */
    USER_ASSIGNED,

    /**
     * <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Exceptional_reservations"
     * >Exceptionally reserved</a>.
     * <p>
     * Reserved on request for restricted use.
     */
    EXCEPTIONALLY_RESERVED,

    /**
     * <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Transitional_reservations"
     * >Transitionally reserved</a>.
     * <p>
     * Deleted from ISO 3166-1 but reserved transitionally.
     */
    TRANSITIONALLY_RESERVED,

    /**
     * <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Indeterminate_reservations"
     * >Indeterminately reserved</a>.
     * <p>
     * Used in coding systems associated with ISO 3166-1.
     */
    INDETERMINATELY_RESERVED,

    /**
     * <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Codes_currently_agreed_not_to_use"
     * >Not used</a>.
     * <p>
     * Not used in ISO 3166-1 in deference to international property
     * organization names.
     */
    NOT_USED
}
