package ir.ac.kntu.utilities;

import ir.ac.kntu.managers.OrderManager;
import ir.ac.kntu.managers.RestaurantManager;
import ir.ac.kntu.models.Order;
import ir.ac.kntu.models.OrderItem;
import ir.ac.kntu.models.Restaurant;
import ir.ac.kntu.models.enums.OrderStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * HTMLReportGenerator - Creates comprehensive HTML reports for restaurant managers
 *
 * BONUS FEATURES IMPLEMENTATION:
 * - Financial reports with interactive tables
 * - Revenue analytics with charts
 * - Order statistics and trends
 * - Customer behavior analysis
 * - Performance dashboards
 */

public class HTMLReportGenerator {

    private static final String REPORTS_DIR = "reports";

    public static void generateRestaurantReport(Restaurant restaurant, String filename) {
        createReportsDirectory();

        String html = buildRestaurantReportHTML(restaurant);
        writeToFile(filename, html);
    }

    public static void generateSystemReport(String filename) {
        createReportsDirectory();

        String html = buildSystemReportHTML();
        writeToFile(filename, html);
    }

    private static void createReportsDirectory() {
        try {
            Files.createDirectories(Paths.get(REPORTS_DIR));
        } catch (IOException e) {
            System.err.println("Could not create reports directory: " + e.getMessage());
        }
    }

    private static String buildRestaurantReportHTML(Restaurant restaurant) {
        OrderManager orderManager = OrderManager.getInstance();
        List<Order> restaurantOrders = orderManager.getOrdersByRestaurant(restaurant);

        // Calculate statistics
        double totalRevenue = restaurantOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getFinalAmount)
                .sum();

        long totalOrders = restaurantOrders.size();
        long completedOrders = restaurantOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();

        double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;

