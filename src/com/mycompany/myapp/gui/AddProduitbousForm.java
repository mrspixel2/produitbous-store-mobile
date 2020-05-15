package com.mycompany.myapp.gui;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.services.ServiceStore;
import com.mycompany.myapp.services.ServiceTask;

import java.util.ArrayList;

public class AddProduitbousForm extends Form{

    Resources res;

    public AddProduitbousForm(Form previous) {
        setTitle("Add a new task");
        setLayout(BoxLayout.y());

        TextField tfName = new TextField("","Nom du produit");
        TextField tfDescription= new TextField("", "Description");
        TextField tfPrice= new TextField("", "Prix");
        TextField tfQuantity= new TextField("", "Quantité");
        TextField tfCategory= new TextField("", "Catégorie");
        MultiButton store = chooseStore(SignInForm.getId());
        TextField tfImage= new TextField("", "Image");
        Button btnValider = new Button("Ajouter Produit");

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((tfName.getText().length()==0)||(tfDescription.getText().length()==0) || (tfPrice.getText().length()==0) || (tfQuantity.getText().length()==0))
                    Dialog.show("Alert", "Please fill all the fields", new Command("OK"));
                else
                {
                    try {
                        Task t = new Task(Integer.parseInt("1"), tfName.getText());
                        if( ServiceTask.getInstance().addTask(t))
                            Dialog.show("Success","Connection accepted",new Command("OK"));
                        else
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                    } catch (NumberFormatException e) {
                        Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                    }

                }


            }
        });

        addAll(tfName,tfDescription,tfPrice,tfQuantity,tfCategory, store,tfImage,btnValider);
        getToolbar().addMaterialCommandToLeftBar("retour", FontImage.MATERIAL_KEYBOARD_RETURN, e-> previous.showBack());

    }

    public MultiButton chooseStore(int id){

        ArrayList<Store> lc = ServiceStore.getInstance().getAllStore();
        ArrayList<String> StoreName = new ArrayList<>();
        ArrayList<String> StoreDescription = new ArrayList<>();
        for (Store cl : lc) {
            if(cl.getId() == id) {
                StoreName.add(cl.getNom());
                StoreDescription.add(cl.getDescription());
            }
        }
        int size = Display.getInstance().convertToPixels(7);
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(size, size, 0xffcccccc), true);

        MultiButton b = new MultiButton("Choisis un store");
        b.addActionListener(e -> {
            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(true);
            for(int iter = 0 ; iter < StoreName.size() ; iter++) {
                MultiButton mb = new MultiButton(StoreName.get(iter));
                mb.setTextLine2(StoreDescription.get(iter));
                d.add(mb);
                mb.addActionListener(ee -> {
                    b.setTextLine1(mb.getTextLine1());
                    b.setTextLine2(mb.getTextLine2());
                    b.setIcon(mb.getIcon());
                    d.dispose();
                    b.revalidate();
                });
            }
            d.showPopupDialog(b);
        });
        return b;
    }

}
