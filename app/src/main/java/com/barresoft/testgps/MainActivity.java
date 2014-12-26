package com.barresoft.testgps;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private boolean led=false;
    private Camera camera = Camera.open();
    private Parameters cameraParameters = camera.getParameters();
    private double speed=0;
    private Chronometer cronometro;
    private boolean cronometroHabilitado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarGPS();

        cronometro = (Chronometer)findViewById(R.id.cronometro);
    }

    private void iniciarGPS(){
        //Enable GPS
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        //Disable GPS
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        context.sendBroadcast(intent);


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                1000,   // 1 segundo
                10, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        speed = location.getSpeed(); //m/s
        speed=speed*3.6; //km/h
        SeekBar sbarVelocidad = (SeekBar)findViewById(R.id.sbarVelocidad);
        int speedMax = sbarVelocidad.getMax();

        TextView txtVelocidad = (TextView)findViewById(R.id.txtVelocidad);
        txtVelocidad.setText((int) speed);

        if (speed > speedMax){
            sbarVelocidad.setMax((int) speed);
        }
        sbarVelocidad.setProgress((int) speed);

        if (speed>0 && cronometroHabilitado==false){
            cronometroHabilitado=true;
            cronometro.start();
            //long time = SystemClock.elapsedRealtime()-cronometro.getBase();
            //t.setBase(SystemClock.elapsedRealtime());
            //t.start();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void LED(){ //SWITCH
        if (led==false) {
            led=true;
            cameraParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(cameraParameters);
            camera.startPreview();
        }else{
            led=false;
            cameraParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(cameraParameters);
            camera.stopPreview();
        }
    }

    private void LED(Boolean bool){
        if (bool){
            led=true;
            cameraParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(cameraParameters);
            camera.startPreview();
        }else{
            led=false;
            cameraParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(cameraParameters);
            camera.stopPreview();
        }
    }

    private void msg(String msg){
        Toast.makeText(getBaseContext(),msg,Toast.LENGTH_LONG).show();
    }

    public void ledSwitch(View view){
        LED();
    }
}
