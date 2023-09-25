package com.Polio.Protection.team.MarkChildren;

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

public class TeamMarkChildrenCheckCampaignAdapter extends RecyclerView.Adapter<TeamMarkChildrenCheckCampaignAdapter.TeamMarkChildrenCheckCampaignHolder> {

    private ArrayList<TeamMarkChildrenCheckCampaignItem> teamMarkChildrenCheckCampaignItemArrayList;
    private Context mContext;

    class TeamMarkChildrenCheckCampaignHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView campaign_no, campaign_id, campaign_start_date, campaign_end_date;

        public TeamMarkChildrenCheckCampaignHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            campaign_no = itemView.findViewById(R.id.campaign_no);
            campaign_id = itemView.findViewById(R.id.campaign_id);
            campaign_start_date = itemView.findViewById(R.id.campaign_start_date);
            campaign_end_date = itemView.findViewById(R.id.campaign_end_date);
        }
    }

    public TeamMarkChildrenCheckCampaignAdapter(Context context, ArrayList<TeamMarkChildrenCheckCampaignItem> teamMarkChildrenCheckCampaignItems) {
        teamMarkChildrenCheckCampaignItemArrayList = teamMarkChildrenCheckCampaignItems;
        mContext = context;
    }

    @NonNull
    @Override
    public TeamMarkChildrenCheckCampaignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_mark_children_check_campaign_list, viewGroup, false);
        TeamMarkChildrenCheckCampaignHolder TCvh = new TeamMarkChildrenCheckCampaignHolder(v);
        return TCvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMarkChildrenCheckCampaignHolder teamMarkChildrenCheckCampaignHolder, final int i) {

        TeamMarkChildrenCheckCampaignItem currentitem = teamMarkChildrenCheckCampaignItemArrayList.get(i);

        int no = currentitem.getCampaign_no();
        teamMarkChildrenCheckCampaignHolder.campaign_no.setText(String.valueOf(no));
        teamMarkChildrenCheckCampaignHolder.campaign_id.setText(currentitem.getCampaign_id());
        teamMarkChildrenCheckCampaignHolder.campaign_start_date.setText(currentitem.getCampaign_star_date());
        teamMarkChildrenCheckCampaignHolder.campaign_end_date.setText(currentitem.getCampaign_end_date());

        teamMarkChildrenCheckCampaignHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + teamMarkChildrenCheckCampaignItemArrayList.get(i));
                String campaign_key = teamMarkChildrenCheckCampaignItemArrayList.get(i).getCampaign_key();
                String campaign_id = teamMarkChildrenCheckCampaignItemArrayList.get(i).getCampaign_id();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, TeamMarkChildrenSearch.class);
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
        return teamMarkChildrenCheckCampaignItemArrayList.size();
    }
}

