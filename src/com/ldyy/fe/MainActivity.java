package com.ldyy.fe;


import java.io.File;

import com.ldyy.myviews.CapatityBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static String TAG = "MainActivity";
	private final boolean D = true;
	private void log(String msg){
		if(D){
			Log.e("", msg);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        testSpaces();
		TextView sdTv = (TextView) findViewById(R.id.tv_sdcard);
		TextView romTv = (TextView) findViewById(R.id.tv_rom);
		CapatityBar romBar = (CapatityBar) findViewById(R.id.cbar_rom);
		CapatityBar sdBar = (CapatityBar) findViewById(R.id.cbar_sdcard);
		
//		String[] sdCardSpaces = getSpace(Environment.getExternalStorageDirectory());
//        String[] romSpaces = getSpace(Environment.getDataDirectory());
        long[] sdCardSpaces = getSpaceNumber(Environment.getExternalStorageDirectory());
        long[] romSpaces = getSpaceNumber(Environment.getDataDirectory());
        
		romBar.setData(romSpaces[0]	, romSpaces[0] - romSpaces[1]);
		sdBar.setData(sdCardSpaces[0], sdCardSpaces[0] - sdCardSpaces[1]);
		
		romTv.setText("手机容量 ：" + Formatter.formatFileSize(this, romSpaces[0]) + 
				"    剩余容量 ：" + Formatter.formatFileSize(this, romSpaces[1]));
		sdTv.setText("sdcard容量 ：" + Formatter.formatFileSize(this, sdCardSpaces[0]) + 
				"    剩余容量 ：" + Formatter.formatFileSize(this, sdCardSpaces[1]));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/**
	 * 根据路径指向的文件系统得到文件系统的信息  
	 * @param path
	 * @return 转换成易于识别的字符串 spaces[0] 总容量， spaces[1] 剩余容量
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private String[] getSpace(File path) {
        //File path = Environment.getDataDirectory();
		String[] spaces = new String[2];
        StatFs stat = new StatFs(path.getPath());  
        long blockCount = 0;  
        long blockSize = 0; 
        long availableBlocks = 0; 
        
        //兼容性
        if(android.os.Build.VERSION.SDK_INT >= 18){
        	blockCount = stat.getBlockCountLong();
        	blockSize = stat.getBlockSizeLong();  
        	availableBlocks = stat.getAvailableBlocksLong();
        }else{
        	blockCount = stat.getBlockCount();
        	blockSize = stat.getBlockSize();  
        	availableBlocks = stat.getAvailableBlocks();
        }
        log("blockCount:"+blockCount+"  blockSize:"+blockSize+"  availableBlocks:"+availableBlocks);
         
        spaces[0] = Formatter.formatFileSize(getApplicationContext(), blockCount*blockSize);  
        spaces[1] = Formatter.formatFileSize(getApplicationContext(), blockSize*availableBlocks);  
        
        return spaces;
    }  
	/**
	 * 根据路径指向的文件系统得到文件系统的信息  
	 * @param path
	 * @return 字节总数 spaces[0] 总容量， spaces[1] 剩余容量
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private long[] getSpaceNumber(File path) {
        //File path = Environment.getDataDirectory();
		long[] spaces = new long[2];
        StatFs stat = new StatFs(path.getPath());  
        long blockCount = 0;  
        long blockSize = 0; 
        long availableBlocks = 0; 
        
        //兼容性
        if(android.os.Build.VERSION.SDK_INT >= 18){
        	blockCount = stat.getBlockCountLong();
        	blockSize = stat.getBlockSizeLong();  
        	availableBlocks = stat.getAvailableBlocksLong();
        }else{
        	blockCount = stat.getBlockCount();
        	blockSize = stat.getBlockSize();  
        	availableBlocks = stat.getAvailableBlocks();
        }
        spaces[0] = blockSize * blockCount;
        spaces[1] = blockSize * availableBlocks;
        return spaces;
    }  
	
	/**
	 * 用于测试不同分区的存储容量
	 */
	private void testSpaces(){
		String[] romSpaces = getSpace(Environment.getDownloadCacheDirectory());
        String[] romSpaces2 = getSpace(Environment.getDataDirectory());
        String[] romSpaces3 = getSpace(Environment.getRootDirectory());
        String[] romSpaces4 = getSpace(Environment.getExternalStorageDirectory());
        String[] romSpaces5 = getSpace(new File("/sdcard/"));
        log(romSpaces[0] + " cache " + romSpaces[1]);
        log(romSpaces2[0] + " data " + romSpaces2[1]);
        log(romSpaces3[0] + " system " + romSpaces3[1]);
        log(romSpaces4[0] + " extral sdcard " + romSpaces4[1]);
        log(romSpaces5[0] + " sdcard " + romSpaces5[1]);
        log("external:"+Environment.getExternalStorageDirectory().getAbsolutePath());
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
	
	
	private long mExitTime;  //返回键退出时间标记
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//实现再按一次退出程序
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(System.currentTimeMillis() - mExitTime  > 2000){
				Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			}else{
				finish();
			}
		}
		return false;
	}
}
