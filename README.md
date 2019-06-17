[![Build Status](https://travis-ci.org/mihxil/i18n-regions.svg?)](https://travis-ci.org/mihxil/i18n-regions)
[![Maven Central](https://img.shields.io/maven-central/v/org.meeuw.i18n/i18n-regions.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.meeuw.i18n%22)
[![snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.meeuw.i18n/i18n-regions.svg)](https://oss.sonatype.org/content/repositories/staging/org/meeuw/i18n/)
[![javadoc](http://www.javadoc.io/badge/org.meeuw.i18n/i18n-regions.svg?color=blue)](http://www.javadoc.io/doc/org.meeuw.i18n/i18n-regions)
[![codecov](https://codecov.io/gh/mihxil/i18n-regions/branch/master/graph/badge.svg)](https://codecov.io/gh/mihxil/i18n-regions)


geographical regions
=============

Introduction
---
This project was started to be able to make better use of [`CountryCode`](https://github.com/TakahikoKawasaki/nv-i18n/blob/master/src/main/java/com/neovisionaries/i18n/CountryCode.java)'s from [nv-i18n](https://github.com/TakahikoKawasaki/nv-i18n)

Using `CountryCode` as a value in your application has several drawbacks:

1. It is not extensible. If you need a value not in that enum, you're stuck.
2. It for example does not contain 'former countries', so e.g. the birth country of a person, or the country of a movie cannot be stored as a 'CountryCode'.
3. It's only applicable to countries, no other regions.

I decided to wrap `CountryCode` in a class `CurrentCountry`, which implements a `Region` interface, which makes it possible to make other implementation of `Region` too, and to address all the above issues if you choose to use `Region` in stead of `CountryCode` as the type of your variable.


Architecture
---
The central interface of this module is [`org.meeuw.i18n.Region`](src/main/java/org/meeuw/i18n/Region.java), which represents some geographical region.

Instances are created via  [java service providers](https://www.baeldung.com/java-spi) implementing [`org.meeuw.i18n.RegionProvider`](src/main/java/org/meeuw/i18n/RegionProvider.java) (registered via [META-INF/services](src/main/resourcces/META-INF/services/org.meeuw.i18n.RegionProvider)).

By default we provide these service 

- For current countries there are [`org.meeuw.i18n.countries.CurrentCountry`'s](src/main/java/org/meeuw/i18n/countries/CurrentCountry.java). Backend by `com.neovisionaries.i18n.CountryCode`
- For former countries there is [`org.meeuw.i18n.contries.FormerCountry`](src/main/java/org/meeuw/i18n/countries/FormerCountry.java), which is backed by  `org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode` (from [i18n-formerly-assigned](https://github.com/mihxil/i18n-formerly-assigned)
- For subdivision of countries [`org.meeuw.i18n.subdivisions.CountrySubdivision`](src/main/java/org/meeuw/i18n/subdivisions/CountrySubdivision.java), which is backed by 
`be.olsson.i18n.subdivision.CountryCodeSubdivision` (from https://github.com/tobias-/i18n-subdivisions)
- Some common user assigned countries are  hard coded in [`org.meeuw.i18.countries..UserAssignedCountry`](src/main/java/org/meeuw/i18n/countries/UserAssignedCountry.java)
- In case there are missing country subdivision they can easily be added via `subdivision.<country code>.properties`. E.g. [`subdivisions.GB.properties`](src/main/resources/subdivisions.GB.properties) provides some which were obviously missing from Great Britain otherwise.


Some utilities to deal with all this are provided in [`org.meeuw.i18n.Regions`](src/main/java/org/meeuw/i18n/Regions.java). 

Example code useage can be seen in the [test cases for the Regions utility](src/test/java/org/meeuw/i18n/RegionsTest.java)
 

Persistence
-----------
[`org.meeuw.i18n.persistence.RegionToStringConverter`](src/main/java/org/meeuw/i18n/persistence/.RegionToStringConverter.java) is meant to arrange JPA persistence of `Region` objects to the database. We want the iso code to be used as simple strings in a database column or so.

This will also deal gracefully with codes which gets unassigned, because `Regions#getByCode` will also fall back to formerly assigned codes.

e.g.
```java
  @ElementCollection
  @Convert(converter = RegionToStringConverter.class)
  protected List<org.meeuw.i18n.Region> countries;
```


XML Binding
----
The Region interface is JAXB-annotated to be marshallable to XML, which obviously should happen by the (ISO) code string.

Translations
----
The region interface also provides `Region#getName(Locale)` to retrieve the name of the region in the given locale. For many countries/locales this is supported by the JVM. Missing values can be provided via the `Regions` resource bundle.

Validation
-----
Given a certain field with type `Region` (or one of its sub types) you may still find that makes too much values available. Therefore we also provide some `javax.validation.ConstraintValidator` and associated annotations to limit possible values.

e.g.
```java
    @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.USER_ASSIGNED | ValidCountry.FORMER, includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
    protected List<org.meeuw.i18n.Region> countries;
```

This list will not validate if you add Regions which don't follow the given rules.

As a utility you can use the settings in the annotation also to filter a stream of regions (e.g. `Regions#values()`)
```java 
 return Regions.values()
            .filter(CountryValidator.fromField(MediaObject.class, "countries"))
            .sorted(Regions.sortByName(LanguageCode.nl))
            .map(GuiEntry::of)
            .collect(Collectors.toList());
```

Optional dependencies
----
Several dependencies are marked `optional` in the pom.xml. E.g. the annotations used to arrange XML bindings and validation are not present (any more) in java 11. If they are not present, this will not make it impossible to use the classes, you just cannot use JAXB, JPA, validation or whatever the missing dependency is related to.


Testing
----
Besides the usual junit test, in the [tests](tests) folder I collect some sample projects to test this stuff out by hand.
Try e.g. 
```bash
cd tests/springboot
mvn spring-boot:run
```


