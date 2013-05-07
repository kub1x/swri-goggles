package show.showdirect;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.widget.Toast;
import android.location.*;

public class MainActivity extends Activity {
	private Button b1;
	private EditText e1;
	private EditText e2;
	double latitude;
	double longitude;
	private TextView tv2;
	private Timer mt = null;
	private TimerTask tk;
	private SurfaceView sv;
	SurfaceHolder h;
	private ImageView i1;
	int d1, d2, d3, d4, d5, d6, d7, d8;
	float draw1,draw2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		e1 = (EditText) findViewById(R.id.editText1);
		e2 = (EditText) findViewById(R.id.editText2);
    
		tv2 = (TextView) findViewById(R.id.textView2);
		// tv2=(TextView)findViewById(R.id.textView3);
		// tv3=(TextView)findViewById(R.id.textView4);
		// tv4=(TextView)findViewById(R.id.textView5);   
		// b1 =(Button)findViewById(R.id.button1);
		// b1.setOnClickListener(this);
		// View button1=this.findViewById(R.id.button1);
		// button1.setOnClickListener();
		//TextView tv = (TextView) findViewById(R.id.textView1);
		//tv.setText("hello user");
		sv=(SurfaceView)findViewById(R.id.surfaceView1);
		sv.setZOrderOnTop(true);// 设置画布 背景透明
		sv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		h = sv.getHolder(); 
		//i1 = (ImageView) findViewById(R.id.imageView1);
		i1=(ImageView)findViewById(R.id.imageView1);
		d1 = R.drawable.n;
		d2 = R.drawable.ne;
		d3 = R.drawable.e;
		d4 = R.drawable.se;
		d5 = R.drawable.s; 
		d6 = R.drawable.sw;
		d7 = R.drawable.w;
		d8 = R.drawable.nw;
		 i1.setVisibility(View.GONE);
		//i1.setVisibility(View.GONE);

	}

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		// 定义处理信息的方法
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 处理代码
				direct();
				// mt.cancel();
				break;
			}
			super.handleMessage(msg);
		}

	};

	private class MyTimeTask extends TimerTask {

		@Override
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}

	}

	public void drawLine() {
 
		Canvas c = h.lockCanvas();
		Paint paint = new Paint();
		c.drawColor(Color.WHITE); // 画板上填充黑色 
        c.drawRect(0, 0, 0, 0, paint);//清屏
		Paint p = new Paint();
		// c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
		p.setColor(Color.BLACK);
		c.drawCircle(39, 39, (float) 4.5, p); 
		Paint p1=new Paint();
		p1.setColor(Color.YELLOW);
		c.drawCircle(draw1, draw2, (float) 4.5, p1);
		h.unlockCanvasAndPost(c);
	}

	final LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

	// LocationListener locationListener =new LocationListener;
	private void getLocation() {
		//
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(serviceName);
		//
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

		String provider = locationManager.getBestProvider(criteria, true); // get GPSinf 
																			 
																			 
		Location location = locationManager.getLastKnownLocation(provider); // get gpslocation																		
																			
		updateToNewLocation(location);
		locationManager.requestLocationUpdates(provider, 1000, 0,
				locationListener);
		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 1000, 0, locationListener);
	}

	private void updateToNewLocation(Location location) {

		TextView tv1;
		tv1 = (TextView) this.findViewById(R.id.textView1);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			tv1.setText("latitude：" + latitude + "\nlongitude" + longitude);
		} else {
			tv1.setText("cannot get");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	double a = 0, b = 0;

	public void direct() {

		String show = "";
		String s1 = e1.getText().toString();
		String s2 = e2.getText().toString();
		if (!TextUtils.isEmpty(e1.getText())
				&& !TextUtils.isEmpty(e2.getText()) && !s1.equals("-")
				&& !s2.equals("-")) {
			i1.setVisibility(View.VISIBLE);
			
			// e1=(EditText)findViewById(R.id.editText1);
			// e2=(EditText)findViewById(R.id.EditText01);
			String e11 = e1.getText().toString();
			String e22 = e2.getText().toString();
			// tv1.setText("--");
			Context context = getApplicationContext();
			// if(e11 != null && e11.length() > 0&&e22 != null && e22.length() >
			// 0)
			// {
			// Double x=
			double lat1 = Double.parseDouble(e1.getText().toString());
			double lon1 = Double.parseDouble(e2.getText().toString());
			
			//show = "please make sure latitude under [-90,90], lontitude under [-180,180]";
			if (lat1 < -90 | lat1 > 90 | lon1 < -180 | lon1 > 180) {
				show = "please make sure latitude under [-90,90], lontitude under [-180,180]";
				CharSequence text = show;
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} else {
				double x, y;
				double z, z1, z2, z3, z4;
				getLocation();
				 double y1=(double)latitude*63567550*Math.PI/180;
				 double x11=(double)longitude*63781400*2*Math.PI/360;
				//double y1 = (double) a * 63567550 / 180;
				//double x11 = (double) b * 63781400 * 2  / 360;
				double y2 = lat1 * 63567550 *Math.PI/ 180;
				double x2 = lon1 * 63781400 * 2*Math.PI / 360;

				x = x2 - x11;
				y = y2 - y1;
                draw1= (float)(39+x*39/1000);
                draw2= (float) (39-y*39/1000);
				z = Math.abs(Math.sin(22.5 * Math.PI / 180));
				z1 = Math.abs(Math.sin(67.5 * Math.PI / 180));
				z3 = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				z4 = Math.abs(y / z3);
				z2 = Math.asin(z4);
				// tv1.setText(String.valueOf(x11));
				// tv2.setText(String.valueOf(x2));
				// tv3.setText(String.valueOf(z4));
				// tv4.setText(String.valueOf(z2));
				drawLine();
				
				if (z4 < z) {
					//toast.cancel();
					if (x > 0)
						i1.setImageResource(d3);
					else
						i1.setImageResource(d7);
				} else if (z4 > z1) {
					//toast.cancel();
					if (y > 0)
						i1.setImageResource(d1);
					else
						i1.setImageResource(d5);
				} else if (z4 > z & z4 < z1) {
					//toast.cancel();
					if (x > 0 & y > 0)
						i1.setImageResource(d2);
					else if (x > 0 & y < 0)
						i1.setImageResource(d4);
					else if (x < 0 & y > 0)
						i1.setImageResource(d8);
					else if (x < 0 & y < 0)
						i1.setImageResource(d6);
				}

				if (x < 5 & y < 5 & x > -5 & y > -5) {
					show = "You can see it";
					CharSequence text = show;
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				;

				// if(x>30) y="North";
				// else y="South";
				tv2.setText("Distance:" + (int) z3);

			}
		} else {
			show = "please input data";
			Context context = getApplicationContext();
			CharSequence text = show;
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
		}
		//a = a + 0.1;
	//	b = b + 0.1;
	}

	protected void dialog() {

		AlertDialog.Builder builder = new Builder(MainActivity.this);

		builder.setMessage("really want to exit?");

		builder.setTitle("hint");

		builder.setPositiveButton("yes",

		new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

				MainActivity.this.finish();

			}

		});

		builder.setNegativeButton("no",

		new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}

		});

		builder.create().show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			dialog();

			return false;

		}
 
		return false;
	}

	public void c2(View source) {
		mt.cancel();  
		String show = "please input data";
		Context context = getApplicationContext();
		CharSequence text = show;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.cancel();
	} 

	public void c1(View source) { 

		mt = new Timer(); // 计时任务开始
		mt.schedule(new MyTimeTask(), 1000, 2000);
    
		
		// tv2.setText("纬度："+ s);
	}

}
