package com.project.rptang.android.weixin_fragment;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by MJJ on 2015/7/25.
 */
public class Utils {


    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context,float dp)
    {
        return (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }
}
