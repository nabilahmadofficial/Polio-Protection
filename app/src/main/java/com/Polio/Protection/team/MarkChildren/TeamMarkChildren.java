package com.Polio.Protection.team.MarkChildren;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Polio.Protection.R;
import com.Polio.Protection.team.MarkChildren.ChildrenAdapter;
import com.Polio.Protection.team.MarkChildren.ChildrenItem;

import java.util.ArrayList;

public class TeamMarkChildren extends AppCompatActivity {

    ImageView backarrow;
    EditText id_card_num;
    Button search_id_card;

    String father_card_num;


    RecyclerView.Adapter Childrenadapter;
    RecyclerView ChildrenrecyclerView;
    RecyclerView.LayoutManager ChildrenlayoutManager;
    final ArrayList<ChildrenItem> childrenItemArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_mark_children);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        id_card_num = findViewById(R.id.ic_card_number);
        search_id_card = findViewById(R.id.search_id_card_btn);

        search_id_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                childrenItemArrayList.clear();
                ChlidrenList();
                father_card_num = id_card_num.getText().toString().trim();

                if (father_card_num.isEmpty() || father_card_num.length() < 13) {
                    id_card_num.setError("Id Card Number cannot be less than 13 characters!");
                    id_card_num.requestFocus();
                    return;
                }

            }
        });

    }
    public void ChlidrenList()
    {

        boolean a = true;
        boolean b = false;

        childrenItemArrayList.add(new ChildrenItem("","1","Aliraza",a));
        childrenItemArrayList.add(new ChildrenItem("","1","Aliraza",b));
        childrenItemArrayList.add(new ChildrenItem("","1","Aliraza",a));
        childrenItemArrayList.add(new ChildrenItem("","1","Aliraza",b));
        childrenItemArrayList.add(new ChildrenItem("","1","Aliraza",a));

        ChildrenrecyclerView = findViewById(R.id.recyclerview_children);
        ChildrenlayoutManager = new LinearLayoutManager(getApplicationContext());
        ChildrenrecyclerView.setLayoutManager(ChildrenlayoutManager);
        Childrenadapter = new ChildrenAdapter(getApplicationContext(), childrenItemArrayList);
        ChildrenrecyclerView.setAdapter(Childrenadapter);
        ChildrenrecyclerView.setHasFixedSize(true);
        ChildrenrecyclerView.setItemViewCacheSize(20);
        ChildrenrecyclerView.setDrawingCacheEnabled(true);

    }
}
