package com.mycompany.myapp.gui;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.InfiniteScrollAdapter;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.*;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Produitbous;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.services.ServiceProduitbous;
import com.mycompany.myapp.services.ServiceStore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchStoreForm extends BaseForm {

    private List<Map<String, String>> recentSearchesList;
    private static final Toolbar.BackCommandPolicy BACK_POLICY = Toolbar.BackCommandPolicy.AS_ARROW;


    /**
     * This method shows the main user interface of the app
     *
     * @param back indicates if we are currently going back to the main form which will display it with a back transition
     * @param errorMessage an error message in case we are returning from a search error
     * @param listings the listing of alternate spellings in case there was an error on the server that wants us to prompt the user
     * for different spellings
     */
    public SearchStoreForm(boolean back, String errorMessage, List<Map<String, Object>> listings) {
        current=this;
        setTitle("Home");
        setLayout(BoxLayout.y());
        getToolbar().setTitleCentered(true);

        TextField search = new TextField("", "recherche store", 20, TextArea.ANY);

        // the component group gives the buttons and text field that rounded corner iOS look when running on iOS. It does nothing on other platforms by default
        Button go = new Button("Go");
        ComponentGroup gp = ComponentGroup.enclose(search, go);

        go.addActionListener(evt -> {
            if(search.getText().length() > 0) {
                try {
                    performSearch(search.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Dialog.show("Error", "You need to enter a search string", "OK", null);
            }
        });


        // we place the other elements in the box Y layout so they are one on top of the other
        Container boxY = BoxLayout.encloseY(
                new SpanLabel("Use the form below to search for houses to buy. You can search by place-name, postcode, or click 'My Location', to search in your current location!"),
                gp);
        add(boxY);



    }

    void performSearch(final String text) throws IOException {

        Form searchResults = new Form("Searching...", new BoxLayout(BoxLayout.Y_AXIS));
        ArrayList<Store> lc = ServiceStore.getInstance().getAllStore1();
        for (Store cl : lc) {
            System.out.println(similarity(cl.getNom(),text));
            if(similarity(cl.getNom(),text) >= 0.3)
                addButton(cl.getNom()+" : "+cl.getDescription(),searchResults,cl);
        }
        getToolbar().addMaterialCommandToLeftBar("retour", FontImage.MATERIAL_KEYBOARD_RETURN, e-> new SearchProduitForm(true, null, null).showBack());
        searchResults.show();

    }



    private void addButton(String title,Form f,Store s) {
        Button image = new Button();
        Container cnt = BorderLayout.west(image);
        cnt.setLeadComponent(image);
        TextArea ta = new TextArea(title);
        ta.setUIID("NewsTopLine");
        ta.setEditable(false);

        cnt.add(BorderLayout.CENTER,
                BoxLayout.encloseY(ta));
        f.add(cnt);
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

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        /* // If you have StringUtils, you can use it to calculate the edit distance:
        return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                                                             (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

}