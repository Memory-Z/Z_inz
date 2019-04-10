package com.inz.z.book_2.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.inz.z.book_2.AddressBook2Pinyin;
import com.inz.z.other_module.R;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/10 10:13.
 */
public class AddressBook2Activity extends AppCompatActivity {

    private Context mContext;
    private List<AddressBook2Pinyin> userList;
    private IndexBar indexBar;
    private RecyclerView leftRv, rightRv;
    private AddressBook2RvAdapter addressBook2RvAdapter;
    private AddressBook2DepAdapter addressBook2DepAdapter;


    private LinearLayout topLl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book2);
        mContext = this;
        initView();
        initData();

    }

    private void initView() {
        indexBar = findViewById(R.id.activity_address_book2_content_right_index_bar);
        leftRv = findViewById(R.id.activity_address_book2_content_left_rv);
        rightRv = findViewById(R.id.activity_address_book2_content_right_content_rv);
        TextView hintTv = findViewById(R.id.activity_address_book2_right_hint_tv);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rightRv.setLayoutManager(manager);
        indexBar.setmPressedShowTextView(hintTv)
                .setNeedRealIndex(true)
                .setmLayoutManager(manager);
        leftRv.setLayoutManager(new LinearLayoutManager(mContext));
        addressBook2DepAdapter = new AddressBook2DepAdapter(mContext);
        leftRv.setAdapter(addressBook2DepAdapter);
        userList = new ArrayList<>();
//        addressBook2Decoration = new AddressBook2Decoration(mContext, userList);
//        rightRv.addItemDecoration(addressBook2Decoration);
        rightRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        addressBook2RvAdapter = new AddressBook2RvAdapter(mContext);
        rightRv.setAdapter(addressBook2RvAdapter);

        TextView searchTv = findViewById(R.id.activity_address_book2_search_tv);
        searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressBook2SearchDialogFragment dialogFragment = new AddressBook2SearchDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "AddressBook2SearchDialogFragment");
            }
        });
    }

    private void initData() {
        refreshUserList("json/all.json");
        setDepList();
    }

    private void setDepList() {
        String[] array = getResources().getStringArray(R.array.address_book2_dep);
        List<String> depList = new ArrayList<>(Arrays.asList(array));
        addressBook2DepAdapter.refreshData(depList);
    }

    private void refreshUserList(String fileName) {
        String userListStr = getJsonStr(fileName, mContext);
        userList = JSONArray.parseArray(userListStr, AddressBook2Pinyin.class);
        if (userList == null) {
            return;
        }
        indexBar.getDataHelper().sortSourceDatas(userList);
        addressBook2RvAdapter.refreshUserList(userList);
        indexBar.setmSourceDatas(userList).invalidate();
//        addressBook2Decoration.setmDatas(userList);

    }


    private String getJsonStr(String fileName, Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager manager = context.getAssets();
            BufferedReader br = new BufferedReader(new InputStreamReader(manager.open(fileName)));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
