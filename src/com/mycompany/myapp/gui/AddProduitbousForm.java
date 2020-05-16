package com.mycompany.myapp.gui;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Produitbous;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.services.ServiceProduitbous;
import com.mycompany.myapp.services.ServiceStore;
import com.mycompany.myapp.services.ServiceTask;

import java.io.IOException;
import java.util.ArrayList;

public class AddProduitbousForm extends Form{

    Resources res;
    private Image img;
    private String imgPath;

    public AddProduitbousForm(Form previous) throws IOException {
        setTitle("Add a new task");
        setLayout(BoxLayout.y());

        TextField tfName = new TextField("","Nom du produit");
        TextField tfDescription= new TextField("", "Description");
        TextField tfPrice= new TextField("", "Prix");
        TextField tfQuantity= new TextField("", "Quantité");
        Picker tfCategory = new Picker();
        tfCategory.setType(Display.PICKER_TYPE_STRINGS);
        tfCategory.setStrings("Velo", "Accessoire","Autre");
        tfCategory.setSelectedString("Velo");
        MultiButton store = chooseStore(SignInForm.getId());
        ImageViewer iv = new ImageViewer();
        Button takeImagefromcamera = new Button("Camera");
        takeImagefromcamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    imgPath = Capture.capturePhoto(Display.getInstance().getDisplayWidth(), -1);
                    img = Image.createImage(imgPath);
                    iv.setImage(img);
                } catch (IOException ex) {
                    System.out.println(ex);
                }

            }
        });
        Button takeImagefromgallery = new Button("Gallerie");
        takeImagefromgallery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().openGallery(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        if (ev != null && ev.getSource() != null) {
                            String imgpath = (String) ev.getSource();
                            Image img = null;
                            try {
                                img = Image.createImage(FileSystemStorage.getInstance().openInputStream(imgpath));
                                iv.setImage(img);
                                imgPath = imgpath;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, Display.GALLERY_IMAGE);

            }
        });
        Button btnValider = new Button("Ajouter Produit");

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((tfName.getText().length()==0)||(tfDescription.getText().length()==0) || (tfPrice.getText().length()==0) || (tfQuantity.getText().length()==0))
                    Dialog.show("Alert", "Please fill all the fields", new Command("OK"));
                else
                {
                    try {
                        Produitbous p = new Produitbous(store.getTextLine1(),tfPrice.getText(),tfQuantity.getText(),tfName.getText(),tfDescription.getText(),imgPath,tfCategory.getText());
                        if( ServiceProduitbous.getInstance().AddProduitbous(p))
                            Dialog.show("Success","Connection accepted",new Command("OK"));
                        else
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                    } catch (NumberFormatException e) {
                        Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                    }

                }


            }
        });

        addAll(tfName,tfDescription,tfPrice,tfQuantity,tfCategory,takeImagefromcamera,takeImagefromgallery,iv, store,btnValider);
        getToolbar().addMaterialCommandToLeftBar("retour", FontImage.MATERIAL_KEYBOARD_RETURN, e-> previous.showBack());

    }

    private void setImage(String filePath, ImageViewer iv) {
        try {
            Image i1 = Image.createImage(filePath);
            iv.setImage(i1);
            iv.getParent().revalidate();
        } catch (Exception ex) {
            Log.e(ex);
            Dialog.show("Error", "Error during image loading: " + ex, "OK", null);
        }
    }

    private void showToast(String text) {
        Image errorImage = FontImage.createMaterial(FontImage.MATERIAL_ERROR, UIManager.getInstance().getComponentStyle("Title"), 4);
        ToastBar.Status status = ToastBar.getInstance().createStatus();
        status.setMessage(text);
        status.setIcon(errorImage);
        status.setExpires(2000);
        status.show();
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
