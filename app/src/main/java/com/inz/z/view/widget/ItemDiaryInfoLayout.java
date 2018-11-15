package com.inz.z.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.inz.z.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/13 17:27.
 */
public class ItemDiaryInfoLayout extends ConstraintLayout {

    private View mView;
    private Context mContext;

    public ItemDiaryInfoLayout(Context context) {
        this(context, null);
    }

    public ItemDiaryInfoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemDiaryInfoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    private RelativeLayout topRl;
    private CircleImageView userPhotoCiv;
    private CircleImageView userPhotoSignCiv;
    private TextView userNameTv;
    private TextView userSignLl;
    private ImageView userSign0Iv;
    private ImageView userSign1Iv;
    private ImageView userSign2Iv;
    private ImageView userSign3Iv;
    private ImageView[] userSigns;
    private LinearLayout sendInfoLl;
    private TextView sendTimeTv;
    private RelativeLayout topRightRl;
    private ImageView topRightMoreIv;
    private TextView topRightAttentionTv;
    private RelativeLayout contentRl;
    private TextView diaryContentTv;
    private LinearLayout imageLl;


    private void initView() {
        if (mView != null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.item_diary_info, null);
            topRl = mView.findViewById(R.id.item_diary_info_top_rl);
            userPhotoCiv = mView.findViewById(R.id.item_diary_info_user_photo_civ);
            userPhotoSignCiv = mView.findViewById(R.id.item_diary_info_user_photo_sign_civ);
            userNameTv = mView.findViewById(R.id.item_diary_info_user_name_tv);
            userSignLl = mView.findViewById(R.id.item_diary_info_user_sign_ll);
            userSign0Iv = mView.findViewById(R.id.item_diary_info_user_sign_0_iv);
            userSign1Iv = mView.findViewById(R.id.item_diary_info_user_sign_1_iv);
            userSign2Iv = mView.findViewById(R.id.item_diary_info_user_sign_2_iv);
            userSign3Iv = mView.findViewById(R.id.item_diary_info_user_sign_3_iv);
            userSigns = new ImageView[]{userSign0Iv, userSign1Iv, userSign2Iv, userSign3Iv};
            sendInfoLl = mView.findViewById(R.id.item_diary_info_send_info_ll);
            sendTimeTv = mView.findViewById(R.id.item_diary_info_send_time_tv);
            topRightRl = mView.findViewById(R.id.item_diary_info_top_right_rl);
            topRightMoreIv = mView.findViewById(R.id.item_diary_info_top_right_more_iv);
            topRightAttentionTv = mView.findViewById(R.id.item_diary_info_top_right_attention_tv);
            contentRl = mView.findViewById(R.id.item_diary_info_content_rl);
            diaryContentTv = mView.findViewById(R.id.item_diary_info_content_info_tv);
            imageLl = mView.findViewById(R.id.item_diary_info_image_ll);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(mView, layoutParams);
        }
    }

    public void setUserPhotoSrc(String userPhotoSrc) {
        if (userPhotoCiv != null) {
            Glide.with(getContext()).load(userPhotoSrc).into(userPhotoCiv);
        }
    }

    public void setUserPhotoSignSrc(String userPhotoSignSrc) {
        if (userPhotoSignCiv != null) {
            Glide.with(getContext()).load(userPhotoSignSrc).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    userPhotoSignCiv.setVisibility(GONE);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(userPhotoSignCiv);
        }
    }

    public void setUserName(String userName) {
        if (userNameTv != null) {
            userNameTv.setText(userName);
        }
    }

    public void setUserSignSrc(String[] userSignSrc) {
        if (userSigns != null) {
            if (userSignSrc.length > 0) {
                int i = 0;
                for (String singSrc : userSignSrc) {
                    Glide.with(getContext()).load(singSrc).into(userSigns[i]);
                    userSigns[i].setVisibility(VISIBLE);
                    i++;
                }
            }
        }
    }

    public void setSendTime(String sendTime) {
        if (sendTimeTv != null) {
            sendTimeTv.setText(sendTime);
        }
    }

    public static enum RightMode {
        MORE,
        ATTENTION,
        RECOMMEND
    }

    public void setTopRightMode(RightMode rightMode) {
        if (topRightMoreIv != null && topRightAttentionTv != null) {
            switch (rightMode) {
                case MORE:
                    topRightMoreIv.setVisibility(VISIBLE);
                    topRightAttentionTv.setVisibility(GONE);
                    break;
                case ATTENTION:
                case RECOMMEND:
                default:
                    topRightMoreIv.setVisibility(GONE);
                    topRightAttentionTv.setVisibility(VISIBLE);
                    break;
            }
        }
    }

    /**
     * 设置日志 内容
     *
     * @param content 内容
     */
    public void setDiaryContent(String content) {
        if (diaryContentTv != null) {
            diaryContentTv.setText(content);
        }
    }

    /**
     * 添加 日志图片
     *
     * @param imageUrls 图片链接数组
     */
    public void addDiaryImages(String[] imageUrls) {
        if (imageLl != null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int width = imageLl.getMeasuredWidth();
            if (width > 60) {
                float oneWidth = (width - 60) / 3;
                float twoWidth = (width - 40) / 2;
                int size = imageUrls.length;
                if (size == 1) {
                    float thisWidth = oneWidth * 2 + 30;
                    ImageView imageView = newImageView((int) twoWidth, (int) thisWidth, imageUrls[0]);
                    imageLl.addView(imageView, layoutParams);
                } else if (size > 1) {
                    // 四张图
                    if (size == 4) {
                        RelativeLayout layout = null;
                        for (int i = 0; i < size; i++) {
                            if (i == 0 || i == 3) {
                                layout = new RelativeLayout(getContext());
                                imageLl.addView(layout, layoutParams);
                            }
                            ImageView imageView = newImageView((int) oneWidth, (int) oneWidth, imageUrls[i]);
                            layout.addView(imageView, layoutParams);
                        }
                    } else {
                        RelativeLayout layout = null;
                        if (size > 9) {
                            size = 9;
                        }
                        for (int i = 0; i < size; i++) {
                            if (i % 3 == 0) {
                                layout = new RelativeLayout(getContext());
                                imageLl.addView(layout);
                            }
                            String imageUrl = imageUrls[i];
                            ImageView imageView = newImageView((int) oneWidth, (int) oneWidth, imageUrl);
                            layout.addView(imageView, layoutParams);
                        }
                    }
                } else {
                    imageLl.setVisibility(GONE);
                }
            }
        }

    }

    /**
     * 新建图片
     *
     * @param width    宽
     * @param imageUrl 图片链接
     * @return 图片
     */
    private ImageView newImageView(int width, int height, String imageUrl) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = height;
        ImageView imageView = new ImageView(getContext());
        layoutParams.setMargins(8, 8, 8, 8);
        imageView.setLayoutParams(layoutParams);
        imageView.setMinimumWidth(width);
        Glide.with(getContext()).load(imageUrl).into(imageView);
        return imageView;
    }
}
