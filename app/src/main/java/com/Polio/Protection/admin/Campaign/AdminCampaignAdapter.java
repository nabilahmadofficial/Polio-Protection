package com.Polio.Protection.admin.Campaign;

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

public class AdminCampaignAdapter extends RecyclerView.Adapter<AdminCampaignAdapter.AdminCampaignHolder> {

    private ArrayList<AdminCampaignItem> adminCampaignItemArrayList;
    private Context mContext;

    class AdminCampaignHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView campaign_no, campaign_start_date, campaign_end_date;

        public AdminCampaignHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            campaign_no = itemView.findViewById(R.id.campaign_no);
            campaign_start_date = itemView.findViewById(R.id.campaign_start_date);
            campaign_end_date = itemView.findViewById(R.id.campaign_end_date);
        }
    }

    public AdminCampaignAdapter(Context context, ArrayList<AdminCampaignItem> campaignItems) {
        adminCampaignItemArrayList = campaignItems;
        mContext = context;
    }

    @NonNull
    @Override
    public AdminCampaignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_campaign_list, viewGroup, false);
        AdminCampaignHolder Cvh = new AdminCampaignHolder(v);
        return Cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCampaignHolder adminCampaignHolder, final int i) {

        AdminCampaignItem currentitem = adminCampaignItemArrayList.get(i);

        int no = currentitem.getCampaign_no();
        adminCampaignHolder.campaign_no.setText(String.valueOf(no));
        adminCampaignHolder.campaign_start_date.setText(currentitem.getCampaign_start_date());
        adminCampaignHolder.campaign_end_date.setText(currentitem.getCampaign_end_date());

        adminCampaignHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + adminCampaignItemArrayList.get(i));
                String campaign_key = adminCampaignItemArrayList.get(i).getCampaign_key();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, AdminAddUpdateCampaign.class);
                    intent.putExtra("campaign_key", campaign_key);
                    intent.putExtra("Action", "Update_Delete_Campaign");
                    mContext.startActivity(intent);
                } else {

                    Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminCampaignItemArrayList.size();
    }
}

