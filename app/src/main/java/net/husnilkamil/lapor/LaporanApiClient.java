package net.husnilkamil.lapor;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LaporanApiClient {
    @GET("/laporans")
    Call<LaporanData> getAllLaporan();
}
