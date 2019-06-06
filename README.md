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

        Region nl = Utils.getByCode("NL");
        assertThat(nl).isNotNull();
        assertThat(nl).isInstanceOf(CurrentCountry.class);
        assertThat(nl.getISOCode()).isEqualTo("NL");
        assertThat(nl.getName()).isEqualTo("Netherlands");

    }
    @Test
    public void getFormerByCode() {

        Region cshh = Utils.getByCode("CSHH");
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        assertThat(cshh.getISOCode()).isEqualTo("CSHH");
        assertThat(cshh.getName()).isEqualTo("Czechoslovakia");
    }

    @Test
    public void getCountrySubDivision() {

        Region utrecht = Utils.getByCode("NL:UT");
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubDivision.class);
        assertThat(utrecht.getISOCode()).isEqualTo("NL:UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }


    @Test
    public void values() {

        Utils.values().forEach(r -> {
            System.out.println(r + " : " + r.getName());
        });

    }
```

Persistence
-----------
`org.meeuw.i18n.persistence.RegionToStringConverter` is meant to arrange persistence of `Region` objects to the database. We want the iso code to be used as simple strings in a database column or so.

