package bijia.com.bijia;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import bijia.com.bijia.activity.LoginActivity;
import bijia.com.bijia.helper.SQLiteHandler;
import bijia.com.bijia.helper.SessionManager;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLiteHandler db;
    private SessionManager session;
    private TextView user_Email;
    private long exitTime = 0;
    private Button id_tab_xinzeng,id_tab_favourite,id_tab_history,id_tab_weilaifenxi;
    private TextView id_tab_day_week_month_sta,btnViewProducts,id_tab_places_sta,id_tab_categories_sta,creat_product,history_chart,future_analysis,guess_like_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);



        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();


        String email = user.get("email");

        // Displaying the user details on the screen
//        user_name.setText(name);
//        txtEmail.setText(email);
//        Data_Cache.email_address =txtEmail.getText().toString();
//        Data_Cache.choise_time_pass=null;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main_screen);
        user_Email =(TextView)header.findViewById(R.id.user_Email);
        user_Email.setText(email);
        Data_Cache.email_address =user_Email.getText().toString();
        Data_Cache.choise_time_pass=null;


        getButtonVIEW();
        setButtonEvent();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.exit_account) {
            logoutUser();


        } else if (id == R.id.nav_send) {
            Intent intent1 = new Intent();
            intent1.setClass(MainScreen.this, Advices_To_Us.class);
            startActivity(intent1);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    private void setButtonEvent() {
        id_tab_xinzeng.setOnClickListener(buttonListener);
        btnViewProducts.setOnClickListener(buttonListener);
        id_tab_day_week_month_sta.setOnClickListener(buttonListener);
        id_tab_favourite.setOnClickListener(buttonListener);
        id_tab_places_sta.setOnClickListener(buttonListener);
        id_tab_categories_sta.setOnClickListener(buttonListener);
        id_tab_history.setOnClickListener(buttonListener);
        id_tab_weilaifenxi.setOnClickListener(buttonListener);
        creat_product.setOnClickListener(buttonListener);
        history_chart.setOnClickListener(buttonListener);
        future_analysis.setOnClickListener(buttonListener);
        guess_like_btn.setOnClickListener(buttonListener);


    }
    private void getButtonVIEW() {
        id_tab_xinzeng =(Button)findViewById(R.id.id_tab_xinzeng);
        creat_product=(TextView)findViewById(R.id.creat_product);
        btnViewProducts =(TextView)findViewById(R.id.btnViewProducts);
        id_tab_weilaifenxi=(Button)findViewById(R.id.id_tab_weilaifenxi);
        id_tab_day_week_month_sta=(TextView)findViewById(R.id.id_tab_day_week_month_sta);
        id_tab_favourite =(Button)findViewById(R.id.id_tab_favorite);
        id_tab_places_sta=(TextView)findViewById(R.id.id_tab_places_sta);
        id_tab_categories_sta=(TextView)findViewById(R.id.id_tab_categories_sta);
        history_chart=(TextView)findViewById(R.id.history_chart);
        id_tab_history=(Button)findViewById(R.id.id_tab_history);
        future_analysis=(TextView)findViewById(R.id.future_analysis);
        guess_like_btn=(TextView)findViewById(R.id.guess_like_btn);


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
                case R.id.id_tab_xinzeng:
                    Intent intent1 = new Intent();
                    intent1.setClass(MainScreen.this, NewProductActivity.class);
                    startActivity(intent1);
                    break;
                               case R.id.btnViewProducts:
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(MainScreen.this);
                    }
                    else{
                        Intent intent2 =new Intent();
                        intent2.setClass(MainScreen.this,AllProductsActivity.class);
                        startActivity(intent2);
                    }
                    break;
//
                case R.id.id_tab_day_week_month_sta:
                    Data_Cache.choise_time_pass=null;
                    Intent intent3 =new Intent();
                    intent3.setClass(MainScreen.this,Day_Week_Month_Year_Sta.class);
                    startActivity(intent3);
                    break;
                case R.id.id_tab_favorite:
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(MainScreen.this);
                    }
                    else {
                        Intent intent4 = new Intent();
                        intent4.setClass(MainScreen.this, Guess_Like.class);
                        startActivity(intent4);
                    }
                    break;
