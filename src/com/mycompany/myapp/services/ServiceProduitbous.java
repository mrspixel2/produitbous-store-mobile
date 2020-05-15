package com.mycompany.myapp.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Produitbous;
import com.mycompany.myapp.entities.Produits;
import com.mycompany.myapp.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceProduitbous {
    public ArrayList<Produitbous> produitbous;

    public static ServiceProduitbous instance=null;
    public boolean resultOK;
    private ConnectionRequest req;

    private ServiceProduitbous() {
        req = new ConnectionRequest();
    }

    public static ServiceProduitbous getInstance() {
        if (instance == null) {
            instance = new ServiceProduitbous();
        }
        return instance;
    }


    public ArrayList<Produitbous> parseProduitbous(String jsonText){
        try {
            produitbous=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Produitbous t = new Produitbous();
                t.setId((Integer) obj.get("id"));
                t.setStore_id((Integer) obj.get("store_id"));
                t.setNom(obj.get("nom").toString());
                t.setDescription(obj.get("description").toString());
                t.setCategorie(obj.get("categorie").toString());
                t.setImage(obj.get("image").toString());
                t.setPrix((Integer) obj.get("prix"));
                t.setQte((Integer) obj.get("qteTotal"));


                produitbous.add(t);
            }


        } catch (IOException ex) {

        }
        return produitbous;
    }

    public ArrayList<Produitbous> getAllProduitbous(){
        String url ="http://127.0.0.1/general/produitbous";//change this in the future//
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                produitbous = parseProduitbous(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return produitbous;
    }

    public boolean AddProduitbous(Produitbous p){
        String url = "http://127.0.0.1:8000/general/produitbou/ajouter/bike1/bike1/qsdqsd.jpg/10/12/vélo/1";
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


    public ArrayList<Produitbous> SearchProduitbous(String key){
        String url ="http://127.0.0.1/general/produitbous"+key;//change this in the future//
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                produitbous = parseProduitbous(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return produitbous;
    }

    public boolean RemoveProduitbous(Produitbous p){
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
