import java.io.Serializable;

public class Paint implements Serializable {
    private String name;
    private int quantity;
    private String color;

    public Paint(String name, int quantity, String color) {
        this.name = name;
        this.quantity = quantity;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
