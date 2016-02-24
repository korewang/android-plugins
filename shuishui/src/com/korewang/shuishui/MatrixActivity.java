package com.korewang.shuishui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MatrixActivity extends Activity implements OnSeekBarChangeListener {

	private int minWidth = 80;
	private ImageView imageview;
	private TextView textscale, textdegress;
	private SeekBar seekscale, seekdegress;
	private Matrix matrix = new Matrix();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matrix);

		imageview = (ImageView) findViewById(R.id.imageview);
		textscale = (TextView) findViewById(R.id.textscale);
		textdegress = (TextView) findViewById(R.id.textdegress);
		seekscale = (SeekBar) findViewById(R.id.seekbarscale);
		seekdegress = (SeekBar) findViewById(R.id.seekbardegress);
		seekscale.setOnSeekBarChangeListener(this);
		seekdegress.setOnSeekBarChangeListener(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		seekscale.setMax(dm.widthPixels - minWidth);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.matrix, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (seekBar.getId() == R.id.seekbarscale) {
			int newWidth = progress + minWidth;
			int newHeight = (int) (newWidth * 3 / 4);
			imageview.setLayoutParams(new LinearLayout.LayoutParams(newWidth,
					newHeight));
			textscale.setText("图像宽度： "+newWidth +"   图像高度："+newHeight);
		} else if (seekBar.getId() == R.id.seekbardegress) {
			Bitmap bitmap = ((BitmapDrawable) (getResources()
					.getDrawable(R.drawable.ic_launcher))).getBitmap();
			matrix.setRotate(progress);
			
			bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			imageview.setImageBitmap(bitmap);
			textdegress.setText(progress +"度");
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
}
