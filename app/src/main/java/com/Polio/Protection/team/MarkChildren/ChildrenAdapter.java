package com.Polio.Protection.team.MarkChildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Polio.Protection.R;

import java.util.ArrayList;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ChildrenViewHolder> {

    private ArrayList<ChildrenItem> childrenAdapterArrayList;
    private Context mContext;

    public static class ChildrenViewHolder extends RecyclerView.ViewHolder {

        public CardView parent;
        public TextView children_no, children_name;
        public CheckBox children_status;

        public ChildrenViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            children_no = itemView.findViewById(R.id.children_no);
            children_name = itemView.findViewById(R.id.children_name);
            children_status = itemView.findViewById(R.id.children_status);

        }
    }
    public ChildrenAdapter(Context context,ArrayList<ChildrenItem> childrenItems)
    {
        childrenAdapterArrayList = childrenItems;
        mContext = context;
    }
    @NonNull
    @Override
    public ChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_children, viewGroup, false);
        ChildrenViewHolder Cvh = new ChildrenViewHolder(v);
        return Cvh;
    }
    @Override
    public void onBindViewHolder(@NonNull ChildrenViewHolder childrenViewHolder, final int i) {

        ChildrenItem currentitem = childrenAdapterArrayList.get(i);
        Boolean children_status;
        String id;

        childrenViewHolder.children_no.setText(currentitem.getChildren_no());
        childrenViewHolder.children_name.setText(currentitem.getChildren_name());

        children_status = currentitem.getCheck();
        if(children_status == true) {
            childrenViewHolder.children_status.setChecked(true);
        } else {
            childrenViewHolder.children_status.setChecked(false);
        }

        id = String.valueOf(childrenAdapterArrayList.get(i).getChildren_id());
    }

    @Override
    public int getItemCount() {
        return childrenAdapterArrayList.size();
    }
}
