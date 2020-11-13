package com.example.myknowyourgovermentapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder{

    TextView office_name;
    TextView name_and_party;

    public OfficialViewHolder(@NonNull View itemView) {
        super(itemView);
        office_name = itemView.findViewById(R.id.officeField);
        name_and_party = itemView.findViewById(R.id.name_with_party);
    }
}
