package bijia.com.bijia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by user on 2015/11/6.
 */
public class Day_Week_Month_Year_Sta extends Activity {
    private Button choise_time_button = null;
    private Button day_sta,week_sta,month_sta,year_sta;
    private TextView display_time_Textview=null;
    private final static int DATE_DIALOG = 0;
    private Calendar c = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_week_month_year_sta);

        display_time_Textview=(TextView)findViewById(R.id.display_time_Textview);
        day_sta=(Button)findViewById(R.id.day_sta);
        month_sta=(Button)findViewById(R.id.month_sta);
        week_sta =(Button)findViewById(R.id.week_sta);
        year_sta =(Button)findViewById(R.id.year_sta);
        choise_time_button = (Button) findViewById(R.id.choise_time_button);

        display_time_Textview.setText(Data_Cache.choise_time_pass);
        Toast.makeText(Day_Week_Month_Year_Sta.this, "提示:先选择要查询的日期", Toast.LENGTH_SHORT).show();

//        day_sta.setEnabled(false);
//        week_sta.setEnabled(false);
//        month_sta.setEnabled(false);
//        year_sta.setEnabled(false);


        choise_time_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
//                day_sta.setEnabled(true);
//                week_sta.setEnabled(true);
//                month_sta.setEnabled(true);
//                year_sta.setEnabled(true);

            }
        });
        day_sta.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                if(is3g == false && isWifi == false )
                {
                    setNetworkMethod(Day_Week_Month_Year_Sta.this);
                }
                else {
                    if (validate()) {
                        Intent intent1 = new Intent();
                        intent1.setClass(Day_Week_Month_Year_Sta.this, Day_Sta.class);
                        startActivity(intent1);
                    }
                }
            }
        });
        week_sta.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                if(is3g == false && isWifi == false )
                {
                    setNetworkMethod(Day_Week_Month_Year_Sta.this);
                }
                else {
                    if (validate()) {
                        Intent intent2 = new Intent();
                        intent2.setClass(Day_Week_Month_Year_Sta.this, Week_Sta.class);
                        startActivity(intent2);
                    }
                }
            }
        });
        month_sta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                if (is3g == false && isWifi == false) {
                    setNetworkMethod(Day_Week_Month_Year_Sta.this);
                } else {
                    if (validate()) {
                        Intent intent3 = new Intent();
                        intent3.setClass(Day_Week_Month_Year_Sta.this, Month_Sta.class);
                        startActivity(intent3);
                    }

                }
            }
        });
        year_sta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                if (is3g == false && isWifi == false) {
                    setNetworkMethod(Day_Week_Month_Year_Sta.this);
                } else {
                    if (validate()) {
                        Intent intent4 = new Intent();
                        intent4.setClass(Day_Week_Month_Year_Sta.this, Year_Sta.class);
                        startActivity(intent4);
                    }
                }
            }
        });
    }
    private boolean validate()
    {
        String time = display_time_Textview.getText().toString().trim();
        if (time.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有选择时间", false);
            return false;
        }
        return true;
    }

    /**
     * 创建日期及时间选择对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DIALOG:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                                display_time_Textview.setText(year+"-"+(month+1)+"-"+dayOfMonth);
//                                display_time_Textview.setText("您选择了：" + year + "年" + (month+1) + "月" + dayOfMonth + "日");
                                Data_Cache.choise_time_pass=String.valueOf(year+"-"+(month+1)+"-"+dayOfMonth);
//                                Toast.makeText(getApplicationContext(), Data_Cache.choise_time_pass,Toast.LENGTH_SHORT).show();
                            }
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;

        }
        return dialog;
    }
    public static void setNetworkMethod(final Context context){
        //提示对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent=null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }

}

