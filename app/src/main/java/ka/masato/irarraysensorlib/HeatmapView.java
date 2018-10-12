package ka.masato.irarraysensorlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

public class HeatmapView extends View {

    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 0;
    private static final String LOG_TAG = "HeatmapView";
    private Paint paint;
    private float strokeWidth = 2.0f;
    private Bitmap bitmap;

    public void setSensingDataList(List<SensingData> sensingDataList) {
        this.sensingDataList = sensingDataList;
    }

    private List<SensingData> sensingDataList;

    public HeatmapView(Context context) {
        super(context);
        paint = new Paint();
    }

    public HeatmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeatmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeatmapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.argb(255, 255, 255, 255));//set background.
        // ペイントストロークの太さを設定
        paint.setStrokeWidth(strokeWidth);
        // Styleのストロークを設定する
        paint.setStyle(Paint.Style.FILL);

        if(bitmap != null) {
            float scaleWidth = (float)800.0 / bitmap.getWidth(); //TODO may be prepare set up file into Raspberri pi system.
            float scaleHeight = (float)getHeight() / bitmap.getHeight();
            canvas.scale(scaleWidth, scaleHeight);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.scale(1,1);
        }

        if(sensingDataList == null){return;}
        for(int i=0; i < sensingDataList.size(); i++){
            SensingData sensingData = sensingDataList.get(i);
            int color = sensingData.getColorBarRGB();
            paint.setColor(color);
            canvas.drawRect(sensingData.getLeft()+OFFSET_X, sensingData.getTop()+OFFSET_Y,
                    sensingData.getRight()+OFFSET_X, sensingData.getBottom()+OFFSET_Y, paint);
            paint.setColor(Color.argb(255,0,0,0));
            String value = Double.toString(sensingData.getValue());
            canvas.drawText(value, sensingData.getLeft()+OFFSET_X+10, sensingData.getTop()+OFFSET_Y+20, paint);

        }

    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
