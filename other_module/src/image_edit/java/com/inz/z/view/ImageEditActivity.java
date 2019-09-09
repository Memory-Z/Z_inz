package com.inz.z.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.inz.z.other_module.R;

/**
 * 图片编辑界面
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/05 11:34.
 */
@Route(path = "/other/imageEdit", group = "other")
public class ImageEditActivity extends AppCompatActivity {
    private static final String TAG = "ImageEditActivity";

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.activity_edit_fl, new EditDialogFragment(), "EditDialogFragment").commit();
        transaction.replace(R.id.activity_edit_fl, new ReadImageFragment(), "ReadImageFragment").commit();

    }


}
