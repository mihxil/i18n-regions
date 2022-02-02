package org.meeuw.i18n.countries;

import java.util.List;
import java.util.Locale;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.formerlyassigned.ValidityRange;


/**
 * Represents a 'former code', of which the code is defined by the enums of {@link FormerlyAssignedCountryCode}.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountry implements Country {
    private static final long serialVersionUID = 0L;

    private final FormerlyAssignedCountryCode code;

    public FormerCountry(@NonNull FormerlyAssignedCountryCode code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code.getISO3166_3_Code();
    }

    public List<String> getFormerCodes() {
        return code.getFormerCodes();
    }

    @Override
    public Locale toLocale() {
        return code.toLocale();
    }

    @Override
    public String getName() {
        return code.getName();
    }

    @Override
    public FormerlyAssignedCountryCode getCountryCode() {
        return code;
    }

    public ValidityRange getValidity() {
        return code.getValidity();
    }

    @Override
    public String toString() {
        return getCountryCode().toString();
    }

    @Override
    public void toStringBuilder(@NonNull StringBuilder builder, @NonNull Locale locale) {
        Country.super.toStringBuilder(builder, locale);
        builder.append(" (").append(getValidity()).append(")");
    }

    @Override
    public int getNumeric() {
        return code.getFormerCodes().stream().filter(c -> {
            try {
                Integer.parseInt(c);
                return true;
            } catch(NumberFormatException nfe) {
                return false;
            }
        }).map(Integer::parseInt).findFirst().orElse(-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormerCountry that = (FormerCountry) o;

        return code == that.code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
