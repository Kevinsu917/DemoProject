package io.githut.kevinsu917.demoproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvActivity;
    private ActivityAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        readActivity();

        rvActivity = (RecyclerView) findViewById(R.id.rvActivity);
        linearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ActivityAdapter();
        rvActivity.setLayoutManager(linearLayoutManager);
        rvActivity.setAdapter(mAdapter);
    }

    private void readActivity(){
        try {
            String packageName = this.getClass().getPackage().getName();
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            ActivityInfo[] infos = packageInfo.activities;
            for(ActivityInfo info : infos){
                if(info.name.contains(packageName) && !info.name.contains("MainActivity")){
                    data.add(info.name);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>{

        @Override
        public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ActivityViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(ActivityViewHolder holder, int position) {
            String name = data.get(position);
            holder.button.setText(name.substring(name.lastIndexOf(".") + 1, name.length()));
            holder.button.setTag(name);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ActivityViewHolder extends RecyclerView.ViewHolder{

            Button button;
            public ActivityViewHolder(View itemView) {
                super(itemView);
                button = (Button) itemView.findViewById(R.id.button);
                button.setOnClickListener(v -> {
                    String name = (String) v.getTag();
                    Intent intent = new Intent();
                    intent.setClassName(MainActivity.this, name);
                    startActivity(intent);
                });
            }
        }
    }
}
