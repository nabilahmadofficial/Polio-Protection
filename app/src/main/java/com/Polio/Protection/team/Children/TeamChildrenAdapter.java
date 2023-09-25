package com.Polio.Protection.team.Children;

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

public class TeamChildrenAdapter extends RecyclerView.Adapter<TeamChildrenAdapter.TeamChildrenHolder> {

    private ArrayList<TeamChildrenItem> teamChildrenItemArrayList;
    private Context mContext;

    class TeamChildrenHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView children_no, children_name, children_gender;

        public TeamChildrenHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            children_no = itemView.findViewById(R.id.children_no);
            children_name = itemView.findViewById(R.id.children_name);
            children_gender = itemView.findViewById(R.id.children_gender);
        }
    }

    public TeamChildrenAdapter(Context context, ArrayList<TeamChildrenItem> childrenItems) {
        teamChildrenItemArrayList = childrenItems;
        mContext = context;
    }

    @NonNull
    @Override
    public TeamChildrenHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_children_list, viewGroup, false);
        TeamChildrenHolder Tvh = new TeamChildrenHolder(v);
        return Tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamChildrenHolder teamChildrenHolder, final int i) {

        TeamChildrenItem currentitem = teamChildrenItemArrayList.get(i);

        int no = currentitem.getChildren_no();
        teamChildrenHolder.children_no.setText(String.valueOf(no));
        teamChildrenHolder.children_name.setText(currentitem.getChildren_name());
        teamChildrenHolder.children_gender.setText(currentitem.getChildren_gender());

        teamChildrenHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + teamChildrenItemArrayList.get(i));
                String children_key = teamChildrenItemArrayList.get(i).getChildren_key();
                String Father_cnic = teamChildrenItemArrayList.get(i).getFather_id_card();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, TeamAddUpdateChildren.class);
                    intent.putExtra("children_key", children_key);
                    intent.putExtra("Father_cnic", Father_cnic);
                    intent.putExtra("Action", "Update_Delete_Children");
                    mContext.startActivity(intent);
                } else {

                    Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamChildrenItemArrayList.size();
    }
}

