# TwilioLookup

[![](https://jitpack.io/v/jrejaud/TwilioLookup.svg)](https://jitpack.io/#jrejaud/TwilioLookup)
[![Build Status](https://travis-ci.org/jrejaud/TwilioLookup.svg?branch=master)](https://travis-ci.org/jrejaud/TwilioLookup)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-TwilioLookup-green.svg?style=true)](https://android-arsenal.com/details/1/3298)

Simple Android wrapper for [Twilio's Phone Lookup](https://www.twilio.com/lookup) API

Example
===
```java
TwilioLookup.setAuthorization("yourTwilioAccountSid", "yourTwilioAuthToken");

TwilioLookup.lookup("theNumberYouWantToLookup", new TwilioLookup.OnResponseCallback() {
    @Override
    public void onValidResponse(TwilioLookupResponse response) {
        Log.d("TAG","Success, country code: "+response.getCountryCode()+" "+response.getCarrier().getName());
    }

    @Override
    public void onInvalidResponse(Response response) {
        Log.d("TAG","Failure, phone number is not valid, "+response.message());
     }

     @Override
    public void onFailure(Throwable throwable) {
        Log.e("TAG", "Error: " + throwable.toString());
    }
});
```


Installation
===

See [JitPack](https://jitpack.io/#jrejaud/TwilioLookup) for more information.

```groovy
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```

```groovy
dependencies {
	    compile 'com.github.jrejaud:TwilioLookup:v1.0'
}
```
