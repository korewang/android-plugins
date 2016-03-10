package com.korewang.shuishui;

import java.util.ArrayList;
import java.util.List;

import com.korwang.shuishui.utils.CreateeBitmapForView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MatrixActivity extends Activity implements OnSeekBarChangeListener {
	private TextView marquee;
	private int minWidth = 80;
	private ImageView imageview;
	private TextView textscale, textdegress;
	private SeekBar seekscale, seekdegress;
	private Matrix matrix = new Matrix();

	// Test loader Manager 不用v4包
	private LoaderManager manager;
	private MyAdapter adapter = null;
	private ListView myList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matrix);
		marquee = (TextView)findViewById(R.id.scroll);
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
		// /registerForContextMenu(textdegress);// 注册上下文关系菜单
		String string = "首先我们要实现<font color='red'>走马灯</font>这样一个效果，<a href='www.baidu.com'>通常来说</a>都是在TextView这个控件中来实现的，而且其中的文字一定是单行显示，如果多行显示，那走马灯效果";
		CharSequence charSequence = Html.fromHtml(string);
		marquee.setText(charSequence);
		marquee.setMovementMethod(LinkMovementMethod.getInstance());
		manager = getLoaderManager();// 加载loadermanager完成异步加载数据的操作
		manager.initLoader(1000, null, callbacks);
		// 实例化listview
		myList = (ListView) findViewById(R.id.myList);
		adapter = new MyAdapter(MatrixActivity.this);
		registerForContextMenu(myList);//
		//createViewBitmap();
	}

	public void createViewBitmap(){
		
	}
	public class MyAdapter extends BaseAdapter {

		private Context context;
		private List<String> list = null;

		public void setList(List<String> list) {
			this.list = list;
		}

		public MyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView view = null;
			if (convertView == null) {
				view = new TextView(context);
			} else {
				view = (TextView) convertView;
			}
			view.setText(list.get(position).toString());
			return view;
		}

	}

	private LoaderCallbacks<Cursor> callbacks = new LoaderCallbacks<Cursor>() {
		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// TODO Auto-generated method stub

		}

		// 完成UI数据提取，更新UI操作
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			// TODO Auto-generated method stub
			List<String> list = new ArrayList<String>();
			while (cursor.moveToNext()) {

				String name = cursor.getString(cursor.getColumnIndex("name"));
				list.add(name);
			}
			// MyAdapter

			adapter.setList(list);
			myList.setAdapter(adapter);
			adapter.notifyDataSetChanged(); // 数据更改
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			// CursorLoader loader = new CursorLoader(MatrixActivity.this);
			Uri url = Uri
					.parse("content://com.korewang.shuishui.unit_content_provider.StudentContentProvider/student");
			// loader.setUri(url);
			// loader.setProjection(projection);

			CursorLoader loader = new CursorLoader(MatrixActivity.this, url,
					null, null, null, null);
			return loader;
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.matrix, menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO 长按后的出来的对话menu
		int menui = item.getItemId();
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (menui) {
		case R.id.add:
			createDiagAlertDialog(MatrixActivity.this);
			break;
		case R.id.delete:
			int position = info.position;
			String name = adapter.getItem(position).toString();

			ContentResolver contentResolver = getContentResolver();
			Uri url = Uri
					.parse("content://com.korewang.shuishui.unit_content_provider.StudentContentProvider/student");
			int count = contentResolver.delete(url, " name = ?",
					new String[] { name });
			if(count > 1){
				manager.restartLoader(1000, null, callbacks);
			}
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void createDiagAlertDialog(Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("提示");
		// alertDialog.setMessage(message)
		View vi = LayoutInflater.from(context).inflate(
				R.layout.diy_dialog_matrix, null);
		final EditText edittext = (EditText) vi.findViewById(R.id.editText1);
		alertDialog.setView(vi);
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String et = edittext.getText().toString().trim();
				Toast.makeText(MatrixActivity.this, et, Toast.LENGTH_SHORT)
						.show();
				ContentResolver contentResolver = getContentResolver();
				Uri url = Uri
						.parse("content://com.korewang.shuishui.unit_content_provider.StudentContentProvider/student");
				ContentValues values = new ContentValues();
				values.put("name", et);
				Uri uri_result = contentResolver.insert(url, values);
				if (uri_result != null) {
					manager.restartLoader(1000, null, callbacks);
				}
			}
		});
		alertDialog.setNegativeButton("CANCEL", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		alertDialog.show();
		// return null;
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
			textscale.setText("图像宽度： " + newWidth + "   图像高度：" + newHeight);
		} else if (seekBar.getId() == R.id.seekbardegress) {
			Bitmap bitmap = ((BitmapDrawable) (getResources()
					.getDrawable(R.drawable.ic_launcher))).getBitmap();
			matrix.setRotate(progress);

			bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			imageview.setImageBitmap(bitmap);
			textdegress.setText(progress + "度");
			if(90 == progress){
				Log.i("TAAG", "roa");
				//createViewBitmap();
				Bitmap bi = CreateeBitmapForView.CreateeBitmapForView(textscale , MatrixActivity.this);
				imageview.setImageBitmap(bi);
			}
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
