package org.meeuw.i18n.openlocationcode;

import java.net.URI;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.meeuw.i18n.Region;

import com.google.openlocationcode.OpenLocationCode;

/**
 * A wrapper around {@link OpenLocationCode}. Every such code represents a rectangular region on earth.
 * See https://plus.codes/
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OpenLocation implements Region, Comparable<OpenLocation> {


    @NotNull
    private final OpenLocationCode code;


    public OpenLocation(@NotNull OpenLocationCode code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return getCode();
    }


    @Override
    public String getCode() {
        return code.getCode();
    }

    public URI getPlusURL() {
        return URI.create("https://plus.codes/" + getCode());
    }

    public OpenLocationCode getOpenLocationCode() {
        return code;
    }

    @Override
    public Locale toLocale() {
        return null;

    }

    @Override
    public Type getType() {
        return Type.UNDEFINED;

    }

    @Override
    public String getName() {
        return code.toString();

    }

    @Override
    public String getBundle() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpenLocation location = (OpenLocation) o;

        return code.equals(location.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public int compareTo(OpenLocation o) {
        return getCode().compareTo(o.getCode());

    }
}

