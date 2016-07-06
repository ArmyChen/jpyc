package com.twopole.app.treasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.twopole.app.R;
import com.twopole.app.base.BaseActivity;
import com.twopole.app.base.BaseOnHeaderActivity;

public class ArticleActivity extends BaseOnHeaderActivity {

    private TextView article_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        article_content = (TextView) findViewById(R.id.article_content);
        initHeader();

        int type = getIntent().getIntExtra("articleType",0);

        switch (type){
            case 0:
                break;
            case 1:
                article_content.setText("\t\t重庆两极软件有限公司（2pole）是一家致力于提供卓越的软件服务和优质产品的软件开发公司。\n公司倾力打造的驾驶技能考试系统是公司的核心产品，为许多驾校带来了教学考核的方便，提高了驾校的声誉和收益。\n客户的成功即是我们的成功，我们勇于创新，我们锐意进取。为中国驾驶技能培训做出积极的贡献。");
                setMyTitle("联系我们");
                break;

        }

    }
}
