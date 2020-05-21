package com.mycompany.myapp.gui;

import com.codename1.components.*;
import com.codename1.contacts.Contact;

import com.codename1.io.Log;
import com.codename1.ui.*;

import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;

import java.io.IOException;


public class SearchProduitForm extends BaseForm {

    public SearchProduitForm(Form previous, Resources res){


        Toolbar.setGlobalToolbar(true);
        Form hi = new Form("Search", BoxLayout.y());
        hi.add(new InfiniteProgress());
        Display.getInstance().scheduleBackgroundTask(()-> {
            // this will take a while...
            Contact[] cnts = Display.getInstance().getAllContacts(true, true, true, true, false, false);
            Display.getInstance().callSerially(() -> {
                hi.removeAll();
                for(Contact c : cnts) {
                    MultiButton m = new MultiButton();
                    m.setTextLine1(c.getDisplayName());
                    m.setTextLine2(c.getPrimaryPhoneNumber());
                    Image pic = c.getPhoto();
                    hi.add(m);
                }
                hi.revalidate();
            });
        });

        hi.getToolbar().addSearchCommand(e -> {
            String text = (String)e.getSource();
            if(text == null || text.length() == 0) {
                // clear search
                for(Component cmp : hi.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
                hi.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                for(Component cmp : hi.getContentPane()) {
                    MultiButton mb = (MultiButton)cmp;
                    String line1 = mb.getTextLine1();
                    String line2 = mb.getTextLine2();
                    boolean show = line1 != null && line1.toLowerCase().indexOf(text) > -1 ||
                            line2 != null && line2.toLowerCase().indexOf(text) > -1;
                    mb.setHidden(!show);
                    mb.setVisible(show);
                }
                hi.getContentPane().animateLayout(150);
            }
        }, 4);

        hi.show();



    }

}
