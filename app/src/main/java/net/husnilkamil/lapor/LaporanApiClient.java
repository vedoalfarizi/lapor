package net.husnilkamil.lapor;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LaporanApiClient {
    @GET("laporans")
    Call<List<Laporan>> getAllLaporan();

    @FormUrlEncoded
    @POST("laporans/create")
    Call<ResponseBody> createLaporan(
        @Field("judul") String judul,
        @Field("uraian") String uraian,
        @Field("pelapor") String pelapor,
        @Field("tanggal") String tanggal,
        @Field("lokasi") String lokasi,
        @Field("foto") String foto
    );

}
