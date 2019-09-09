package com.inz.z.base.view.widget;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/29 11:55.
 */
public interface DeleteItemTouchListener {
    void onSwiped(int position);

    void onSwitch(int currentPosition, int targetPosition);

}
