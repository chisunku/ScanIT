package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class BotFragment extends Fragment {

    String TAG = "BotFragment";
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatgpt_layout, parent, false);
//        BottomNavigationView navigationView = getActivity().findViewById(R.id.bottom_navigation);
//        navigationView.setCheckedItem(item.getItemId());
        SearchView sv = view.findViewById(R.id.search_view);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submit
                sv.clearFocus();
                searchChatGPT searchChatGPT = new searchChatGPT(view, getActivity(), getContext());
                searchChatGPT.execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text change
                return true;
            }
        });

        CardView cv = view.findViewById(R.id.details);
        DataController db = new DataController(getContext());
        try{
            Cursor c = db.retrieveCart();
            String items = "";
            while(c.moveToNext()){
                items += c.getString(1)+",";
            }
            if(items.length()==0){
                cv.setVisibility(View.INVISIBLE);
            }
            else {
                items="give me 7 unique products similar to these products: "+items;
                Log.d("TAG", "onCreateView: items in cart" + items);
                chatGPT gpt = new chatGPT(view, getActivity(), getContext());
                gpt.execute(items);
//                Log.d(TAG, "onCreateView: response : " + res);
//                JSONObject json = new JSONObject(res);
//                String response = json.getJSONArray("choices").getJSONObject(0).get("text").toString();
//                Log.d(TAG, "onCreateView: answer : "+response);
//                ListView lv = view.findViewById(R.id.list);
//                List<String> listArr = Arrays.asList(response.split("\\n"));
////                listArr.remove(0);
////                listArr.remove(1);
//                for(int i=0;i<listArr.size();i++){
//                    Log.d(TAG, "onCreateView: list has : "+listArr.get(i) );
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listArr);
//                lv.setAdapter(adapter);
            }
        }catch(Exception e){
            Log.e(TAG, "onCreateView: error in home, maybe chatGPT only ");
            e.printStackTrace();
        }

        return view;
    }
}
