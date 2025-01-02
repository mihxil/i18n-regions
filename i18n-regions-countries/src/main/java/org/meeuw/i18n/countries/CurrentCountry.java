package org.meeuw.i18n.countries;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.Region;

import com.neovisionaries.i18n.CountryCode;

/**
 * Represents a country of which the code is one of the enum values of {@link CountryCode}.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CurrentCountry implements Country {
    private static final long serialVersionUID = 0L;

    private final CountryCode code;

    public static final ThreadLocal<Boolean> ALWAYS_USE_CDN_FOR_ICONS = ThreadLocal.withInitial(() -> false);

    static final String WEBJARS;
    static final String CDNWEBJARS;


    public static final boolean HAS_WEBJARS_JAR;
    static {
        Optional<String> localWebJars = getLocalWebJars();
        CDNWEBJARS = getCdnWebJars();
        WEBJARS = getLocalWebJars().orElse(CDNWEBJARS);
        HAS_WEBJARS_JAR =  getLocalWebJars().isPresent();
    }

    public static CurrentCountry of(CountryCode code) {
        if (code == null) {
            return null;
        }
        return new CurrentCountry(code);
    }

    public CurrentCountry(@NonNull CountryCode code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return getAlpha2();
    }

    public String getAlpha2() {
        return code.getAlpha2();
    }

    public String getAlpha3() {
        return code.getAlpha3();
    }

    @Override
    public int getNumeric() {
        return code.getNumeric();
    }

    @Override
    public Locale toLocale() {
        return code.toLocale();
    }


    /**
     * For {@link CurrentCountry} also {@code code.toLocale().getDisplayCountry(locale)} is used.
     */
    @Override
    public String getName(@NonNull Locale locale) {
        try {
            return ResourceBundle.getBundle(getBundle(), locale).getString(this.getCode());
        } catch (MissingResourceException mse){
            if (code.getAssignment() == CountryCode.Assignment.OFFICIALLY_ASSIGNED) {
                return code.toLocale().getDisplayCountry(locale);
            } else {
                return code.getName();
            }
        }
    }

    @Override
    public String getName() {
        return code.getName();
    }

    @Override
    public CountryCode getCountryCode() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }


    private static final int A = Character.codePointOf("Regional Indicator Symbol Letter A");

    /**
     * Countries can also be represented as unicode 'emojis'. In non-windows this will be shown as a little flag too.
     * @since 2.2
     */
    @Override
    public Optional<String> getEmoji() {
        StringBuilder builder = new StringBuilder();
        for (char c : getAlpha2().toCharArray()) {
            int target = A - 'A' + c;
            char[] chars = Character.toChars(target);
            builder.append(chars);
        }
        return Optional.of(builder.toString());
    }

    @Override
    public void toStringBuilder(@NonNull StringBuilder builder, @NonNull Locale locale) {
        Country.super.toStringBuilder(builder, locale);
        if (code.getAssignment() != CountryCode.Assignment.OFFICIALLY_ASSIGNED) {
            builder.append(" (").append(code.getAssignment()).append(")");
        }
    }

    public CountryCode.Assignment getAssignment() {
        return code.getAssignment();
    }

    /**
     * {@inheritDoc}
     * <p>
     * For countries, we provide urls to the SVG's at <a href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/">cdnjs.cloudflare.com/ajax/libs/flag-icon-css/</a>, unless the corresponding `webjars` is on the class-path, in
     * which case a non-absolute URI to those resources is returned.
     * </p>
     * <p>
     * This only works for officially assigned countries, other ones return the empty optional.
     * </p>
     */
    @Override
    public Optional<URI> getIcon() {
        if (getAssignment() == CountryCode.Assignment.OFFICIALLY_ASSIGNED) {
            if (ALWAYS_USE_CDN_FOR_ICONS.get()) {
                return Optional.of(URI.create(CDNWEBJARS + getCode().toLowerCase() + ".svg"));
            } else {
                return Optional.of(URI.create(WEBJARS + getCode().toLowerCase() + ".svg"));
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrentCountry that = (CurrentCountry) o;

        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code.name());
    }


    /**
     * Returns the base link to the locally installed webjars flag icons, if installed.
     * Otherwise, returns the empty optional.
     */
    static Optional<String> getLocalWebJars() {
        URL url  = Country.class.getClassLoader().getResource("META-INF/maven/org.webjars.npm/flag-icons/pom.properties");
        if (url != null) {
            Properties prop = new Properties();
            try (InputStream input = url.openStream()) {
                prop.load(input);
                return Optional.of("/webjars/flag-icons/" + prop.getProperty("version") + "/flags/4x3/");
            } catch (IOException e) {
                Logger.getLogger(Country.class.getName()).warning(url + ":" + e.getMessage());
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the base link to the CDN webjars flag icons. The version of was determined at
     * build time (and picked up from maven.properties.
     */
    static String getCdnWebJars()  {
        URL url  = Region.class.getClassLoader()
            .getResource("META-INF/maven/org.meeuw.i18n/i18n-regions-countries/maven.properties");
        Properties prop = new Properties();
        if (url != null) {

            try (InputStream input = url.openStream()) {
                prop.load(input);
            } catch (NullPointerException | IOException e) {
                Logger.getLogger(Country.class.getName()).warning(url + ":" + e.getClass() + ":" + e.getMessage());
            }
        }
        String version = prop.getProperty("flag-icons.version");
        if (version == null) {
            throw new IllegalStateException("No version found in  " + prop);
        }
        return "https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/" + version + "/flags/4x3/";
    }
}
