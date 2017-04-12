package bijia.com.bijia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by user on 2015/11/6.
 */
public class Future_Analysis extends Activity {
    private Button compare_price;
    private EditText inputshangpinname;
    private Spinner spinnerdidian;
    private String name,choice_place;
    private ArrayAdapter<String> adapter;


    String Place []={"请选择地点","台北市","新北市","桃园市","台中市","台南市","高雄市"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_analysis);


        compare_price =(Button)findViewById(R.id.compare_price);
        inputshangpinname =(EditText)findViewById(R.id.inputshangpinname);
        spinnerdidian = (Spinner) findViewById(R.id.spinnerdidian );

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Place);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdidian.setAdapter(adapter);

        spinnerdidian.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });



        compare_price.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                if (validate()) {
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(Future_Analysis.this);
                    }
                    else {
                        Data_Cache.compare_price_product_name = inputshangpinname.getText().toString();
                        Data_Cache.compare_price_product_place = spinnerdidian.getSelectedItem().toString();
                        Intent intent2 = new Intent();
                        intent2.setClass(Future_Analysis.this, Compare_Product_Price.class);
                        startActivity(intent2);
                    }
                }

            }
        });



    }
    private boolean validate()
    {
        name =inputshangpinname.getText().toString().trim();
        choice_place= spinnerdidian.getSelectedItem().toString();
        if (name.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写商品名称", false);
            return false;
        }
        if(choice_place.equals("请选择地点"))
        {
            DialogUtil.showDialog(this, "您还没有选择地点", false);
            return false;
        }

        return true;
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
