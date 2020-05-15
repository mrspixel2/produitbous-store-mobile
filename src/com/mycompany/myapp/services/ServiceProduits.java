/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.services;


import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Produits;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jakou
 */
public class ServiceProduits {
    
     public ArrayList<Produits> tasks;
    
    public static ServiceProduits instance=null;
    public boolean resultOK;
    private ConnectionRequest req;

    private ServiceProduits() {
         req = new ConnectionRequest();
    }

    public static ServiceProduits getInstance() {
        if (instance == null) {
            instance = new ServiceProduits();
        }
        return instance;
    }

    
        public ArrayList<Produits> parseTasks(String jsonText){
        try {
            tasks=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            
            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Produits t = new Produits();
              //  float id = Float.parseFloat(obj.get("id").toString());
                t.setIdvelo(obj.get("idvelo").toString());
               t.setDate(obj.get("date").toString());
               t.setImage(obj.get("image").toString());
               t.setDescription(obj.get("description").toString());
                       t.setEmail(obj.get("email").toString());
                       t.setLocalisation(obj.get("localisation").toString());
                       t.setNom(obj.get("nom").toString());
                                              t.setPrix(obj.get("prix").toString());

                tasks.add(t);
            }
            
            
        } catch (IOException ex) {
            
        }
        return tasks;
    }
    
    public ArrayList<Produits> getAllTasks(){
        String url ="http://127.0.0.1/pide/web/app_dev.php/Espace/?api=1";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                tasks = parseTasks(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return tasks;
    }
    public int Contacter(Produits p){
        String url ="http://127.0.0.1/pide/web/app_dev.php/Espace/mail?api=1&id=1&idvelo="+p.getIdvelo();
        req.setUrl(url);
        req.setPost(false);
        NetworkManager.getInstance().addToQueue(req);

          return 1;
    }
     public void reclamation(Produits p,String t , String c){
        String url ="http://127.0.0.1/pide/web/app_dev.php/reclamation/new?api=1&id=1&idvelo="+p.getIdvelo()+"&type="+t+"&continue="+c;
        req.setUrl(url);
        req.setPost(false);
        NetworkManager.getInstance().addToQueue(req);

              }
}
