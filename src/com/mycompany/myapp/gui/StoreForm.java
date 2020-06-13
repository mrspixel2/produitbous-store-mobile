/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.SpanLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Produitbous;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.services.ServiceProduitbous;
import com.mycompany.myapp.services.ServiceStore;
import com.mycompany.myapp.services.ServiceTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoreForm extends BaseForm {


    public StoreForm(Resources res) throws IOException {
        super.addSideMenuStore(res);
        setLayout(BoxLayout.y());
        ArrayList<Store> lc = ServiceStore.getInstance().getAllStore1();
        for (Store cl : lc) {
            System.out.println(cl.getNom());
            addButton(cl.getNom()+" : "+cl.getDescription(),cl);
        }
    }


    private void addButton(String title,Store s) {
        Button image = new Button();
        Container cnt = BorderLayout.west(image);
        cnt.setLeadComponent(image);
        TextArea ta = new TextArea(title);
        ta.setUIID("NewsTopLine");
        ta.setEditable(false);

        cnt.add(BorderLayout.CENTER,
                BoxLayout.encloseY(ta));
        add(cnt);
        image.addActionListener(e -> {
            try {
                selectedStore(s);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private void addButton(Image img, String title, boolean liked, int likeCount, int commentCount, Produitbous p,Form f) {
        int height = Display.getInstance().convertToPixels(11.5f);
        int width = Display.getInstance().convertToPixels(14f);
        Button image = new Button(img.fill(width, height));
        image.setUIID("Label");
        Container cnt = BorderLayout.west(image);
        cnt.setLeadComponent(image);
        TextArea ta = new TextArea(title);
        ta.setUIID("NewsTopLine");
        ta.setEditable(false);

        Label likes = new Label(likeCount + " TND  ", "NewsBottomLine");
        likes.setTextPosition(RIGHT);
        if(!liked) {
            FontImage.setMaterialIcon(likes, FontImage.MATERIAL_FAVORITE);
        } else {
            Style s = new Style(likes.getUnselectedStyle());
            s.setFgColor(0xff2d55);
            FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, s);
            likes.setIcon(heartImage);
        }
        Label comments = new Label(commentCount + " Quatité", "NewsBottomLine");
        FontImage.setMaterialIcon(likes, FontImage.MATERIAL_CHAT);


        cnt.add(BorderLayout.CENTER,
                BoxLayout.encloseY(
                        ta,
                        BoxLayout.encloseX(likes, comments)
                ));
        f.add(cnt);
        image.addActionListener(e -> {
            new DetailProduitbouForm(res,p).show();
        });
    }

    void selectedStore(Store s) throws IOException {
        Form results = new Form("Searching...", new BoxLayout(BoxLayout.Y_AXIS));
        ArrayList<Produitbous> lc = ServiceProduitbous.getInstance().getAllProduitbous();
        for (Produitbous cl : lc) {
            System.out.println("le id de store du produit : "+cl.getStore_id());
            System.out.println("le id de store : "+s.getId());
            if(cl.getStore_id().equals(Integer.toString(s.getId())))
                addButton(Image.createImage(FileSystemStorage.getInstance().openInputStream(cl.getImage())),cl.getNom()+" : "+cl.getDescription(), true, (int) Double.parseDouble(cl.getPrix()), (int) Double.parseDouble(cl.getQte()),cl,results);
        }
        getToolbar().addMaterialCommandToLeftBar("retour", FontImage.MATERIAL_KEYBOARD_RETURN, e-> new SearchProduitForm(true, null, null).showBack());
        results.show();
    }


}


