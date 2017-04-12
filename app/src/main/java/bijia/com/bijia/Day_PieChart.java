package bijia.com.bijia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/11/6.
 */
public class Day_PieChart extends Activity {
    private com.github.mikephil.charting.charts.PieChart mChart;
    private static String url_day_categories_piechart = "http://163.17.21.33/android_connect/today_product_class.php";
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_FOOD = "food";
    private static final String TAG_CLOTH = "cloth";
    private static final String TAG_APPLIANCES = "appliances";
    private static final String TAG_DAILY_NECESSITIES= "daily_necessities";
    private  String food_number;
    private  String cloth_number;
    private  String appliances_number;
    private  String daily_necessities_number;
    private TextView food;
    private TextView cloth;
    private TextView appliances;
    private TextView daily_necessities;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_categories_pichart);
        getButtonVIEW();
        setButtonEvent();

        mChart = (com.github.mikephil.charting.charts.PieChart) findViewById(R.id.categories_pie_chart);
//        PieData mPieData = getPieData(4, 100);//分成4个部分
        new LoadCategoriesPieChart().execute();
//        showChart(mChart, mPieData);
    }

    private void setButtonEvent() {
        food.setOnClickListener(buttonListener);
        cloth.setOnClickListener(buttonListener);
        appliances.setOnClickListener(buttonListener);
        daily_necessities.setOnClickListener(buttonListener);
    }
    private void getButtonVIEW() {
        food =(TextView)findViewById(R.id.food);
        cloth=(TextView)findViewById(R.id.cloth);
        appliances=(TextView)findViewById(R.id.appliances);
        daily_necessities=(TextView)findViewById(R.id.daily_necessities);
    }
    private View.OnClickListener buttonListener = new View.OnClickListener(){
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
            switch(view.getId()){

                case R.id.food:
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(Day_PieChart.this);
                    }
                    else {
                        Intent intent1 = new Intent();
                        intent1.setClass(Day_PieChart.this, Food_Chart_Day.class);
                        startActivity(intent1);
                    }
                    break;
                case R.id.cloth:
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(Day_PieChart.this);
                    }
                    else {
                        Intent intent2 = new Intent();
                        intent2.setClass(Day_PieChart.this, Cloth_Chart_Day.class);
                        startActivity(intent2);
                    }
                    break;
                case R.id.appliances:
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(Day_PieChart.this);
                    }
                    else {
                        Intent intent3 = new Intent();
                        intent3.setClass(Day_PieChart.this, Applicances_Chart_Day.class);
                        startActivity(intent3);
                    }
                    break;
                case R.id.daily_necessities:
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(Day_PieChart.this);
                    }
                    else {
                        Intent intent4 = new Intent();
                        intent4.setClass(Day_PieChart.this, Daily_Necessities_Chart_Day.class);
                        startActivity(intent4);
                    }
                    break;

            }
        }
    };
    private void showChart(com.github.mikephil.charting.charts.PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
//        pieChart.setHoleRadius(0);  //实心圆

        pieChart.setDescription("每类购买次数/总购买次数");
        mChart.setCenterTextSize(20f);

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText("百分比");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);  //最左边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

//        for (int i = 0; i < count; i++) {
//            xValues.add("" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
//        }

        xValues.add(0 ,"食品");
        xValues.add(1 ,"服装");
        xValues.add(2 ,"电器");
        xValues.add(3, "日常用品");


        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */

        float quarterly1 =   new Float(food_number);
        float quarterly2 = new Float(cloth_number);
        float quarterly3 = new Float(appliances_number);
        float quarterly4 = new Float(daily_necessities_number);

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));
        yValues.add(new Entry(quarterly4, 3));
        if(quarterly1 ==0&&quarterly2 ==0 &&quarterly3 ==0 &quarterly4 ==0)
        {
            Toast.makeText(Day_PieChart.this, "没有购买记录", Toast.LENGTH_SHORT).show();

        }


        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }

    class LoadCategoriesPieChart extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Day_PieChart.this);
            pDialog.setMessage("生成圆饼图中... 请稍候...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_email",Data_Cache.email_address));
            params.add(new BasicNameValuePair("time_sta",Data_Cache.choise_time_pass));
            JSONObject json = jsonParser.makeHttpRequest(url_day_categories_piechart, "GET", params);
            Log.d("test", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray categoriesObj = json.getJSONArray(TAG_CATEGORIES); // JSON Array

                    for (int i = 0; i < categoriesObj.length(); i++) {
                        JSONObject c = categoriesObj.getJSONObject(i);
                        // Storing each json item in variable
                        food_number = c.getString(TAG_FOOD);
                        cloth_number = c.getString(TAG_CLOTH);
                        appliances_number= c.getString(TAG_APPLIANCES);
                        daily_necessities_number= c.getString(TAG_DAILY_NECESSITIES);

                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    PieData mPieData = getPieData(4, 100);//分成4个部分
                    showChart(mChart, mPieData);
                }
            });
        }
    }

    protected void onRestart() {
        super.onRestart();
        new LoadCategoriesPieChart().execute();

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