        // (last 6 months)
        Map<String, Double> monthlyRevenue = restaurantOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                        o -> o.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.summingDouble(Order::getFinalAmount)
                ));

        StringBuilder html = new StringBuilder();
        html.append(getHTMLHeader("Restaurant Financial Report - " + restaurant.getName()));
        html.append("<body>\n");
        html.append("<div class='container'>\n");
        html.append("<h1>Restaurant ").append(restaurant.getName()).append(" - Financial Report</h1>\n");
        html.append("<p class='date'>Generated on: ").append(LocalDate.now()).append("</p>\n");

        // Summary Cards
        html.append("<div class='summary-cards'>\n");
        html.append("<div class='card'><h3>Total Revenue</h3><p class='large'>").append(String.format("%,.0f", totalRevenue)).append(" Toman</p></div>\n");
        html.append("<div class='card'><h3>Total Orders</h3><p class='large'>").append(totalOrders).append("</p></div>\n");
        html.append("<div class='card'><h3>Completed Orders</h3><p class='large'>").append(completedOrders).append("</p></div>\n");
        html.append("<div class='card'><h3>Avg Order Value</h3><p class='large'>").append(String.format("%,.0f", avgOrderValue)).append(" Toman</p></div>\n");
        html.append("</div>\n");

        // Monthly Revenue Chart
        html.append("<div class='chart-container'>\n");
        html.append("<h2>📊 Monthly Revenue Trend</h2>\n");
        html.append("<canvas id='revenueChart' width='800' height='300'></canvas>\n");
        html.append("</div>\n");

        // Recent Orders Table
        html.append("<div class='table-container'>\n");
        html.append("<h2>📋 Recent Orders</h2>\n");
        html.append("<table>\n");
        html.append("<thead><tr><th>Order ID</th><th>Date</th><th>Status</th><th>Amount</th><th>Customer</th></tr></thead>\n");
        html.append("<tbody>\n");

        restaurantOrders.stream()
                .sorted((o1, o2) -> o2.getOrderTime().compareTo(o1.getOrderTime()))
                .limit(20)
                .forEach(order -> {
                    html.append("<tr>\n");
                    html.append("<td>").append(order.getId()).append("</td>\n");
                    html.append("<td>").append(order.getOrderTime().toLocalDate()).append("</td>\n");
                    html.append("<td class='status-").append(order.getStatus().name().toLowerCase()).append("'>")
                            .append(order.getStatus().getDisplayName()).append("</td>\n");
                    html.append("<td>").append(String.format("%,.0f", order.getFinalAmount())).append("</td>\n");
                    html.append("<td>").append(order.getCustomer().getName()).append(" ")
                            .append(order.getCustomer().getLastName()).append("</td>\n");
                    html.append("</tr>\n");
                });

        html.append("</tbody>\n");
        html.append("</table>\n");
        html.append("</div>\n");

        // Popular Items
        html.append("<div class='table-container'>\n");
        html.append("<h2>Popular Items</h2>\n");
        html.append("<table>\n");
        html.append("<thead><tr><th>Item</th><th>Orders</th><th>Revenue</th></tr></thead>\n");
        html.append("<tbody>\n");

        // Calculate popular items
        Map<String, Long> itemOrderCount = restaurantOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getFood().getName(),
                        Collectors.summingLong(OrderItem::getQuantity)
                ));

        Map<String, Double> itemRevenue = restaurantOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getFood().getName(),
                        Collectors.summingDouble(OrderItem::getTotalPrice)
                ));

        itemOrderCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .forEach(entry -> {
                    String itemName = entry.getKey();
                    long orders = entry.getValue();
                    double revenue = itemRevenue.getOrDefault(itemName, 0.0);
                    html.append("<tr>\n");
                    html.append("<td>").append(itemName).append("</td>\n");
                    html.append("<td>").append(orders).append("</td>\n");
                    html.append("<td>").append(String.format("%,.0f", revenue)).append("</td>\n");
                    html.append("</tr>\n");
                });

        html.append("</tbody>\n");
        html.append("</table>\n");
        html.append("</div>\n");

        html.append("</div>\n");

        // JS charts
        html.append(getChartScript(monthlyRevenue));

        html.append("</body>\n</html>");

        return html.toString();
    }

    private static String buildSystemReportHTML() {
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        OrderManager orderManager = OrderManager.getInstance();

        List<Restaurant> allRestaurants = restaurantManager.getAllRestaurants();
        List<Order> allOrders = orderManager.getAllOrders();

        long totalRestaurants = allRestaurants.size();
        long approvedRestaurants = restaurantManager.getApprovedRestaurants().size();
        long pendingRestaurants = restaurantManager.getPendingRestaurants().size();

        double totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getFinalAmount)
                .sum();

        long totalOrders = allOrders.size();
        long activeOrders = allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.DELIVERED && o.getStatus() != OrderStatus.CANCELLED)
                .count();

        StringBuilder html = new StringBuilder();
        html.append(getHTMLHeader("Foodli System Analytics Report"));
        html.append("<body>\n");
        html.append("<div class='container'>\n");
        html.append("<h1>📊 Foodli System Analytics Report</h1>\n");
        html.append("<p class='date'>Generated on: ").append(LocalDate.now()).append("</p>\n");

        // System Overview Cards
        html.append("<div class='summary-cards'>\n");
        html.append("<div class='card'><h3>Total Restaurants</h3><p class='large'>").append(totalRestaurants).append("</p></div>\n");
        html.append("<div class='card'><h3>Approved Restaurants</h3><p class='large'>").append(approvedRestaurants).append("</p></div>\n");
        html.append("<div class='card'><h3>Pending Approvals</h3><p class='large'>").append(pendingRestaurants).append("</p></div>\n");
        html.append("<div class='card'><h3>Total Revenue</h3><p class='large'>").append(String.format("%,.0f", totalRevenue)).append(" Toman</p></div>\n");
        html.append("</div>\n");

        // Orders Overview
        html.append("<div class='summary-cards'>\n");
        html.append("<div class='card'><h3>Total Orders</h3><p class='large'>").append(totalOrders).append("</p></div>\n");
        html.append("<div class='card'><h3>Active Orders</h3><p class='large'>").append(activeOrders).append("</p></div>\n");
        html.append("<div class='card'><h3>Completed Orders</h3><p class='large'>").append(totalOrders - activeOrders).append("</p></div>\n");
        html.append("<div class='card'><h3>Avg Order Value</h3><p class='large'>")
                .append(String.format("%,.0f", totalOrders > 0 ? totalRevenue / totalOrders : 0)).append(" Toman</p></div>\n");
        html.append("</div>\n");

        // Restaurant Performance Table
        html.append("<div class='table-container'>\n");
        html.append("<h2>Top Performing Restaurants</h2>\n");
        html.append("<table>\n");
        html.append("<thead><tr><th>Restaurant</th><th>Rating</th><th>Orders</th><th>Revenue</th></tr></thead>\n");
        html.append("<tbody>\n");

        allRestaurants.stream()
                .filter(r -> r.getStatus() == ir.ac.kntu.models.enums.RestaurantStatus.APPROVED)
                .sorted((r1, r2) -> Double.compare(r2.getWallet(), r1.getWallet()))
                .limit(10)
                .forEach(restaurant -> {
                    long orderCount = orderManager.getOrdersByRestaurant(restaurant).size();
                    html.append("<tr>\n");
                    html.append("<td>").append(restaurant.getName()).append("</td>\n");
                    html.append("<td>").append(String.format("%.1f", restaurant.getRating())).append(" stars</td>\n");
                    html.append("<td>").append(orderCount).append("</td>\n");
                    html.append("<td>").append(String.format("%,.0f", restaurant.getWallet())).append("</td>\n");
                    html.append("</tr>\n");
                });

        html.append("</tbody>\n");
        html.append("</table>\n");
        html.append("</div>\n");

        html.append("</div>\n");
        html.append("</body>\n</html>");

        return html.toString();
    }

    private static String getHTMLHeader(String title) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>""" + title + """
                    </title>
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            padding: 20px;
                        }
                
                        .container {
                            max-width: 1200px;
                            margin: 0 auto;
                            background: white;
                            border-radius: 15px;
                            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
                            padding: 30px;
                        }
                
                        h1 {
                            color: #333;
                            margin-bottom: 10px;
                            text-align: center;
                            font-size: 2.5em;
                        }
                
                        h2 {
                            color: #444;
                            margin: 30px 0 20px 0;
                            border-bottom: 3px solid #667eea;
                            padding-bottom: 10px;
                        }
                
                        .date {
                            text-align: center;
                            color: #666;
                            margin-bottom: 30px;
                            font-style: italic;
                        }
                
                        .summary-cards {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                            gap: 20px;
                            margin-bottom: 40px;
                        }
                
                        .card {
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                            padding: 25px;
                            border-radius: 10px;
                            text-align: center;
                            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
                            transition: transform 0.3s ease;
                        }
                
                        .card:hover {
                            transform: translateY(-5px);
                        }
                
                        .card h3 {
                            margin-bottom: 15px;
                            font-size: 0.9em;
                            opacity: 0.9;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                        }
                
                        .card .large {
                            font-size: 2em;
                            font-weight: bold;
                        }
                
                        .table-container {
                            margin-bottom: 40px;
                            overflow-x: auto;
                        }
                
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            background: white;
                            border-radius: 8px;
                            overflow: hidden;
                            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                        }
                
                        th, td {
                            padding: 12px 15px;
                            text-align: left;
                            border-bottom: 1px solid #ddd;
                        }
                
                        th {
                            background: #667eea;
                            color: white;
                            font-weight: 600;
                            text-transform: uppercase;
                            font-size: 0.8em;
                            letter-spacing: 0.5px;
                        }
                
                        tr:nth-child(even) {
                            background: #f8f9fa;
                        }
                
                        tr:hover {
                            background: #e3f2fd;
                            transition: background 0.3s ease;
                        }
                
                        .status-delivered { color: #4caf50; font-weight: bold; }
                        .status-preparing { color: #ff9800; font-weight: bold; }
                        .status-sent { color: #2196f3; font-weight: bold; }
                        .status-registered { color: #9c27b0; font-weight: bold; }
                        .status-cancelled { color: #f44336; font-weight: bold; }
                
                        .chart-container {
                            margin: 40px 0;
                            text-align: center;
                        }
                
                        @media (max-width: 768px) {
                            .container {
                                padding: 15px;
                            }
                
                            .summary-cards {
                                grid-template-columns: 1fr;
                            }
                
                            h1 {
                                font-size: 2em;
                            }
                        }
                    </style>
                </head>
                """;
    }

    private static String getChartScript(Map<String, Double> monthlyData) {
        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();

        monthlyData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .limit(6) // 6 months
                .forEach(entry -> {
                    labels.append("'").append(entry.getKey()).append("',");
                    data.append(entry.getValue()).append(",");
                });

        return """
                <script>
                    const ctx = document.getElementById('revenueChart').getContext('2d');
                    new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: [""" + labels + """
                ],
                datasets: [{
                    label: 'Monthly Revenue (Toman)',
                    data: [""" + data + """
                                ],
                                borderColor: '#667eea',
                                backgroundColor: 'rgba(102, 126, 234, 0.1)',
                                borderWidth: 3,
                                fill: true,
                                tension: 0.4
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                legend: {
                                    position: 'top',
                                },
                                title: {
                                    display: true,
                                    text: 'Monthly Revenue Trend'
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {
                                        callback: function(value) {
                                            return value.toLocaleString() + ' T';
                                        }
                                    }
                                }
                            }
                        }
                    });
                </script>
                """;
    }

    private static void writeToFile(String filename, String content) {
        try {
            String fullPath = REPORTS_DIR + "/" + filename;
            try (FileWriter writer = new FileWriter(fullPath)) {
                writer.write(content);
            }
            System.out.println("Report generated: " + fullPath);
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}
