package com.example.myknowyourgovermentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder>{

    private List<Official> officialList;
    private MainActivity ma;

    OfficialAdapter(List<Official> officialList, MainActivity ma){
        this.officialList = officialList;
        this.ma = ma;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_entry, parent, false);

        itemView.setOnClickListener(ma);

        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
        Official official = officialList.get(position);

        holder.office_name.setText(official.getOffice());
        holder.name_and_party.setText(official.getName() + " (" + official.getParty()+ ")");
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
