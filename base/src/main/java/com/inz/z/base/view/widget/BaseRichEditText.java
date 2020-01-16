package com.inz.z.base.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.inz.z.base.util.L;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 富文本编辑
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/06 13:14.
 */
public class BaseRichEditText extends AppCompatEditText {
    private static final String TAG = "BaseRichEditText";

    private static final List<String[]> flagMapList = new ArrayList<>(16);

    private static final String SPAN_FLAG_IMAGE_START = "^ImageStart^";
    private static final String SPAN_FLAG_IMAGE_END = "^ImageEnd^";
    private static final String SPAN_FLAG_AT_START = "@";
    private static final String SPAN_FLAG_AT_END = " ";
    private static final String SPAN_FLAG_TOPIC_START = "#";
    private static final String SPAN_FLAG_TOPIC_END = "# ";


    private StringBuilder contentSb = new StringBuilder();
    private List<ContentTextBean> contentStrList = new LinkedList<>();
    private Handler mRichEditTextHandler;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

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

    class FlagStyleBean {

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
        mRichEditTextHandler = new Handler(new RichEditTextHandlerCallback());
        flagMapList.add(new String[]{SPAN_FLAG_AT_START, SPAN_FLAG_AT_END});
        flagMapList.add(new String[]{SPAN_FLAG_TOPIC_START, SPAN_FLAG_TOPIC_END});
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        // start : 改变的位置。lengthBefore: 原长度（改变前长度；如：删除一个字符时，长度为1 ）。lengthAfter: 新长度（改变后长度，如：写入一个字符时，长度为1 ）
        L.i(TAG, "onTextChanged: -------------- " + start + " ----- b : " + lengthBefore + " ---a: " + lengthAfter);
        if (lengthAfter == 0 && lengthBefore != 0) {
            // 新长度为0，长度不为0 ，表明 lengthBefore 的 内容被删除
            try {
                String delStr = oldStr.substring(start, start + lengthBefore);
                L.i(TAG, "onTextChanged: delStr = " + delStr);
            } catch (IndexOutOfBoundsException e) {
                L.e(TAG, "onTextChanged: ", e);
            }
        }
//        checkTextChanged(text.toString());
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        executor.execute(new ChangeTextRunnable(text.toString()));
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

    private SpannableString spannableString;

