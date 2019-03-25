/*
 * Copyright (c) 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.zhihu.testviewdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author nekocode @ Zhihu Inc.
 * @since 2017/07/27
 */
public class StatusBarUtil {

    /**
     * 使 Activity 的 StatusBar 变透明
     */
    public static void translucentStatusBar(@NonNull Activity activity) {
        final Window window = activity.getWindow();

        // NaigationBar 不透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // 全屏
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.x
            // StatusBar 去除颜色
            cleanStatusBarBackgroundColor(activity);
        } else {
            // Content 填充 & StatusBar 颜色叠加
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 设置 FitsSystemWindow 为 False，不预留 StatusBar 位置
        final ViewGroup contentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        final View contentChild = contentView.getChildAt(0);
        if (contentChild != null) {
            ViewCompat.setFitsSystemWindows(contentChild, false);
        }
    }

    /**
     * 去除 StatusBar 的颜色
     */
    public static void cleanStatusBarBackgroundColor(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.x
            final Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 恢复 StatusBar 的颜色
     */
    public static void restoreStatusBarBackgroundColor(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.x
            final Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 显示 StatusBar
     */
    public static void showStatusBar(@NonNull Activity activity) {
        final Window window = activity.getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setAttributes(attrs);
    }

    /**
     * 隐藏 StatusBar
     */
    public static void hideStatusBar(@NonNull Activity activity) {
        final Window window = activity.getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);
    }

    /**
     * 添加 FakeStatusBar Drawable 到 View 的背景中
     */
    public static StatusBarDrawable addStatusBarDrawableToView(@NonNull View rootView, int color) {
        final Drawable backgroud = rootView.getBackground();

        if (!(backgroud instanceof CombinedDrawable)) {
            final Context context = rootView.getContext();
            final StatusBarDrawable statusBarDrawable =
                    new StatusBarDrawable(color, StatusBarUtil.getStatusBarHeight(context));

            rootView.setBackground(new CombinedDrawable(backgroud, statusBarDrawable));
            rootView.setPadding(
                    rootView.getPaddingLeft(),
                    rootView.getPaddingTop() + StatusBarUtil.getStatusBarHeight(context),
                    rootView.getPaddingRight(),
                    rootView.getPaddingBottom());

            return statusBarDrawable;

        } else {
            return ((CombinedDrawable) backgroud).statusBarDrawable;
        }
    }

    /**
     * 从 View 的背景中清除 FakeStatusBar Drawable
     */
    public static void removeStatusBarDrawableFromView(View rootView) {
        final Drawable background = rootView.getBackground();

        if (background instanceof CombinedDrawable) {
            rootView.setBackground(((CombinedDrawable) background).origin);

            rootView.setPadding(
                    rootView.getPaddingLeft(),
                    rootView.getPaddingTop() - StatusBarUtil.getStatusBarHeight(rootView.getContext()),
                    rootView.getPaddingRight(),
                    rootView.getPaddingBottom());
        }
    }

    /**
     * 设置 StatusBar 的文本颜色为黑色
     * @link <a href="http://www.jianshu.com/p/932568ed31af"/>
     */
    public static void setStatusBarLightMode(Activity activity, boolean lightmode) {
        // 尝试将小米或魅族手机的状态栏文字改为黑色
        setMIUIStatusBarLightMode(activity, lightmode);
        setFlymeStatusBarLightMode(activity, lightmode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
            final View view = activity.getWindow().getDecorView();
            int flag = view.getSystemUiVisibility();
            if (lightmode) {
                flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flag &= (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            view.setSystemUiVisibility(flag);
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        final int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static volatile Class<?> sMiuiClazz;
    private static volatile Field sMiuiField1;
    private static volatile Method sMiuiField2;
    private static boolean sMiuiError = false;

    /**
     * MIUI 系统中设置 StatusBar 的文本颜色为黑色
     */
    private static boolean setMIUIStatusBarLightMode(Activity activity, boolean lightmode) {
        if (sMiuiError) return false;

        boolean result = false;
        try {
            int darkModeFlag = 0;
            if (sMiuiClazz == null) {
                synchronized (StatusBarUtil.class) {
                    if (sMiuiClazz == null) {
                        sMiuiClazz = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                        sMiuiField1 = sMiuiClazz.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                        sMiuiField2 = activity.getWindow().getClass()
                                .getMethod("setExtraFlags", int.class, int.class);
                    }
                }
            }

            darkModeFlag = sMiuiField1.getInt(sMiuiClazz);
            sMiuiField2.invoke(activity.getWindow(), lightmode ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException ignored) {
            sMiuiError = true;
        } catch (Exception ignored) {
        }
        return result;
    }

    private static volatile Field sFlymeField1;
    private static volatile Field sFlymeField2;
    private static boolean sFlymeError = false;

    /**
     * Flyme 系统中设置 StatusBar 的文本颜色为黑色
     */
    private static boolean setFlymeStatusBarLightMode(Activity activity, boolean lightmode) {
        if (sFlymeError) return false;

        boolean result = false;
        try {
            if (sFlymeField1 == null) {
                synchronized (StatusBarUtil.class) {
                    if (sFlymeField1 == null) {
                        sFlymeField1 = WindowManager.LayoutParams.class
                                .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                        sFlymeField2 = WindowManager.LayoutParams.class
                                .getDeclaredField("meizuFlags");
                        sFlymeField1.setAccessible(true);
                        sFlymeField2.setAccessible(true);
                    }
                }
            }

            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

            int bit = sFlymeField1.getInt(null);
            int value = sFlymeField2.getInt(lp);
            if (lightmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            sFlymeField2.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (NoSuchFieldException ignored) {
            sFlymeError = true;
        } catch (Exception ignored) {
        }
        return result;
    }


    /**
     * FakeStatusBar Drawable
     */
    public static class StatusBarDrawable extends Drawable {
        private final Paint paint;
        private final int height;

        public StatusBarDrawable(int color, int height) {
            this.paint = new Paint();
            this.paint.setColor(color);
            this.height = height;
        }

        public void setColor(int color) {
            paint.setColor(color);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            canvas.drawRect(0f, 0f, canvas.getWidth(), height, paint);
        }

        @Override
        public void setAlpha(@IntRange(from = 0, to = 255) int i) {
            paint.setAlpha(i);
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    /**
     * 用于组合原背景和 FakeStatusBar Drawable
     */
    private static class CombinedDrawable extends LayerDrawable {
        private Drawable origin;
        private StatusBarDrawable statusBarDrawable;

        CombinedDrawable(@Nullable Drawable origin, @NonNull StatusBarDrawable statusBarDrawable) {
            super(origin == null ? new Drawable[] {statusBarDrawable} : new Drawable[] {origin, statusBarDrawable});
            this.origin = origin;
            this.statusBarDrawable = statusBarDrawable;
        }
    }
}
