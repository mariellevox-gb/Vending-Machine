import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT NAME: Hiraya Digital Vending System
 * VERSION: 2.0 (Live Cart & Global Stock Tracking)
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
        Product[][] machine = {
            { // Column 1: Guhit
                new Product("Calamansi", 25.0, "🍹", 5), new Product("Chuckie", 30.0, "🧃", 5),
                new Product("Lucky Day", 35.0, "☕", 5), new Product("Mtn Dew", 20.0, "🍾", 5),
                new Product("Pineapple", 28.0, "🍍", 5), new Product("Water", 15.0, "💧", 10)
            },
            { // Column 2: Papak
                new Product("Boy Bawang", 12.0, "🧄", 10), new Product("Ding Dong", 10.0, "🥜", 10),
                new Product("Piattos", 18.0, "🍟", 10), new Product("Nagaraya", 15.0, "🥜", 10),
                new Product("Oishi Prawn", 12.0, "🦐", 10), new Product("SkyFlakes", 10.0, "🧇", 10)
            },
            { // Column 3: Himagas
                new Product("Fudgee Barr", 12.0, "🍰", 5), new Product("Hansel", 10.0, "🍪", 5),
                new Product("Cloud 9", 15.0, "🍫", 5), new Product("Beng-Beng", 15.0, "🍫", 5),
                new Product("Dried Mango", 45.0, "🥭", 5), new Product("Potchi", 5.0, "🍬", 20)
            }
        };

        while (true) { // Infinite loop for the machine operation
            if (isMachineEmpty(machine)) {
                JOptionPane.showMessageDialog(null, "MACHINE OUT OF ORDER: All items are sold out!");
                break;
            }

            runCustomerSession(machine);
        }
    }

    public static void runCustomerSession(Product[][] machine) {
        List<CartItem> cart = new ArrayList<>();
        boolean sessionActive = true;

        while (sessionActive) {
            int totalItemsInCart = 0;
            for (CartItem ci : cart) totalItemsInCart += ci.quantity;

            StringBuilder menu = new StringBuilder("<html><body style='width: 550px;'>");
            menu.append("<div style='background-color:#2E7D32; color:white; padding:10px; text-align:center;'>");
            menu.append("<h2 style='margin:0;'>HIRAYA VENDING MACHINE</h2>");
            menu.append("<p style='margin:0;'>Items in your Cart: <b>" + totalItemsInCart + "</b></p></div><br>");
            
            menu.append("<table border='1' cellspacing='0' cellpadding='5' style='width:100%; border-collapse:collapse;'>");
            menu.append("<tr style='background-color: #E8F5E9;'><th>#</th><th>GUHIT</th><th>PAPAK</th><th>HIMAGAS</th></tr>");

            for (int row = 0; row < 6; row++) {
                menu.append("<tr><td align='center' bgcolor='#f2f2f2'><b>" + (row + 1) + "</b></td>");
                for (int col = 0; col < 3; col++) {
                    Product p = machine[col][row];
                    String color = (p.stock > 0) ? "#2E7D32" : "#C62828";
                    menu.append("<td align='center'>" + p.icon + "<br>" + p.name + "<br><b>P" + p.price + "</b><br>" +
                                "<small style='color:"+color+"'>Stock: " + p.stock + "</small></td>");
                }
                menu.append("</tr>");
            }
            menu.append("</table><br>Enter <b>'Col Row'</b> to add or <b>'ORDER'</b> to pay:</body></html>");

            String choice = JOptionPane.showInputDialog(null, menu.toString(), "Selection", JOptionPane.PLAIN_MESSAGE);

            if (choice == null) return; 
            if (choice.equalsIgnoreCase("ORDER")) {
                if (cart.isEmpty()) JOptionPane.showMessageDialog(null, "Cart is empty!");
                else sessionActive = false;
            } else {
                try {
                    String[] parts = choice.split(" ");
                    int c = Integer.parseInt(parts[0]) - 1;
                    int r = Integer.parseInt(parts[1]) - 1;
                    Product selected = machine[c][r];

                    if (selected.stock <= 0) {
                        JOptionPane.showMessageDialog(null, "Out of Stock!");
                        continue;
                    }

                    String qtyStr = JOptionPane.showInputDialog("Qty for " + selected.name + "? (Max: " + selected.stock + ")");
                    int qty = Integer.parseInt(qtyStr);
                    if (qty > 0 && qty <= selected.stock) {
                        selected.stock -= qty;
                        cart.add(new CartItem(selected, qty));
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid quantity.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Format: 1 1");
                }
            }
        }
        processPayment(cart);
    }

    public static void processPayment(List<CartItem> cart) {
        double subtotal = 0;
        for (CartItem item : cart) subtotal += (item.product.price * item.quantity);

        // Student ID Validation
        String idStr = JOptionPane.showInputDialog("Mabini ID for 10% Discount (220000-259999):");
        double discount = 0;
        if (idStr != null && idStr.matches("\\d+")) {
            int id = Integer.parseInt(idStr);
            if (id >= 220000 && id <= 259999) discount = subtotal * 0.10;
        }

        double total = subtotal - discount;
        double cash = 0;
        while (cash < total) {
            String cashStr = JOptionPane.showInputDialog(String.format("Total: P%.2f\nInsert Cash:", total));
            if (cashStr == null) return;
            cash = Double.parseDouble(cashStr);
        }

        displayReceipt(cart, subtotal, discount, total, cash);
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
            for (int r = 0; r < 6; r++) {
                if (machine[c][r].stock > 0) return false;
            }
        }
        return true;
    }
}