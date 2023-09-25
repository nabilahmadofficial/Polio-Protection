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

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ChildrenViewHolder> {

    private ArrayList<ChildrenItem> childrenItemArrayList;
    private Context mContext;

    class ChildrenViewHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView children_no, children_name;

        public ChildrenViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            children_no = itemView.findViewById(R.id.children_no);
            children_name = itemView.findViewById(R.id.children_name);
        }
    }

    public ChildrenAdapter(Context context, ArrayList<ChildrenItem> childrenItems) {
        childrenItemArrayList = childrenItems;
        mContext = context;
    }

    @NonNull
    @Override
    public ChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_children_list, viewGroup, false);
        ChildrenViewHolder Cvh = new ChildrenViewHolder(v);
        return Cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildrenViewHolder childrenViewHolder, final int i) {

        ChildrenItem currentitem = childrenItemArrayList.get(i);

        int id = currentitem.getChildren_no();
        childrenViewHolder.children_no.setText(String.valueOf(id));
        childrenViewHolder.children_name.setText(currentitem.getChildren_name());

        childrenViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked on: " + childrenItemArrayList.get(i));
                String Father_Cnic = childrenItemArrayList.get(i).getFather_cnic();
                String Children_key = childrenItemArrayList.get(i).getChildren_key();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(mContext, AdminChildrenInfo.class);
                    intent.putExtra("Father_Cnic", Father_Cnic);
                    intent.putExtra("Children_key", Children_key);
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, "No Internet Connection !!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return childrenItemArrayList.size();
    }
}


