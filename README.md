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
The central interface of this module is [`org.meeuw.i18n.Region`](i18n-regions/src/main/java/org/meeuw/i18n/Region.java), which represents some geographical region.

Instances are created via  [java service providers](https://www.baeldung.com/java-spi) implementing [`org.meeuw.i18n.spi.RegionProvider`](i18n-regions/src/main/java/org/meeuw/i18n/spi/RegionProvider.java) (registered via [META-INF/services](i18n-regions/src/main/resources/META-INF/services/org.meeuw.i18n.spi.RegionProvider)), which are all managed by  [`org.meeuw.i18n.RegionService`](i18n-regions/src/main/java/org/meeuw/i18n/RegionService.java). 

Providers are distributes via different artifacts, so you can in that way select what kind of regions the  service should provide.


Providers
========

### Countries
If you only need countries, you can take a dependency on [`org.meeuw.i18n:i18n-regions-countries`](https://search.maven.org/search?q=g:org.meeuw.i18n%20AND%20a:i18n-regions-countries&core=gav)

It provides:
- For current countries there are [`org.meeuw.i18n.countries.CurrentCountry`'s](i18n-regions-countries/src/main/java/org/meeuw/i18n/countries/CurrentCountry.java). Backend by `com.neovisionaries.i18n.CountryCode`
- For former countries there is [`org.meeuw.i18n.countries.FormerCountry`](i18n-regions-countries/src/main/java/org/meeuw/i18n/countries/FormerCountry.java), which is backed by  `org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode` (from [i18n-formerly-assigned](https://github.com/mihxil/i18n-formerly-assigned))
- Some common user assigned countries are  hard coded in [`org.meeuw.i18.countries.UserAssignedCountry`](i18n-regions-countries/src/main/java/org/meeuw/i18n/countries/UserAssignedCountry.java)

### Subdivisions of countries
These are provided in [`org.meeuw.i18n:i18n-regions-subdivisions`](https://search.maven.org/search?q=g:org.meeuw.i18n%20AND%20a:i18n-regions-subdivisions&core=gav)
- For subdivision of countries [`org.meeuw.i18n.subdivisions.CountrySubdivision`](i18n-regions-subdivisions/src/main/java/org/meeuw/i18n/subdivisions/CountrySubdivision.java), which is backed by 
`be.olsson.i18n.subdivision.CountryCodeSubdivision` (from https://github.com/tobias-/i18n-subdivisions)
- In case there are missing country subdivision they can easily be added via `subdivision.<country code>.properties`. E.g. [`subdivisions.GB.properties`](i18n-regions-subdivisions/src/main/resources/subdivisions.GB.properties) provides some which were obviously missing from Great Britain otherwise.

### Continents
A list of codes for the continents is provided in [`org.meeuw.i18n:i18n-regions-continents`](https://search.maven.org/search?q=g:org.meeuw.i18n%20AND%20a:i18n-regions-continents&core=ga)

### More
In the same fashion arbitrary region implementations can easily be plugged in. This project also provides [a region implemtention based on google 'open location code'](i18n-regions-openlocationcode).


Persistence
-----------
[`org.meeuw.i18n.persistence.RegionToStringConverter`](i18n-regions/src/main/java/org/meeuw/i18n/persistence/RegionToStringConverter.java) is meant to arrange JPA persistence of `Region` objects to the database. We want the iso code to be used as simple strings in a database column or so.

This will also deal gracefully with codes which gets unassigned, because `RegionService#getByCode` will also fall back to formerly assigned codes.

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
 protected List<
        // valid are countries (further validated by @ValidCountry), and a list of codes.
        org.meeuw.i18n.
        @ValidRegion(classes = {Country.class}, includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.USER_ASSIGNED | ValidCountry.FORMER, excludes = {"XN"})
        @NotNull Region> countries;
```
or, if you prefer, on the collection itself:
```java
    @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.USER_ASSIGNED | ValidCountry.FORMER, includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
    protected List<org.meeuw.i18n.Region> countries;
```

This list will not validate if you add Regions which don't follow the given rules.

It can also be used on `java.util.Locale`, which contain a country component too:
```java
 protected List<
        @ValidRegion(classes = {Country.class})
        @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.USER_ASSIGNED | ValidCountry.FORMER, excludes = {"XN"})
        @Language(mayContainCountry = true)
        @NotNull Locale> languages;
```
(For completeness also [`@Language`](i18n-regions/src/main/java/org/meeuw/i18n/validation/Language.java) is provided).

As a utility there is `org.meeuw.i18n.validation.RegionValidatorService` which can be used to for the settings in these annotations also to filter a stream of regions (e.g. `RegionService#values()`)
```java 
 return RegionService.getInstance().values()
            .filter(RegionValidatorService.getInstance().fromProperty(MediaObject.class, "countries"))
            .sorted(Regions.sortByName(LanguageCode.nl))
            .map(GuiEntry::of)
            .collect(Collectors.toList());
```

Optional dependencies
----
Several dependencies are marked `optional` in the pom.xml. E.g. the annotations used to arrange XML bindings and validation are not present (any more) in java 11. If they are not present, this will not make it impossible to use the classes, you just cannot use JAXB, JPA, validation or whatever the missing dependency is related to. It's only about annotations so that doesn't cause (by the JSR-175 specification) problems.

Building and Jigsaw
---
This projects needs to build with java 11. It produces byte code compatible for java 8 though (besides module-info.class)  The goal is to be compatible with [jigsaw](https://www.baeldung.com/project-jigsaw-java-modularity), which was introduced in java 9.

If you use java 11 then you can require `org.meeuw.i18n` in `module-info.java`


Testing
----
Besides the usual junit test in [src/test](src/test), in the [tests](tests) folder I collect some sample projects to test this stuff out by hand.
Try e.g. 
```bash
cd tests/springboot
mvn spring-boot:run
```

Logging
----
Some logging happens via the `java.util.logging` framework to avoid any extra dependencies.

When you use slf4j or logback you could take this dependency:
```xml
<dependency>
  <!-- region service uses java.util.logging. This makes it log to logback as springboot does -->
  <groupId>org.slf4j</groupId>
  <artifactId>jul-to-slf4j</artifactId>
  <version>1.7.25</version>
</dependency>
```
There are very few log events, it is not important.


Version 1.0
---
For version 1.0 I'm trying to more properly modularize all this test it. It's a bit more complicated then I anticipated and lots of tests are failing now. Probably some stuff must be moved to proper black box testing. I'm also doubting if the white box testing module-info.java  patching is configured correctly and/or properly supported by Intellij and/or maven.

