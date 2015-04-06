package mobile.javan.co.id.presensi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import java.util.zip.Inflater;

import mobile.javan.co.id.presensi.R;

/**
 * Created by Purwa on 05/04/2015.
 */
public class MenuDrawer extends ListView{
    private Path myArc;

    private Paint mPaintText;

    private void init(AttributeSet attrs, Context context) {
        this.myArc = new Path();
        // create paint object
        this.mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        // set style
        this.mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
        // set color
        this.mPaintText.setColor(getResources().getColor(R.color.black));
        // set text Size
        this.mPaintText.setTextSize(getResources().getDimension(
                R.dimen.fifteen_sp));
    }
    public MenuDrawer(Context context, AttributeSet ats, int defStyle) {
        super(context, ats, defStyle);
        init(ats, context);

    }

    public MenuDrawer(Context context, AttributeSet ats) {
        super(context, ats);
        init(ats, context);
    }

    public MenuDrawer(Context context) {
        super(context);
        init(null, context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        Inflater mInflater =
        super.onDraw(canvas);
    }
}
