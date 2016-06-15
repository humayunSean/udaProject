/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bhi.humayun.udaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity{

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.detail_activity);
    if(savedInstanceState == null){
      getSupportFragmentManager().beginTransaction().add(R.id.container,new DetailFragment()).commit();
    }
  }
  
  public static class DetailFragment extends Fragment{
    
    private String movieDtls;
    public DetailFragment(){
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
      
      View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
      
      Intent intent = getActivity().getIntent();
      if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
        movieDtls = intent.getStringExtra(Intent.EXTRA_TEXT);
        String[] spil =  movieDtls.split("#");
        ((TextView) rootView.findViewById(R.id.detail_text)).setText(spil[0]);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.detailimage);
        Picasso.with(getActivity()).load(spil[1]).into(imageView);
      }
      return rootView;
    }
  }
}


