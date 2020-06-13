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
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.services.ServiceStore;
import com.mycompany.myapp.services.ServiceTask;

import java.io.IOException;

/**
 *
 * @author bhk
 */
public class AddStoreForm extends BaseForm{

    public AddStoreForm(Form previous, Resources res) {
        setTitle("Ajouter Store");
        setLayout(BoxLayout.y());

        TextField tfNom = new TextField("","nom du store");
        TextField tfDescription= new TextField("", "description");
        Button btnValider = new Button("Add store");

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((tfNom.getText().length()==0)||(tfDescription.getText().length()==0))
                    Dialog.show("Alert", "Please fill all the fields", new Command("OK"));
                else
                {
                    try {
                        Store t = new Store(tfNom.getText(), tfDescription.getText());
                        if( ServiceStore.getInstance().AddStore(t))
                            Dialog.show("Success","Connection accepted",new Command("OK"));
                        else
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                    } catch (NumberFormatException e) {
                        Dialog.show("ERROR", "entrer nom et description propre", new Command("OK"));
                    }

                }


            }
        });

        addAll(tfNom,tfDescription,btnValider);

        getToolbar().addMaterialCommandToLeftBar("Back", FontImage.MATERIAL_ARROW_BACK, e-> {
            try {
                new StoreForm(res).show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }


}