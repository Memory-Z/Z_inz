package com.inz.z;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inz.z.other_module.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/2 9:48.
 */
public class AddressBookRvAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AddressBookPinyin> bookPinyinList;

    public AddressBookRvAdapter(Context mContext) {
        this.mContext = mContext;
        bookPinyinList = new ArrayList<>();
    }

    public AddressBookRvAdapter(Context mContext, List<AddressBookPinyin> bookPinyinList) {
        this.mContext = mContext;
        this.bookPinyinList = bookPinyinList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_address_book, viewGroup, false);
        return new AddressBookRvHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof AddressBookRvHolderView) {
            AddressBookPinyin bookPinyin = bookPinyinList.get(i);
            AddressBookRvHolderView view = (AddressBookRvHolderView) viewHolder;
            String imageUrl = bookPinyin.getUserPhoto();
            if (imageUrl == null || "".equals(imageUrl)) {
                Glide.with(mContext).load(R.drawable.ic_vd_user).into(view.userPhotoCiv);
            } else {
                Glide.with(mContext).load(imageUrl).into(view.userPhotoCiv);
            }
            view.userName.setText(bookPinyin.getUserName());
        }
    }

    @Override
    public int getItemCount() {
        return bookPinyinList.size();
    }


    public void loadMoreUser(List<AddressBookPinyin> list) {
        if (list != null) {
            bookPinyinList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public class AddressBookRvHolderView extends RecyclerView.ViewHolder {
        private CircleImageView userPhotoCiv;
        private TextView userName;

        public AddressBookRvHolderView(@NonNull View itemView) {
            super(itemView);
            userPhotoCiv = itemView.findViewById(R.id.item_address_book_civ);
            userName = itemView.findViewById(R.id.item_address_book_name_tv);
        }
    }
}
