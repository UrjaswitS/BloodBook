package app.com.project.urjaswitk.bloodbook.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import app.com.project.urjaswitk.bloodbook.R;

public class SplashActivity extends AppCompatActivity {

    TextView tv,textView;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Intent i = new Intent(SplashActivity.this, ChooserActivity.class);
        startActivity(i);
        finish();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, SplashActivity.class);
    }

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        tv = (TextView) findViewById(R.id.tv);
//        textView=(TextView) findViewById(R.id.textView);
//        String fontpath= "font/alger.TTF";
//        Typeface tf=Typeface.createFromAsset(getAssets(),fontpath);
//        textView.setTypeface(tf);
//
//        Random generator = new Random();
//        int randomInteger = generator.nextInt(9);
//
//        if(randomInteger == 0)
//        {
//            tv.setText("BLOOD Is Meant To Circulate . Pass It Around");
//        }
//
//        else  if(randomInteger == 1)
//        {
//            tv.setText("Spare Only 15 Mins And Save One Life");
//        }
//        else  if(randomInteger == 2)
//        {
//            tv.setText("The Life You Save May Be Your's Child");
//        }
//        else  if(randomInteger == 3)
//        {
//            tv.setText("Donate Your Blood For A Reason,Let The Reason To Be Life");
//        }
//        else  if(randomInteger ==4)
//        {
//            tv.setText("Donate! It Is A Bloody Good Job");
//        }
//        else  if(randomInteger == 5)
//        {
//            tv.setText("The Blood Donar Of Today Might Be Recipient Of Tommorrow");
//        }
//        else  if(randomInteger == 6)
//        {
//            tv.setText("Don't Let Mosquitoes Get To Your Blood First");
//        }
//        else  if(randomInteger == 7)
//        {
//            tv.setText("The Measure Of Life is Not It's Duration But It's Donation");
//        }
//        else  if(randomInteger == 8)
//        {
//            tv.setText("THE POWER TO SAVE LIVES RUNS IN YOUR VEINS");
//        }
//        else
//        {
//            tv.setText("We Can't Help Everyone But Everyone Can Help SomeOne");
//        }
//
//        new MySplash().execute();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
//    class MySplash extends AsyncTask<Void, Void, Void>
//    {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try
//            {
//                Thread.sleep(2000);
//                finish();
//            }
//            catch (Exception ex)
//            {
//                Toast.makeText(SplashActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
//        @Override
//        protected void onPostExecute(Void aVoid) {
//
//            super.onPostExecute(aVoid);
//            Intent i = new Intent(SplashActivity.this,ChooserActivity.class);
//            startActivity(i);
//        }
//    }
}

