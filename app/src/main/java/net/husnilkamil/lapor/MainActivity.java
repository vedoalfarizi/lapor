package net.husnilkamil.lapor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new LaporanAdapter();
        adapter.setHandler(this);

        rvLaporan= findViewById(R.id.rvMain);
        getAllLaporan();
        rvLaporan.setLayoutManager(new LinearLayoutManager(this));
        rvLaporan.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getAllLaporan(){
        LaporanApiClient client = (new Retrofit.Builder()
        .baseUrl("http://nagarikapa.com/lapor/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build())
        .create(LaporanApiClient.class);

        Call<List<Laporan>> call = client.getAllLaporan();

        call.enqueue(new Callback<List<Laporan>>() {
            @Override
            public void onResponse(Call<List<Laporan>> call, Response<List<Laporan>> response) {
                Toast.makeText(MainActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                List<Laporan> laporanList = response.body();

                adapter.setDataLaporan((ArrayList<Laporan>) laporanList);
            }

            @Override
            public void onFailure(Call<List<Laporan>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void click(Laporan m) {
//        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
//        detailActivityIntent.putExtra("laporan_extra_key", m);
//        startActivity(detailActivityIntent);
    }
}
