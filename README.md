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
    public void values() {

        Utils.values().forEach(r -> {
            System.out.println(r + " : " + r.getName());
        });

    }
```
