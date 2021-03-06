package com.inz.z.view.fragment;


import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * 抽象 处理框架
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/7/21 20:39
 */
public abstract class AbsBackHandledFragment extends Fragment {
    // 受保护 的接口
    protected BackHandlerInterface backHandlerInterface;

    public abstract String getTagText();

    public abstract Boolean onBackPressed();

    /**
     * 处理接口
     */
    public interface BackHandlerInterface {
        void setSelectedFragment(AbsBackHandledFragment absBackHandledFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!((getActivity()) instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface !");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        backHandlerInterface.setSelectedFragment(this);
    }
}
