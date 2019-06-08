[![Build Status](https://travis-ci.org/mihxil/i18n-regions.svg?)](https://travis-ci.org/mihxil/i18n-regions)
[![Maven Central](https://img.shields.io/maven-central/v/org.meeuw.i18n/i18n-regions.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.meeuw.i18n%22)
[![snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.meeuw.i18n/i18n-regions.svg)](https://oss.sonatype.org/content/repositories/staging/org/meeuw/i18n/)
[![javadoc](http://www.javadoc.io/badge/org.meeuw.i18n/i18n-regions.svg?color=blue)](http://www.javadoc.io/doc/org.meeuw.i18n/i18n-regions)

geographical regions
=============

introduction
---
This project was started to be able to make better use of `CountryCode`'s from [nv-i18n](https://github.com/TakahikoKawasaki/nv-i18n)

Using `CountryCode` as a value in your application has several drawbacks:

1. It is not extensible. If you need a value not in that enum, you're stuck.
2. It does not contain 'former countries', so e.g. the birth country of a person, or the country of a Movie cannot be stored as a 'CountryCode'.
3. It's only applicable to countries, no other regions.

I decided to wrap `CountryCode` in a class `CurrentCountry`, which implements a `Region` interface, which makes it possible to make other implementation of `Region` too, and to address all the above issues if you choose to use `Region` in stead of `CountryCode` as the type of your variable.


implementation
---
The central interface of this module is `org.meeuw.i18n.Region`, which represents some geographical region.


Instances are created by services implementing `org.meeuw.i18n.RegionProvider` (registered via META-INF/services).

By default we provide services backed by `com.neovisionaries.i18n.CountryCode` (for current countries), by `org.meeuw.i18n.FormerlyAssignedCountryCode` (for former countries) and by `be.olsson.i18n.subdivision.CountryCodeSubdivision` (for subdivisions of countries)

Some utilities to deal with all this are provided in `org.meeuw.i18n.Utils`.

E.g. 
```java


    @Test
    public void getCurrentByCode() {

        Optional<Region> nl = Regions.getByCode("NL");
        assertThat(nl).isPresent();
        assertThat(nl.get()).isInstanceOf(CurrentCountry.class);
        assertThat(nl.get().getISOCode()).isEqualTo("NL");
        assertThat(nl.get().getName()).isEqualTo("Netherlands");
    }

    @Test
    public void getCurrentByCodeAsCountry() {
        Optional<CurrentCountry> nl = Regions.getByCode("NL", CurrentCountry.class);
        assertThat(nl.get().getISO3166_3_Code()).isEqualTo("NLD");
    }
    @Test
    public void getFormerByCode() {

        Region cshh = Regions.getByCode("CSHH").orElse(null);
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        assertThat(cshh.getISOCode()).isEqualTo("CSHH");
        assertThat(cshh.getName()).isEqualTo("Czechoslovakia");
    }
    @Test
    public void getFormerByCodeAsCountry() {
        Optional<Country> nl = Regions.getByCode("CSHH", Country.class);
        assertThat(nl.get().getISO3166_3_Code()).isEqualTo("CSHH");
    }

    @Test
    public void getCountrySubDivision() {

        Region utrecht = Regions.getByCode("NL:UT").orElse(null);
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubDivision.class);
        assertThat(utrecht.getISOCode()).isEqualTo("NL:UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }


    @Test
    public void values() {

        Regions.values().forEach(r -> {
            System.out.println(r.getISOCode()  + " : " + r.getName());
        });

    }
```

Persistence
-----------
`org.meeuw.i18n.persistence.RegionToStringConverter` is meant to arrange JPA persistence of `Region` objects to the database. We want the iso code to be used as simple strings in a database column or so.

This will also deal gracefully with codes which gets unassigned, because `Regions#getByCode` will also fall back to formerly assigned codes.

TODO
----
- The persistence solution is not yet well tested
- We may add validators, to give the possibility to limit the possible values of a `Region` typed variable
- We may add implementations of  JAXB/Jackon-adapters to arrange proper xml/json bindings of member of type `Region`


