package com.bhi.humayun.udaproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by humayun on 04-06-2016.
 */
public class MainFragment extends Fragment {

    private final String LOG_TAG = FetchMovieData.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        FetchMovieData fetchMovieData = new FetchMovieData();
        fetchMovieData.execute();
    }

    CustomAdapter customAdapter;

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        String[] data = {"Hello World"};
   //     List<String> movieString = new ArrayList<String>(Arrays.asList(data));

        String[] poster = {"http://image.tmdb.org/t/p/w185//811DjJTon9gD6hZ8nCjSitaIXFQ.jpg"};
    //    List<String> posterString = new ArrayList<String>(Arrays.asList(data));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] titlePath = new String[data.length];
    //    MovieDB[] movieDBs = new MovieDB[data.length];
        for (int i =0;i<data.length;i++ ){
        //    MovieDB movieDB = new MovieDB();
        //    movieDB.titlePath = data[i]+"#"+poster[i];
         //   movieDBs[i] = movieDB;
            titlePath[i] = data[i]+"#"+poster[i];
        }

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(titlePath));

        customAdapter = new CustomAdapter(getActivity(),R.layout.list_item, R.id.list_item_textview, weekForecast);
 //       movieData = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.list_item_textview, movieString);

        ListView listview = (ListView) rootView.findViewById(R.id.listview_forecast);
        listview.setAdapter(customAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            
             @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                String movieDT = customAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), com.bhi.humayun.udaproject.DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieDT);
                startActivity(intent);
            }
        });


        return rootView;
    }

    public class FetchMovieData extends AsyncTask<String,Void,String[]>{


        HttpURLConnection urlConnection = null;
        BufferedReader bufferReader = null;
        String movieJsonStr = null; 
        
        @Override
        protected String[] doInBackground(String... param) {

            try {
                final String Base_Url = "https://api.themoviedb.org/3/discover/movie?";
                final String startDate = "primary_release_date.gte";
                final String endDate = "primary_release_date.lte";
                final String app_id = "api_key";

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
                
                bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                
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
            }catch (IOException e){
                Log.e(LOG_TAG,"UdaProject Error:" + e);
            }finally{
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
            
            try {
                return parseMovieData(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private String[] parseMovieData(String movieString) throws JSONException{
            final String MOVIE_TITLE = "title";
            final String MOVIE_TOP  = "results";
            final String MOVIE_POSTER  = "poster_path";

            final String movieDbPath = "http://image.tmdb.org/t/p/w185/";

            JSONObject movieJson = new JSONObject(movieString);
            JSONArray movieResults = movieJson.getJSONArray(MOVIE_TOP);

            String[] titles = new String[movieResults.length()];
            for(int i = 0; i<movieResults.length();i++){
                String title = "";
                String poster = "";
                JSONObject resultJson = movieResults.getJSONObject(i);

                poster = movieDbPath+resultJson.getString(MOVIE_POSTER);
                title = resultJson.getString(MOVIE_TITLE)+"#"+poster;

                titles[i] = title;
            }
            
            return titles;
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
        protected void onPostExecute(String[] result) {

//            Date date = new Date();
//            String stringDate = date.toString();
//            Toast.makeText(getActivity(), stringDate, Toast.LENGTH_SHORT).show();


               if (result != null) {
                   customAdapter.clear();
                for(String move:result) {
                    customAdapter.add(move);
                }
            }
        }
    }

    public class CustomAdapter extends ArrayAdapter<String>{

        Context context;
        private LayoutInflater inflater = null;
        List<String> movieUrl;
        int tl = 0;
        String[] tit;
        String[] pot;

    /*    public CustomAdapter(Context cont,String[] title, String[] poster){
            context = cont;
            title1 = title;
            poster1 = poster;
            tl = title.length;

        }
    */
   //     public CustomAdapter(Context context, int resource, MovieDB[] mov) {
        public CustomAdapter(Context context, int resource, int rsc, List<String> mov){
            super(context, resource, rsc, mov);
            this.context = context;
            this.movieUrl = mov;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            View rowView = inflater.inflate(R.layout.image_item,null);
            viewHolder.mImg = (ImageView) rowView.findViewById(R.id.imageView1);
            viewHolder.mTxt = (TextView) rowView.findViewById(R.id.text_view1);

            String[] spil =  movieUrl.get(position).split("#");
            Log.v(LOG_TAG, movieUrl.get(position) +"spil huma:" + spil[0]);
            viewHolder.mTxt.setText(spil[0]);
            Picasso.with(context).load(spil[1]).into(viewHolder.mImg);

            return rowView;
        }
    }

    protected static class ViewHolder
    {
        ImageView mImg;
        TextView mTxt;
    }

}





