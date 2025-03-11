package org.meeuw.i18n.test.countries;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.Country;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationTests {


    public static class A {

        @JsonProperty
        Country country;

    }

    @Test
    public void json() throws JsonProcessingException {
        A a = new A();
        a.country = Country.getByCode("NL").orElseThrow();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(a));

        A rounded = mapper.readValue("{\"country\":\"NL\"}", A.class);
        assert rounded.country.getCode().equals("NL");
    }
}


