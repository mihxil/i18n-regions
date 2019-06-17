package org.meeuw.i18n.countries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class VehicleRegistrationCodeTest {


    @Test
    public void values() {
        List<String > differences = new ArrayList<>();
        for (VehicleRegistrationCode code : VehicleRegistrationCode.values()) {

            if (! code.getName().equals(code.getCode() == null ? null : code.getCode().getName())) {
                differences.add(code.getName() + " " + (code.getCode() == null ? null : code.getCode().getName()));
            }
        }
        System.out.println(differences.stream().collect(Collectors.joining("\n")));
    }

}
