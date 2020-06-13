package com.mycompany.myapp.gui;

import com.codename1.capture.Capture;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.ImageViewer;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.push.Push;
import com.codename1.push.PushContent;
import com.codename1.ui.*;
import static com.codename1.ui.CN1Constants.GALLERY_IMAGE;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.*;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.NumericConstraint;
import com.codename1.ui.validation.Validator;
import com.mycompany.myapp.entities.Produitbous;
import com.mycompany.myapp.entities.Store;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.services.ServiceProduitbous;
import com.mycompany.myapp.services.ServiceStore;
import com.mycompany.myapp.services.ServiceTask;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.codename1.ui.CN.openGallery;

public class AddProduitbousForm extends BaseForm{

    Resources res;
    private Image img;
    private String imgPath;
    private String name;
    Object s ;

    public AddProduitbousForm(Form previous) throws IOException {
        TextModeLayout tl = new TextModeLayout(3, 2);
        setTitle("Add a new product");
        setLayout(BoxLayout.y());
        FloatingActionButton fab  = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        RoundBorder rb = (RoundBorder)fab.getUnselectedStyle().getBorder();
        rb.uiid(true);

        TextComponent tfName = new TextComponent().labelAndHint("Nom");
        TextComponent tfDescription= new TextComponent().labelAndHint("Description");
        TextComponent tfPrice = new TextComponent().labelAndHint("Prix");
        TextComponent tfQuantity= new TextComponent().labelAndHint("Quantité");
        Picker tfCategory = new Picker();
        tfCategory.setType(Display.PICKER_TYPE_STRINGS);
        tfCategory.setStrings("Velo", "Accessoire","Autre");
        tfCategory.setSelectedString("Velo");
        MultiButton store = chooseStore(SignInForm.getId());
        ImageViewer iv = new ImageViewer();
        Validator val = new Validator();
        val.addConstraint(tfPrice,new NumericConstraint(true));
        val.addConstraint(tfQuantity,new NumericConstraint(true));

        Button avatar = new Button("");
        avatar.setUIID("InputAvatar");
        Image defaultAvatar = FontImage.createMaterial(FontImage.MATERIAL_CAMERA, "InputAvatarImage", 8);
        // Image circleMaskImage = getResources().getImage("circle.png");
        //defaultAvatar = defaultAvatar.scaled(circleMaskImage.getWidth(), circleMaskImage.getHeight());
        defaultAvatar = ((FontImage)defaultAvatar).toEncodedImage();
        //Object circleMask = circleMaskImage.createMask();
        //defaultAvatar = defaultAvatar.applyMask(circleMask);
        avatar.setIcon(defaultAvatar);
        avatar.addActionListener(e -> {
            if(Dialog.show("Camera or Gallery", "Would you like to use the camera or the gallery for the picture?", "Camera", "Gallery")) {
                String pic = Capture.capturePhoto();
                if(pic != null) {
                    s=pic ;
                    if (pic != null) {
                        try {
                            name = System.currentTimeMillis()+".jpg";
                            String pathToBeStored = FileSystemStorage.getInstance().getAppHomePath() + name ;
                            imgPath = pathToBeStored;
                            img = Image.createImage(pic);
                            OutputStream os = FileSystemStorage.getInstance().openOutputStream(pathToBeStored);
                            ImageIO.getImageIO().save(img, os, ImageIO.FORMAT_JPEG, 0.9f);
                            os.close();
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    iv.setImage(img);
                }
            } else {
                openGallery(ee -> {
                    if(ee.getSource() != null) {
                        s = ee.getSource();
                        try {
                            img = Image.createImage(s.toString());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        iv.setImage(img);

                    }
                }, GALLERY_IMAGE);
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
                    if(isValid(tfPrice.getText()) && isValid(tfQuantity.getText())) {
                        try {
                            System.out.println(avatar.getSelectCommandText());
                            //String fichernom = System.currentTimeMillis() + ".jpg";
                            Produitbous p = new Produitbous(store.getTextLine1(), tfPrice.getText(), tfQuantity.getText(), tfName.getText(), tfDescription.getText(), imgPath, tfCategory.getText());
                            if (ServiceProduitbous.getInstance().AddProduitbous(p)) {
                                System.out.println(s.toString());
                                //ServiceProduitbous.getInstance().rimage(imgPath, name ) ;
                                Dialog.show("Success", "Connection accepted", new Command("OK"));
                            } else
                                Dialog.show("ERROR", "Server error", new Command("OK"));
                        } catch (NumberFormatException e) {
                            Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                        }
                    }else
                    {
                        Dialog.show("ERROR", "Prix et Quantité doit etre un nombre", new Command("OK"));
                    }

                }


            }
        });

        add(tfName);
        add(tfDescription);
        add(tfPrice);
        add(tfQuantity);
        add(tfCategory);
        add(avatar);
        add(iv);
        add(store);
        add(btnValider);
        getToolbar().addMaterialCommandToLeftBar("retour", FontImage.MATERIAL_KEYBOARD_RETURN, e-> previous.showBack());

    }

    public  boolean isValid(Object value) {
        String v = (String)value;
        for(int i = 0 ; i < v.length() ; i++) {
            char c = v.charAt(i);
            if(c >= '0' && c <= '9') {
                continue;
            }
            return false;
        }
        return true;
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

    public void push(String message) {
        PushContent content = PushContent.get();
        if (content != null) {
            String imageUrl = content.getImageUrl();
            // The image attachment URL in the push notification
            // or `null` if there was no image attachment.
        }
    }

}
