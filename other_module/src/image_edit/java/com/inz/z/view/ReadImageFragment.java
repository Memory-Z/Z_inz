package com.inz.z.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inz.z.other_module.R;

import java.util.Arrays;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/10 11:58.
 */
public class ReadImageFragment extends Fragment {
    private static final String TAG = "ReadImageFragment";

    private Context mContext;
    private View mView;
    private RecyclerView readImageRv;
    private LinearLayoutManager linearLayoutManager;
    private ReadImageRecyclerAdapter readImageRecyclerAdapter;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.read_fragment, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mContext = getContext();
        initView();
        initData();
    }


    private void initView() {
        readImageRv = mView.findViewById(R.id.image_read_rv);
        linearLayoutManager = new LinearLayoutManager(mContext);
        readImageRv.setLayoutManager(linearLayoutManager);
        readImageRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        readImageRecyclerAdapter = new ReadImageRecyclerAdapter(mContext);
        readImageRecyclerAdapter.setImageListener(new ReadImageListenerImpl());
        readImageRv.setAdapter(readImageRecyclerAdapter);
        readImageRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isDragging = false;
            private float allDy = 0;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(TAG, "onScrollStateChanged: newState = " + newState + " ; isDragging = " + isDragging + " ; AllDy = " + allDy);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (isDragging) {
//                            int position = linearLayoutManager.findFirstVisibleItemPosition();
//                            int p = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//                            int count = readImageRecyclerAdapter.getItemCount();
//                            Log.i(TAG, "onScrollStateChanged: position = " + position + " ; p = " + p + " ; count = " + count);
//                            if (p > 0 && p < count) {
//                                recyclerView.smoothScrollToPosition(p);
//                            } else {
//                                int height = ReadImageFragment.this.mView.getMeasuredHeight();
//
//                                boolean isChange = Math.abs(allDy) >= (float) height / 2;
//                                if (isChange) {
//                                    if (allDy < 0) {
//                                        // 下滑，显示上部
//                                        recyclerView.smoothScrollToPosition(position > 0 ? position - 1 : position);
//                                    } else if (allDy > 0) {
//                                        // 上滑，显示下部
//                                        recyclerView.smoothScrollToPosition(position < count ? position + 1 : count);
//                                    }
//                                } else {
//                                    // 回到原来位置
//                                    recyclerView.smoothScrollToPosition(position);
//                                }
//                            }
                            isDragging = false;
                            allDy = 0;
                        }
                        // 静止
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (!isDragging) {
                            isDragging = true;
                        }
                        // 拖动中
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        // 自动滚动中
                        break;
                }


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                allDy += dy;
                Log.i(TAG, "onScrolled: dx = " + dx + " ; dy = " + dy);

            }
        });
    }

    private String[] srcArray = {
            "https://wallions.com/uploads/post-thumbs/w1920/279620.jpg",
            "https://wallions.com/uploads/post-thumbs/w1920/276672.jpg",
            "https://wallions.com/uploads/post-thumbs/w1920/279620.jpg",
            "https://wallions.com/uploads/post-thumbs/w1920/278520.jpg",
            "https://wallions.com/uploads/post-thumbs/w1920/275582.jpg",
            "https://wallions.com/uploads/post-thumbs/w1920/285944.jpg",
            "https://wallions.com/uploads/post-thumbs/w1920/274871.jpg",
            "https://wallions.com/uploads/post-thumbs/w1920/279497.jpg",
            "https://w.wallhaven.cc/full/dg/wallhaven-dge9ll.jpg"
    };

    private List<String> strings;

    private void initData() {
        strings = Arrays.asList(srcArray);
        readImageRecyclerAdapter.refreshImageList(strings);
    }

    private class ReadImageListenerImpl implements ReadImageRecyclerAdapter.ReadImageListener {
        @Override
        public void onPageClick(View view, int position) {
            FragmentManager manager = getChildFragmentManager();
            EditDialogFragment dialogFragment = new EditDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("imageSrc", strings.get(position));
            bundle.putInt("imagePosition", position);
            dialogFragment.setArguments(bundle);
            dialogFragment.setEditImageDialogListener(editImageDialogListener = new EditImageListenerImpl());
            dialogFragment.show(manager, "EditDialogFragment");
        }
    }

    private EditDialogFragment.EditImageDialogListener editImageDialogListener;

    private class EditImageListenerImpl implements EditDialogFragment.EditImageDialogListener {
        @Override
        public void onPictureSave(int position, String picLocalPath) {
            if (readImageRecyclerAdapter != null) {
                readImageRecyclerAdapter.replaceImage(position, picLocalPath);
            }
        }

        @Override
        public void onDialogClose() {
            editImageDialogListener = null;
            DialogFragment dialogFragment = (DialogFragment) getChildFragmentManager().findFragmentByTag("EditDialogFragment");
            if (dialogFragment != null) {
                dialogFragment.dismissAllowingStateLoss();
            }
        }
    }
}