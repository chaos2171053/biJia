package bijia.com.bijia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
public class EditProductActivity extends Activity {

    private EditText txtName;
    private EditText txtPrice;
    private  EditText txtDesc;
    private EditText product_number;
    private Button btnSave;
    private Button btnDelete;


    String pid;

    // Progress Dialog
    private ProgressDialog pDialog;
    private List<String> list = new ArrayList<String>();
    private TextView myTextView;
    private Spinner mySpinner;
    private  Spinner Spinner2;

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adaptert;
    String Leibie []={"请选择商品类别","食品","服装","电器","日常用品"};
    String Place []={"请选择商品购买地点","台北市","新北市","桃园市","台中市","台南市","高雄市"};
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_detials = "http://163.17.21.33/android_connect/get_product_details.php";

    // url to update product
    private static final String url_update_product = "http://163.17.21.33/android_connect/update_product.php";

    // url to delete product
    private static final String url_delete_product = "http://163.17.21.33/android_connect/delete_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_SHANGPINLEIBIE = "shangpinleibie";
    private static final String TAG_GOUMAIDIDIAN = "goumaididian";
    private static final String  TAG_USER_EMAIL ="user_email";
    private static final String TAG_PRODUCTS_NUMBER="products_number";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        txtName = (EditText) findViewById(R.id.inputshangpinname);
        txtPrice = (EditText) findViewById(R.id.inputshangpinprice);
        txtDesc = (EditText) findViewById(R.id.inputDesc);
        product_number =(EditText)findViewById(R.id.product_number);





        // myTextView = (TextView) findViewById(R.id.textView);
        mySpinner = (Spinner) findViewById(R.id.spinnerleibie);
        Spinner2 =(Spinner) findViewById(R.id.spinnerdidian);
        // 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Leibie);
        adaptert = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Place);

        // 第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 第四步：将适配器添加到下拉列表上
        mySpinner.setAdapter(adapter);
        Spinner2.setAdapter(adaptert);
        // 第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
						/* 将所选mySpinner 的值带入myTextView 中 */
//                myTextView.setText("您选择的是：" + adapter.getItem(arg2));


            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                myTextView.setText("NONE");
            }
        });
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);

        // Getting complete product details in background thread
        new GetProductDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                // starting background task to update product
                if(validate()){
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(EditProductActivity.this);
                    }
                    else {
                        new SaveProductDetails().execute();
                    }
                }
            }});

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                // deleting product in background thread
                if(is3g == false && isWifi == false )
                {
                    setNetworkMethod(EditProductActivity.this);
                }
                else {
                    new DeleteProduct().execute();
                }
            }
        });

    }
    private boolean validate()
    {
        String price = txtPrice.getText().toString().trim();
        String name =txtName.getText().toString().trim();
        String number= product_number.getText().toString().trim();
        String categories_judge=mySpinner.getSelectedItem().toString();
        String places_judge= Spinner2.getSelectedItem().toString();
        float price_judge=0;
        float number_judge=0;
        if (name.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写商品名称", false);
            return false;
        }


        if (price.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写商品总价", false);
            return false;
        }
        else if(price.equals("."))
        {
            DialogUtil.showDialog(this, "商品总价格式不符", false);
            return false;
        }
        else{
            price_judge = new Float(price);
        }


        if(number.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写商品数量", false);
            return false;
        }
        else if(number.equals("."))
        {
            DialogUtil.showDialog(this, "商品数量格式不符", false);
            return false;
        }
        else{
            number_judge =new Float(number);

        }


        if(price_judge<=0)
        {
            DialogUtil.showDialog(this, "商品总价必须大于0", false);
            return false;
        }

        if(number_judge<=0)
        {
            DialogUtil.showDialog(this, "商品数量必须大于0", false);
            return false;
        }


        if(categories_judge.equals("请选择商品类别"))
        {
            DialogUtil.showDialog(this, "您还没有选择商品类别", false);
            return false;
        }


        if(places_judge.equals("请选择商品购买地点"))
        {
            DialogUtil.showDialog(this, "您还没有选择商品购买地点", false);
            return false;
        }



        return true;
    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("读取中... 请稍候...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text

                            // display product data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtPrice.setText(product.getString(TAG_PRICE));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
                            product_number.setText(product.getString(TAG_PRODUCTS_NUMBER));
                            String categories=product.getString(TAG_SHANGPINLEIBIE);
                            String place =product.getString(TAG_GOUMAIDIDIAN);

                            switch (categories)
                            {
                                case "食品":
                                    mySpinner.setSelection(1,true);
                                    break;
                                case "服装":
                                    mySpinner.setSelection(2,true);
                                    break;
                                case "电器":
                                    mySpinner.setSelection(3,true);
                                    break;
                                case "日常用品":
                                    mySpinner.setSelection(4,true);
                                    break;
                            }
//                            if (categories.equals("食品")){
//                                mySpinner.setSelection(1,true);
//                            }
//                            if(categories.equals("服装")){
//                                mySpinner.setSelection(2,true);
//                            }
//                            if(categories.equals("电器")){
//                                mySpinner.setSelection(3,true);
//                            }
//                            if(categories.equals("日常用品")){
//                                mySpinner.setSelection(4,true);
//                            }
//                            if(place.equals("台北市")){
//                                Spinner2.setSelection(1,true);
//                            }
//                            if(place.equals("新北市")){
//                                Spinner2.setSelection(2,true);
//                            }
//                            if(place.equals("桃园市")){
//                                Spinner2.setSelection(3,true);
//                            }
//                            if(place.equals("台中市")){
//                                Spinner2.setSelection(4,true);
//                            }
//                            if(place.equals("台南市")){
//                                Spinner2.setSelection(5,true);
//                            }
//                            if(place.equals("高雄市")){
//                                Spinner2.setSelection(6,true);
//                            }
                            switch (place)
                            {
                                case "台北市":
                                    Spinner2.setSelection(1,true);
                                    break;
                                case "新北市":
                                    Spinner2.setSelection(2,true);
                                    break;
                                case  "桃园市":
                                    Spinner2.setSelection(3,true);
                                    break;
                                case "台中市":
                                    Spinner2.setSelection(4,true);
                                    break;
                                case "台南市":
                                    Spinner2.setSelection(5,true);
                                    break;
                                case "高雄市":
                                    Spinner2.setSelection(6,true);
                                    break;
                            }


                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save product Details
     * *///<url类型，进度值类型，返回值类型>
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("保存中 ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = txtName.getText().toString();
            String price = txtPrice.getText().toString();
            String description = txtDesc.getText().toString();
            String number =product_number.getText().toString();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_PRICE, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));
            params.add(new BasicNameValuePair(TAG_PRODUCTS_NUMBER,number));
            params.add(new BasicNameValuePair(TAG_USER_EMAIL,Data_Cache.email_address));
            params.add(new BasicNameValuePair(TAG_SHANGPINLEIBIE,mySpinner.getSelectedItem().toString()));
            params.add(new BasicNameValuePair(TAG_GOUMAIDIDIAN, Spinner2.getSelectedItem().toString()));
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            Toast.makeText(EditProductActivity.this, "成功修改", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("删除中...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_product, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            Toast.makeText(EditProductActivity.this, "成功删除", Toast.LENGTH_SHORT).show();

        }

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


