package telas;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private JButton btnSair;

    public MenuPrincipalView() {

        setTitle("Sistema Academia - Menu Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel painelSuperior = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        btnSair = new JButton("Sair");

        painelSuperior.add(btnSair);

        add(painelSuperior, BorderLayout.NORTH);

        btnSair.addActionListener(e -> {

            new WebView().setVisible(true);

            this.dispose();
        });
    }
}