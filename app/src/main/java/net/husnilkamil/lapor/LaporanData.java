package net.husnilkamil.lapor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LaporanData {
    @SerializedName("data")
    @Expose
    public List<Laporan> data = null;
}
