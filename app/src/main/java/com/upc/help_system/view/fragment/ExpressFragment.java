package com.upc.help_system.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.upc.help_system.BuildConfig;
import com.upc.help_system.MyApplication;
import com.upc.help_system.R;
import com.upc.help_system.model.Express;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.MyLoction;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.TimeUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.MyResponse;
import com.upc.help_system.utils.network.RequestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpressFragment extends Fragment {
    Context context;
    ExpressViewHolder expressViewHolder;
    View v;
    MyLoction loc=null;
    public ExpressFragment() {
        context = MyApplication.getContext();
        String myloc = SharedPreferenceUtil.getString("location","location");
        if(myloc!=null&&!myloc.equals("")){
            loc = MyGson.fromJson(myloc,MyLoction.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_express, null);
        if(expressViewHolder==null){
            expressViewHolder = new ExpressViewHolder();
        }
        init();
        return v;
    }

    private void init() {
        List<String> companys = new ArrayList<String>(Arrays.asList("EMS", "申通", "中通", "圆通", "汇通", "韵达", "顺丰"));
        List<String> positon = new ArrayList<String>(Arrays.asList("14号楼", "研二", "邮局", "麦趣尔", "北门"));
        String msg = "";
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        Resources resources = context.getResources();
        String express[] = resources.getStringArray(R.array.express_company);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, express);
        expressViewHolder.express_company.setAdapter(adapter);
        expressViewHolder.name.setText(SharedPreferenceUtil.getString("user","name"));
        expressViewHolder.phone_number.setText(SharedPreferenceUtil.getString("user","phone"));
        expressViewHolder.destination_to.setText(loc.getAddr());
        if (clipboardManager.hasPrimaryClip()) {
            msg = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();  // 获取内容
            String finalMsg = msg;
            String finalMsg1 = msg;
            String company_string = null;
            String location_string = null;
            for (String company : companys) {
                if (stringContains(finalMsg, company)) {
                    company_string = company;
                }
            }
            for (String location : positon) {
                if (stringContains(finalMsg, location)) {
                    location_string = location;
                }
            }
            String tokennumber = getTaken(finalMsg1);
            if(company_string!=null&& !company_string.equals("")){
                String finalCompany_string = company_string;
                String finalLocation_string = location_string;
                Dialog dialog = new AlertDialog.Builder(context)
                        .setTitle("检测到")
                        .setMessage("是否使用剪贴板内容自动填充？")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                expressViewHolder.express_company.setText(finalCompany_string);
                                expressViewHolder.express_company.append(finalLocation_string);
                                expressViewHolder.take_number.setText(tokennumber);
                            }
                        })
                        .create();
                dialog.show();
            }

        }

        expressViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MainTable table = new MainTable();
                table.setContent("帮取快递:" + expressViewHolder.express_company.getText().toString() + "大小：" + Container.volume.values()[expressViewHolder.volume.getSelectedItemPosition()] +
                        "重量：" + Container.weight.values()[expressViewHolder.weight.getSelectedItemPosition()]);
                table.setTip(Float.parseFloat(expressViewHolder.tip.getText().toString()));
                table.setState(1);
                table.setCatagory(Container.EXPRESS);
                table.setHelp_loc(expressViewHolder.destination_to.getText().toString());
                //TODO （1）百度地图自动获取位置
                table.setPub_loc(MyGson.toJson(loc));
                //TODO　(2) 用户类
                table.setPub_person(SharedPreferenceUtil.getString("user","name"));
                table.setPub_time(TimeUtil.getCurrentTime());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService service = retrofit.create(RequestService.class);
                Call<MyResponse> call = service.addMainTable(table);

                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response<MyResponse> response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Express express_temp = new Express();
                        express_temp.setId(response.body().getValue());
                        express_temp.setCompany(expressViewHolder.express_company.getText().toString());
                        express_temp.setName(expressViewHolder.name.getText().toString());
                        express_temp.setPhone_number(expressViewHolder.phone_number.getText().toString());
                        express_temp.setTake_number(expressViewHolder.take_number.getText().toString());
                        express_temp.setVolume(expressViewHolder.volume.getCount());
                        express_temp.setWeight(expressViewHolder.weight.getCount());
                        Call<Void> call2 = service.addExpress(express_temp);
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
                                Snackbar.make(v, "发布成功", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().finish();
                                    }
                                }).show();
                                expressViewHolder.button.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
                                Toast.makeText(context, "连接不到服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
        expressViewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    boolean stringContains(String msg, String content) {
        return msg.indexOf(content) != -1;
    }
    String getTaken(String msg) {
        String regEx = "\\（(\\d+)";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(msg);
        if (mat.find()) {
            System.out.println(mat.group(0));
            System.out.println(mat.group(1));
            return mat.group(1);
        }
        return "";
    }
    class ExpressViewHolder {

        public Button button;
        public AutoCompleteTextView express_company;
        public EditText take_number;
        public EditText name;
        public EditText phone_number;
        public EditText destination_to;
        public EditText tip;
        public Spinner volume;
        public Spinner weight;
        public Button btn_cancel;

        public ExpressViewHolder() {

            button = (Button) v.findViewById(R.id.btn_makesure);
            express_company = (AutoCompleteTextView) v.findViewById(R.id.express_company);
            take_number = (EditText) v.findViewById(R.id.take_number);
            name = (EditText) v.findViewById(R.id.name);
            phone_number = (EditText) v.findViewById(R.id.usr_name);
            destination_to = (EditText) v.findViewById(R.id.destination_to);
            tip = (EditText) v.findViewById(R.id.tip);
            volume = (Spinner) v.findViewById(R.id.volume);
            weight = (Spinner) v.findViewById(R.id.weight);
            btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        }
    }



}
