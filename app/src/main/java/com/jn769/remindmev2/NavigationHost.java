package com.jn769.remindmev2;

import androidx.fragment.app.Fragment;

/**
 * Created by Jorge Nieves on 3/26/19.
 */
public interface NavigationHost {
    void navigateTo(Fragment fragment, boolean addToBackstack);
}
