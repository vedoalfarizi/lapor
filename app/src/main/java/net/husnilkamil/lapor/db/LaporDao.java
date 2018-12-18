package net.husnilkamil.lapor.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LaporDao {
    @Query("SELECT * FROM laporans")
    List<Lapor> getLaporans();

    @Query("SELECT * FROM laporans WHERE id = :idLaporan")
    Lapor getById(int idLaporan);

    @Query("SELECT * FROM laporans WHERE favorite = 1")
    List<Lapor> getFavLaporans();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLaporan(Lapor lapor);

    @Delete
    void delete(Lapor lapor);
}
