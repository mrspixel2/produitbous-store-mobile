package com.mycompany.myapp.services;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.User;

import java.util.ArrayList;

public class ServiceRegister {

    public ArrayList<User> user ;
    public static ServiceRegister instance=null;
    public boolean resultOK;
    private ConnectionRequest req;
    private boolean ret;

    private ServiceRegister() {
        req = new ConnectionRequest();
    }
    public static ServiceRegister getInstance() {
        if (instance == null) {
            instance = new ServiceRegister ();
        }
        return instance;
    }

    public boolean  inscri(User u){
        String url = "http://127.0.0.1:8000/general/store/all/register?username="+u.getUsername()+"&email="+u.getEmail()+"&pass="+u.getPassword();
        System.out.println("*********************");
        System.out.println(url);
        System.out.println("***********************");
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String t = new String(req.getResponseData());
                if(t.contains("information incomplete")){
                    ret=false;
                    System.out.println("Inscription nOn");
                }
                else{
                    ret=true;
                    System.out.println("Inscription Ok");
                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return ret;
    }
}
