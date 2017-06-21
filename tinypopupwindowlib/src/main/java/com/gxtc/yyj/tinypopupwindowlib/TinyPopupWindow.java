package com.gxtc.yyj.tinypopupwindowlib;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by ASUS on 2017/6/20.
 * PopupWindow的简单封装 采用Builder模式 使用起来更为简洁
 */

public class TinyPopupWindow extends PopupWindow {

    private TinyPopupWindow(Context context) {
        super(context);
    }


    public static class Builder {
        private Context mContext;
        private int width;
        private int height;
        private int resId;
        private boolean outsideTouchable;
        private boolean focusable;
        private Drawable backgroundDrawable;
        private boolean clippingEnabled;
        private int animationStyle;
        private OnDismissListener listener;
        private OnViewActionListener mActionListener;
        private LinkedHashMap<Integer, OnClickListener> mClickHashMap;
        private LinkedHashMap<Integer, CharSequence> mTextHashMap;

        private Builder() {
            mTextHashMap = new LinkedHashMap<>();
            mClickHashMap = new LinkedHashMap<>();
        }

        /**
         * 必须传入context
         *
         * @param context
         * @return
         */
        public Builder withContext(Context context) {
            this.mContext = context;
            return this;
        }

        /**
         * 设置宽和高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * 设置内容View
         *
         * @param resId
         * @return
         */
        public Builder setContentView(int resId) {
            this.resId = resId;
            return this;
        }

        /**
         * 设置外部是否可以触摸
         *
         * @param outsideTouchable
         * @return
         */
        public Builder setOutsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        /**
         * 设置是否占有焦点
         *
         * @param focusable
         * @return
         */
        public Builder setFocusable(boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        /**
         * 设置背景
         *
         * @param backgroundDrawable
         * @return
         */
        public Builder setBackgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        /**
         * 设置背景颜色
         *
         * @param backgroundColor
         * @return
         */
        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundDrawable = new ColorDrawable(backgroundColor);
            return this;
        }

        /**
         * 设置是否可以超出父控件
         *
         * @param clippingEnabled
         * @return
         */
        public Builder setClippingEnabled(boolean clippingEnabled) {
            this.clippingEnabled = clippingEnabled;
            return this;
        }

        /**
         * 设置动画主题
         *
         * @param animationStyle
         * @return
         */
        public Builder setAnimationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        /**
         * 设置dismiss监听
         *
         * @param listener
         * @return
         */
        public Builder setOnDismissListener(OnDismissListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置view动作监听
         *
         * @param actionListener
         * @return
         */
        public Builder setOnViewActionListener(OnViewActionListener actionListener) {
            this.mActionListener = actionListener;
            return this;
        }

        /**
         * 设置单个点击监听
         *
         * @param id
         * @param onClickListener
         * @return
         */
        public Builder setOnClickListener(int id, OnClickListener onClickListener) {
            mClickHashMap.put(id, onClickListener);
            return this;
        }

        /**
         * 批量设置监听
         *
         * @param onClickListener
         * @param ids
         * @return
         */
        public Builder setOnClickListener(int[] ids, OnClickListener onClickListener) {
            if (ids != null && ids.length > 0) {
                for (int id :
                        ids) {
                    mClickHashMap.put(id, onClickListener);
                }
            }
            return this;
        }

        /**
         * 设置文本
         *
         * @param id
         * @param text
         * @return
         */
        public Builder setText(int id, CharSequence text) {
            mTextHashMap.put(id, text);
            return this;
        }

        /**
         * 批量设置文本
         *
         * @param ids
         * @param text
         * @return
         */
        public Builder setText(int[] ids, CharSequence[] text) {
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    mTextHashMap.put(ids[i], text[i]);
                }
            }
            return this;
        }

        /**
         * 初始化
         *
         * @return
         */
        public TinyPopupWindow build() {
            if (mContext == null)
                throw new IllegalStateException("Method withContext() must be invoked!");
            TinyPopupWindow popupWindow = new TinyPopupWindow(mContext);
            View view = LayoutInflater.from(mContext).inflate(resId, null);
            if (view == null) throw new IllegalStateException("the resId must be layout id!");
            popupWindow.setContentView(view);
            View v = null;
            if (mClickHashMap.size() > 0) {
                Set<Map.Entry<Integer, OnClickListener>> entries = mClickHashMap.entrySet();
                for (Map.Entry<Integer, OnClickListener> e :
                        entries) {
                    int clickId = e.getKey();
                    final OnClickListener listener = e.getValue();
                    v = view.findViewById(clickId);
                    if (v != null && listener != null && !v.hasOnClickListeners())
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onClick(v);
                            }
                        });
                }
            }
            if (mTextHashMap.size() > 0) {
                Set<Map.Entry<Integer, CharSequence>> entries = mTextHashMap.entrySet();
                for (Map.Entry<Integer, CharSequence> e :
                        entries) {
                    int textViewId = e.getKey();
                    CharSequence value = e.getValue();
                    v = view.findViewById(textViewId);
                    if (v != null && value != null && v instanceof TextView)
                        ((TextView) v).setText(value);
                }
            }
            if (mActionListener != null) mActionListener.onViewAction(view);
            popupWindow.setAnimationStyle(animationStyle);
            popupWindow.setOutsideTouchable(outsideTouchable);
            popupWindow.setFocusable(focusable);
            popupWindow.setClippingEnabled(clippingEnabled);
            if (backgroundDrawable != null) {
                popupWindow.setBackgroundDrawable(backgroundDrawable);
            }
            popupWindow.setHeight(height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : height);
            popupWindow.setWidth(width == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : width);
            if (listener != null) {
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        listener.onDismiss();
                    }
                });
            }
            return popupWindow;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * dismiss监听接口
     */
    public interface OnDismissListener {
        void onDismiss();
    }

    /**
     * View Action监听
     */
    public interface OnViewActionListener {
        void onViewAction(View view);
    }

    /**
     * 点击事件监听
     */
    public interface OnClickListener {
        void onClick(View view);
    }
}
