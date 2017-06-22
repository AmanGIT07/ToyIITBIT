package com.example.android.toyiitbit;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.tv);

        PackageInfo pInfo = null;
        try {
            /*PackageManager manager = context.getPackageManager();
               PackageInfo info = manager.getPackageInfo(
               context.getPackageName(), 0);
                String version = info.versionName;*/
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        int vc = pInfo.versionCode;
        tv.setText("Version name: "+version+" Version Code: "+String.valueOf(vc));

    }
}
