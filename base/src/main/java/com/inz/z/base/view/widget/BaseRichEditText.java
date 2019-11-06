package com.inz.z.base.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.inz.z.base.R;
import com.inz.z.base.util.L;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 富文本编辑
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/06 13:14.
 */
public class BaseRichEditText extends AppCompatEditText {
    private static final String TAG = "BaseRichEditText";

    private StringBuilder contentSb = new StringBuilder();
    private List<ContentTextBean> contentStrList = new LinkedList<>();
    private Handler mRichEditTextHandler;

    private class ContentTextBean {
        /**
         * 排序位置
         */
        int indexParagraph;
        /**
         * 内容
         */
        String content;
        /**
         * 图片连接
         */
        String imageSrc;
        /**
         * 内容样式
         */
        List<TextStyle> textStyleList;

    }

    private class TextStyle {
        /**
         * 内部开始位置
         */
        int start;
        /**
         * 内部结束位置
         */
        int end;
        /**
         * 开始标志位
         */
        String startFlag;
        /**
         * 结束标志位
         */
        String endFlag = " ";
        /**
         * 替换类型
         */
        CharacterStyle style;
    }

    public BaseRichEditText(Context context) {
        this(context, null);
    }

    public BaseRichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParameter();
    }

    public BaseRichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParameter();
    }

    private void initParameter() {
        mRichEditTextHandler = new Handler(Looper.getMainLooper());

    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        L.i(TAG, "onTextChanged: -------------- " + start + " -- " + text + " --- b : " + lengthBefore + " ---a: " + lengthAfter);
        checkTextChanged(text.toString());
    }


    @Override
    public void append(CharSequence text, int start, int end) {
        super.append(text, start, end);
        L.i(TAG, "append: -----------------");
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.EDITABLE);
        L.i(TAG, "setText ------------ " + text);
    }

    /**
     * 就数据内容
     */
    private String oldStr = "";
    /**
     * 段落列表
     */
    private LinkedList<String> oldContentList = new LinkedList<>();

    /**
     * 是否有内容改变
     */
    private void checkTextChanged(String newTextStr) {
        if (oldStr != null && !oldStr.equals(newTextStr)) {
            oldStr = newTextStr;
            // 不相等的时候. 设置 每段样式
            SpannableString spannableString = new SpannableString(newTextStr);
            // 分段保存
            String[] paragraphs = newTextStr.split("\n");
            if (oldContentList == null) {
                oldContentList = new LinkedList<>();
            }
            oldContentList.clear();
            // 开始编号
            int startIndex = 0;
            for (String p : paragraphs) {
                setTextColorSpanWithAt(spannableString, newTextStr, p, startIndex);
                // + 1 "\n"  占用 1个字符 位置，导致位置偏移
                startIndex += p.length() + 1;
                oldContentList.add(p);
            }
            int selectedEnd = getSelectionEnd();
            setText(spannableString, BufferType.EDITABLE);
            setSelection(selectedEnd);
            setMovementMethod(LinkMovementMethod.getInstance());
            setHintTextColor(Color.TRANSPARENT);
        }
    }

    /**
     * 设置显示样式
     *
     * @param spannableString
     * @param allContent
     * @param content
     * @param start
     */
    private void setTextColorSpanWithAt(SpannableString spannableString, String allContent, String content, int start) {
        L.i(TAG, "setTextColorSpanWithAt: all = " + allContent + " ---content = " + content + "-- Start = " + start);
        if (content.isEmpty()) {
            if (start == 0) {
                content = allContent;
            } else {
                return;
            }
        }
        int charIndex = content.indexOf("@");
        if (charIndex == -1) {
            return;
        }
        String atString = content.substring(charIndex);
        int s = start + charIndex;
        int backIndex = atString.indexOf("  ");
        if (backIndex < 1) {
            if (backIndex == -1) {
                return;
            }
            setTextColorSpanWithAt(spannableString, allContent, atString, s);
            return;
        }
        int end = s + backIndex;
        L.i(TAG, "setTextColorSpanWithAt: start = " + s + " -- charIndex = " + charIndex + " --- backIndex = " + backIndex + " --- end = " + end);
        if (end - s > 1) {
            L.i(TAG, "setTextColorSpanWithAt: gt 1. ");
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#4CD2FF"));
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {

                    mRichEditTextHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "---", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            spannableString.setSpan(colorSpan, s, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(backgroundColorSpan, s, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(clickableSpan, s, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        }
        // 如果匹配成功，以成功的结束作为开始
        // 如果匹配失败，以原开始作为开始，
        String otherStr = atString.substring(backIndex);
        setTextColorSpanWithAt(spannableString, allContent, otherStr, end);
    }


}
