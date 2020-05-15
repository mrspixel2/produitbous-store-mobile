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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hpp
 */
public class ServiceLogin {



    public ArrayList<User> user ;
    public static ServiceLogin instance=null;
    public boolean resultOK;
    private ConnectionRequest req;
    private String Timestamp;


    private ServiceLogin() {
        req = new ConnectionRequest();
    }
    public static ServiceLogin getInstance() {
        if (instance == null) {
            instance = new ServiceLogin ();
        }
        return instance;
    }


    public ArrayList<User>  parseAnnonce(String jsonText){
        try {
            user=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            if(list==null) return null ;
            for(Map<String,Object> obj : list){
                User t = new User();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int) id);
                user.add(t);
            }


        } catch (IOException ex) {

        }
        return user ;
    }
    public ArrayList<User>  getUserlogin(String nom , String mdp){
        String url = "http://127.0.0.1:8000/general/store/all/login?nom="+nom+"&mdp="+mdp;
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
