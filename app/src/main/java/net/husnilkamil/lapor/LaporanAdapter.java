package net.husnilkamil.lapor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanHolder>{

    ArrayList<Laporan> dataLaporan;
    OnItemClick handler;

    public void setDataLaporan(ArrayList<Laporan> laporans){
        this.dataLaporan = laporans;
        notifyDataSetChanged();
    }

    public void setHandler(OnItemClick clickHandler){
        this.handler = clickHandler;
    }

    @NonNull
    @Override
    public LaporanAdapter.LaporanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(
                viewGroup.getContext()
        ).inflate(R.layout.listlaporan, viewGroup, false);

        LaporanHolder holder = new LaporanHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanHolder holder, int i) {
        Laporan laporan = dataLaporan.get(i);


    }

    @Override
    public int getItemCount() {
        if(dataLaporan != null){
            return dataLaporan.size();
        }else{
            return 0;
        }
    }

    public class LaporanHolder extends RecyclerView.ViewHolder{

        ImageView imgCover;
        TextView textJudul, textPelapor, textTanggal, textLokasi;

        public LaporanHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imageCover);
            textJudul = itemView.findViewById(R.id.textJudul);
            textPelapor = itemView.findViewById(R.id.textPelapor);
            textTanggal = itemView.findViewById(R.id.textTanggal);
            textLokasi = itemView.findViewById(R.id.textLokasi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Laporan m = dataLaporan.get(position);
                    handler.click(m);
                }
            });
        }
    }

    public interface OnItemClick{
        void click(Laporan m);
    }
}
