package cn.weipan.fb.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.weipan.fb.R;
import cn.weipan.fb.listener.OnSegmentChangeListener;

public class SegmentButton extends LinearLayout implements OnClickListener {

	private TextView tvLeft;
	private TextView tvRight;
	private boolean isLeftSelected;
	
	
	public boolean isLeftSelected() {
		return isLeftSelected;
	}


	private OnSegmentChangeListener onSegmentChangeListener;
	
	public OnSegmentChangeListener getOnSegmentChangeListener() {
		return onSegmentChangeListener;
	}


	public void setOnSegmentChangeListener(OnSegmentChangeListener onSegmentChangeListener) {
		this.onSegmentChangeListener = onSegmentChangeListener;
	}


	public SegmentButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.view_segment_button, this);
		tvLeft = (TextView) findViewById(R.id.tv_left);
		tvRight = (TextView) findViewById(R.id.tv_right);
		
		
		//取自定义的属性
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SegmentButton);
		String leftText = typedArray.getString(R.styleable.SegmentButton_leftText);
		String righText = typedArray.getString(R.styleable.SegmentButton_rightText);
		typedArray.recycle();
		
		tvLeft.setText(leftText);
		tvRight.setText(righText);
		
		tvLeft.setOnClickListener(this);
		tvRight.setOnClickListener(this);
		
		setSelected(true);
	}
	
	
	/**
	 * 设置选中状态  true 选左边  false 选右边
	 */
	public void setSelected(boolean isLeftSelected){
		if(isLeftSelected){
			tvLeft.setSelected(true);
			tvRight.setSelected(false);
		}else{
			tvLeft.setSelected(false);
			tvRight.setSelected(true);
		}
		this.isLeftSelected = isLeftSelected;
		if(onSegmentChangeListener != null){
			onSegmentChangeListener.onChange(isLeftSelected);
		}
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.tv_left){
			setSelected(true);
		}else if(v.getId() == R.id.tv_right){
			setSelected(false);
		}
	}

}
