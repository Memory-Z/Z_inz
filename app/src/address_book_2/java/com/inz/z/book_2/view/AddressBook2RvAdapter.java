package com.inz.z.book_2.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inz.z.R;
import com.inz.z.book_2.AddressBook2Pinyin;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/2 9:48.
 */
public class AddressBook2RvAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AddressBook2Pinyin> bookPinyinList;

    public AddressBook2RvAdapter(Context mContext) {
        this.mContext = mContext;
        bookPinyinList = new ArrayList<>();
    }

    public AddressBook2RvAdapter(Context mContext, List<AddressBook2Pinyin> bookPinyinList) {
        this.mContext = mContext;
        this.bookPinyinList = bookPinyinList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_address_book2, viewGroup, false);
        return new AddressBookRvHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof AddressBookRvHolderView) {
            AddressBook2Pinyin bookPinyin = bookPinyinList.get(i);
            AddressBookRvHolderView view = (AddressBookRvHolderView) viewHolder;
            String imageUrl = bookPinyin.getUserPhoto();
            if (imageUrl == null || "".equals(imageUrl)) {
                Glide.with(mContext).load(R.drawable.ic_vd_user).into(view.userPhotoCiv);
            } else {
                Glide.with(mContext).load(imageUrl).into(view.userPhotoCiv);
            }
            view.userNameTv.setText(bookPinyin.getUserName());
            view.roomNumTv.setText(String.format(mContext.getString(R.string.address_book2_room), bookPinyin.getRoomNum()));
            view.userPhoneTv.setText(bookPinyin.getUserPhone());
            view.departmentTv.setText(bookPinyin.getDepartment());
        }
    }

    @Override
    public int getItemCount() {
        return bookPinyinList.size();
    }


    public void loadMoreUser(List<AddressBook2Pinyin> list) {
        if (list != null) {
            bookPinyinList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 刷新
     *
     * @param list 用户列表
     */
    public void refreshUserList(List<AddressBook2Pinyin> list) {
        this.bookPinyinList.clear();
        this.bookPinyinList.addAll(list);
        notifyDataSetChanged();
    }

    public class AddressBookRvHolderView extends RecyclerView.ViewHolder {
        private CircleImageView userPhotoCiv;
        private TextView userNameTv;
        private TextView departmentTv;
        private TextView userPhoneTv;
        private TextView roomNumTv;

        public AddressBookRvHolderView(@NonNull View itemView) {
            super(itemView);
            userPhotoCiv = itemView.findViewById(R.id.item_address_book2_civ);
            userNameTv = itemView.findViewById(R.id.item_address_book2_right_top_name_tv);
            departmentTv = itemView.findViewById(R.id.item_address_book2_right_bottom_dep_tv);
            userPhoneTv = itemView.findViewById(R.id.item_address_book2_right_bottom_phone_tv);
            roomNumTv = itemView.findViewById(R.id.item_address_book2_right_top_room_tv);
        }
    }
}
