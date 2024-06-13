import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PaintInventory implements Serializable {
    private List<Paint> paints;

    public PaintInventory() {
        this.paints = new ArrayList<>();
    }

    public void addPaint(Paint paint) {
        paints.add(paint);
    }

    public void removePaint(String name, String color) {
        paints.removeIf(p -> p.getName().equals(name) && p.getColor().equals(color));
    }

    public void removePaint(Paint paint) {
        paints.remove(paint);
    }

    public List<Paint> getAllPaints() {
        return paints;
    }

    public Paint findPaintByNameAndColor(String name, String color) {
        for (Paint paint : paints) {
            if (paint.getName().equals(name) && paint.getColor().equals(color)) {
                return paint;
            }
        }
        return null;
    }

    public List<Paint> getLowStockPaints(int threshold) {
        List<Paint> lowStockPaints = new ArrayList<>();
        for (Paint paint : paints) {
            if (paint.getQuantity() < threshold) {
                lowStockPaints.add(paint);
            }
        }
        return lowStockPaints;
    }

    public void saveToFile(String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
        }
    }

    public static PaintInventory loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (PaintInventory) ois.readObject();
        }
    }
}
