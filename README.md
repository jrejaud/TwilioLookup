# TwilioLookup
Android wrapper for Twilio's Phone Lookup API

Simple Android wrapper for [Twilio's Phone Lookup](https://www.twilio.com/lookup)

Example
===
```
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

