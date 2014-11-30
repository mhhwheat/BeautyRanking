package org.wheat.widget;

import org.wheat.beautyranking.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

//虚线控件
public class DashedLine extends View
{
	private Paint paint=null;
	private Path path=null;
	//线条宽度
	private int strokeWith=2;
	public DashedLine(Context context)
	{
		this(context, null);
	}
	public DashedLine(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		//通过R.styleable.dashedline获得我们在attrs.xml中定义的
		//<declare-styleable name="dashedline"> TypedArray
		TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.dashedline);
		
		//我们在attrs.xml中<declare-styleable name="dashedline">节点下
    	//添加了<attr name="lineColor" format="color" />
        //表示这个属性名为lineColor类型为color。当用户在布局文件中对它有设定值时
        //可通过TypedArray获得它的值当用户无设置值是采用默认值0XFF00000
		int lineColor=a.getColor(R.styleable.dashedline_lineColor, 0XFFC0C0C0);
		a.recycle();
		this.paint=new Paint();
		this.path=new Path();
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setColor(lineColor);
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(strokeWith);
	}
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		this.path.moveTo(0.0F, 0.0F);
		this.path.lineTo(getMeasuredWidth(), 0.0F);
		canvas.drawPath(this.path, this.paint);
	}
	

}
