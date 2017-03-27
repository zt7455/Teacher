package com.shengliedu.teacher.teacher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button {
	int radius = 0;
	Paint p = new Paint(Paint.DITHER_FLAG);
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				invalidate();
			}
		}

	};
	boolean isDown = false;
	boolean isUp = false;

	public MyButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		p.setARGB(70, 3, 3, 3);
	}

	public MyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub.
		p.setARGB(70, 3, 3, 3);
	}

	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		p.setARGB(70, 3, 3, 3);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, p);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			radius = 1;
			isDown = false;
			isUp = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						radius += 3;
						if (isDown) {
							break;
						}
						if (radius > getWidth() * 2 / 3) {
							break;
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendEmptyMessage(1);
					}
				}
			}).start();
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			radius = getWidth() * 2 / 3;
			isDown = true;
			isUp = false;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						radius -= 3;
						if (isUp) {
							break;
						}
						if (radius < 3) {
							radius = 0;
							break;
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendEmptyMessage(1);
					}
				}
			}).start();
		}

		return super.onTouchEvent(event);

	}

	private void dThread() {
		// TODO Auto-generated method stub

	}
}
