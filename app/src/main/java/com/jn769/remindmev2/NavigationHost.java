package com.jn769.remindmev2;

import android.support.v4.app.Fragment;

/**
 * Created by Jorge Nieves on 3/26/19.
 */
public interface NavigationHost {
    void navigateTo(Fragment fragment, boolean addToBackstack);
}
