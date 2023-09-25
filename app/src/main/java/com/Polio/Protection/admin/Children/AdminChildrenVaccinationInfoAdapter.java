package com.Polio.Protection.admin.Children;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Polio.Protection.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AdminChildrenVaccinationInfoAdapter extends RecyclerView.Adapter<AdminChildrenVaccinationInfoAdapter.AdminChildrenVaccinationHolder> {

    private ArrayList<AdminChildrenVaccinationInfoItem> adminChildrenVaccinationInfoItemArrayList;
    private Context mContext;

    class AdminChildrenVaccinationHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView children_no, campaign_id, mark_date, mark_time;

        public AdminChildrenVaccinationHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            children_no = itemView.findViewById(R.id.children_no);
            campaign_id = itemView.findViewById(R.id.campaign_id);
            mark_date = itemView.findViewById(R.id.mark_date);
            mark_time = itemView.findViewById(R.id.time_mark);
        }
    }

    public AdminChildrenVaccinationInfoAdapter(Context context, ArrayList<AdminChildrenVaccinationInfoItem> adminChildrenVaccinationInfoItems) {
        adminChildrenVaccinationInfoItemArrayList = adminChildrenVaccinationInfoItems;
        mContext = context;
    }

    @NonNull
    @Override
    public AdminChildrenVaccinationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_children_vaccination_info_list, viewGroup, false);
        AdminChildrenVaccinationHolder ACVvh = new AdminChildrenVaccinationHolder(v);
        return ACVvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminChildrenVaccinationHolder adminChildrenVaccinationHolder, final int i) {

        AdminChildrenVaccinationInfoItem currentitem = adminChildrenVaccinationInfoItemArrayList.get(i);

        int no = currentitem.getNo();
        adminChildrenVaccinationHolder.children_no.setText(String.valueOf(no));
        adminChildrenVaccinationHolder.campaign_id.setText(currentitem.getCampaign_id());
        adminChildrenVaccinationHolder.mark_date.setText(currentitem.getMark_date());
        adminChildrenVaccinationHolder.mark_time.setText(currentitem.getMark_time());

        adminChildrenVaccinationHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + adminChildrenVaccinationInfoItemArrayList.get(i));
                String children_key = adminChildrenVaccinationInfoItemArrayList.get(i).getChild_key();
                String campaign_key = adminChildrenVaccinationInfoItemArrayList.get(i).getCampaign_key();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, AdminChildrenVaccinationDeatils.class);
                    intent.putExtra("children_key", children_key);
                    intent.putExtra("campaign_key", campaign_key);
                    mContext.startActivity(intent);
                } else {

                    Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminChildrenVaccinationInfoItemArrayList.size();
    }
}

