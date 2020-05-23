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
import com.mycompany.myapp.entities.Rating;
import com.mycompany.myapp.entities.RatingWidget;
import com.mycompany.myapp.services.ServiceRating;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailProduitbouForm extends BaseForm{
    Form current;
    EncodedImage enim;
    Image img;
    int owner_id = SignInForm.getId();

    public DetailProduitbouForm(Resources res, Produitbous evt){
        super("Newsfeed", BoxLayout.y());
        Container c1=new Container();
        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_ARROW_LEFT, e-> {
            try {
                new ProduitbousForm(res);
            } catch (IOException ioException) {
                ioException.printStackTrace();
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
        RatingWidget ratingwidget = new RatingWidget();
        c1.add(ratingwidget.createRatedStarRankSlider(evt));


        try {
            enim=EncodedImage.create("/giphy.gif");

            img = Image.createImage(FileSystemStorage.getInstance().openInputStream(evt.getImage()));

            add(img);
            add(c1);
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
                new ProduitbousForm(res).show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        comt.addActionListener(e -> {
            RatingWidget r = new RatingWidget();
            Dialog d = new Dialog("Noter ce Produit");
            d.setLayout(BoxLayout.yCenter());
            Container flowLayout = FlowLayout.encloseIn(r.createStarRankSlider());
            d.add(flowLayout);
            Button sb = new Button("submit");
            sb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ex) {
                    Rating rating = new Rating(owner_id,evt.getId(),r.a);
                    ServiceRating.getInstance().AddRank(rating);
                    Dialog.show("Succes", "added rank", new Command("OK"));
                    new DetailProduitbouForm(res,evt).show();
                }
            });
            d.add(sb);
            d.show(getHeight() / 2, 0, 0, 0);
        });
        add(comt);
        add(pan);
        add(back);
    }

}


