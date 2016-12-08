package com.example.phuong.alarm;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by phuong on 07/12/2016.
 */

public class CaculatorActivity extends AppCompatActivity {
    MediaPlayer mp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp=MediaPlayer.create(this, R.raw.quehuongtoi);
        mp.start();

        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide activity title
        setContentView(R.layout.dialog_caculator_alarm);
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Repeat");
        dialog.setContentView(R.layout.dialog_caculator_alarm);

        final TextView tvNumber1 = (TextView) dialog.findViewById(R.id.tvNumber1);
        final TextView tvNumber2 = (TextView) dialog.findViewById(R.id.tvNumber2);
        final TextView tvNumber3 = (TextView) dialog.findViewById(R.id.tvNumber3);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        final EditText edtResult  = (EditText) dialog.findViewById(R.id.result);

        Random rd = new Random();
        int number1 = rd.nextInt(100);
        int number2 = rd.nextInt(100);
        int number3 = rd.nextInt(100);

        Log.d("TAG11",number1+" 11 "+number2+" 11 "+number3+"   ");
        tvNumber1.setText(String.valueOf(number1));
        tvNumber2.setText(String.valueOf(number2));
        tvNumber3.setText(String.valueOf(number3));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = Integer.parseInt(tvNumber1.getText().toString()) - Integer.parseInt(tvNumber2.getText().toString()) + Integer.parseInt(tvNumber3.getText().toString());
                Log.d("TAG111",result+"");
                if(String.valueOf(result).equals(edtResult.getText().toString())){
                    mp.release();
                }
                else{
                    Toast.makeText(getApplication(),"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
