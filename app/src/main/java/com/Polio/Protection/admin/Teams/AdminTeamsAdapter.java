package com.Polio.Protection.admin.Teams;

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

public class AdminTeamsAdapter extends RecyclerView.Adapter<AdminTeamsAdapter.AdminTeamHolder> {

    private ArrayList<AdminTeamsItem> adminTeamsItemArrayList;
    private Context mContext;

    class AdminTeamHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView team_no, team_id;

        public AdminTeamHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            team_no = itemView.findViewById(R.id.team_no);
            team_id = itemView.findViewById(R.id.team_id);
        }
    }

    public AdminTeamsAdapter(Context context, ArrayList<AdminTeamsItem> teamsItems) {
        adminTeamsItemArrayList = teamsItems;
        mContext = context;
    }

    @NonNull
    @Override
    public AdminTeamHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_teams_list, viewGroup, false);
        AdminTeamHolder Tvh = new AdminTeamHolder(v);
        return Tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTeamHolder adminTeamHolder, final int i) {

        AdminTeamsItem currentitem = adminTeamsItemArrayList.get(i);

        int no = currentitem.getTeam_no();
        adminTeamHolder.team_no.setText(String.valueOf(no));
        adminTeamHolder.team_id.setText(currentitem.getTeam_id());

        adminTeamHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + adminTeamsItemArrayList.get(i));
                String team_key = adminTeamsItemArrayList.get(i).getTeam_key();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, AdminAddUpdateTeams.class);
                    intent.putExtra("teams_key", team_key);
                    intent.putExtra("Action", "Update_Delete_Team");
                    mContext.startActivity(intent);
                } else {

                    Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminTeamsItemArrayList.size();
    }
}