//
                case R.id.id_tab_places_sta:
                    final String[] place_sta = {
                            "台北市","新北市","桃园市","台中市","台南市","高雄市","关闭"
                    };new AlertDialog.Builder(MainScreen.this)
                        .setItems(place_sta, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        if (is3g == false && isWifi == false) {
                                            setNetworkMethod(MainScreen.this);
                                        } else {
                                            Intent intent6 = new Intent();
                                            intent6.setClass(MainScreen.this, Taibei_Sta.class);
                                            startActivity(intent6);
                                        }
                                        break;
                                    case 1:
                                        if (is3g == false && isWifi == false) {
                                            setNetworkMethod(MainScreen.this);
                                        } else {
                                            Intent intent7 = new Intent();
                                            intent7.setClass(MainScreen.this, Xinbei_Sta.class);
                                            startActivity(intent7);
                                        }
                                        break;
                                    case 2:
                                        if (is3g == false && isWifi == false) {
                                            setNetworkMethod(MainScreen.this);
                                        } else {
                                            Intent intent8 = new Intent();
                                            intent8.setClass(MainScreen.this, Taoyuan_Sta.class);
                                            startActivity(intent8);
                                        }
                                        break;
                                    case 3:
                                        if (is3g == false && isWifi == false) {
                                            setNetworkMethod(MainScreen.this);
                                        } else {
                                            Intent intent9 = new Intent();
                                            intent9.setClass(MainScreen.this, Taizhong_Sta.class);
                                            startActivity(intent9);
                                        }
                                        break;
                                    case 4:
                                        if (is3g == false && isWifi == false) {
                                            setNetworkMethod(MainScreen.this);
                                        } else {
                                            Intent intent10 = new Intent();
                                            intent10.setClass(MainScreen.this, Tainan_Sta.class);
                                            startActivity(intent10);
                                        }
                                        break;
                                    case 5:
                                        if (is3g == false && isWifi == false) {
                                            setNetworkMethod(MainScreen.this);
                                        } else {
                                            Intent intent11 = new Intent();
                                            intent11.setClass(MainScreen.this, Gaoxiong_Sta.class);
                                            startActivity(intent11);
                                        }
                                        break;
                                    case 6:
                                        Toast.makeText(MainScreen.this, "关闭", Toast.LENGTH_SHORT).show();


                                }
                            }
                        })
                        .show();
                    break;
                case R.id.id_tab_categories_sta:
                    final String[] categories_sta = {
                            "食品","服装","电器","日常用品","关闭"
                    };
                    new AlertDialog.Builder(MainScreen.this)
                            .setItems(categories_sta, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            if (is3g == false && isWifi == false) {
                                                setNetworkMethod(MainScreen.this);
                                            } else {
                                                Intent intent12 = new Intent();
                                                intent12.setClass(MainScreen.this, Food_Sta.class);
                                                startActivity(intent12);
                                            }
                                            break;
                                        case 1:
                                            if (is3g == false && isWifi == false) {
                                                setNetworkMethod(MainScreen.this);
                                            } else {
                                                Intent intent13 = new Intent();
                                                intent13.setClass(MainScreen.this, Cloth_Sta.class);
                                                startActivity(intent13);
                                            }
                                            break;
                                        case 2:
                                            if (is3g == false && isWifi == false) {
                                                setNetworkMethod(MainScreen.this);
                                            } else {
                                                Intent intent14 = new Intent();
                                                intent14.setClass(MainScreen.this, Appliances_Sta.class);
                                                startActivity(intent14);
                                            }
                                            break;
                                        case 3:
                                            if (is3g == false && isWifi == false) {
                                                setNetworkMethod(MainScreen.this);
                                            } else {
                                                Intent intent15 = new Intent();
                                                intent15.setClass(MainScreen.this, Daily_Necessities_Sta.class);
                                                startActivity(intent15);
                                            }
                                            break;
                                        case 4:
                                            Toast.makeText(MainScreen.this, "关闭", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            })
                            .show();
                    break;
                case R.id.id_tab_history:
                    Data_Cache.choise_time_pass=null;
                    Intent intent16 =new Intent();
                    intent16.setClass(MainScreen.this,History_Chart.class);
                    startActivity(intent16);
                    break;
                case R.id.id_tab_weilaifenxi:
                    Intent intent17=new Intent();
                    intent17.setClass(MainScreen.this,Future_Analysis.class);
                    startActivity(intent17);
                    break;
                case R.id.creat_product:
                    Intent intent18 = new Intent();
                    intent18.setClass(MainScreen.this, NewProductActivity.class);
                    startActivity(intent18);
                    break;
                case R.id.history_chart:
                    Data_Cache.choise_time_pass=null;
                    Intent intent19 =new Intent();
                    intent19.setClass(MainScreen.this,History_Chart.class);
                    startActivity(intent19);
                    break;
                case R.id.future_analysis:
                    Intent intent20=new Intent();
                    intent20.setClass(MainScreen.this, Future_Analysis.class);
                    startActivity(intent20);
                    break;
                case R.id.guess_like_btn:
                    if(is3g == false && isWifi == false )
                    {
                        setNetworkMethod(MainScreen.this);
                    }
                    else {
                        Intent intent21 = new Intent();
                        intent21.setClass(MainScreen.this, Guess_Like.class);
                        startActivity(intent21);
                    }
                    break;



            }
        }
    };


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
