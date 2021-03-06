package com.example.sample7applicationcomponent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	EditText inputView;
	private static final int REQUEST_CODE_MY = 0;
	int state = 0;
	String mSavedFileName;
	LocalBroadcastManager mLBM;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLBM = LocalBroadcastManager.getInstance(this);
        inputView = (EditText)findViewById(R.id.edit_message);
        Button btn = (Button)findViewById(R.id.btn_my);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, MyActivity.class);
				String text = inputView.getText().toString();
				i.putExtra(MyActivity.EXTRA_MESSAGE, text);
				i.putExtra(MyActivity.EXTRA_AGE, 41);
				i.putExtra(MyActivity.EXTRA_NAME, "ysi");
				Person p = new Person();
				p.name = "ysi";
				p.age = 41;
				i.putExtra(MyActivity.EXTRA_PERSON, p);
				startActivityForResult(i, REQUEST_CODE_MY);
//				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
        
        
        btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MyService.class);
				intent.putExtra("count" , 100);
				startService(intent);
			}
		});
        
        btn = (Button)findViewById(R.id.btn_stop);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MyService.class);
				stopService(intent);
			}
		});
        
        if (savedInstanceState != null) {
        	state = savedInstanceState.getInt("state",0);
        	mSavedFileName = savedInstanceState.getString("filename");
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putInt("state", state);
    	outState.putString("filename",mSavedFileName);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	IntentFilter filter = new IntentFilter(MyService.ACTION_MOD_ZERO);
//    	registerReceiver(mReceiver, filter);
    	mLBM.registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			final int count = intent.getIntExtra("count", 0);
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, " activity count : " + count, Toast.LENGTH_SHORT).show();
				}
			});
			MyService.isReceived = true;
//			setResultCode(Activity.RESULT_OK);
		}
	};
    @Override
    protected void onPause() {
    	super.onPause();
//    	unregisterReceiver(mReceiver);
    	mLBM.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == REQUEST_CODE_MY && resultCode == Activity.RESULT_OK) {
    		String message = data.getStringExtra(MyActivity.PARAM_RESULT_MESSAGE);
    		Toast.makeText(this, "result : " + message, Toast.LENGTH_SHORT).show();
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
