package com.jrejaud.twiliolookup;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jrejaud on 3/16/16.
 */
public interface TwilioService {
    @GET("PhoneNumbers/{phoneNumber}?Type=carrier")
    Call<TwilioLookupResponse> lookup(@Path("phoneNumber") String phoneNumber);
}