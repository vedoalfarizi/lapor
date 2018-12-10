package net.husnilkamil.lapor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LaporanApiClient {
    @GET("laporans")
    Call<List<Laporan>> getAllLaporan();

//    @FormUrlEncoded
//    @POST("absensi/insert")
//    public void insertAbsen(
//            @Field("bp") String bp,
//            @Field("nama") String nama,
//            @Field("kelas") String kelas,
//            @Field("mata_kuliah") String mata_kuliah);
}
