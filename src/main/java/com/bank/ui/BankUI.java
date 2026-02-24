package com.bank.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.bank.model.BankAccount;
import com.bank.service.AccountService;

public class BankUI extends JFrame {

    private final AccountService service = new AccountService();
    private final BankAccount account = service.createAccount("ACC-1001", BigDecimal.valueOf(1000));

    private final JLabel balanceLabel = new JLabel();
    private final JTextArea historyArea = new JTextArea(10, 40);

    public BankUI() {
        setTitle("Bank Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updateBalance();
        top.add(balanceLabel);

        JTextField amountField = new JTextField(10);
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");

        depositBtn.addActionListener((ActionEvent e) -> {
            try {
                BigDecimal amt = new BigDecimal(amountField.getText().trim());
                service.deposit(account, amt);
                updateBalance();
                refreshHistory();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        withdrawBtn.addActionListener((ActionEvent e) -> {
            try {
                BigDecimal amt = new BigDecimal(amountField.getText().trim());
                service.withdraw(account, amt);
                updateBalance();
                refreshHistory();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        top.add(amountField);
        top.add(depositBtn);
        top.add(withdrawBtn);

        add(top, BorderLayout.NORTH);

        historyArea.setEditable(false);
        add(new JScrollPane(historyArea), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshHistory());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshBtn);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        refreshHistory();
    }

    private void updateBalance() {
        balanceLabel.setText("Balance: " + service.getBalance(account));
    }

    private void refreshHistory() {
        historyArea.setText(service.getMiniStatement(account));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new BankUI().setVisible(true);
        });
    }
}
