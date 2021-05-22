package app.classes;

import app.forms.StartForm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class Currency {
    private String name;
    private String imagepath;
    private ImageIcon image;
    private float value;
    private int amount;
    private float total;


    public Currency(String name, String imagepath, float value, int amount) {
        this.name = name;
        this.imagepath = imagepath;
        this.value = value;
        this.amount = amount;
        this.setTotal();
        this.setImage();
    }


    public void setImage() {
        URL url = Currency.class.getClassLoader().getResource(this.imagepath);
        if (url != null) {
            try {
                this.image = new ImageIcon(ImageIO.read(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.image = null;
        }
    }

    public float getTotal() {
        return total;
    }

    public void setTotal() {
        this.total = StartForm.round(value * amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", imagepath='" + imagepath + '\'' +
                ", image=" + image +
                ", value=" + value +
                ", amount=" + amount +
                ", total=" + total +
                '}';
    }
}
