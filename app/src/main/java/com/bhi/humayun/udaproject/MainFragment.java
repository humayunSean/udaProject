package com.bhi.humayun.udaproject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by humayun on 04-06-2016.
 */
public class MainFragment extends Fragment {



    @Override
    public void onStart() {
        super.onStart();
        FetchMovieData fetchMovieData = new FetchMovieData();
        fetchMovieData.execute();
    }

    ArrayAdapter<String> movieData;

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        String[] data = {"Hello World","Not so hello"};
        List<String> movieString = new ArrayList<String>(Arrays.asList(data));

        movieData = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.list_item_textview, movieString);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listview = (ListView) rootView.findViewById(R.id.listview_forecast);
        listview.setAdapter(movieData);



        return rootView;
    }

    public class FetchMovieData extends AsyncTask<Void,Void,Void>{

        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        HttpURLConnection urlConnection = null;
        BufferReader bufferReader = null;
        String movieJsonStr = null; 
        @Override
        protected Void doInBackground(Void... params) {

            try {
                final String Base_Url = "https://api.themoviedb.org/3/discover/movie?";
                final String startDate = "primary_release_date.gte";
                final String endDate = "primary_release_date.lte";
                final String app_id = "app_key";

                Date date = new Date();
                String stringDate = date.toString();

                int len = stringDate.length();
                String[] subDate = stringDate.split(" ");

                String todayDate = dateSimplified(subDate);
                String stDate = startdateSimplified(subDate);

                Uri movieUrl  = Uri.parse(Base_Url).buildUpon()
                    .appendQueryParameter(startDate,stDate)
                    .appendQueryParameter(endDate,todayDate)
                    .appendQueryParameter(app_id,"35f4a6a472c42472f7f14863e5c108a2").build();
                    
                URL url = new URL(movieUrl.toString());    
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                
                InputStream inputStream =  urlConnection.getInputStream();
                
                if(inputStream == null){
                    return null;
                }
                
                bufferReader = new BufferReader(new InputStreamReader(inputStream));
                
                String line;
                StringBuffer buffer = new StringBuffer();
                if((line = bufferReader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0){
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG,"Date UdaProject:" + movieUrl);
            }catch (IOException  e){
                Log.e(LOG_TAG,"UdaProject Error:" + e);
            }finally(){
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(bufferReader != null){
                    try{
                        bufferReader.close();
                    }catch(IOException e){
                        Log.e(LOG_TAG,"UdaProject Error:" + e);
                    }
                }
            }

            return null;
        }

        public String monthSimplified(String date){
            String month ="";
            switch(date){
                case "Jan": month = "01";
                case "Feb": month = "02";
                case "Mar": month = "03";
                case "Apr": month = "04";
                case "May": month = "05";
                case "Jun": month = "06";
                case "Jul": month = "07";
                case "Aug": month = "08";
                case "Sep": month = "09";
                case "Oct": month = "10";
                case "Nov": month = "11";
                case "Dec": month = "12";
            }
            return month;
        }

        public String dateSimplified(String[] date){
            String month = monthSimplified(date[1]);
            String startD = date[5]+"-"+month+"-"+date[2];
            return startD;
        }

        public String startdateSimplified(String[] date){
            String month = monthSimplified(date[1]);
            if(month == "1"){
                month = "12";
            }else if(month == "12" || month == "11"){
                int day = Integer.parseInt(month);
                day--;
                month = Integer.toString(day);
            }else{
                int day = Integer.parseInt(month);
                day--;
                month = "0"+ Integer.toString(day);
            }
            String startD = date[5]+"-"+month+"-"+date[2];
            return startD;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Date date = new Date();
            String stringDate = date.toString();
            Toast.makeText(getActivity(), stringDate, Toast.LENGTH_SHORT).show();
        }
    }
}
