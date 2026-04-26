import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT NAME: Hiraya Digital Vending System
 * VERSION: 3.0 (Alpha-Numeric Grid Selection: A1, B3, C6)
 */
public class HirayaVendingMachine {

    static class Product {
        String name; double price; String icon; int stock;
        Product(String name, double price, String icon, int stock) {
            this.name = name; this.price = price; this.icon = icon; this.stock = stock;
        }
    }

    static class CartItem {
        Product product; int quantity;
        CartItem(Product product, int quantity) {
            this.product = product; this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        // Standard 6x3 Grid
        Product[][] machine = {
            { // Column A (Index 0)
                new Product("Calamansi", 25.0, "🍹", 10), new Product("Chuckie", 30.0, "🧃", 10),
                new Product("Lucky Day", 35.0, "☕", 10), new Product("Mtn Dew", 20.0, "🍾", 10),
                new Product("Pineapple", 28.0, "🍍", 10), new Product("Water", 15.0, "💧", 20)
            },
            { // Column B (Index 1)
                new Product("Boy Bawang", 12.0, "🧄", 15), new Product("Ding Dong", 10.0, "🥜", 15),
                new Product("Piattos", 18.0, "🍟", 15), new Product("Nagaraya", 15.0, "🥜", 15),
                new Product("Oishi Prawn", 12.0, "🦐", 15), new Product("SkyFlakes", 10.0, "🧇", 15)
            },
            { // Column C (Index 2)
                new Product("Fudgee Barr", 12.0, "🍰", 10), new Product("Hansel", 10.0, "🍪", 10),
                new Product("Cloud 9", 15.0, "🍫", 10), new Product("Beng-Beng", 15.0, "🍫", 10),
                new Product("Dried Mango", 45.0, "🥭", 10), new Product("Potchi", 5.0, "🍬", 25)
            }
        };

        while (true) {
            if (isMachineEmpty(machine)) {
                JOptionPane.showMessageDialog(null, "HIRAYA VENDING: Currently Out of Stock.");
                break;
            }
            runCustomerSession(machine);
        }
    }

    public static void runCustomerSession(Product[][] machine) {
        List<CartItem> cart = new ArrayList<>();
        boolean sessionActive = true;

        while (sessionActive) {
            int cartCount = cart.stream().mapToInt(i -> i.quantity).sum();

            StringBuilder menu = new StringBuilder("<html><body style='width: 550px;'>");
            menu.append("<div style='background-color:#1B5E20; color:white; padding:10px; text-align:center;'>");
            menu.append("<h2 style='margin:0;'>HIRAYA VENDING MACHINE</h2>");
            menu.append("<p style='margin:0;'>Cart Items: <b>" + cartCount + "</b> | Coordinate Mode: ON</p></div><br>");
            
            menu.append("<table border='1' cellspacing='0' cellpadding='5' style='width:100%; border-collapse:collapse;'>");
            menu.append("<tr style='background-color: #C8E6C9;'>");
            menu.append("<th>Row</th><th>A (Drinks)</th><th>B (Snacks)</th><th>C (Sweets)</th></tr>");

            for (int r = 0; r < 6; r++) {
                menu.append("<tr><td align='center' bgcolor='#eeeeee'><b>" + (r + 1) + "</b></td>");
                for (int c = 0; c < 3; c++) {
                    Product p = machine[c][r];
                    String color = (p.stock > 0) ? "#2E7D32" : "#C62828";
                    menu.append("<td align='center'>" + p.icon + "<br>" + p.name + "<br><b>P" + p.price + "</b><br>" +
                                "<small style='color:"+color+"'>Stock: " + p.stock + "</small></td>");
                }
                menu.append("</tr>");
            }
            menu.append("</table><br>");
            menu.append("<b>Input Code:</b> Enter <b>Letter + Number</b> (e.g., <b>A2</b> for Chuckie)<br>");
            menu.append("<b>Commands:</b> Type <b>'ORDER'</b> to pay or <b>'EXIT'</b> to quit.</body></html>");

            String input = JOptionPane.showInputDialog(null, menu.toString(), "Selection Interface", JOptionPane.PLAIN_MESSAGE);

            if (input == null || input.equalsIgnoreCase("EXIT")) return;
            if (input.equalsIgnoreCase("ORDER")) {
                if (cart.isEmpty()) JOptionPane.showMessageDialog(null, "Cart is empty!");
                else sessionActive = false;
                continue;
            }

            try {
                // Parsing Alpha-Numeric (e.g., A1, B3)
                char colChar = Character.toUpperCase(input.charAt(0));
                int rowIdx = Character.getNumericValue(input.charAt(1)) - 1;
                int colIdx = colChar - 'A'; // Converts 'A' to 0, 'B' to 1, 'C' to 2

                if (colIdx >= 0 && colIdx < 3 && rowIdx >= 0 && rowIdx < 6) {
                    Product selected = machine[colIdx][rowIdx];
                    if (selected.stock <= 0) {
                        JOptionPane.showMessageDialog(null, "Selected item is out of stock!");
                    } else {
                        String qtyStr = JOptionPane.showInputDialog("Qty for " + selected.name + "? (Max: " + selected.stock + ")");
                        if (qtyStr != null) {
                            int qty = Integer.parseInt(qtyStr);
                            if (qty > 0 && qty <= selected.stock) {
                                selected.stock -= qty;
                                cart.add(new CartItem(selected, qty));
                            } else { JOptionPane.showMessageDialog(null, "Invalid quantity."); }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Code! Use A1 to C6.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error! Use format: A1, B2, etc.");
            }
        }
        processPayment(cart);
    }

    // ... [Payment and Receipt methods remain same as previous versions] ...

    public static void processPayment(List<CartItem> cart) {
        double subtotal = 0;
        for (CartItem item : cart) subtotal += (item.product.price * item.quantity);
        String idStr = JOptionPane.showInputDialog("Mabini ID for 10% Discount:");
        double disc = 0;
        if (idStr != null && idStr.matches("\\d+")) {
            int id = Integer.parseInt(idStr);
            if (id >= 220000 && id <= 259999) disc = subtotal * 0.10;
        }
        double total = subtotal - disc;
        double cash = 0;
        while (cash < total) {
            String cashStr = JOptionPane.showInputDialog(String.format("Total: P%.2f\nInsert Cash:", total));
            if (cashStr == null) return;
            cash = Double.parseDouble(cashStr);
        }
        displayReceipt(cart, subtotal, disc, total, cash);
    }

    public static void displayReceipt(List<CartItem> cart, double sub, double disc, double tot, double cash) {
        StringBuilder sb = new StringBuilder("--- HIRAYA RECEIPT ---\n");
        for (CartItem i : cart) sb.append(String.format("%-15s x%-2d P%7.2f\n", i.product.name, i.quantity, (i.product.price * i.quantity)));
        sb.append("----------------------\n");
        sb.append(String.format("Subtotal:   P%8.2f\n", sub));
        sb.append(String.format("Discount:  -P%8.2f\n", disc));
        sb.append(String.format("TOTAL:      P%8.2f\n", tot));
        sb.append(String.format("CASH:       P%8.2f\n", cash));
        sb.append(String.format("CHANGE:     P%8.2f\n", cash - tot));
        JOptionPane.showMessageDialog(null, "<html><pre>" + sb.toString() + "</pre></html>");
    }

    public static boolean isMachineEmpty(Product[][] machine) {
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 6; r++) if (machine[c][r].stock > 0) return false;
        }
        return true;
    }
}