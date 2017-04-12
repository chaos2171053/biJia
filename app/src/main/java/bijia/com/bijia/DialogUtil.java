package bijia.com.bijia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by user on 2015/11/6.
 */
public class DialogUtil {
    // 定义一个显示消息的对话框
    public static void showDialog(final Context ctx
            , String msg , boolean closeSelf)
    {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setMessage(msg).setCancelable(false);
        if(closeSelf)
        {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // 结束当前Activity
                    ((Activity)ctx).finish();
                }
            });
        }
        else
        {
            builder.setPositiveButton("确定", null);
        }
        builder.create().show();
    }
    // 定义一个显示指定组件的对话框
    public static void showDialog(Context ctx , View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setView(view).setCancelable(false)
                .setPositiveButton("确定", null);
        builder.create()
                .show();
    }
}

