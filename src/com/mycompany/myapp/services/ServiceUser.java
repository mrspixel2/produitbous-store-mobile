package com.mycompany.myapp.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hpp
 */
public class ServiceUser {
    public ArrayList<User> user ;
    public static ServiceUser instance=null;
    public boolean resultOK;
    private ConnectionRequest req;
    private ServiceUser() {
        req = new ConnectionRequest();
    }
    public static ServiceUser  getInstance() {
        if (instance == null) {
            instance = new ServiceUser ();
        }
        return instance;
    }

    public ArrayList<User> parseAnnonce(String jsonText){
        try {
            user=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                User u = new User() ;
                float id = Float.parseFloat(obj.get("id").toString());
                u.setId((int)id);
                u.setImg_user(obj.get("img_user").toString());
                u.setNom(obj.get("nom").toString());
                user.add(u);
            }
        } catch (IOException ex) {
        }
        return user ;
    }
    public ArrayList<User> getAllTasks(int id){
        String url = "http://127.0.0.1:8000/general/user/"+id;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                user = parseAnnonce(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return user;
    }



}
