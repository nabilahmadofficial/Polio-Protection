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

public class AdminViewResultChildrenAdapter extends RecyclerView.Adapter<AdminViewResultChildrenAdapter.AdminViewResultChildrenHolder> {

    private ArrayList<AdminViewResultChildrenItem> adminViewResultChildrenItemArrayList;
    private Context mContext;

    class AdminViewResultChildrenHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView children_no, children_name, mark_date, mark_time;

        public AdminViewResultChildrenHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            children_no = itemView.findViewById(R.id.children_no);
            children_name = itemView.findViewById(R.id.children_name);
            mark_date = itemView.findViewById(R.id.mark_date);
            mark_time = itemView.findViewById(R.id.time_mark);
        }
    }

    public AdminViewResultChildrenAdapter(Context context, ArrayList<AdminViewResultChildrenItem> adminViewResultChildrenItems) {
        adminViewResultChildrenItemArrayList = adminViewResultChildrenItems;
        mContext = context;
    }

    @NonNull
    @Override
    public AdminViewResultChildrenHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_view_result_children_list, viewGroup, false);
        AdminViewResultChildrenHolder TCRvh = new AdminViewResultChildrenHolder(v);
        return TCRvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewResultChildrenHolder adminViewResultChildrenHolder, final int i) {

        AdminViewResultChildrenItem currentitem = adminViewResultChildrenItemArrayList.get(i);

        int no = currentitem.getChildren_no();
        adminViewResultChildrenHolder.children_no.setText(String.valueOf(no));
        adminViewResultChildrenHolder.children_name.setText(currentitem.getChildren_name());
        adminViewResultChildrenHolder.mark_date.setText(currentitem.getMark_date());
        adminViewResultChildrenHolder.mark_time.setText(currentitem.getMark_time());

        adminViewResultChildrenHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + adminViewResultChildrenItemArrayList.get(i));
                String children_key = adminViewResultChildrenItemArrayList.get(i).getChildren_key();
                String father_cnic = adminViewResultChildrenItemArrayList.get(i).getFather_cnic();
                String campaign_key = adminViewResultChildrenItemArrayList.get(i).getCampaign_key();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, AdminViewResultChildrenVaccination.class);
                    intent.putExtra("children_key", children_key);
                    intent.putExtra("father_cnic", father_cnic);
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
        return adminViewResultChildrenItemArrayList.size();
    }
}

