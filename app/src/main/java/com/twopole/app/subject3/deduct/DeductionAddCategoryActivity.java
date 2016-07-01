package com.twopole.app.subject3.deduct;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.model.subject3.DeductionCategory;

import java.sql.SQLException;

public class DeductionAddCategoryActivity extends BaseActivity {
    Button msave_button;
    EditText mcate_name;
    String categoryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deduction_add_category);
        msave_button = (Button) findViewById(R.id.save_button);
        mcate_name = (EditText) findViewById(R.id.cate_name);

        categoryCode = getIntent().getStringExtra("categoryCode");
        if (!categoryCode.isEmpty()) {//编辑操作
            try {
                DeductionCategory deductionCategory = new DeductionCategory();
                deductionCategory.setCategory_code(String.valueOf(categoryCode));
                mcate_name.setText(getDatabaseHelper().getDeductionCategoryDao().queryForMatching(deductionCategory).get(0).getCategory_name());
            } catch (SQLException e) {
                showTip("没有此条数据");
            }
        }

        msave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcate_name.getText().toString() != "") {
                    if (!categoryCode.isEmpty()) {//编辑操作
                        try {
                            DeductionCategory deductionCategory2 = new DeductionCategory();
                            deductionCategory2.setCategory_code(String.valueOf(categoryCode));
                            DeductionCategory result = getDatabaseHelper().getDeductionCategoryDao().queryForMatching(deductionCategory2).get(0);
                            DeductionCategory deductionCategory = new DeductionCategory();
                            deductionCategory.setImei(getTelephonyManager().getDeviceId());
                            deductionCategory.setCategory_name(mcate_name.getText().toString());
                            deductionCategory.setCategory_code(result.getCategory_code());
                            deductionCategory.setId(result.getId());

                            int result2 = getDatabaseHelper().getDeductionCategoryDao().update(deductionCategory);
                            if (result2 > 0) {
                                showTip("保存成功");
                            }
                        } catch (SQLException e) {
                            showTip("保存失败");
                        }
                    } else {
                        DeductionCategory deductionCategory = new DeductionCategory();
                        deductionCategory.setImei(getTelephonyManager().getDeviceId());
                        deductionCategory.setCategory_name(mcate_name.getText().toString());
                        try {

                            int result2 = getDatabaseHelper().getDeductionCategoryDao().create(deductionCategory);
                            if (result2 > 0) {
                                showTip("保存成功");
                            }
                        } catch (SQLException e) {
                            showTip("保存失败");
                        }
                    }

                } else {
                    showTip("分类名称不能为空");
                }
            }
        });
    }
}
