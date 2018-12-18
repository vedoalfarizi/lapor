package net.husnilkamil.lapor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imgCover;
    TextView textJudul, textTanggal, textLokasi, textPelapor, textUraian;
    Button btnFav, btnShare;

    private int id, fav;
    private String cover_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgCover = findViewById(R.id.imageCover);
        textJudul = findViewById(R.id.textJudul);
        textTanggal = findViewById(R.id.textTanggal);
        textLokasi = findViewById(R.id.textLokasi);
        textPelapor = findViewById(R.id.textPelapor);
        textUraian = findViewById(R.id.textUraian);
        btnFav = (Button) findViewById(R.id.buttonFav);
        btnShare = (Button) findViewById(R.id.buttonShare);

        Intent intent = getIntent();
        if(intent != null){
            Laporan laporan= intent.getParcelableExtra("laporan_extra_key");
            textJudul.setText(laporan.judul.toUpperCase());
            textTanggal.setText(laporan.tanggal);
            textLokasi.setText(laporan.lokasi);
            textPelapor.setText(laporan.pelapor);
            textUraian.setText(laporan.uraian);

            cover_url = "http://nagarikapa.com/lapor/storage/" + laporan.foto;
            Glide.with(imgCover).load(cover_url).into(imgCover);

            if(laporan.favorite == 0){
                btnFav.setBackgroundResource(R.drawable.ic_btn_unlike);
            }else{
                btnFav.setBackgroundResource(R.drawable.ic_btn_like);
            }

            id = laporan.id;
            fav = laporan.favorite;
        }

        btnFav.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonFav :
                submitFav();
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.buttonShare :
                shareContent();
        }
    }

    private void shareContent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("plain/text");
        shareIntent.putExtra(Intent.EXTRA_TEXT, cover_url);

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    private void submitFav(){
        LaporanApiClient client = (new Retrofit.Builder()
                .baseUrl("http://nagarikapa.com/lapor/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build())
                .create(LaporanApiClient.class);

        Call<ResponseBody> call = client.favorite(id, fav);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    Toast.makeText(DetailActivity.this, s, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
