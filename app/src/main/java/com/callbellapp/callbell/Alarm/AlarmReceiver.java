package com.callbellapp.callbell.Alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.callbellapp.callbell.GelenKutusuActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.StartActivity;


public class AlarmReceiver extends BroadcastReceiver {
    public NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {

        int GecmisIsletmelerArrayList = StartActivity.spLogin.getInt("GecmisIsletmelerArrayList",0);

        if (GecmisIsletmelerArrayList!=0) {

            // Alarm işlemlerinin yapılacağı yer

            bildirimOlusturma(context);
        }
    }

    public void bildirimOlusturma(Context context){

        NotificationCompat.Builder builder;

        NotificationManager bildirimYoneticisi =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, GelenKutusuActivity.class);

        PendingIntent gidilecekIntent = PendingIntent.getActivity(context
                ,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String kanalId = "kanalId";
            String kanalAd = "kanalAd";
            String kanalTanım = "kanalTanım";
            int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel kanal = bildirimYoneticisi.getNotificationChannel(kanalId);

            if(kanal==null){
                kanal = new NotificationChannel(kanalId,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanım);
                bildirimYoneticisi.createNotificationChannel(kanal);
            }

            builder = new NotificationCompat.Builder(context,kanalId);

            builder.setContentTitle("CallBell")
                    .setContentText(String.valueOf(R.string.AlarmReceiverAlarmMetni))
                    .setSmallIcon(R.drawable.room_service_red_48dp)
                    .setAutoCancel(true)
                    .setContentIntent(gidilecekIntent);

        }else {

            builder = new NotificationCompat.Builder(context);

            builder.setContentTitle("CallBell")
                    .setContentText(String.valueOf(R.string.AlarmReceiverAlarmMetni))
                    .setSmallIcon(R.drawable.room_service_red_48dp)
                    .setAutoCancel(true)
                    .setContentIntent(gidilecekIntent);
        }

        bildirimYoneticisi.notify(1,builder.build());
    }
}
