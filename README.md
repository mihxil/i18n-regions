[![Build Status](https://travis-ci.org/mihxil/i18n-regions.svg?)](https://travis-ci.org/mihxil/i18n-regions)
[![Maven Central](https://img.shields.io/maven-central/v/org.meeuw.i18n/i18n-regions.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.meeuw.i18n%22)
[![snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.meeuw.i18n/i18n-regions.svg)](https://oss.sonatype.org/content/repositories/staging/org/meeuw/i18n/)

Geographical regions
--------------------

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
`org.meeuw.i18n.persistence.RegionToStringConverter` is meant to arrange persistence of `Region` objects to the database. We want the iso code to be used as simple strings in a database column or so.

