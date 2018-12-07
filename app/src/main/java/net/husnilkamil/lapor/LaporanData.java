package net.husnilkamil.lapor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LaporanData {
    @SerializedName("results")
    @Expose
    public List<Laporan> results = null;
}
