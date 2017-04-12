package bijia.com.bijia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/11/6.
 */
public class Compare_Product_Price extends Activity {
    private LineChart chart;
    JSONParser jsonParser = new JSONParser();
    private static String url_bijia = "http://163.17.21.33/android_connect/bijia.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BIJIA= "bijia";
    private ProgressDialog pDialog;
    ArrayList<String> date_data = new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_price);

        chart = (LineChart) findViewById(R.id.chart);
        MarkerView mv = new MyMarkerView(this, R.layout.marker_view);
        chart.setMarkerView(mv);
        new LoadComparePrize().execute();
    }
    private void showchart() {

        String dataset_label1 = "最近90天价格走势";
        ArrayList<Entry> yVals1 = new ArrayList<>();
        int n=0;
        for (int i=0;i<90;i++){
            if(new Float(date_data.get(i))==0){
                n=n+1;
                continue;

            }else {
                yVals1.add(new Entry(new Float(date_data.get(i)), i));
            }
        }
        if(n==90)
        {
            Toast.makeText(Compare_Product_Price.this, "最近90天没有用户" + "在" + Data_Cache.compare_price_product_place + "购买 " + Data_Cache.compare_price_product_name + " 的记录", Toast.LENGTH_SHORT).show();
        }
        else {
            DecimalFormat df = new DecimalFormat("#.#");//只显示小数点后1位
            Toast.makeText(Compare_Product_Price.this, "明天"+Data_Cache.compare_price_product_place+"的 "+Data_Cache.compare_price_product_name + " 的价格可能是" + df.format(new Float(date_data.get(180))) + "元", Toast.LENGTH_LONG).show();
        }
        LineDataSet dataSet1 = new LineDataSet(yVals1, dataset_label1);
        dataSet1.setColor(Color.rgb(79, 199, 50));
        dataSet1.setLineWidth(5);
        dataSet1.setCircleSize(5);
        dataSet1.setDrawValues(false);
        dataSet1.setValueTextSize(15);

        String dataset_label2 = "价格预测";
        ArrayList<Entry> yVals2 = new ArrayList<>();
        for (int i=0;i<90;i++){
            if(new Float(date_data.get(90+i))==0){
                continue;}
            else{
                yVals2.add(new Entry(new Float(date_data.get(90+i)),i));
            }
        }


        LineDataSet dataSet2 = new LineDataSet(yVals2, dataset_label2);
        dataSet2.setColor(Color.rgb(0, 122, 255));
        dataSet2.setHighLightColor(Color.rgb(255, 59, 48)); // 高亮的线的颜色
        dataSet2.setLineWidth(3);
//        dataSet2.setDrawValues(true);
        dataSet2.setCircleSize(0);
//        dataSet2.setDrawCubic(true);//圆滑线
//        dataSet2.setCubicIntensity(0.0001f);
        dataSet2.setValueTextSize(15);

        List<LineDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(dataSet1);
        dataSetList.add(dataSet2);

        List<String> xVals = new ArrayList<>();
        for (int i = 89; i>=0; i--) {
            // x轴显示的数据

            if(i==0){
                xVals.add(89,"今天");
            }
            else {
                xVals.add(i+"天前");
            }

        }




        LineData data = new LineData(xVals, dataSetList);
        XAxis xAxis = chart.getXAxis(); // 把X坐标轴放置到底部。默认的是在顶部。
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setDescription("单价 元");
        chart.setDrawBorders(true); //是否在折线图上添加边框
        chart.setPinchZoom(true);
        chart.setMaxVisibleValueCount(5);//一屏超过5个时不显示具体数值，设置超过60无效
//        chart.animateX(2500);
        chart.animateY(2500);
//        chart.animateXY(2500, 3000);//x、y轴动画
        chart.setData(data);
        chart.invalidate();

    }
    class LoadComparePrize extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Compare_Product_Price.this);
            pDialog.setMessage("生成折线图中... 请稍候...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            String product_name = Data_Cache.compare_price_product_name;
            String product_place =Data_Cache.compare_price_product_place;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("choise_name",product_name));
            params.add(new BasicNameValuePair("choise_place", product_place));
            JSONObject json = jsonParser.makeHttpRequest(url_bijia, "GET", params);
            Log.d("test", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray bijiaObj = json.getJSONArray(TAG_BIJIA); // JSON Array
                    for (int i = 0; i < bijiaObj.length(); i++) {
                        JSONObject c = bijiaObj.getJSONObject(i);
                        for(;i<=180;i++){
                            date_data.add(c.getString("b" + "+" + String.valueOf(i)));
                        }

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
                    showchart();

                }
            });
        }
    }
    private class MyMarkerView extends MarkerView {

        private TextView tvContent;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            DecimalFormat mf = new DecimalFormat("#.#");//只显示小数点后1位
            float n = (float) e.getVal();
            tvContent.setText(mf.format(n) + "元");
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

