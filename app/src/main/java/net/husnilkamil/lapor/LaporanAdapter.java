package net.husnilkamil.lapor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanHolder>{

    @NonNull
    @Override
    public LaporanAdapter.LaporanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanAdapter.LaporanHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LaporanHolder extends RecyclerView.ViewHolder{

        public LaporanHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
