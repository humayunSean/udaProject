package com.bhi.humayun.udaproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.if.container, new MainFragment()).commit();
        }
    }
    
    public static class MainFragment extends Fragment{
        
        ArrayAdapter<String> movieData;
        
        public MainFragment(){}
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            
            String[] data = {"Hello World"};
            List<String> movieString = new ArrayList<String>(Arrays.asList(data));
            
            movieData = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.list_item_textview, movieString);
            
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            ListView listview = (ListView) rootView.findViewById(R.id.listview_forecast);
            listview.setAdapter(movieData);
            return rootView;
        } 
    }
}
