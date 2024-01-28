package org.meeuw.i18n.countries;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

import java.util.logging.Logger;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.bind.jaxb.Code;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import com.neovisionaries.i18n.CountryCode;

/**
 * Represent a 'country', this can be a current (see {@link CurrentCountry} or former country (see {@link FormerCountry )}. It could also be some user defined country (see {@link UserAssignedCountry})
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@XmlJavaTypeAdapter(Code.class)
public interface Country extends Region {

    /**
     * A usefull predicate, e.g. to filter streams of {@link RegionService#values()}
     *
     * Checks wether the region if a {@link CurrentCountry} which is {@link CountryCode.Assignment#OFFICIALLY_ASSIGNED}.
     */
    Predicate<Region> IS_OFFICIAL = (c) -> c instanceof CurrentCountry && ((CurrentCountry) c).getAssignment() == CountryCode.Assignment.OFFICIALLY_ASSIGNED;
    /**
     * A usefull predicate, e.g. to filter streams of {@link RegionService#values()}
     *
     * Checks wether the region if a {@link FormerCountry}
     */
    Predicate<Region> IS_FORMER = c -> c instanceof FormerCountry;

    /**
     * A useful predicate, e.g. to filter streams of {@link RegionService#values()}
     *
     * Checks wether the region if a {@link UserAssignedCountry}
     */
    Predicate<Region> IS_USER_ASSIGNED = c -> c instanceof UserAssignedCountry;

    static CurrentCountry of(CountryCode code) {
        return new CurrentCountry(code);
    }
    static FormerCountry of(FormerlyAssignedCountryCode code) {
        return new FormerCountry(code);
    }


    static Optional<Country> getByCode(@NonNull String code) {
        return RegionService.getInstance().getByCode(code, true, Country.class);
    }

    @Override
    default String getBundle() {
        return "Country";
    }

    @Override
    default Type getType() {
        return Type.COUNTRY;
    }

    int getNumeric();


    /**
     * Returns the underlying code of this Country object. This may be some {@link Enum} (like {@link CountryCode}), but in cases where the set is a bit more dynamic it can also be simply a {@link String}
     * @since 0.5
     */
    Serializable getCountryCode();




}
