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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/11/6.
 */
public class NewProductActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;
    private List<String> list = new ArrayList<String>();
    private TextView myTextView;
    private Spinner mySpinner;
    private  Spinner Spinner2;
    private EditText selectedSpinner;

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adaptert;
    String Leibie []={"请选择商品类别","食品","服装","电器","日常用品"};
    String Place []={"请选择商品购买地点","台北市","新北市","桃园市","台中市","台南市","高雄市"};
    JSONParser jsonParser = new JSONParser();
    EditText inputshangpinname;
    EditText inputshangpinprice;
    EditText product_number;
    EditText inputDesc;


    // url to create new product
    private static String url_create_product = "http://163.17.21.33/android_connect/create_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

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
        // Edit Text 利用在XML文件中定义的View的ID属性来获取相应的view对象
        inputshangpinname = (EditText) findViewById(R.id.inputshangpinname);
        inputshangpinprice = (EditText) findViewById(R.id.inputshangpinprice);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
        product_number=(EditText)findViewById(R.id.product_number);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                ConnectivityManager manager = (ConnectivityManager)
                        getSystemService(CONNECTIVITY_SERVICE);
                //For Data network check
                final boolean is3g =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                final boolean isWifi =
                        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                // creating new product in background thread
                if(validate()){
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(NewProductActivity.this);
                    }
                    else {
                        new CreateNewProduct().execute();
                    }
                }
            }
        });
    }
    private boolean validate()
    {
        String price = inputshangpinprice.getText().toString().trim();
        String name =inputshangpinname.getText().toString().trim();
        String number= product_number.getText().toString().trim();
        String categories_judge=mySpinner.getSelectedItem().toString();
        String places_judge= Spinner2.getSelectedItem().toString();
        float price_judge=0;
        float number_judge=0;
//        float price_judge = new Float(price);
//        float number_judge =new Float(number);
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
     * Background Async Task to Create new product
     * */


    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("新增中..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * 类AsyncTask来异步执行任务*/
        protected String doInBackground(String... args) {
            String name = inputshangpinname.getText().toString();
            String price = inputshangpinprice.getText().toString();
            String description = inputDesc.getText().toString();
            String number =product_number.getText().toString();

            // Building Parameters Android 向Web提供参数params：关键字，表示函数的参数是可变个数的，即可变的方法参数
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("description", description));
            params.add(new BasicNameValuePair("products_number",number));
            params.add(new BasicNameValuePair("user_email",Data_Cache.email_address));
            params.add(new BasicNameValuePair("shangpinleibie", mySpinner.getSelectedItem().toString()));
            params.add(new BasicNameValuePair("goumaididian",Spinner2.getSelectedItem().toString()));



            // getting JSON Object
            // Note that create product url accepts POST method  Android向Web提交参数的4种方式总结：第二种：基于http协议通过post方式提交参数
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product


                    Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    startActivity(i);


                    // closing this screen
                    finish();

                } else {
                    // failed to create product
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
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(NewProductActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
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

