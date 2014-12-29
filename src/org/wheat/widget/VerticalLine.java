package org.wheat.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
/**
 * 
* @ClassName: VerticalLine 
* @Description: 本来是想画一条竖线，暂时没有用 
* @author hogachen
* @date 2014年12月24日 上午10:11:14 
*
 */
public class VerticalLine extends View{

	private Paint paint=null;
	private Path path=null;
	//线条宽度
	private int strokeWith=2;
	public VerticalLine(Context context)
	{
		this(context, null);
	}
	public VerticalLine(Context context, AttributeSet attrs) 
	{
		super(context, attrs);

		this.paint=new Paint();
		this.path=new Path();
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setColor(0XFFC0C0C0);
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(strokeWith);
	}
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		this.path.moveTo(0.0F, 0.0F);
		this.path.lineTo( 0.0F,getMeasuredHeight());
		canvas.drawPath(this.path, this.paint);
	}
	
}
