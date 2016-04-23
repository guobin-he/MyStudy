/**
 * 1) Use a singleton class to store global variables
 * 2) Handle click event
 * 3) Start an Activity with extra parameters and receive result from it
 * 4) Return back result to the activity that started this activity
 * 5) Learn getIntent of an Activity
 */
package com.ghe.mystudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int DEFAULT_INT = -1;
    int mMyNumber;
    int mCallerNumber;
    int mBackNumber;

    public static class Global {
        private int mCurrentNumber;
        private static Global mInstance;
        private Global() {}
        public static synchronized Global getInstance() {
            if (mInstance==null) {
                mInstance = new Global();
                mInstance.mCurrentNumber = 0;
            }
            return mInstance;
        }

        public int IncreaseNumber() {
            return(mCurrentNumber++);
        }
    }

    @Override
    public void onClick(View v) {
        if (v!=null && v.getId()==R.id.activity_main_button_forward) {
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("CallerNumber", mMyNumber);
            startActivityForResult(intent, 1, null);
        }
        if (v!=null && v.getId()==R.id.activity_main_button_back) {
            Intent data = new Intent();
            data.putExtra("BackNumber", mMyNumber);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!=1) {
            return;
        }
        if(resultCode==RESULT_OK) {
            if((mBackNumber = data.getIntExtra("BackNumber", DEFAULT_INT)) == DEFAULT_INT) {
                Log.e(Constants.TAG, "why???");
            }
            String text = "My:" + mMyNumber + "    " + "Caller:" + mCallerNumber + "    " + "Back from:" + mBackNumber;
            ((TextView) findViewById(R.id.activity_main_text_edit)).setText(text);
        }
        if(resultCode==RESULT_CANCELED) {
            String text = "My:" + mMyNumber + "    " + "Caller:" + mCallerNumber + "    " + "Back from:Cancelled";
            ((TextView) findViewById(R.id.activity_main_text_edit)).setText(text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnForward = (Button)findViewById(R.id.activity_main_button_forward);
        assert btnForward != null;
        btnForward.setOnClickListener(this);

        Button btnBack = (Button)findViewById(R.id.activity_main_button_back);
        assert btnBack != null;
        btnBack.setOnClickListener(this);

        Intent intent = getIntent();
        if(Intent.ACTION_MAIN.equals(intent.getAction()) &&
            intent.getCategories()!=null && intent.getCategories().contains(Intent.CATEGORY_LAUNCHER)) {
            mMyNumber = Global.getInstance().IncreaseNumber();
            mCallerNumber=0;
        }else if((mCallerNumber = intent.getIntExtra("CallerNumber", DEFAULT_INT)) != DEFAULT_INT) {
            mMyNumber = Global.getInstance().IncreaseNumber();
        }else {
            Log.e(Constants.TAG, "why???");
        }
        String text = "My:" + mMyNumber + "    " + "Caller:" + mCallerNumber;
        ((TextView) findViewById(R.id.activity_main_text_edit)).setText(text);
    }
}
