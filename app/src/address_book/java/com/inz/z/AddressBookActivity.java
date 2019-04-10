package com.inz.z;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.inz.z.view.activity.AbsBaseActivity;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/2 9:34.
 */
public class AddressBookActivity extends AbsBaseActivity {

    private static final String TAG = "AddressBookActivity";
    //    private Context mContext;
    private List<AddressBookPinyin> addressBookPinyinList = new ArrayList<>();
    private IndexBar indexBar;
    private RecyclerView recyclerView;
    private AddressBookRvAdapter addressBookRvAdapter;
    private AddressBookDecoration suspensionDecoration;

    @Override
    protected void initWindow() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean myOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_address_book);
//        mContext = this;
//        initView();
//    }


    @Override
    protected void initView() {
        indexBar = findViewById(R.id.activity_address_book_right_index_bar);
        recyclerView = findViewById(R.id.activity_address_book_left_rv);
        TextView indexBarHintTv = findViewById(R.id.activity_address_book_hint_tv);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        indexBar.setmPressedShowTextView(indexBarHintTv)
                .setNeedRealIndex(true)
                .setmLayoutManager(manager);
        TextView searchTv = findViewById(R.id.activity_address_book_search_tv);
        searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressSearchDialogFragment dialogFragment = new AddressSearchDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "AddressSearchDialogFragment");
            }
        });

        suspensionDecoration = new AddressBookDecoration(mContext, addressBookPinyinList);
        addressBookRvAdapter = new AddressBookRvAdapter(mContext);
        recyclerView.addItemDecoration(suspensionDecoration);
        recyclerView.setAdapter(addressBookRvAdapter);
        setUserList();
    }

    private String[] photo = new String[]{
            "https://b-ssl.duitang.com/uploads/item/201506/26/20150626160448_GPnjW.jpeg",
            "https://drscdn.500px.org/photo/300179805/q%3D80_m%3D2000/v2?webp=true&sig=77f34e1cbb9a9c265a5e4ab580576f80e52eea1c016e5c296fcb766d89f97c2c",
            "https://drscdn.500px.org/photo/300212847/q%3D80_m%3D2000/v2?webp=true&sig=8761c9eaddd8321d6bd61f15db7b6ffc15149c8a71aa21bc6cfd36e493633f5a",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-759228.png",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-759223.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758823.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758366.png",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758094.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758054.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757836.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757833.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757832.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757738.png",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757645.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757610.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757530.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757480.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757417.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757389.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757355.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757177.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-756834.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-756770.png"
    };

    private void setUserList() {
        int photoSize = photo.length;
        String[] userArray = getResources().getStringArray(R.array.address_book);
        for (int i = 0; i < userArray.length; i++) {
            int j = i % photoSize;
            String userName = userArray[i];
            AddressBookPinyin pinyin = new AddressBookPinyin();
            pinyin.setUserName(userName);
            pinyin.setUserPhoto(photo[j]);
            addressBookPinyinList.add(pinyin);
        }
        indexBar.getDataHelper().sortSourceDatas(addressBookPinyinList);
        addressBookRvAdapter.loadMoreUser(addressBookPinyinList);
        indexBar.setmSourceDatas(addressBookPinyinList).invalidate();
        suspensionDecoration.setmDatas(addressBookPinyinList);
    }
}
