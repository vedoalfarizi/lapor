package net.husnilkamil.lapor;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements LaporanAdapter.OnItemClick{

    private RecyclerView rvLaporan;
    private LaporanAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new LaporanAdapter();
        adapter.setHandler(this);

        rvLaporan= findViewById(R.id.rvMain);
        rvLaporan.setLayoutManager(new LinearLayoutManager(this));
        rvLaporan.setAdapter(adapter);

        db = Room.databaseBuilder(this, AppDatabase.class, "lapor.db")
                .allowMainThreadQueries()
                .build();

        getAllLaporan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addMenu:
                getFormInput();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllLaporan(){

        if(isConnected()){
            LaporanApiClient client = (new Retrofit.Builder()
                    .baseUrl("http://nagarikapa.com/lapor/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build())
                    .create(LaporanApiClient.class);

            Call<List<Laporan>> call = client.getAllLaporan();

            call.enqueue(new Callback<List<Laporan>>() {
                @Override
                public void onResponse(Call<List<Laporan>> call, Response<List<Laporan>> response) {
                    List<Laporan> laporanList = response.body();

                    adapter.setDataLaporan((ArrayList<Laporan>) laporanList);
                    saveLaporanstoDb(laporanList);
                }

                @Override
                public void onFailure(Call<List<Laporan>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            List<Lapor> lapors = db.laporDao().getLaporans();
            ArrayList<Laporan> laporans = new ArrayList<>();
            for(Lapor n : lapors){
                Laporan l = new Laporan(
                    n.id,
                    n.judul,
                    n.uraian,
                    n.pelapor,
                    n.tanggal,
                    n.lokasi,
                    n.foto
                );
                laporans.add(l);
            }
            adapter.setDataLaporan(laporans);
        }
    }

    private void saveLaporanstoDb(final List<Laporan> results){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Laporan l : results){
                    Lapor lapor = new Lapor();
                    lapor.id = l.id;
                    lapor.judul = l.judul;
                    lapor.uraian = l.uraian;
                    lapor.pelapor = l.pelapor;
                    lapor.tanggal = l.tanggal;
                    lapor.lokasi = l.lokasi;
                    lapor.foto = l.foto;

                    db.laporDao().insertLaporan(lapor);
                }
            }
        }).start();
    }

    @Override
    public void click(Laporan m) {
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra("laporan_extra_key", m);
        startActivity(detailActivityIntent);
    }

    public void getFormInput(){
        Intent insertActivityIntent = new Intent(this, InsertActivity.class);
        startActivity(insertActivityIntent);
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
