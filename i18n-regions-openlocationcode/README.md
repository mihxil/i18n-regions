Open Location Codes as regions
---
A wrapper around [`OpenLocationCode`](https://github.com/google/open-location-code/blob/master/java/src/main/java/com/google/openlocationcode/OpenLocationCode.java).


Every such code represents a rectangular region on earth. 

See https://plus.codes/

So this exposes every open location code as a instance of Region. I'm not sure how usefull this is, but implementing this shows how the region service framework could be implemented with very many instances.