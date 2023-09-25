package com.Polio.Protection.team.MarkChildren;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Polio.Protection.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TeamMarkChildrenSearchAdapter extends RecyclerView.Adapter<TeamMarkChildrenSearchAdapter.TeamMarkChildrenSearchAdapterViewHolder> {

    private ArrayList<TeamMarkChildrenSearchItem> teamMarkChildrenSearchItems;
    private Context mContext;

    class TeamMarkChildrenSearchAdapterViewHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView children_no, children_name;
        public CheckBox children_status;

        public TeamMarkChildrenSearchAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            children_no = itemView.findViewById(R.id.children_no);
            children_name = itemView.findViewById(R.id.children_name);
            children_status = itemView.findViewById(R.id.children_status);

        }
    }

    public TeamMarkChildrenSearchAdapter(Context context, ArrayList<TeamMarkChildrenSearchItem> childrenItems) {
        teamMarkChildrenSearchItems = childrenItems;
        mContext = context;
    }

    @NonNull
    @Override
    public TeamMarkChildrenSearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_mark_children_search_list, viewGroup, false);
        TeamMarkChildrenSearchAdapterViewHolder TCvh = new TeamMarkChildrenSearchAdapterViewHolder(v);
        return TCvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamMarkChildrenSearchAdapterViewHolder teamMarkChildrenSearchAdapterViewHolder, final int i) {

        TeamMarkChildrenSearchItem currentitem = teamMarkChildrenSearchItems.get(i);
        final String children_status;

        int id = currentitem.getChildren_no();
        teamMarkChildrenSearchAdapterViewHolder.children_no.setText(String.valueOf(id));
        teamMarkChildrenSearchAdapterViewHolder.children_name.setText(currentitem.getChildren_name());
        children_status = currentitem.getCheck();
        if (children_status.equals("yes")) {
            teamMarkChildrenSearchAdapterViewHolder.children_status.setChecked(true);
        } else if (children_status.equals("no")) {
            teamMarkChildrenSearchAdapterViewHolder.children_status.setChecked(false);
        }

        teamMarkChildrenSearchAdapterViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (children_status.equals("yes")) {

                    Toast.makeText(mContext, "Children Already Mark !!!", Toast.LENGTH_LONG).show();

                } else if (children_status.equals("no")) {

                    Log.d(TAG, "onClick: clicked on: " + teamMarkChildrenSearchItems.get(i));
                    String campaign_key = teamMarkChildrenSearchItems.get(i).getCampaign_key();
                    String campaign_id = teamMarkChildrenSearchItems.get(i).getCampaign_id();
                    String father_cnic = teamMarkChildrenSearchItems.get(i).getFather_cnic();
                    String children_key = teamMarkChildrenSearchItems.get(i).getChildren_key();

                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        Intent intent = new Intent(mContext, TeamMarkChildrenMark.class);
                        intent.putExtra("campaign_key", campaign_key);
                        intent.putExtra("campaign_id", campaign_id);
                        intent.putExtra("father_cnic", father_cnic);
                        intent.putExtra("children_key", children_key);
                        mContext.startActivity(intent);
                    } else {

                        Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return teamMarkChildrenSearchItems.size();
    }
}

