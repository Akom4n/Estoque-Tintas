import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PaintInventoryGUI {
    private JFrame frame;
    private PaintInventory inventory;
    private static final String FILE_NAME = "paint_inventory.ser";

    public PaintInventoryGUI() {
        // Configurar o LookAndFeel para um tema mais moderno
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Carregar o inventário de arquivo ou criar um novo
        try {
            inventory = PaintInventory.loadFromFile(FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            inventory = new PaintInventory();
        }

        frame = new JFrame("Estoque de Tintas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());

        // Configurar o layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Gerenciamento de Estoque de Tintas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Botões
        JButton addButton = new JButton("Adicionar Tinta");
        JButton removeButton = new JButton("Remover Tinta");
        JButton listButton = new JButton("Listar Tintas");
        JButton editButton = new JButton("Editar Tinta");

        // Adicionar botões ao painel
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        frame.add(addButton, gbc);

        gbc.gridy = 1;
        gbc.gridx = 1;
        frame.add(removeButton, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        frame.add(listButton, gbc);

        gbc.gridy = 2;
        gbc.gridx = 1;
        frame.add(editButton, gbc);

        // Ações dos botões
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPaint();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removePaint();
            }
        });

        listButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listPaints();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editPaint();
            }
        });

        // Salvar inventário ao sair
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    inventory.saveToFile(FILE_NAME);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }

    private void addPaint() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField nameField = new JTextField(10);
        JTextField quantityField = new JTextField(10);
        JTextField colorField = new JTextField(10);

        panel.add(new JLabel("Nome da Tinta:"));
        panel.add(nameField);
        panel.add(new JLabel("Quantidade:"));
        panel.add(quantityField);
        panel.add(new JLabel("Cor da Tinta:"));
        panel.add(colorField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Adicionar Tinta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String color = colorField.getText();
            inventory.addPaint(new Paint(name, quantity, color));
        }
    }

    private void removePaint() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField nameField = new JTextField(10);
        JTextField colorField = new JTextField(10);

        panel.add(new JLabel("Nome da Tinta:"));
        panel.add(nameField);
        panel.add(new JLabel("Cor da Tinta:"));
        panel.add(colorField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Remover Tinta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String color = colorField.getText();
            inventory.removePaint(name, color);
        }
    }

    private void listPaints() {
        StringBuilder sb = new StringBuilder();
        for (Paint paint : inventory.getAllPaints()) {
            sb.append("Nome: ").append(paint.getName()).append(", Quantidade: ").append(paint.getQuantity()).append(", Cor: ").append(paint.getColor()).append("\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(frame, scrollPane, "Lista de Tintas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editPaint() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField nameField = new JTextField(10);
        JTextField colorField = new JTextField(10);
        JTextField quantityField = new JTextField(10);

        panel.add(new JLabel("Nome da Tinta:"));
        panel.add(nameField);
        panel.add(new JLabel("Cor da Tinta:"));
        panel.add(colorField);
        panel.add(new JLabel("Nova Quantidade:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Editar Tinta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String color = colorField.getText();
            Paint paint = null;
            for (Paint p : inventory.getAllPaints()) {
                if (p.getName().equalsIgnoreCase(name) && p.getColor().equalsIgnoreCase(color)) {
                    paint = p;
                    break;
                }
            }

            if (paint != null) {
                int newQuantity = Integer.parseInt(quantityField.getText());
                paint.setQuantity(newQuantity);
            } else {
                JOptionPane.showMessageDialog(frame, "Tinta não encontrada ou cor incorreta", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new PaintInventoryGUI();
    }
}
