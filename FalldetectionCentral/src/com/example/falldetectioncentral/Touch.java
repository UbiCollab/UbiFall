package com.example.falldetectioncentral;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Touch extends Fragment implements OnTouchListener{

public boolean onTouch(View v, MotionEvent event) {
   Log.d("TOUCH", "%touch ");
    return true;
}
}