package net.husnilkamil.lapor;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import net.husnilkamil.lapor.db.AppDatabase;
import net.husnilkamil.lapor.db.Lapor;

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
    private AppDatabase db;

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

        db = Room.databaseBuilder(this, AppDatabase.class, "lapor.db")
                .allowMainThreadQueries()
                .build();

        getFavoriteLaporan();
    }

    private void getFavoriteLaporan(){
        if(isConnected()){
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
        }else{
            List<Lapor> lapors = db.laporDao().getFavLaporans();
            ArrayList<Laporan> laporans = new ArrayList<>();
            for(Lapor n : lapors){
                Laporan l = new Laporan(
                        n.id,
                        n.judul,
                        n.uraian,
                        n.pelapor,
                        n.tanggal,
                        n.lokasi,
                        n.foto,
                        n.favorite
                );
                laporans.add(l);
            }
            adapter.setDataLaporan(laporans);
        }
    }

    @Override
    public void click(Laporan m) {

    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
