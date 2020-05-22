package com.mycompany.myapp.services;

import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Produitbous;
import com.mycompany.myapp.entities.Produits;
import com.mycompany.myapp.entities.Rating;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceRating {

    public ArrayList<Rating> Ratings;

    public static ServiceRating instance=null;
    public boolean resultOK;
    private ConnectionRequest req;

    private ServiceRating() {
        req = new ConnectionRequest();
    }

    public static ServiceRating getInstance() {
        if (instance == null) {
            instance = new ServiceRating();
        }
        return instance;
    }

    public ArrayList<Rating> parseRating(String jsonText){
        try {
            Ratings =new ArrayList<Rating>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Rating r = new Rating();
                float id = Float.parseFloat(obj.get("id").toString());
                r.setId((int) id);
                r.setId_produitbou((int) obj.get("id_produit"));
                r.setId_user((int) obj.get("id_user"));
                r.setRate((int) obj.get("rat"));
                Ratings.add(r);
            }


        } catch (IOException ex) {

        }
        return Ratings;
    }

    public ArrayList<Rating> parseRate(String jsonText){
        try {
            Ratings =new ArrayList<Rating>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Rating r = new Rating();
                r.setRate((int) Float.parseFloat(obj.get("1").toString()));
                Ratings.add(r);
            }


        } catch (IOException ex) {

        }
        return Ratings;
    }

    public boolean AddRank(Rating r){
        String url = "http://127.0.0.1:8000/general/rating/add?iduser="+r.getId_user()+"&idproduit="+r.getId_produitbou()+"&rate="+r.getRate();
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public ArrayList<Rating> getrate(Produitbous p){
        String url = "http://127.0.0.1:8000/general/rating/prod?idproduit="+p.getId();
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                if(req.getResponseData() != null){
                Ratings = parseRate(new String(req.getResponseData()));
                req.removeResponseListener(this);}
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return Ratings;
    }

}
