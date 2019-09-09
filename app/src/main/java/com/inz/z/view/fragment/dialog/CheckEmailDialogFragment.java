package com.inz.z.view.fragment.dialog;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.inz.z.R;
import com.inz.z.util.AppBaseTools;

/**
 * 监测邮箱弹窗
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/29 11:39.
 */
public class CheckEmailDialogFragment extends AbsBaseDialogFragment {

    private EditText userEmailEt, emailCodeEt;
    private Button sendCodeBtn, nextBtn;
    private FragmentRegisterInterface fragmentRegisterInterface;

    public interface FragmentRegisterInterface {
        void setUserEmail(String userEmail);

        void setEmailCode(String emailCode);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = R.style.AppTheme_BottomDialog;
        setStyle(DialogFragment.STYLE_NO_FRAME, theme);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(mContext).inflate(R.layout.fragment_register_email, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.transparent);
        }
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        userEmailEt = mView.findViewById(R.id.fragment_register_email_et);
        emailCodeEt = mView.findViewById(R.id.fragment_register_code_et);
        sendCodeBtn = mView.findViewById(R.id.fragment_register_get_code_btn);
        nextBtn = mView.findViewById(R.id.fragment_register_next_btn);
        // 默认不可点击
        nextBtn.setFocusable(false);
        nextBtn.setClickable(false);
        ImageButton closeIBtn = mView.findViewById(R.id.fragment_register_close_ibtn);
        closeIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailDialogFragment.this.dismiss();
            }
        });
    }

    @Override
    public void initData() {
        // 发送按钮点击事件
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentRegisterInterface != null) {
                    String userEmail = userEmailEt.getText().toString();
                    if (!"".equals(userEmail)) {
                        fragmentRegisterInterface.setUserEmail(userEmail);
                        sendCodeBtn.setText("发送中...");
                        sendCodeBtn.setClickable(false);
                        sendCodeBtn.setFocusable(false);
                        // 开始倒计时 90s
                        CheckEmailDialogFragment.this.startCountDown(90000);
                    } else {
                        AppBaseTools.showShortCenterToast(mContext, "邮箱不能为空");
                    }
                }
            }
        });
        userEmailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    sendCodeBtn.setClickable(true);
                    sendCodeBtn.setFocusable(true);
                } else {
                    sendCodeBtn.setClickable(false);
                    sendCodeBtn.setFocusable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    nextBtn.setClickable(true);
                    nextBtn.setFocusable(true);
                } else {
                    nextBtn.setFocusable(false);
                    nextBtn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentRegisterInterface != null) {
                    String emailCode = emailCodeEt.getText().toString();
                    if (!"".equals(emailCode)) {
                        fragmentRegisterInterface.setEmailCode(emailCode);
                    }
                }
            }
        });
    }

    public void setFragmentRegisterInterface(FragmentRegisterInterface fragmentRegisterInterface) {
        this.fragmentRegisterInterface = fragmentRegisterInterface;
    }

    private static CountDownTimer countDownTimer;

    /**
     * 开始倒计时
     *
     * @param millisInFuture 时长 单位 ms
     */
    private void startCountDown(long millisInFuture) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished > 999) {
                    String timeStr = (millisUntilFinished / 1000) + "s";
                    sendCodeBtn.setText(timeStr);
                } else {
                    sendCodeBtn.setText(R.string.get_email_code);
                    sendCodeBtn.setFocusable(true);
                    sendCodeBtn.setClickable(true);
                }
            }

            @Override
            public void onFinish() {
                sendCodeBtn.setText(R.string.get_email_code);
                sendCodeBtn.setFocusable(true);
                sendCodeBtn.setClickable(true);
            }
        };
        countDownTimer.start();
    }
}
