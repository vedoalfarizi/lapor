package net.husnilkamil.lapor;

import android.os.Parcel;
import android.os.Parcelable;

public class Laporan implements Parcelable{

    public int id;
    public String judul;
    public String uraian;
    public String pelapor;
    public String tanggal;
    public String lokasi;
    public String foto;

    public Laporan(int id, String judul, String uraian, String pelapor, String tanggal, String lokasi, String foto){
        this.id = id;
        this.judul = judul;
        this.uraian = uraian;
        this.pelapor = pelapor;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.foto = foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(judul);
        dest.writeString(uraian);
        dest.writeString(pelapor);
        dest.writeString(tanggal);
        dest.writeString(lokasi);
        dest.writeString(foto);
    }

    protected Laporan(Parcel in){

        id = in.readInt();
        judul = in.readString();
        uraian = in.readString();
        pelapor = in.readString();
        tanggal = in.readString();
        lokasi = in.readString();
        foto = in.readString();
    }

    public static final Creator<Laporan> CREATOR = new Creator<Laporan>() {
        @Override
        public Laporan createFromParcel(Parcel in) {
            return new Laporan(in);
        }

        @Override
        public Laporan[] newArray(int size) {
            return new Laporan[size];
        }
    };
}


