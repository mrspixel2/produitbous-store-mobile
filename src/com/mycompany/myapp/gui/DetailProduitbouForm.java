package com.mycompany.myapp.gui;


import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Preferences;
import com.codename1.maps.Coord;
import com.codename1.messaging.Message;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Produitbous;

import java.io.IOException;

public class DetailProduitbouForm extends BaseForm{
    Form current;
    EncodedImage enim;
    Image img;

    public DetailProduitbouForm(Resources res, Produitbous evt){
        super("Newsfeed", BoxLayout.y());
        Container c1=new Container();
        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_ARROW_LEFT, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    new ProduitbousForm(res, current).showBack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            enim=EncodedImage.create("/giphy.gif");
        } catch (IOException ex) {
            //Logger.getLogger(MyApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        Label ll=new Label();
        Label titre=new Label("Nom : "+evt.getNom());
        Label desc=new Label("Description : "+evt.getDescription());
        Label date=new Label("Prix : "+evt.getPrix());
        Label nbrmax=new Label("Quantité : "+evt.getQte());
        Label nbr =new Label("Categorie : "+evt.getCategorie());


        try {
            enim=EncodedImage.create("/giphy.gif");

            img = Image.createImage(FileSystemStorage.getInstance().openInputStream(evt.getImage()));

            add(img);
            add(titre);
            add(desc);
            add(date);
            add(nbr);
            add(nbrmax);


        } catch (IOException ex) {

        }







        LocalNotification n = new LocalNotification();
        n.setId("Rookiebike");
        n.setAlertBody("c'est le temps de voir le produit");
        n.setAlertTitle("Break Time!");
        n.setAlertSound("/notification_sound_bells.mp3"); //file name must begin with notification_sound


        Display.getInstance().scheduleLocalNotification(n,System.currentTimeMillis() ,LocalNotification.REPEAT_MINUTE);
        Display.getInstance().showNotify();


        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
        FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, 3);

        Button back = new Button("Back");
        Button comt= new Button("noter ce produit");
        Button pan= new Button("Ajouter au panier");
        back.requestFocus();
        back.addActionListener(e -> {
            try {
                new ProduitbousForm(res,current).showBack();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        comt.addActionListener(e -> {
            // block this from happening twice
            Preferences.set("alreadyRated", true);
            InteractionDialog id = new InteractionDialog("Please Rate "  + Display.getInstance().getProperty("AppName", "The App"));
            int height = id.getPreferredH();
            Form f = Display.getInstance().getCurrent();
            id.setLayout(new BorderLayout());
            Slider rate = createStarRankSlider();
            Button ok = new Button("OK");
            Button no = new Button("No Thanks");
            id.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(rate)).
                    add(BorderLayout.SOUTH, GridLayout.encloseIn(2, no, ok));
            id.show(f.getHeight()  - height - f.getTitleArea().getHeight(), 0, 0, 0);
            no.addActionListener(ex -> id.dispose());
            ok.addActionListener(exx -> {
                id.dispose();
                if(rate.getProgress() >= 9) {
                    if(Dialog.show("Rate On Store", "Would you mind rating us in the appstore?", "Go To Store", "Dismiss")) {

                    }
                } else {
                    if(Dialog.show("Tell Us Why?", "Would you mind writing us a short message explaining how we can improve?", "Write", "Dismiss")) {
                        Message m = new Message("Heres how you can improve  " + Display.getInstance().getProperty("AppName", "the app"));

                    }
                }
            });
        });
        add(comt);
        add(pan);
        add(back);
    }

    private void initStarRankStyle(Style s, Image star) {
        s.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_BOTH);
        s.setBorder(Border.createEmpty());
        s.setBgImage(star);
        s.setBgTransparency(0);
    }

    private Slider createStarRankSlider() {
        Slider starRank = new Slider();
        starRank.setEditable(true);
        starRank.setMinValue(0);
        starRank.setMaxValue(10);
        Font fnt = Font.createTrueTypeFont("native:MainLight", "native:MainLight").
                derive(Display.getInstance().convertToPixels(5, true), Font.STYLE_PLAIN);
        Style s = new Style(0xffff33, 0, fnt, (byte)0);
        Image fullStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        s.setOpacity(100);
        s.setFgColor(0);
        Image emptyStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        initStarRankStyle(starRank.getSliderEmptySelectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderEmptyUnselectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderFullSelectedStyle(), fullStar);
        initStarRankStyle(starRank.getSliderFullUnselectedStyle(), fullStar);
        starRank.setPreferredSize(new Dimension(fullStar.getWidth() * 5, fullStar.getHeight()));
        return starRank;
    }

}


