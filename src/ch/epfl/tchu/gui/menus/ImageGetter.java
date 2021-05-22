package ch.epfl.tchu.gui.menus;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageGetter {

    private ImageGetter(){}

    public static Image getImage(String root){
        Image image = null;
        try (InputStream stream = new FileInputStream(root)) {
            image = new Image(stream);
        }catch (FileNotFoundException e){
            System.out.println("didn't find java giff");
            throw new Error();}
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
