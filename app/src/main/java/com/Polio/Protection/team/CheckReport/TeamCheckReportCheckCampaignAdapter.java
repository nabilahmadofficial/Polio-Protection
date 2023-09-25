package com.Polio.Protection.team.CheckReport;

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
import com.Polio.Protection.team.MarkChildren.TeamMarkChildrenCheckCampaignItem;
import com.Polio.Protection.team.MarkChildren.TeamMarkChildrenSearch;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TeamCheckReportCheckCampaignAdapter extends RecyclerView.Adapter<TeamCheckReportCheckCampaignAdapter.TeamCheckReportCheckCampaignHolder> {

    private ArrayList<TeamCheckReportCheckCampaignItem> teamCheckReportCheckCampaignItemArrayList;
    private Context mContext;

    class TeamCheckReportCheckCampaignHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView campaign_no, campaign_id, campaign_start_date, campaign_end_date;

        public TeamCheckReportCheckCampaignHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            campaign_no = itemView.findViewById(R.id.campaign_no);
            campaign_id = itemView.findViewById(R.id.campaign_id);
            campaign_start_date = itemView.findViewById(R.id.campaign_start_date);
            campaign_end_date = itemView.findViewById(R.id.campaign_end_date);
        }
    }

    public TeamCheckReportCheckCampaignAdapter(Context context, ArrayList<TeamCheckReportCheckCampaignItem> teamCheckReportCheckCampaignItems) {
        teamCheckReportCheckCampaignItemArrayList = teamCheckReportCheckCampaignItems;
        mContext = context;
    }

    @NonNull
    @Override
    public TeamCheckReportCheckCampaignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_check_report_check_campaign_list, viewGroup, false);
        TeamCheckReportCheckCampaignHolder TCRvh = new TeamCheckReportCheckCampaignHolder(v);
        return TCRvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamCheckReportCheckCampaignHolder teamCheckReportCheckCampaignHolder, final int i) {

        TeamCheckReportCheckCampaignItem currentitem = teamCheckReportCheckCampaignItemArrayList.get(i);

        int no = currentitem.getCampaign_no();
        teamCheckReportCheckCampaignHolder.campaign_no.setText(String.valueOf(no));
        teamCheckReportCheckCampaignHolder.campaign_id.setText(currentitem.getCampaign_id());
        teamCheckReportCheckCampaignHolder.campaign_start_date.setText(currentitem.getCampaign_star_date());
        teamCheckReportCheckCampaignHolder.campaign_end_date.setText(currentitem.getCampaign_end_date());

        teamCheckReportCheckCampaignHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + teamCheckReportCheckCampaignItemArrayList.get(i));
                String campaign_key = teamCheckReportCheckCampaignItemArrayList.get(i).getCampaign_key();
                String campaign_id = teamCheckReportCheckCampaignItemArrayList.get(i).getCampaign_id();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, TeamCheckReport.class);
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
        return teamCheckReportCheckCampaignItemArrayList.size();
    }
}

