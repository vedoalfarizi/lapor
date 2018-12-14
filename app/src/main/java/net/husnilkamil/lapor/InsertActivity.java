package net.husnilkamil.lapor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsertActivity extends AppCompatActivity{
    private TextInputLayout textInputJudul;
    private TextInputLayout textInputPelapor;
    private TextInputLayout textInputUraian;
    private TextInputLayout textInputFoto;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public double longtitude, latitude;
    public  String lokasi;

    Geocoder geocoder;
    List<Address> addresses;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        textInputJudul = findViewById(R.id.textInputJudul);
        textInputPelapor = findViewById(R.id.textInputPelapor);
        textInputUraian = findViewById(R.id.textInputUraian);
        textInputFoto = findViewById(R.id.textInputFoto);

        geocoder = new Geocoder(this, Locale.getDefault());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longtitude = location.getLongitude();
                latitude = location.getLatitude();

                try {
                    addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                    lokasi = addresses.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    private boolean validateJudul(){
        String judulInput = textInputJudul.getEditText().getText().toString().trim();

        if(judulInput.isEmpty()){
            textInputJudul.setError("Judul tidak boleh kosong");
            return false;
        }else{
            textInputJudul.setError(null);
            return true;
        }
    }

    private boolean validatePelapor(){
        String pelaporInput = textInputPelapor.getEditText().getText().toString().trim();

        if(pelaporInput.isEmpty()){
            textInputPelapor.setError("Nama Anda tidak boleh kosong");
            return false;
        }else{
            textInputPelapor.setError(null);
            return true;
        }
    }

    private boolean validateUraian(){
        String uraianInput = textInputUraian.getEditText().getText().toString().trim();

        if(uraianInput.isEmpty()){
            textInputUraian.setError("Uraian laporan tidak boleh kosong");
            return false;
        }else{
            textInputUraian.setError(null);
            return true;
        }
    }

    private boolean validateFoto(){
        String fotoInput = textInputFoto.getEditText().getText().toString().trim();

        if(fotoInput.isEmpty()){
            textInputFoto.setError("Foto tidak boleh kosong");
            return false;
        }else{
            textInputFoto.setError(null);
            return true;
        }
    }


    public void submitInput(View v){
        if(!validateJudul() | !validatePelapor() | !validateUraian() | !validateFoto()){
            return;
        }

        String judul = textInputJudul.getEditText().getText().toString();
        String uraian = textInputUraian.getEditText().getText().toString();
        String pelapor = textInputPelapor.getEditText().getText().toString();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(calendar.getTime());

        String foto = textInputFoto.getEditText().getText().toString();

        LaporanApiClient client = (new Retrofit.Builder()
            .baseUrl("http://nagarikapa.com/lapor/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build())
                .create(LaporanApiClient.class);

        Call<ResponseBody> call = client.createLaporan(judul, uraian, pelapor, tanggal, lokasi, foto);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    Toast.makeText(InsertActivity.this, s, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(InsertActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
