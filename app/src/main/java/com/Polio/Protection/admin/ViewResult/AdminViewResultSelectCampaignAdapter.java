package com.Polio.Protection.admin.ViewResult;

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

public class AdminViewResultSelectCampaignAdapter extends RecyclerView.Adapter<AdminViewResultSelectCampaignAdapter.AdminViewResultSelectCampaignHolder> {

    private ArrayList<AdminViewResultSelectCampaignItem> adminViewResultSelectCampaignItems;
    private Context mContext;

    class AdminViewResultSelectCampaignHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView campaign_no, campaign_id, campaign_start_date, campaign_end_date;

        public AdminViewResultSelectCampaignHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            campaign_no = itemView.findViewById(R.id.campaign_no);
            campaign_id = itemView.findViewById(R.id.campaign_id);
            campaign_start_date = itemView.findViewById(R.id.campaign_start_date);
            campaign_end_date = itemView.findViewById(R.id.campaign_end_date);
        }
    }

    public AdminViewResultSelectCampaignAdapter(Context context, ArrayList<AdminViewResultSelectCampaignItem> adminViewResultSelectCampaignItemArrayList) {
        adminViewResultSelectCampaignItems = adminViewResultSelectCampaignItemArrayList;
        mContext = context;
    }

    @NonNull
    @Override
    public AdminViewResultSelectCampaignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_check_report_check_campaign_list, viewGroup, false);
        AdminViewResultSelectCampaignHolder AVRSCvh = new AdminViewResultSelectCampaignHolder(v);
        return AVRSCvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewResultSelectCampaignHolder adminViewResultSelectCampaignHolder, final int i) {

        AdminViewResultSelectCampaignItem currentitem = adminViewResultSelectCampaignItems.get(i);

        int no = currentitem.getCampaign_no();
        adminViewResultSelectCampaignHolder.campaign_no.setText(String.valueOf(no));
        adminViewResultSelectCampaignHolder.campaign_id.setText(currentitem.getCampaign_id());
        adminViewResultSelectCampaignHolder.campaign_start_date.setText(currentitem.getCampaign_star_date());
        adminViewResultSelectCampaignHolder.campaign_end_date.setText(currentitem.getCampaign_end_date());

        adminViewResultSelectCampaignHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + adminViewResultSelectCampaignItems.get(i));
                String campaign_key = adminViewResultSelectCampaignItems.get(i).getCampaign_key();
                String campaign_id = adminViewResultSelectCampaignItems.get(i).getCampaign_id();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, AdminViewResult.class);
                    intent.putExtra("campaign_key", campaign_key);
                    intent.putExtra("campaign_id", campaign_id);
                    mContext.startActivity(intent);
                } else {

                    Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminViewResultSelectCampaignItems.size();
    }
}