    /**
     * 是否有内容改变
     */
    private void checkTextChanged(String newTextStr) {
        if (oldStr != null && !oldStr.equals(newTextStr)) {
            oldStr = newTextStr;
            // 不相等的时候. 设置 每段样式
            spannableString = new SpannableString(newTextStr);
            // 分段保存
            String[] paragraphs = newTextStr.split("\n");
            if (oldContentList == null) {
                oldContentList = new LinkedList<>();
            }
            oldContentList.clear();
            for (int i = 0; i < flagMapList.size(); i++) {
                String[] flag = flagMapList.get(i);
                if (flag.length != 2) {
                    continue;
                }
                // 开始编号
                int startIndex = 0;
                for (String p : paragraphs) {
                    setTextColorSpanWithAt(spannableString, newTextStr, p, startIndex, flag[0], flag[1]);
                    // + 1 "\n"  占用 1个字符 位置，导致位置偏移
                    startIndex += p.length() + 1;
                    // 防止多次添加
//                    oldContentList.add(p);
                }
            }
            if (mRichEditTextHandler != null) {
                mRichEditTextHandler.sendEmptyMessage(RICH_EDIT_TEXT_CHANGE);
            } else {
                int selectedEnd = getSelectionEnd();
                setText(spannableString, BufferType.EDITABLE);
                setSelection(selectedEnd);
                setMovementMethod(LinkMovementMethod.getInstance());
                setHintTextColor(Color.TRANSPARENT);
            }
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
    private void setTextColorSpanWithAt(SpannableString spannableString, String allContent, String content, int start, String startFlag, String endFlag) {
        L.i(TAG, "setTextColorSpanWithAt: all = " + allContent + " ---content = " + content + "-- Start = " + start);
        if (content.isEmpty()) {
            if (start == 0) {
                content = allContent;
            } else {
                return;
            }
        }
        int charIndex = content.indexOf(startFlag);
        if (charIndex == -1) {
            return;
        }
        String atString = content.substring(charIndex);
        int s = start + charIndex;
        int backIndex = atString.indexOf(endFlag);
        if (backIndex < 1) {
            if (backIndex == -1) {
                return;
            }
            setTextColorSpanWithAt(spannableString, allContent, atString, s, startFlag, endFlag);
            return;
        }
        int end = s + backIndex + endFlag.length();
        L.i(TAG, "setTextColorSpanWithAt: start = " + s + " -- charIndex = " + charIndex + " --- backIndex = " + backIndex + " --- end = " + end);
        if (end - s > 1) {
            L.i(TAG, "setTextColorSpanWithAt: gt 1. ");
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#4CD2FF"));
            ClickableSpan clickableSpan = new TextClickListener(atString.substring(0, backIndex));
            spannableString.setSpan(colorSpan, s, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(backgroundColorSpan, s, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(clickableSpan, s, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        }
        // 如果匹配成功，以成功的结束作为开始
        // 如果匹配失败，以原开始作为开始，
        String otherStr = atString.substring(backIndex + endFlag.length());
        setTextColorSpanWithAt(spannableString, allContent, otherStr, end, startFlag, endFlag);
    }

    /**
     * 设置内容 Spannable
     *
     * @param start 开始位置
     * @param end   结束位置
     */
    private void setContentSpan(SpannableString spannableString, String content, int start, int end) {

    }


    /**
     * 添加图片资源
     *
     * @param url url
     */
    public void addImageUrl(URL url) {
        ImageSpan imageSpan = new ImageSpan(getContext(), Uri.parse(url.getPath()));
        int selectedStart = getSelectionStart();
        int selectedEnd = getSelectionEnd();
        String before = oldStr.substring(0, selectedStart);
        int beforeLastBr = before.lastIndexOf("\n");
        if (beforeLastBr != selectedStart) {
            before += "\n";
        }
        String after = oldStr.substring(selectedEnd);
        int afterFirstBr = after.indexOf("\n");
        if (afterFirstBr != selectedEnd) {
            after = "\n" + after;
        }
        // TODO: 2019/11/06  配对资源
        String urlFormat = "\n" + SPAN_FLAG_IMAGE_START + url.getPath() + SPAN_FLAG_IMAGE_END + "\n";
        int formatStrLength = urlFormat.length();
        String s = before + urlFormat + after;


        setText(s, BufferType.EDITABLE);
    }

    /**
     * 内容改变线程
     */
    private class ChangeTextRunnable implements Runnable {
        /**
         * 文字内容
         */
        private String content;

        ChangeTextRunnable(String content) {
            this.content = content;
        }

        @Override
        public void run() {
            checkTextChanged(content);
        }
    }

    /**
     * 文字点击
     */
    private class TextClickListener extends ClickableSpan {
        private String content;

        TextClickListener(String content) {
            super();
            this.content = content;
        }

        @Override
        public void onClick(@NonNull View v) {
            if (mRichEditTextHandler != null) {
                Bundle bundle = new Bundle();
                bundle.putString("content", content);
                Message message = Message.obtain();
                message.what = RICH_EDIT_TEXT_CLICK;
                message.setData(bundle);
                mRichEditTextHandler.sendMessage(message);
            }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
        }
    }

    private static final int RICH_EDIT_TEXT_CLICK = 0x0001;
    private static final int RICH_EDIT_TEXT_CHANGE = 0x0002;
    private static final int RICH_EDIT_TEXT_OTHER = 0x0003;

    /**
     * 富文本 Handler 回调处理
     */
    private class RichEditTextHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int what = msg.what;
            Bundle bundle = msg.getData();
            switch (what) {
                case RICH_EDIT_TEXT_CLICK:
                    String clickText = bundle.getString("content", "");
                    Toast.makeText(getContext(), clickText, Toast.LENGTH_SHORT).show();
                    break;
                case RICH_EDIT_TEXT_CHANGE:
                    if (spannableString != null) {
                        int selectedEnd = getSelectionEnd();
                        L.i(TAG, "handleMessage: selectedEnd = " + selectedEnd);
                        setText(spannableString, BufferType.EDITABLE);
                        setSelection(selectedEnd);
                        setMovementMethod(LinkMovementMethod.getInstance());
                        setHintTextColor(Color.TRANSPARENT);
                        spannableString = null;
                    }
                    break;
                case RICH_EDIT_TEXT_OTHER:
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
