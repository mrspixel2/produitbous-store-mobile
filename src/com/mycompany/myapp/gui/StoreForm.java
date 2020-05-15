/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.services.ServiceStore;
import com.mycompany.myapp.services.ServiceTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StoreForm extends BaseForm {

    private Form current;
    ArrayList<Store> list;


    public StoreForm(Resources res,Form previous) {

        super("Stores", BoxLayout.y());
        Form fo = this;
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Liste des Stores");
        super.addSideMenuStore(res);




        ArrayList<Store> lc = ServiceStore.getInstance().getAllStore();
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        for (Store cl : lc) {
            data.add(createListEntry(cl.getId() , cl.getNom(), cl.getDescription()));
        }
        DefaultListModel<Map<String, Object>> model = new DefaultListModel<>(data);
        MultiList ml = new MultiList(model);
        fo.add(ml);
        fo.show();
    }

    private Map<String, Object> createListEntry(int id, String name, String description) {


        int mm = Display.getInstance().convertToPixels(3);
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(mm * 3, mm * 4, 0), false);
        Image icon1 = URLImage.createToStorage(placeholder, "icon1", "photo");



        Map<String, Object> entry = new HashMap<>();
       /* try {
            enc = EncodedImage.create(photo);
        } catch (IOException e) {
        }
        imgs = URLImage.createToStorage(enc,url,url,URLImage.RESIZE_SCALE);
        imgv = new ImageViewer(imgs);*/
        entry.put("Line1",id);
        entry.put("Line2", name);
        entry.put("Line3", description);

        return entry;
    }
}


