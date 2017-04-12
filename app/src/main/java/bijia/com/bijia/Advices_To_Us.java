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
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/11/6.
 */
public class Advices_To_Us extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();



    EditText help_feedback;
    // url to create new product
    private static String url_description = "http://163.17.21.33/android_connect/android_user_feedback/description.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advices_to_us);

        // Edit Text



        help_feedback = (EditText) findViewById(R.id.help_feedback);
        // Create button
        Button tijiao_button = (Button) findViewById(R.id.tijiao_button);

        // button click event
        tijiao_button.setOnClickListener(new View.OnClickListener() {

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
                // creating new product in background thread
                if(validate()) {
                    if (is3g == false && isWifi == false) {
                        setNetworkMethod(Advices_To_Us.this);
                    } else {
                        new Feed_Back().execute();
                    }
                }
            }
        });
    }
    private boolean validate()
    {
        String description = help_feedback.getText().toString().trim();

        if (description.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写建议", false);
            return false;
        }
        return true;
    }
    /**
     * Background Async Task to Create new product
     * */
    class Feed_Back extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Advices_To_Us.this);
            pDialog.setMessage("建议提交中...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {


            String description = help_feedback.getText().toString();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("description", description));
            params.add(new BasicNameValuePair("user_email",Data_Cache.email_address));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_description,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), MainScreen.class);//ProblemProblemProblemProblemProblemProblemProblemProblemProblem
                    startActivity(i);
                    // setContentView(R.layout.thanks);
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();//在命令行中打印出异常信息在程序中出现的位置及原因
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
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

