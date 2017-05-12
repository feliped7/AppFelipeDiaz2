package com.example.ingenieria.appfelipe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String[] PYP = {"4 - 5", "6 - 7", "8 - 9","0 - 1", "2 - 3"};
    private static final Integer[] FESTIVOS = {1, 9, 79, 103, 104, 121, 149, 170, 177, 184, 201, 219, 233, 289, 310, 317, 342, 359};


    TextView tv1,tv_placa,tv_proximo_dia;

    private String placaElegida;

    //Button btnNotificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String picoYplacaHoy;

        tv1=(TextView)findViewById(R.id.tv1);
        tv_placa=(TextView)findViewById(R.id.tv_placa);
        tv_proximo_dia=(TextView)findViewById(R.id.tv_proximo_dia);
        //btnNotificacion=(Button) findViewById(R.id.btnNotificacion);
        //btnNotificacion.setOnClickListener(this);

        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_placa.setText(prefe.getString("placa","0-1"));

        Calendar localCalendar = Calendar.getInstance();

        int diaAnio = localCalendar.get(Calendar.DAY_OF_YEAR);
        int diaSemana = localCalendar.get(Calendar.DAY_OF_WEEK);

        if(Arrays.asList(FESTIVOS).contains(diaAnio) || diaSemana == 1 || diaSemana == 7) {// si es festivo o sabado o domingo
            picoYplacaHoy="NO APLICA";
        }else{
            int j=diaAnio;
            while(j>7){
                j=localCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY?j-11:j-6;//si es viernes resta 11 y si es otro dia resta 6
                localCalendar.set(Calendar.DAY_OF_YEAR, j);// cambia la fecha a la fecha restada
            }
            picoYplacaHoy=PYP[localCalendar.get(Calendar.DAY_OF_YEAR)-2];//se resta dos porque lunes comenzo el dia 2
        }
        tv1.setText(" "+picoYplacaHoy+" ");

        //próximo pico y placa ... solo cuando el mismo dia corresponde con el pico y placa
        localCalendar.set(Calendar.DAY_OF_YEAR, diaAnio);//vuelve al dia actual
        if(picoYplacaHoy.equals(tv_placa.getText().toString())){

            int j = diaAnio;

            j = localCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY? j+11: j+6;

            localCalendar.set(Calendar.DAY_OF_YEAR, j);
            Date date = localCalendar.getTime();
            SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
            String date1 = format1.format(date);
            tv_proximo_dia.setText(date1);
            notificacion(date1);
        }

        else {
            //el primer dia del año pico y placa
            int posicion = Arrays.asList(PYP).indexOf(placaElegida);
            int j = 2 + posicion;

            while (j<diaAnio){


                j = localCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ? j + 11 : j + 6;

                localCalendar.set(Calendar.DAY_OF_YEAR, j);
            }

            Date date = localCalendar.getTime();

            SimpleDateFormat format1 = new SimpleDateFormat("EEEE");
            String date1 = format1.format(date);
            tv_proximo_dia.setText("En " + (j - diaAnio) + " días, "+date1);

        }

    }

    public void notificacion(String  proximo){

        NotificationCompat.Builder notificacion = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setVibrate(new long[] {100, 250, 100, 500})
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setLargeIcon((((BitmapDrawable) getResources()
                        .getDrawable(R.drawable.info))
                        .getBitmap()))
                .setContentTitle(getString(R.string.diaP))
                .setContentText(proximo)
                .setTicker("Pico y placa");

        // Sonido de notificaciones
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificacion.setSound(defaultSound);
        Intent intentNotificacion = new Intent(this,MainActivity.class);
        PendingIntent intentPendiente = PendingIntent.getActivity(this,0,intentNotificacion,0);
        notificacion.setContentIntent(intentPendiente);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(10,notificacion.build());


    }




    @Override


    protected void onResume() {
        super.onResume();
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_placa.setText(prefe.getString("placa","0-1"));
    }

    public void configuracion(View v)
    {
        Intent i = new Intent(this, ConfigActivity.class );
        startActivity(i);
    }

    public void acercade(View v) {

        Intent j = new Intent(this,ActivityAcercaDe.class );
        startActivity(j);
    }

    @Override
    public void onClick(View v) {

    }
}
