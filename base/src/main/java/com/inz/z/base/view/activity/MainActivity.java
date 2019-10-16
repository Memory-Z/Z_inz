package com.inz.z.base.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.inz.z.base.R;
import com.inz.z.base.view.widget.DeleteItemTouchHelperCallbackExt;
import com.inz.z.base.view.widget.DeleteItemTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/29 11:14.
 */
@Route(path = "/base/MainActivity", group = "base")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private BaseMainAdapter mainAdapter;
    private DeleteItemTouchHelperCallbackExt helperCallbackExt;
    private ItemTouchHelper itemTouchHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_main_activity);
        mContext = this;
        recyclerView = findViewById(R.id.base_main_rv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mainAdapter = new BaseMainAdapter(mContext);
        mainAdapter.setItemListener(new BaseMainAdapter.ItemListener() {
            @Override
            public void onStartDrop(RecyclerView.ViewHolder viewHolder) {
                if (itemTouchHelper != null) {
                    itemTouchHelper.startDrag(viewHolder);
                }
            }
        });

        recyclerView.setAdapter(mainAdapter);
        recyclerView.setItemViewCacheSize(2);


        helperCallbackExt = new DeleteItemTouchHelperCallbackExt(new DeleteItemTouchListener() {
            @Override
            public void onSwiped(int position) {
                mainAdapter.swiped(position);
            }

            @Override
            public void onSwitch(int currentPosition, int targetPosition) {
                mainAdapter.switchItem(currentPosition, targetPosition);
            }
        });
//        itemTouchHelper = new ItemTouchHelper(helperCallbackExt);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list.add("Demanded -> " + i);
        }
        mainAdapter.refreshList(list);
    }
}
