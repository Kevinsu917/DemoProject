package io.githut.kevinsu917.demoproject;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import io.githut.kevinsu917.demoproject.customview.CyclePager;

public class CycleAdActivity extends BaseActivity {

    CyclePager cyclePager;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_ad);
        getSupportActionBar().setTitle(R.string.activity_cycle_ad_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //初始化数据
        ArrayList<String> defaultData = new ArrayList<>();
        defaultData.add("http://pic25.nipic.com/20121112/5955207_224247025000_2.jpg");
        defaultData.add("http://www.pptbz.com/pptpic/UploadFiles_6909/201204/2012041411433867.jpg");

        //刷新的数据
        ArrayList<String> refreshData = new ArrayList<>();
        refreshData.add("http://pic20.nipic.com/20120428/5455122_162725484388_2.jpg");
        refreshData.add("http://www.pptbz.com/pptpic/UploadFiles_6909/201110/20111014111307895.jpg");
        refreshData.add("http://scimg.jb51.net/allimg/160618/77-16061Q44U6444.jpg");
        refreshData.add("http://picm.photophoto.cn/082/050/005/0500050031.jpg");

        cyclePager = (CyclePager) findViewById(R.id.cyclePager);
        cyclePager.start(defaultData);

        textView = (TextView) findViewById(R.id.textview);
        textView.setOnClickListener(v -> cyclePager.refreshData(refreshData));
    }
}
