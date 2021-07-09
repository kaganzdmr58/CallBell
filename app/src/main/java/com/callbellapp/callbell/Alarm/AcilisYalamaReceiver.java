package com.callbellapp.callbell.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.callbellapp.callbell.StartActivity;

import java.util.Calendar;

public class AcilisYalamaReceiver extends BroadcastReceiver {

    //bildirm saati işlemleri
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(context
                ,0,alarmIntent,0);


        String bildirimSaati = StartActivity.spLogin.getString("bildirimSaati","12:00");
        String [] saatDakika = bildirimSaati.split(":");
        int saat = Integer.parseInt(saatDakika[0]);
        int dakika = Integer.parseInt(saatDakika[1]);
        // Alarm çalışması ayarlanması
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,saat);
        calendar.set(Calendar.MINUTE,dakika);
        calendar.set(Calendar.SECOND,0);

        long neZamanTetiklenecek = calendar.getTimeInMillis();
        long tekrarlamaSuresi = 1000*60*1;  // 1 dk

        alarmManager.setInexactRepeating(
                 AlarmManager.RTC_WAKEUP
                ,neZamanTetiklenecek
                ,alarmManager.INTERVAL_DAY  // günün aynı saatinde çalışıyor.
                ,pendingIntent);
        //Toast.makeText(context,"Açılış yakalandı.",Toast.LENGTH_SHORT).show();
    }
}
