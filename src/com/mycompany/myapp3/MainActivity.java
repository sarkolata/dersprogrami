package com.mycompany.myapp3;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.text.*;
import java.util.*;
import android.view.View.*;
import android.content.*;

public class MainActivity extends Activity
{
	TextView tv;
	public boolean tn;
	int ders;
	Date[] dersbasi;
	Date[] derssonu;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		final Button ayar = (Button) findViewById(R.id.ayar);
		
		ayar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Intent ayarsyf = new Intent(MainActivity.this, Settings.class);
					startActivity(ayarsyf);
					Toast.makeText(MainActivity.this, "ananza", Toast.LENGTH_LONG).show();
				}
			});
						
		
		try {
	
			
		dersbasi = new Date[] {
			saatAyarla(8,00),
			saatAyarla(8,00),
			saatAyarla(8,50),
			saatAyarla(9,35),
			saatAyarla(10,20),
			saatAyarla(11,15),
			saatAyarla(12,45),
			saatAyarla(13,25),
			saatAyarla(14,10)
		};
			
		derssonu = new Date[] {
			    saatAyarla(8,40),
				saatAyarla(8,40),
				saatAyarla(9,30),
				saatAyarla(10,15),
				saatAyarla(11,00),
				saatAyarla(11,55),
				saatAyarla(13,20),
				saatAyarla(14,05),
				saatAyarla(14,50)
		};
			 
			mainstart();
			
		} catch (Exception e) {
			
	   Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
		}
		
    }
	
	public void mainstart() throws ParseException{
		
		tv = (TextView) findViewById(R.id.yazi);

		ders= dersHesapla(dersbasi, derssonu);

		if(tn==true){
			//teneffüs}
			timeDiff(getCurrentDate(), dersbasi[ders]);
		} else {
			//ders
			timeDiff(getCurrentDate(), derssonu[ders]);
		}
	}
	
	public Date saatAyarla(int saat, int dakika) throws ParseException {
		
		Date parsedDate = getCurrentDate();
		
		parsedDate.setHours(saat);
		parsedDate.setMinutes(dakika);
		parsedDate.setSeconds(0);
		
		return parsedDate;
	}
	
	public int dersHesapla (Date[] bas, Date[] son) throws ParseException {
		
		int dersamt =0;
        
		Date now = getCurrentDate();
		
		for(int i=1; i<9; i++) {
			
			if(son[i].after(now)) {
				
				if(teneffus(son[i-1], bas[i])){
                    
					tn=true;
				} else {
					tn=false;
				}
				
				dersamt = i;
				
				break;
			}
			
		}
		return dersamt;
	}
	
	public boolean teneffus(Date dersbitis, Date sonrakidersbasi) throws ParseException{
		boolean tenef = false;
		
		Date now = getCurrentDate();
		if(now.after(dersbitis) && now.before(sonrakidersbasi)) {
			tenef = true;
		} else {
			tenef = false;
		}
	
		return tenef;
	}
	
	public Date getCurrentDate() throws ParseException
	{
		Calendar cal = Calendar.getInstance();

		String dateInString = new java.text.SimpleDateFormat("DD/MM/yyyy HH:mm:ss").format(cal.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/yyyy HH:mm:ss");
		Date parsedDate = formatter.parse(dateInString);
		
		return parsedDate;
	}
	
	public long timeDiff(Date date1, Date date2)
	{
		long fark = date2.getTime() - date1.getTime();
		
		MyTimer tim = new MyTimer(fark,1000);
        tim.start();
		
		
		return fark;
	}
	
	public class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
           // tv.setText("changed by the constructor");
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
			if(tn==false){
				try{
				tv.setText("Ders bitti! Sütun zamanı :)");
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								try
								{
									mainstart();
								}
								catch (ParseException e)
								{}
							}
						}, 2000);
				mainstart();
				}catch(Exception e) {
					Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
				}
			} else {
				try{
                tv.setText("Teneffüs bitti! Ders zamanı :(");
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								try
								{
									mainstart();
								}
								catch (ParseException e)
								{}
							}
						}, 2000);
				
				}catch(Exception e){
					Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
        }

        @Override
        public void onTick(long diff) {
            // TODO Auto-generated method stub
			long diffSeconds = diff / 1000 % 60;  
			long diffMinutes = diff / (60 * 1000) % 60; 
			if(tn==true) {
				tv.setText(ders + ". ders'e " + String.valueOf(diffMinutes) + ":" + String.valueOf(diffSeconds) + " kaldı");
			} else {
				if(ders==8){
					tv.setText("okulun bitmesine "+ String.valueOf(diffMinutes) + ":" + String.valueOf(diffSeconds) + " kaldı");
				} else if(ders==5){
					tv.setText("öğle teneffüsüne "+ String.valueOf(diffMinutes) + ":" + String.valueOf(diffSeconds) + " kaldı");
				} else {
					tv.setText(ders+ ". teneffüs'e "+ String.valueOf(diffMinutes) + ":" + String.valueOf(diffSeconds) + " kaldı");
				}
			}
            
        }

    }
}
