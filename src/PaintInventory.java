import java.io.*;
import java.util.ArrayList;

public class PaintInventory implements Serializable {
    private ArrayList<Paint> paints;

    public PaintInventory() {
        paints = new ArrayList<>();
    }

    public void addPaint(Paint paint) {
        paints.add(paint);
    }

    public void removePaint(String name, String color) {
        paints.removeIf(paint -> paint.getName().equalsIgnoreCase(name) && paint.getColor().equalsIgnoreCase(color));
    }

    public Paint findPaint(String name) {
        for (Paint paint : paints) {
            if (paint.getName().equalsIgnoreCase(name)) {
                return paint;
            }
        }
        return null;
    }

    public ArrayList<Paint> getAllPaints() {
        return paints;
    }

    public void updatePaint(String name, int newQuantity) {
        Paint paint = findPaint(name);
        if (paint != null) {
            paint.setQuantity(newQuantity);
        }
    }

    public void saveToFile(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    public static PaintInventory loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (PaintInventory) in.readObject();
        }
    }
}
