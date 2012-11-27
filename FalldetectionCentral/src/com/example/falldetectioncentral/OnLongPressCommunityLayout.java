package com.example.falldetectioncentral;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class OnLongPressCommunityLayout extends View {
Paint paint;

public OnLongPressCommunityLayout(Context context) {
    this(context, null);        
}

public OnLongPressCommunityLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
}

public OnLongPressCommunityLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);                
}

@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    paint = new Paint();
    paint.setColor(Color.BLUE);

    canvas.drawRect(0.f, 0.f, 240.f, 240.f, paint);

	}

public void draw(int x, int y){
	
	
	
}
}
