import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT NAME: Hiraya Digital Vending System
 * DEVELOPER: Marielle Vox
 * DESCRIPTION: A 6x3 automated food vending system with student discount logic.
 */
public class HirayaVendingMachine {

    static class Product {
        String name;
        double price;
        String icon;
        int stock;

        Product(String name, double price, String icon, int stock) {
            this.name = name;
            this.price = price;
            this.icon = icon;
            this.stock = stock;
        }
    }

    static class CartItem {
        Product product;
        int quantity;

        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        // Initialize 6x3 Grid for Hiraya Vending Machine
        Product[][] machine = {
            { // Column 1: Guhit (Beverages)
                new Product("Canned Calamansi", 25.0, "🍹", 15),
                new Product("Chuckie", 30.0, "🧃", 15),
                new Product("Kopiko Lucky Day", 35.0, "☕", 15),
                new Product("Mountain Dew", 20.0, "🍾", 15),
                new Product("Canned Pineapple", 28.0, "🍍", 15),
                new Product("Mineral Water", 15.0, "💧", 20)
            },
            { // Column 2: Papak (Savory Snacks)
                new Product("Boy Bawang", 12.0, "🧄", 20),
                new Product("Ding Dong", 10.0, "🥜", 20),
                new Product("Piattos Cheese", 18.0, "🍟", 20),
                new Product("Nagaraya Adobo", 15.0, "🥜", 20),
                new Product("Oishi Prawn", 12.0, "🦐", 20),
                new Product("SkyFlakes Cheese", 10.0, "🧇", 20)
            },
            { // Column 3: Himagas (Sweets)
                new Product("Fudgee Barr", 12.0, "🍰", 15),
                new Product("Hansel Mocha", 10.0, "🍪", 15),
                new Product("Cloud 9 Classic", 15.0, "🍫", 15),
                new Product("Beng-Beng", 15.0, "🍫", 15),
                new Product("Dried Mangoes", 45.0, "🥭", 10),
                new Product("Potchi Strawberry", 5.0, "🍬", 30)
            }
        };

        List<CartItem> cart = new ArrayList<>();
        boolean shopping = true;

