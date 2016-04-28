package com.example.abu.registrationnew;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.Precision.Component.FP.BiomSDK.BiometricComponent;
import com.Precision.Component.FP.BiomSDK.ErrorCode;

public class MainActivity extends AppCompatActivity {


    Button btnImageCapture;
    ImageView fpImageView;

    int imageWidth;
    int imageHeight;
    byte[] rawImage;
    byte[] isoTemplate;
    byte[] isoImage;
    List<byte[]> isoTemplates;
    byte[] isoFPTemplate;
    Bitmap img;


    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BiometricComponent  biometricComponent = new BiometricComponent(MainActivity.this);
        fpImageView = (ImageView) findViewById(R.id.FPimgview);
        btnImageCapture = (Button) findViewById(R.id.btnCapture);
        isoTemplates = new ArrayList<byte[]>();
        try{
            File f = new File("/sdcard/ISO/");
            for(String file: f.list()){
                FileInputStream fin = new FileInputStream("/sdcard/ISO/"+file);
                isoFPTemplate = new byte[fin.available()];
                fin.read(isoFPTemplate);
                fin.close();
                if(isoFPTemplate != null){
                    isoTemplates.add(isoFPTemplate);
                }
            }

			/*ByteArrayOutputStream bytearrayoutstream = new ByteArrayOutputStream();

			Bitmap bitmapimage = BitmapFactory.decodeFile("/sdcard/BMP/P06_F02_dpi__01.bmp");
			boolean comression = bitmapimage.compress(CompressFormat.JPEG, 90, bytearrayoutstream);
			if(comression){
				FileOutputStream fout = new FileOutputStream("/sdcard/P06_jpegImage.jpg");
				fout.write(bytearrayoutstream.toByteArray());
				fout.close();
			}*/


        }catch(Exception e){

        }finally{

        }



        btnImageCapture.setOnClickListener(new OnClickListener() {

            /*@SuppressLint("SdCardPath")*/
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
             /*   fpImageView.setImageBitmap(null);
                int iResult = biometricComponent.FPCapture(0);*/
                int iResult = biometricComponent.FPCapture(1);
                if(iResult == 0){
//                     next activity
                    Intent intent = new Intent(MainActivity.this, inputData.class);
                    startActivity(intent);
//                     end of jump
                    imageHeight = biometricComponent.getImageHeight();
                    imageWidth = biometricComponent.getImageWidth();
                    rawImage = biometricComponent.getRawImageData();
                    isoTemplate = biometricComponent.getISOTemplate();
                    isoImage = biometricComponent.getISOImage();
                    fpImageView.setImageBitmap(biometricComponent.RawToBitmap(rawImage, imageWidth, imageHeight));
                    try{
                        FileOutputStream fout = new FileOutputStream("/sdcard/LiveFMRTemplate");
                        fout.write(isoTemplate);
                        fout.close();
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"Exception in writing file", Toast.LENGTH_SHORT).show();
                    }finally{

                    }

                    try{
                        FileOutputStream fout = new FileOutputStream("/sdcard/FIRImage");
                        fout.write(isoImage);
                        fout.close();
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"Exception in writing file", Toast.LENGTH_SHORT).show();
                    }finally{

                    }
                    if(isoImage != null || isoTemplate != null)
                        Toast.makeText(getApplicationContext(),"Image Capture Success", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(),"Image Capture Failed", Toast.LENGTH_SHORT).show();

                }else if(iResult == -1){
                    Toast.makeText(MainActivity.this,"Generate ISO Image Failed", Toast.LENGTH_SHORT).show();
                }else if(iResult == 701){
                    Toast.makeText(MainActivity.this,"Image Captured Failed", Toast.LENGTH_SHORT).show();
                }else if(iResult == 500){
                    Toast.makeText(MainActivity.this,"Image capture License failed", Toast.LENGTH_SHORT).show();
                }else if(iResult  == 700){
                    Toast.makeText(MainActivity.this,"Please connect the scanner", Toast.LENGTH_SHORT).show();
                }else if(iResult == 707){
                    Toast.makeText(MainActivity.this,"Scanner already initialized", Toast.LENGTH_SHORT).show();
                }else if(iResult == -501){
                    Toast.makeText(MainActivity.this,"License Not Found", Toast.LENGTH_SHORT).show();
                }else if(iResult == 718){
                    Toast.makeText(MainActivity.this,"Exception in process", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Scanner initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
//to go to next activity

/*
public void sendMessage(View view) {
    Intent intent = new Intent(this, inputData.class);
    startActivity(intent);
*/

}
 //end o ging to next activity
 /*@Override
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
    }*/
