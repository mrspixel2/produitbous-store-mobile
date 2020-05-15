package com.mycompany.myapp.services;

import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.entities.User;
import com.mycompany.myapp.gui.SignInForm;
import com.mycompany.myapp.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceStore {

    public ArrayList<Store> Store;

    public static ServiceStore instance=null;
    public boolean resultOK;
    private ConnectionRequest req;
    int owner_id = SignInForm.getId();

    private ServiceStore() {
        req = new ConnectionRequest();
    }

    public static ServiceStore getInstance() {
        if (instance == null) {
            instance = new ServiceStore();
        }
        return instance;
    }


    public ArrayList<Store> parseStore(String jsonText){
        try {
            Store=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Store t = new Store();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int) id);
                //t.setOwner_id((Integer) obj.get("owner_id"));
                t.setNom(obj.get("nom").toString());
                t.setDescription(obj.get("description").toString());

                Store.add(t);
            }


        } catch (IOException ex) {

        }
        return Store;
    }

    public ArrayList<Store> getAllStore(){
        String url ="http://127.0.0.1:8000/general/store/all/store";//change this in the future//
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Store = parseStore(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return Store;
    }

    public ArrayList<Store> getUserStores(){
        String url = "http://127.0.0.1:8000/general/Store/getUserStores/"+owner_id;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Store = parseStore(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return Store;
    }

    public boolean AddStore(Store p){

        String url = "http://127.0.0.1:8000/general/store/addstore/"+owner_id+"/"+p.getNom()+"/"+p.getDescription();
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

    public ArrayList<Store> SearchStore(String key){
        String url ="http://127.0.0.1/general/Store"+key;//change this in the future//
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Store = parseStore(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return Store;
    }

    public boolean RemoveStore(Store p){
        String url = Statics.BASE_URL + "/api/tasks/remove?"+p.getId();
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


}
