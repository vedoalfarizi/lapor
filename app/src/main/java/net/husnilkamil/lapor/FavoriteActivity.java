package net.husnilkamil.lapor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteActivity extends AppCompatActivity implements LaporanAdapter.OnItemClick{
    private RecyclerView rvFavorite;
    private LaporanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new LaporanAdapter();
        adapter.setHandler(this);

        rvFavorite= findViewById(R.id.rvFav);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        rvFavorite.setAdapter(adapter);

        getFavoriteLaporan();
    }

    private void getFavoriteLaporan(){

        LaporanApiClient client = (new Retrofit.Builder()
                .baseUrl("http://nagarikapa.com/lapor/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build())
                .create(LaporanApiClient.class);

        Call<List<Laporan>> call = client.getFavLaporan();

        call.enqueue(new Callback<List<Laporan>>() {
            @Override
            public void onResponse(Call<List<Laporan>> call, Response<List<Laporan>> response) {
                List<Laporan> laporanList = response.body();

                adapter.setDataLaporan((ArrayList<Laporan>) laporanList);
            }

            @Override
            public void onFailure(Call<List<Laporan>> call, Throwable t) {
                Toast.makeText(FavoriteActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void click(Laporan m) {

    }
}
