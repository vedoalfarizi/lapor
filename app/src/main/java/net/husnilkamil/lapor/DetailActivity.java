package net.husnilkamil.lapor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    ImageView imgCover;
    TextView textJudul, textTanggal, textLokasi, textPelapor, textUraian;

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

        Intent intent = getIntent();
        if(intent != null){
            Laporan laporan= intent.getParcelableExtra("laporan_extra_key");
            textJudul.setText(laporan.judul.toUpperCase());
            textTanggal.setText(laporan.tanggal);
            textLokasi.setText(laporan.lokasi);
            textPelapor.setText(laporan.pelapor);
            textUraian.setText(laporan.uraian);

//            String poster_url = "http://image.tmdb.org/t/p/w342" + laporan.foto;
//            Glide.with(imgCover).load(poster_url).into(imgCover);
        }
    }
}
