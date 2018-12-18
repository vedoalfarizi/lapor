package net.husnilkamil.lapor.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "laporans")
public class Lapor {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "judul")
    public String judul;

    @ColumnInfo(name = "uraian")
    public String uraian;

    @ColumnInfo(name = "pelapor")
    public String pelapor;

    @ColumnInfo(name = "tanggal")
    public String tanggal;

    @ColumnInfo(name = "lokasi")
    public String lokasi;

    @ColumnInfo(name = "foto")
    public String foto;

}
