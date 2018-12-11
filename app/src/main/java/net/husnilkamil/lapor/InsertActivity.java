package net.husnilkamil.lapor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class InsertActivity extends AppCompatActivity {
    private TextInputLayout textInputJudul;
    private TextInputLayout textInputPelapor;
    private TextInputLayout textInputUraian;
    private TextInputLayout textInputFoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        textInputJudul = findViewById(R.id.textInputJudul);
        textInputPelapor = findViewById(R.id.textInputPelapor);
        textInputUraian = findViewById(R.id.textInputUraian);
        textInputFoto = findViewById(R.id.textInputFoto);
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

        String input = "Judul: " + textInputJudul.getEditText().getText().toString();
        input+="\n";
        input+= "Nama Anda: " + textInputPelapor.getEditText().getText().toString();
        input+="\n";
        input+= "Uraian: " + textInputUraian.getEditText().getText().toString();
        input+="\n";
        input+= "Foto: " + textInputFoto.getEditText().getText().toString();
        input+="\n";

        Toast.makeText(this, input, Toast.LENGTH_LONG).show();
    }
}
