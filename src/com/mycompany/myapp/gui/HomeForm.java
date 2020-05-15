/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.io.Cookie;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;

import java.io.IOException;


public class HomeForm extends BaseForm{

    Form current;

    public HomeForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    @Override
    protected boolean isCurrentInbox() {
        return true;
    }



    public HomeForm(Resources res) {

        super.addSideMenu(res);
        current=this;
        setTitle("Home");
        setLayout(BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getContentPane().setScrollVisible(false);


        Container c = new Container(getLayout());
        c.add(new Label("Choose an option"));
        Button btnConsulterProduits = new Button("Consulter les produits");
        Button btnConsulterStores = new Button("Consulter les Stores");


        btnConsulterProduits.addActionListener(e-> {
            new ProduitbousForm(res,current).show();
        });
        btnConsulterStores.addActionListener(e-> {
            new StoreForm(res,current).show();
        });
        c.addAll(btnConsulterProduits,btnConsulterStores);
        add(c);
        
        
    }
    
    
}