        while (shopping) {
            StringBuilder menu = new StringBuilder("<html><body style='width: 550px;'>");
            menu.append("<div style='text-align:center;'>");
            menu.append("<h1 style='color: #2E7D32; margin:0;'>HIRAYA VENDING MACHINE</h1>");
            menu.append("<p style='color: #555;'>Project: Application Software 2 Showcase</p>");
            menu.append("</div><hr>");
            
            menu.append("<table border='1' cellspacing='0' cellpadding='5' style='width:100%; border-collapse:collapse;'>");
            menu.append("<tr style='background-color: #E8F5E9;'><th>#</th><th>GUHIT (Drinks)</th><th>PAPAK (Savory)</th><th>HIMAGAS (Sweets)</th></tr>");

            for (int row = 0; row < 6; row++) {
                menu.append("<tr><td align='center' bgcolor='#f2f2f2'><b>" + (row + 1) + "</b></td>");
                for (int col = 0; col < 3; col++) {
                    Product p = machine[col][row];
                    String stockStatus = (p.stock > 0) ? "Stock: " + p.stock : "OUT OF STOCK";
                    String color = (p.stock > 0) ? "#2E7D32" : "#C62828";
                    
                    menu.append("<td align='center' style='padding: 8px;'>" + p.icon + "<br>" + p.name + 
                                "<br><b>P" + p.price + "</b><br><small style='color:"+color+";'>" + stockStatus + "</small></td>");
                }
                menu.append("</tr>");
            }
            menu.append("</table><br>");
            menu.append("<b>[ADD TO CART]:</b> Enter 'Col Row' (e.g., 1 2 for Chuckie)<br>");
            menu.append("<b>[PLACE ORDER]:</b> Type <b>'ORDER'</b> to checkout.<br>");
            menu.append("<b>[EXIT]:</b> Click Cancel or type 'EXIT'</body></html>");

            String choice = JOptionPane.showInputDialog(null, menu.toString(), "Hiraya Vending Machine - Interface", JOptionPane.PLAIN_MESSAGE);

            if (choice == null || choice.equalsIgnoreCase("EXIT")) {
                System.exit(0);
            } else if (choice.equalsIgnoreCase("ORDER")) {
                if (cart.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Your cart is empty! Please select a product first.");
                } else {
                    shopping = false;
                }
            } else {
                try {
                    String[] parts = choice.split(" ");
                    int colIdx = Integer.parseInt(parts[0]) - 1;
                    int rowIdx = Integer.parseInt(parts[1]) - 1;

                    Product selected = machine[colIdx][rowIdx];

                    if (selected.stock <= 0) {
                        JOptionPane.showMessageDialog(null, "The item " + selected.name + " is currently unavailable.");
                        continue;
                    }

                    String qtyInput = JOptionPane.showInputDialog("Enter quantity for " + selected.name + " (Max: " + selected.stock + "):");
                    if (qtyInput != null) {
                        int qty = Integer.parseInt(qtyInput);
                        if (qty > 0 && qty <= selected.stock) {
                            selected.stock -= qty;
                            cart.add(new CartItem(selected, qty));
                            JOptionPane.showMessageDialog(null, "Added " + qty + " unit(s) of " + selected.name + " to cart.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid quantity requested.");
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Input Error: Please use the format 'Column Row' (e.g., 2 3)");
                }
            }
        }
        processPayment(cart);
    }

    public static void processPayment(List<CartItem> cart) {
        double subtotal = 0;
        for (CartItem item : cart) {
            subtotal += (item.product.price * item.quantity);
        }

        String idStr = JOptionPane.showInputDialog("Enter Mabini Colleges Student ID (220000-259999) for 10% Discount:");
        double discount = 0;
        boolean isStudent = false;

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                if (id >= 220000 && id <= 259999) {
                    discount = subtotal * 0.10;
                    isStudent = true;
                    JOptionPane.showMessageDialog(null, "Student ID Verified. Discount Applied!");
                }
            } catch (Exception e) {}
        }

        double total = subtotal - discount;
        double cash = 0;

        while (cash < total) {
            String cashStr = JOptionPane.showInputDialog(String.format("Payment Required: P%.2f\nEnter Cash Amount (PHP Only):", total));
            if (cashStr == null) return;
            try {
                cash = Double.parseDouble(cashStr);
                if (cash < total) JOptionPane.showMessageDialog(null, "Insufficient cash. Total is P" + total);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid numeric amount.");
            }
        }

        displayFinalReceipt(cart, subtotal, discount, total, cash, isStudent);
    }

    public static void displayFinalReceipt(List<CartItem> cart, double sub, double disc, double total, double cash, boolean isStudent) {
        StringBuilder sb = new StringBuilder("HIRAYA VENDING MACHINE - RECEIPT\n");
        sb.append("------------------------------------------\n");
        for (CartItem item : cart) {
            sb.append(String.format("%-20s x%-3d P%8.2f\n", item.product.name, item.quantity, (item.product.price * item.quantity)));
        }
        sb.append("------------------------------------------\n");
        sb.append(String.format("Subtotal:              P%10.2f\n", sub));
        if (isStudent) sb.append(String.format("Mabini Disc (10%%):   -P%10.2f\n", disc));
        sb.append(String.format("Total Amount:          P%10.2f\n", total));
        sb.append(String.format("Cash Received:         P%10.2f\n", cash));
        sb.append(String.format("Change:                P%10.2f\n", cash - total));
        sb.append("------------------------------------------\n");
        sb.append("       Thank you for choosing Hiraya!      \n");

        JOptionPane.showMessageDialog(null, "<html><pre>" + sb.toString() + "</pre></html>", "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}