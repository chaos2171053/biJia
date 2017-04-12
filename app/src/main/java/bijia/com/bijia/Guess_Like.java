package bijia.com.bijia;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2015/11/6.
 */
public class Guess_Like extends ListActivity {
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> productsList;
    JSONArray products = null;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GUESS_LIKE_PRODUCTS = "guess_like_products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_SHANGPINLEIBIE = "shangpinleibie";
    private static final String TAG_GOUMAIDIDIAN = "goumaididian";
    private static final String TAG_DANJIA = "danjia";


    private static final String url_guess_like = "http://163.17.21.33/android_connect/product_push.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_like);

        productsList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();
        new Guess_U_Like().execute();
    }

    class Guess_U_Like extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Guess_Like.this);
            pDialog.setMessage("读取中... 请稍候...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_email", Data_Cache.email_address));
            JSONObject json = jParser.makeHttpRequest(url_guess_like, "GET", params);
            Log.d("All Products: ", json.toString());
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_GUESS_LIKE_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String goumaididian = c.getString(TAG_GOUMAIDIDIAN);
                        String shangpinleibie = c.getString(TAG_SHANGPINLEIBIE);
                        String danjia = c.getString(TAG_DANJIA);

                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_GOUMAIDIDIAN, goumaididian);
                        map.put(TAG_SHANGPINLEIBIE, shangpinleibie);
                        map.put(TAG_DANJIA, danjia);
                        productsList.add(map);
                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            Guess_Like.this, productsList,
                            R.layout.guess_like_list_item, new String[]{TAG_PID,
                            TAG_NAME, TAG_GOUMAIDIDIAN, TAG_SHANGPINLEIBIE, TAG_DANJIA},
                            new int[]{R.id.pid, R.id.name, R.id.shangpinleibie, R.id.goumaididian, R.id.danjia});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}

