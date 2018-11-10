package com.fjun.hassalarm;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * API for hass.io
 * Supports updating the custom sensor "next_alarm" with a new {@link State}.
 * A API key is required.
 */
public interface HassApi {
    @POST("/api/states/sensor.next_alarm")
    Call<ResponseBody> setNextAlarm(@Body State state, @Header("x-ha-access") String apiKey);
}
