import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        JButton lowStockButton = new JButton("Tinta com Baixo Estoque");
        JButton searchButton = new JButton("Buscar e Filtrar");
        JButton exportButton = new JButton("Exportar Dados");
        JButton importButton = new JButton("Importar Dados");

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

        gbc.gridy = 3;
        gbc.gridx = 0;
        frame.add(lowStockButton, gbc);

        gbc.gridy = 3;
        gbc.gridx = 1;
        frame.add(searchButton, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        frame.add(exportButton, gbc);

        gbc.gridy = 4;
        gbc.gridx = 1;
        frame.add(importButton, gbc);

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

        lowStockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkLowStock();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchAndFilter();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportData();
            }
        });

        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importData();
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
        JComboBox<String> paintComboBox = new JComboBox<>();
        for (Paint paint : inventory.getAllPaints()) {
            paintComboBox.addItem(paint.getName() + " - " + paint.getColor());
        }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Tinta:"));
        panel.add(paintComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Remover Tinta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedPaint = (String) paintComboBox.getSelectedItem();
            if (selectedPaint != null) {
                String[] parts = selectedPaint.split(" - ");
                String name = parts[0];
                String color = parts[1];
                Paint paint = inventory.findPaintByNameAndColor(name, color);
                if (paint != null) {
                    inventory.removePaint(paint);
                    JOptionPane.showMessageDialog(frame, "Tinta removida com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Tinta não encontrada", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
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
        JComboBox<String> paintComboBox = new JComboBox<>();
        for (Paint paint : inventory.getAllPaints()) {
            paintComboBox.addItem(paint.getName() + " - " + paint.getColor());
        }

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField quantityField = new JTextField(10);

        panel.add(new JLabel("Tinta:"));
        panel.add(paintComboBox);
        panel.add(new JLabel("Nova Quantidade:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Editar Tinta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedPaint = paintComboBox.getSelectedItem().toString();
            String[] parts = selectedPaint.split(" - ");
            String name = parts[0];
            String color = parts[1];
            Paint paint = inventory.findPaintByNameAndColor(name, color);
            if (paint != null) {
                int newQuantity = Integer.parseInt(quantityField.getText());
                paint.setQuantity(newQuantity);
            } else {
                JOptionPane.showMessageDialog(frame, "Tinta não encontrada", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void checkLowStock() {
        String thresholdStr = JOptionPane.showInputDialog(frame, "Entre com o limite de estoque baixo:");
        if (thresholdStr != null && !thresholdStr.isEmpty()) {
            int threshold = Integer.parseInt(thresholdStr);
            java.util.List<Paint> lowStockPaints = inventory.getLowStockPaints(threshold);
            StringBuilder sb = new StringBuilder();
            for (Paint paint : lowStockPaints) {
                sb.append("Nome: ").append(paint.getName()).append(", Quantidade: ").append(paint.getQuantity()).append(", Cor: ").append(paint.getColor()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(frame, scrollPane, "Tintas com Baixo Estoque", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchAndFilter() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField searchField = new JTextField(10);
        JComboBox<String> filterCriteria = new JComboBox<>(new String[]{"Nome", "Cor", "Quantidade"});
        JRadioButton exactMatch = new JRadioButton("Correspondência Exata");
        JRadioButton caseSensitive = new JRadioButton("Diferenciar Maiúsculas e Minúsculas");

        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(exactMatch);
        optionsGroup.add(caseSensitive);

        panel.add(new JLabel("Texto de Busca:"));
        panel.add(searchField);
        panel.add(new JLabel("Critério de Filtro:"));
        panel.add(filterCriteria);
        panel.add(exactMatch);
        panel.add(caseSensitive);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Buscar e Filtrar Tintas", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String searchText = searchField.getText();
            String selectedFilter = filterCriteria.getSelectedItem().toString();
            boolean isExactMatch = exactMatch.isSelected();
            boolean isCaseSensitive = caseSensitive.isSelected();

            // Realizar a busca e aplicar os filtros
            List<Paint> filteredPaints = new ArrayList<>();
            for (Paint paint : inventory.getAllPaints()) {
                boolean match = false;
                switch (selectedFilter) {
                    case "Nome":
                        if (isExactMatch) {
                            match = isCaseSensitive ? paint.getName().equals(searchText) : paint.getName().equalsIgnoreCase(searchText);
                        } else {
                            match = isCaseSensitive ? paint.getName().contains(searchText) : paint.getName().toLowerCase().contains(searchText.toLowerCase());
                        }
                        break;
                    case "Cor":
                        if (isExactMatch) {
                            match = isCaseSensitive ? paint.getColor().equals(searchText) : paint.getColor().equalsIgnoreCase(searchText);
                        } else {
                            match = isCaseSensitive ? paint.getColor().contains(searchText) : paint.getColor().toLowerCase().contains(searchText.toLowerCase());
                        }
                        break;
                    case "Quantidade":
                        try {
                            int quantity = Integer.parseInt(searchText);
                            if (isExactMatch) {
                                match = paint.getQuantity() == quantity;
                            } else {
                                match = paint.getQuantity() >= quantity;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                        break;
                }
                if (match) {
                    filteredPaints.add(paint);
                }
            }

            // Exibir os resultados
            StringBuilder sb = new StringBuilder();
            for (Paint paint : filteredPaints) {
                sb.append("Nome: ").append(paint.getName()).append(", Quantidade: ").append(paint.getQuantity()).append(", Cor: ").append(paint.getColor()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(frame, scrollPane, "Resultado da Busca e Filtro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Dados do Estoque");
        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                inventory.saveToFile(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(frame, "Dados do estoque foram salvos com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao salvar os dados do estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Arquivo de Dados do Estoque");
        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToImport = fileChooser.getSelectedFile();
            try {
                inventory = PaintInventory.loadFromFile(fileToImport.getAbsolutePath());
                JOptionPane.showMessageDialog(frame, "Dados do estoque foram importados com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao importar os dados do estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new PaintInventoryGUI();
    }
}
