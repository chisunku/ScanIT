package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class searchChatGPT extends AsyncTask<String, String, String> {

    View v;
    Activity a;

    Context context;
    String TAG = "chatGPT";
    ProgressBar progressBar;

    public searchChatGPT(View ctx, Activity a, Context c){
        this.v = ctx;
        this.a = a;
        context = c;
        progressBar = v.findViewById(R.id.searchPB);
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        Log.d("TAG", "doInBackground: in do in back");
        try{

            URL url = new URL("https://api.openai.com/v1/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer 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");
            connection.setRequestMethod("POST");

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", "give me a list of 7 products for" + strings[0]);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 256);

            OutputStream outputStream = connection.getOutputStream();
            byte[] requestBodyBytes = requestBody.toString().getBytes("utf-8");
            outputStream.write(requestBodyBytes, 0, requestBodyBytes.length);
            outputStream.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line);
            }
            response = responseBuilder.toString();

            int responseCode = connection.getResponseCode();
            Log.i("TAG", "Server responded with: " + responseCode+" response: "+response );
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("TAG", "doInBackground: reponse got ");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        CardView search = v.findViewById(R.id.searchcard);
        search.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        progressBar.setVisibility(View.GONE);
        super.onPostExecute(s);
        Log.d("TAG", "postExecute: "+s);
        DataController db = new DataController(context);
//        Log.d(TAG, "onCreateView: response : " + res);
        try {
            JSONObject json = new JSONObject(s);
            String response = json.getJSONArray("choices").getJSONObject(0).get("text").toString();
            Log.d(TAG, "onCreateView: answer : " + response);
            ListView lv = v.findViewById(R.id.searchresult);
            List<String> listArr = Arrays.asList(response.split("\\n"));
            ArrayList<String> arr = new ArrayList<>(listArr);
            if(arr.size()>2) {
                arr.remove(0);
//                arr.remove(1);
            }
            for (int i = 0; i < arr.size(); i++) {
                if(arr.get(i).length()<1)
                    arr.remove(i);
                Log.d(TAG, "onCreateView: list has : " + arr.get(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_activated_1, arr);
            lv.setAdapter(adapter);
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, lv);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            Log.d(TAG, "onPostExecute: height "+totalHeight );
            ViewGroup.LayoutParams params = lv.getLayoutParams();
            params.height = totalHeight + (lv.getDividerHeight() * adapter.getCount()) + 100;
            adapter.notifyDataSetChanged();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Do something when an item is clicked
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    db.insertTodo(selectedItem.replaceAll("^\\d.",""),0);
                    Toast.makeText(context, "You clicked " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
