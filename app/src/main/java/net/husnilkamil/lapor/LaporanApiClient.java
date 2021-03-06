package net.husnilkamil.lapor;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LaporanApiClient {
    @GET("laporans")
    Call<List<Laporan>> getAllLaporan();

    @GET("laporans/favorite")
    Call<List<Laporan>> getFavLaporan();

    @Multipart
    @POST("laporans/create")
    Call<ResponseBody> createLaporan(
            @Part("judul") RequestBody pjudul,
            @Part("uraian") RequestBody puraian,
            @Part("pelapor") RequestBody ppelapor,
            @Part("tanggal") RequestBody ptanggal,
            @Part("lokasi") RequestBody plokasi,
            @Part MultipartBody.Part file
            );

    @FormUrlEncoded
    @POST("laporans/fav")
    Call<ResponseBody> favorite(
            @Field("id") int id,
            @Field("favorite") int fav
    );

}
