package org.meeuw.i18n.test.countries;

import tools.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.Country;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SerializationTests {


    public static class A {

        @JsonProperty
        Country country;

    }

    @Test
    public void json()  {
        A a = new A();
        a.country = Country.getByCode("NL").orElseThrow();
        JsonMapper mapper = new JsonMapper();
        System.out.println(mapper.writeValueAsString(a));

        A rounded = mapper.readValue("{\"country\":\"NL\"}", A.class);
        assert rounded.country.getCode().equals("NL");
    }
}


