package com.trial.rajas.fitrack;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by rajas on 11/1/2017.
 */

public class InitializeLayouts {

    public static LinearLayout initializeLinearLayout(View viewById) {
        return (LinearLayout) viewById;
    }

    public static TextView initializeTextView(View viewById) {
        return (TextView) viewById;
    }
}
