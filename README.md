Geographical regions
--------------------

The central interface of this module is `org.meeuw.i18n.Region`, which represents some geographical region.


Instances are created by services implementing `org.meeuw.i18n.RegionProvider` (registered via META-INF/services).

By default we provide services backed by `com.neovisionaries.i18n.CountryCode` (for current countries), by `org.meeuw.i18n.FormerlyAssignedCountryCode` (for former countries) and by `be.olsson.i18n.subdivision.CountryCodeSubdivision` (for subdivisions of countries)
