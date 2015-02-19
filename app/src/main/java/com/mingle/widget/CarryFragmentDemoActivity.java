package com.mingle.widget;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class CarryFragmentDemoActivity extends ActionBarActivity implements View.OnClickListener {


    private CarryFragmentLayout carryFragmentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carry_fragment_demo);
        carryFragmentLayout= (CarryFragmentLayout) findViewById(R.id.carryFragment);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(carryFragmentLayout.isOpen()){
                carryFragmentLayout.closeFragment();
                return  false;
            }
        }


        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_carry_fragment_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            carryFragmentLayout.closeFragment();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void onClick(View view){
        TextView textView=   (TextView)view;


        BlankFragment fragment=new BlankFragment(textView.getText().toString());

               carryFragmentLayout.openFragment(getSupportFragmentManager(),fragment,view);



    }


}
