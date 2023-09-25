package com.Polio.Protection.admin.Worker;

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

public class AdminWorkerAdapter extends RecyclerView.Adapter<AdminWorkerAdapter.AdminWorkerHolder> {

    private ArrayList<AdminWorkerItem> adminWorkerItemArrayList;
    private Context mContext;

    class AdminWorkerHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView worker_no, worker_name, worker_gender;

        public AdminWorkerHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            worker_no = itemView.findViewById(R.id.worker_no);
            worker_name = itemView.findViewById(R.id.worker_name);
            worker_gender = itemView.findViewById(R.id.worker_gender);
        }
    }

    public AdminWorkerAdapter(Context context, ArrayList<AdminWorkerItem> workerItems) {
        adminWorkerItemArrayList = workerItems;
        mContext = context;
    }

    @NonNull
    @Override
    public AdminWorkerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_worker_list, viewGroup, false);
        AdminWorkerHolder Avh = new AdminWorkerHolder(v);
        return Avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminWorkerHolder adminWorkerHolder, final int i) {

        AdminWorkerItem currentitem = adminWorkerItemArrayList.get(i);

        int no = currentitem.getWorker_no();
        adminWorkerHolder.worker_no.setText(String.valueOf(no));
        adminWorkerHolder.worker_name.setText(currentitem.getWorker_name());
        adminWorkerHolder.worker_gender.setText(currentitem.getWorker_gender());

        adminWorkerHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + adminWorkerItemArrayList.get(i));
                String worker_key = adminWorkerItemArrayList.get(i).getWorker_key();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, AdminAddUpdateWorker.class);
                    intent.putExtra("worker_key", worker_key);
                    intent.putExtra("Action", "Update_Delete_Worker");
                    mContext.startActivity(intent);
                } else {

                    Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminWorkerItemArrayList.size();
    }
}

