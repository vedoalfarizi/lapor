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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout textInputJudul;
    private TextInputLayout textInputPelapor;
    private TextInputLayout textInputUraian;
    private Button btnChooseFoto;
    private ImageView imageLaporan;
    private String imageLaporanLoc = "";
    private static final int CAMERA_PIC_REQUEST = 1111;
    private String postPath;
    private Uri fileUri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    private static final String TAG = InsertActivity.class.getSimpleName();

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
        btnChooseFoto = (Button) findViewById(R.id.buttonFoto);
        imageLaporan = (ImageView) findViewById(R.id.imageLaporan);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            btnChooseFoto.setEnabled(false);
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 10);
            return;
        }else{
            btnChooseFoto.setEnabled(true);
        }

        btnChooseFoto.setOnClickListener(this);

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
            }, 0);

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btnChooseFoto.setEnabled(true);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonFoto :
                dispatchPictureTakerAction();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_PIC_REQUEST){
                if(Build.VERSION.SDK_INT > 21){
                    Glide.with(this).load(imageLaporanLoc).into(imageLaporan);
                    postPath = imageLaporanLoc;
                }else{
                    Glide.with(this).load(fileUri).into(imageLaporan);
                    postPath = fileUri.getPath();
                }
            }
        }
    }

    private void dispatchPictureTakerAction() {
        if(Build.VERSION.SDK_INT > 21){
            Intent iTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(iTakePicture.resolveActivity(getPackageManager()) != null){
                File photo = null;

                photo = createPhotoFile();

                if(photo != null){
                    Uri outputUri = FileProvider.getUriForFile(
                            this,
                            "net.husnilkamil.lapor.fileprovider",
                            photo
                    );
                    Toast.makeText(this, "Foto siap dikirim", Toast.LENGTH_SHORT).show();
                    iTakePicture.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                    startActivityForResult(iTakePicture, CAMERA_PIC_REQUEST);
                }
            }
        }else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    private File createPhotoFile() {
        String nama = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    nama,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            Toast.makeText(this, "Gagal membuat gambar" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        imageLaporanLoc = image.getAbsolutePath();

        return image;
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
        if(postPath == null || postPath == ""){
            Toast.makeText(this, "Foto bukti tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return false;
        }else{
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

        File file = new File(postPath);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody pjudul = RequestBody.create(MediaType.parse("text/plain"), judul);
        RequestBody puraian = RequestBody.create(MediaType.parse("text/plain"), uraian);
        RequestBody ppelapor = RequestBody.create(MediaType.parse("text/plain"), pelapor);
        RequestBody ptanggal = RequestBody.create(MediaType.parse("text/plain"), tanggal);
        RequestBody plokasi = RequestBody.create(MediaType.parse("text/plain"), lokasi);

        LaporanApiClient client = (new Retrofit.Builder()
            .baseUrl("http://nagarikapa.com/lapor/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build())
                .create(LaporanApiClient.class);

        Call<ResponseBody> call = client.createLaporan(pjudul, puraian, ppelapor, ptanggal, plokasi, filePart);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }
}
