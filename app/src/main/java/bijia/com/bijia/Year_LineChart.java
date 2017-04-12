package bijia.com.bijia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;

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
public class Year_LineChart extends Activity {
    private static String url_year_month_sta = "http://163.17.21.33/android_connect/history_year_goods_class.php";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_YEAR_STA = "year_sta";
    private static final String TAG_JAN = "jan";
    private static final String TAG_FEB = "feb";
    private static final String TAG_MAR = "mar";
    private static final String TAG_APR = "apr";
    private static final String TAG_MAY = "may";
    private static final String TAG_JUN = "jun";
    private static final String TAG_JUL = "jul";
    private static final String TAG_AUG = "aug";
    private static final String TAG_SEP = "sep";
    private static final String TAG_OCT = "oct";
    private static final String TAG_NOV = "nov";
    private static final String TAG_DEC = "dec";
    private  String jan_number;
    private  String feb_number;
    private  String mar_number;
    private  String apr_number;
    private  String may_number;
    private  String jun_number;
    private  String jul_number;
    private  String aug_number;
    private  String sep_number;
    private  String oct_number;
    private  String nov_number;
    private  String dec_number;

    private LineChart mLineChart;
    private ProgressDialog pDialog;
//  private Typeface mTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.year_linechart);

        mLineChart = (LineChart) findViewById(R.id.chart);

        MarkerView mv = new MyMarkerView(this, R.layout.marker_view);
        mLineChart.setMarkerView(mv);


//      mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
        new LoadYearStaLineChart().execute();
//        LineData mLineData = getLineData(3, 100);
//        showChart(mLineChart, mLineData, Color.rgb(
// ));
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(true);  //是否在折线图上添加边框

        // no description text
        lineChart.setDescription("月消费总额(元)");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//


        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    /**
     * 生成一个数据
     * @param count 表示图表中有多少个坐标点
     * @param range 用来生成range以内的随机数
     * @return
     */
    private LineData getLineData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 1; i <=count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + i+"月");
        }

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
//        for (int i = 0; i < count; i++) {
//            float value = (float) (Math.random() * range) + 3;
//            yValues.add(new Entry(value, i));
//        }
//        float quarterly1 =new Float(jan_number);
//        float quarterly2 =new Float(feb_number);
//        float quarterly3 =new Float(mar_number);
//        float quarterly4 =new Float(apr_number);
//        float quarterly5 =new Float(may_number);
//        float quarterly6 =new Float(jun_number);
//        float quarterly7 =new Float(jul_number);
//        float quarterly8 =new Float(aug_number);
//        float quarterly9 =new Float(sep_number);
//        float quarterly10 =new Float(oct_number);
//        float quarterly11 =new Float(nov_number);
//        float quarterly12 =new Float(dec_number);

        float quarterly1;
        float quarterly2;
        float quarterly3;
        float quarterly4;
        float quarterly5;
        float quarterly6;
        float quarterly7;
        float quarterly8;
        float quarterly9;
        float quarterly10;
        float quarterly11;
        float quarterly12;


        if (jan_number.equals("null")){
            quarterly1=0;
        }
        else
        {
            quarterly1 =new Float(jan_number);
        }


        if (feb_number.equals("null")){
            quarterly2=0;
        }
        else
        {
            quarterly2 =new Float(feb_number);
        }


        if (mar_number.equals("null")){
            quarterly3=0;
        }
        else
        {
            quarterly3 =new Float(mar_number);
        }


        if (apr_number.equals("null")){
            quarterly4=0;
        }
        else
        {
            quarterly4 =new Float(apr_number);
        }


        if (may_number.equals("null")){
            quarterly5=0;
        }
        else
        {
            quarterly5 =new Float(may_number);
        }


        if (jun_number.equals("null")){
            quarterly6=0;
        }
        else
        {
            quarterly6 =new Float(jun_number);
        }
        if (jul_number.equals("null")){
            quarterly7=0;
        }
        else
        {
            quarterly7 =new Float(jul_number);
        }



        if (aug_number.equals("null")){
            quarterly8=0;
        }
        else
        {
            quarterly8 =new Float(aug_number);
        }

        if (sep_number.equals("null")){
            quarterly9=0;
        }
        else
        {
            quarterly9 =new Float(sep_number);
        }
        if (oct_number.equals("null")){
            quarterly10=0;
        }
        else
        {
            quarterly10 =new Float(oct_number);
        }

        if (nov_number.equals("null")){
            quarterly11=0;
        }
        else
        {
            quarterly11 =new Float(nov_number);
        }
        if (dec_number.equals("null")){
            quarterly12=0;
        }
        else
        {
            quarterly12 =new Float(dec_number);
        }

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));
        yValues.add(new Entry(quarterly4, 3));
        yValues.add(new Entry(quarterly5, 4));
        yValues.add(new Entry(quarterly6, 5));
        yValues.add(new Entry(quarterly7, 6));
        yValues.add(new Entry(quarterly8, 7));
        yValues.add(new Entry(quarterly9, 8));
        yValues.add(new Entry(quarterly10,9));
        yValues.add(new Entry(quarterly11, 10));
        yValues.add(new Entry(quarterly12, 11));



        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(2.5f); // 线宽
        lineDataSet.setCircleSize(3.5f);// 显示的圆形大小
        lineDataSet.setColor(Color.rgb(88, 86, 214));// 显示颜色
        lineDataSet.setCircleColor(Color.rgb(255, 45, 85));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        XAxis xAxis = mLineChart.getXAxis(); // 把X坐标轴放置到底部。默认的是在顶部。
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
//        lineDataSet.setDrawCubic(true);//圆滑线
        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        return lineData;
    }
    class LoadYearStaLineChart extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Year_LineChart.this);
            pDialog.setMessage("生成折线图中... 请稍候...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_email",Data_Cache.email_address));
            params.add(new BasicNameValuePair("time_sta",Data_Cache.choise_time_pass));
            JSONObject json = jsonParser.makeHttpRequest(url_year_month_sta, "GET", params);
            Log.d("test", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray yearstaObj = json.getJSONArray(TAG_YEAR_STA); // JSON Array

                    for (int i = 0; i < yearstaObj.length(); i++) {
                        JSONObject c = yearstaObj.getJSONObject(i);
                        // Storing each json item in variable

                        jan_number= c.getString(TAG_JAN);
                        feb_number = c.getString(TAG_FEB);
                        mar_number= c.getString(TAG_MAR);
                        apr_number= c.getString(TAG_APR);
                        may_number= c.getString(TAG_MAY);
                        jun_number=c.getString(TAG_JUN);
                        jul_number=c.getString(TAG_JUL);
                        aug_number=c.getString(TAG_AUG);
                        sep_number=c.getString(TAG_SEP);
                        oct_number=c.getString(TAG_OCT);
                        nov_number=c.getString(TAG_NOV);
                        dec_number=c.getString(TAG_DEC);

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
                    LineData mLineData = getLineData(12, 100);
                    showChart(mLineChart, mLineData, Color.WHITE);
                }
            });
        }
    }
    protected void onRestart() {
        super.onRestart();
        new LoadYearStaLineChart().execute();

    }
    private class MyMarkerView extends MarkerView {

        private TextView tvContent;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            float n = (float) e.getVal();
            tvContent.setText(n + "");
            // if (e instanceof CandleEntry) {
            // CandleEntry ce = (CandleEntry) e;
            // tvContent.setText(""
            // + Utils.formatNumber(ce.getHigh(), 0, true));
            // } else {
            // tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
            // }
        }

        @Override
        public int getXOffset() {
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset() {
            return -getHeight();
        }
    }
}

