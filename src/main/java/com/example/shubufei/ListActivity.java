package com.example.shubufei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.icbc.R;
import com.example.shubufei.adapter.FunctionAdapter;
import com.example.shubufei.adapter.GongYiAdapter;
import com.example.shubufei.untils.SpacesItemDecoration;
import com.example.shubufei.adapter.cMakeLineAdapter;
import com.example.shubufei.untils.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton buttonReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView=findViewById(R.id.rv_list);
        buttonReturn=findViewById(R.id.iv_return);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getData(getIntent().getStringExtra("ctype"),"");

    }
    private void getData(String ctype,String cvalue) {

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("methodname","GetRefData");
            jsonObject.put("ctype",ctype);
            jsonObject.put("cvalue",cvalue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String obj=jsonObject.toString();
        Log.i("json object",obj);

        Call<ResponseBody> data = Request.getRequestbody(obj);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    Gson gson = new Gson();
                    final List<DataBean> dataBeanList=new ArrayList<>();
                    JsonArray arry = new JsonParser().parse(response.body().string()).getAsJsonArray();
                    for (JsonElement jsonElement : arry) {
                        dataBeanList.add(gson.fromJson(jsonElement, DataBean.class));
                    }
                    if(getIntent().getStringExtra("ctype").equals("GongYi")){
                        GongYiAdapter adapter=new GongYiAdapter(dataBeanList);
                        adapter.setOnitemClickLintener(new GongYiAdapter.OnitemClick() {
                            @Override
                            public void onItemClick(int position) {
                                if (position==0){
                                    return;
                                }
                                Intent intent=new Intent();
                                intent.putExtra("data",dataBeanList.get(position));
                                setResult(100,intent);
                                finish();
                            }
                        });
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
                        recyclerView.setAdapter(adapter);

                    }else if(getIntent().getStringExtra("ctype").equals("cMakeLine")){
                        cMakeLineAdapter adapter=new cMakeLineAdapter(dataBeanList);
                        adapter.setOnitemClickLintener(new cMakeLineAdapter.OnitemClick() {
                            @Override
                            public void onItemClick(int position) {
                                if (position==0){
                                    return;
                                }
                                Intent intent=new Intent();
                                intent.putExtra("data",dataBeanList.get(position));
                                setResult(200,intent);
                                finish();
                            }
                        });
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else if(getIntent().getStringExtra("ctype").equals("Inventory")){
                        FunctionAdapter adapter=new FunctionAdapter(dataBeanList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
                        recyclerView.setAdapter(adapter);
                        adapter.setOnitemClickLintener(new FunctionAdapter.OnitemClick() {
                            @Override
                            public void onItemClick(int position) {
                                if (position==0){
                                    return;
                                }
                                Intent intent=new Intent();
                                intent.putExtra("data",dataBeanList.get(position));
                                setResult(300,intent);
                                finish();
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            } });

    }


}