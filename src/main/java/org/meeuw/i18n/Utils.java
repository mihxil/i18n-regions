package org.meeuw.i18n;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 3.8
 */
public class Utils {

    public static CountryCode _getByCode(String s) {
        CountryCode code = CountryCode.getByCode(s);
        if (code == null) {
            return VehicleRegistrationCode.valueOf(s).getCode();
        } else {
            return code;
        }

    }

    public static <T extends Region> T getByCode(String s) {
        ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);
        Iterator<RegionProvider> iterator = loader.iterator();
        while(iterator.hasNext()) {
            RegionProvider<T> provider = iterator.next();
            Optional<T> byCode = provider.getByCode(s);
            if (byCode.isPresent()) {
                return byCode.get();
            }

        }
        return null;
    }
}
