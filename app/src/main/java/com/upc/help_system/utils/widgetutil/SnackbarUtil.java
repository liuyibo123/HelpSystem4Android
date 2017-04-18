package upc.com.help_system.utils.widgetutil;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.upc.help_system.R;

/**
 * Created by liuyibo on 2017/3/31.
 * textXXX 将snackbar的字体颜色改变
 * info warning alert confirm改变snackbar背景颜色
 */

public class SnackbarUtil{
    private static final int red = 0xfff44336;
    private static final int green = 0xff4caf50;
    private static final int blue = 0xff2195f3;
    private static final int orange = 0xffffc107;


    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }
    private static TextView getSnackBarTextView(Snackbar snackbar){
        TextView textView = (TextView) getSnackBarLayout(snackbar).findViewById(R.id.snackbar_text);
        if(textView!=null){
            return textView;
        }
        return null;
    }

    public static Snackbar colorTextView(Snackbar snackbar ,int colorId){
        TextView textView = getSnackBarTextView(snackbar);
        if(textView!=null){
            textView.setTextColor(colorId);
        }
        return snackbar;
    }
    public static Snackbar colorSnackBar(Snackbar snackbar, int colorId) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
        }

        return snackbar;
    }
    public static Snackbar modifySnackbar(Snackbar snackbar,int backgroundColor, int textColor){
        Snackbar after = colorTextView(snackbar,textColor);
        after = colorSnackBar(after,backgroundColor);
        return after;
    }
    public static Snackbar textRed(Snackbar snackbar){
        return colorTextView(snackbar,red);
    };
    public static Snackbar textGreen(Snackbar snackbar){
        return colorTextView(snackbar,green);
    };
    public static Snackbar textBlue(Snackbar snackbar){
        return colorTextView(snackbar,blue);
    };
    public static Snackbar textOrange(Snackbar snackbar){
        return colorTextView(snackbar,orange);
    };

    public static Snackbar info(Snackbar snackbar) {
        return colorSnackBar(snackbar, blue);
    }

    public static Snackbar warning(Snackbar snackbar) {
        return colorSnackBar(snackbar, orange);
    }

    public static Snackbar alert(Snackbar snackbar) {
        return colorSnackBar(snackbar, red);
    }

    public static Snackbar confirm(Snackbar snackbar) {
        return colorSnackBar(snackbar, green);
    }
}

