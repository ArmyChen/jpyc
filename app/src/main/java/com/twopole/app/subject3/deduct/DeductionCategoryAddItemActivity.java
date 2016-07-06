package com.twopole.app.subject3.deduct;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.model.subject3.DeductionItem;

import java.sql.SQLException;

public class DeductionCategoryAddItemActivity extends BaseActivity {
    Button msave_button;
    EditText mde_name;
    EditText mde_reason;
    EditText mde_score;
    String categoryCode;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deduction_category_add_item);
        msave_button = (Button) findViewById(R.id.save_button_item);
        mde_name = (EditText) findViewById(R.id.de_name);//扣分名称
        mde_reason = (EditText) findViewById(R.id.de_reason);//扣分说明
        mde_score = (EditText) findViewById(R.id.de_score);//扣分分数

        categoryCode = getIntent().getStringExtra("categoryCode");//或取分类id
        itemId  = getIntent().getIntExtra("itemId",0);//或取项目id

        if(itemId > 0){//编辑模式
            try {
                DeductionItem deductionItem = getDatabaseHelper().getDeductionItemDao().queryForId(itemId);
                mde_name.setText(deductionItem.getItem_name());
                mde_reason.setText(deductionItem.getItem_reason());
                mde_score.setText(deductionItem.getItem_score());

            } catch (SQLException e) {

            }
        }


        msave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = mde_name.getText().toString();
                String reasonStr = mde_reason.getText().toString();
                String scoreStr = mde_score.getText().toString();
                if(nameStr == "" || reasonStr == ""|| scoreStr ==""){
                        showTip("请输入扣分项目内容");
                        return;
                }

                if(categoryCode != null && categoryCode != ""){//如果分类存在


                    if(itemId > 0){//编辑模式
                        DeductionItem deductionItem = new DeductionItem();
                        deductionItem.setImei(getTelephonyManager().getDeviceId());
                        deductionItem.setItem_category_code(categoryCode);
                        deductionItem.setItem_name(nameStr);
                        deductionItem.setItem_reason(reasonStr);
                        deductionItem.setItem_score(scoreStr);
                        deductionItem.setId(itemId);

                        try {
                            int result =  getDatabaseHelper().getDeductionItemDao().update(deductionItem);
                            if(result > 0){
                                showTip("保存成功");
                            }else {
                                showTip("保存失败");
                            }
                        } catch (SQLException e) {
                            showTip(e.getMessage());
                        }

                    }else{
                        DeductionItem deductionItem = new DeductionItem();
                        deductionItem.setImei(getTelephonyManager().getDeviceId());
                        deductionItem.setItem_category_code(categoryCode);
                        deductionItem.setItem_name(nameStr);
                        deductionItem.setItem_reason(reasonStr);
                        deductionItem.setItem_score(scoreStr);

                        try {
                            int result =  getDatabaseHelper().getDeductionItemDao().create(deductionItem);
                            if(result > 0){
                                showTip("保存成功");
                            }else {
                                showTip("保存失败");
                            }
                        } catch (SQLException e) {
                            showTip(e.getMessage());
                        }
                    }



                }
            }
        });



    }
}
