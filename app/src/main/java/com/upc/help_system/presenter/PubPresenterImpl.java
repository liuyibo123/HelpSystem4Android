package com.upc.help_system.presenter;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.upc.help_system.R;
import com.upc.help_system.model.ExpressModel;
import com.upc.help_system.model.MainTableModel;
import com.upc.help_system.view.activity.MainActivity;
import com.upc.help_system.view.activity.PubActivity;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/4/26.
 */

public class PubPresenterImpl implements PubPresenter {
    PubActivity view;
    public PubPresenterImpl(PubActivity view) {
        this.view = view;
    }
    @Override
    public void OnTabClicked(String s) {
        switch (s){
            case "取快递":
                View v = view.getLayoutInflater().inflate(R.layout.fragment_express,null);
                view.framePub.removeAllViews();
                view.framePub.addView(v);
                Button button = (Button) view.findViewById(R.id.btn_makesure);
                AutoCompleteTextView express_company = (AutoCompleteTextView) view.findViewById(R.id.express_company);
                EditText take_number = (EditText) view.findViewById(R.id.take_number);
                EditText name = (EditText) view.findViewById(R.id.name);
                EditText phone_number = (EditText) view.findViewById(R.id.phone_number);
                EditText express_description = (EditText) view.findViewById(R.id.express_description);
                EditText content = (EditText) view.findViewById(R.id.content);
                EditText destination_to = (EditText) view.findViewById(R.id.destination_to);
                EditText tip = (EditText) view.findViewById(R.id.tip);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainTableModel.MainTable mainTable = new MainTableModel().new MainTable();
                        mainTable.setPubtime(new Timestamp(System.currentTimeMillis()));
                        mainTable.setAskid(1);
                        mainTable.setContent(content.getText().toString());
                        mainTable.setDestination_from(express_company.getText().toString());
                        mainTable.setDestination_to(destination_to.getText().toString());
                        mainTable.setHelperid(1);
                        mainTable.setId(1);
                        mainTable.setNowperson(0);
                        mainTable.setNeedperson(1);
                        mainTable.setAccepttime(null);
                        mainTable.setState(0);
                        mainTable.setTip(Float.parseFloat(tip.getText().toString()));
                        mainTable.setTname("express");

                        /*
                        * 0 发布未被接收
                        * 1 发布已被接收
                        * 2 已经完成
                        */
                        ExpressModel model = new ExpressModel();
                        model.postExpress("add_express",model.new Express(express_company.getText().toString(),take_number.getText().toString(),name.getText().toString(),phone_number.getText().toString(),express_description.getText().toString()),mainTable);
                    }
                });
        }
    }
}
