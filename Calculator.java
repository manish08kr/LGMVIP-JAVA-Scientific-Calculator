import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Function;

class Calculator extends JFrame {
    private JTextField textField = new JTextField(15);
    private Font bigFont = new Font("TimesNewRoman", Font.PLAIN, 25);
    private CalculatorOp op = new CalculatorOp();

    public Calculator() {
        setupUI();
    }

    private void setupUI() {
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFont(bigFont);

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "sin", "cos", "log", "C"
        };

        setLayout(new BorderLayout());
        add(textField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4));
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            button.setFont(bigFont);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setTitle("Calculator");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = ((JButton) event.getSource()).getText();
            if (command.matches("[0-9]+") || command.equals(".")) {
                textField.setText(textField.getText() + command);
            } else if (command.equals("C")) {
                textField.setText("");
            } else if (command.equals("=")) {
                evaluateExpression();
            } else if (command.equals("sin")) {
                applyTrigFunction(Math::sin);
            } else if (command.equals("cos")) {
                applyTrigFunction(Math::cos);
            } else if (command.equals("log")) {
                applyUnaryOperation(Math::log);
            } else {
                op.setTotal(textField.getText());
                op.setOperator(command);
                textField.setText("");
            }
        }

        private void evaluateExpression() {
            try {
                double result = op.calculate(textField.getText());
                textField.setText(String.valueOf(result));
            } catch (Exception ex) {
                textField.setText("Error");
            }
        }

        private void applyTrigFunction(Function<Double, Double> function) {
            try {
                double input = Double.parseDouble(textField.getText());
                double result = function.apply(Math.toRadians(input));
                textField.setText(String.valueOf(result));
            } catch (Exception ex) {
                textField.setText("Error");
            }
        }

        private void applyUnaryOperation(Function<Double, Double> operation) {
            try {
                double input = Double.parseDouble(textField.getText());
                double result = operation.apply(input);
                textField.setText(String.valueOf(result));
            } catch (Exception ex) {
                textField.setText("Error");
            }
        }
    }

    private class CalculatorOp {
        private double total;
        private String operator;

        public CalculatorOp() {
            total = 0;
            operator = "+";
        }

        public void setTotal(String n) {
            total = Double.parseDouble(n);
        }

        public void setOperator(String op) {
            operator = op;
        }

        public double calculate(String n) {
            double num = Double.parseDouble(n);
            switch (operator) {
                case "+":
                    total += num;
                    break;
                case "-":
                    total -= num;
                    break;
                case "*":
                    total *= num;
                    break;
                case "/":
                    if (num != 0) {
                        total /= num;
                    } else {
                        throw new ArithmeticException("Division by zero");
                    }
                    break;
            }
            return total;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator().setVisible(true);
        });
    }
}
