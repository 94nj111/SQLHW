package Jdbc1;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/hwsql?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "*********";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add apartment");
                    System.out.println("2: add random apartments");
                    System.out.println("3: delete apartment");
                    System.out.println("4: change apartment");
                    System.out.println("5: view apartments");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1" -> addApartment(sc);
                        case "2" -> insertRandomApartments(sc);
                        case "3" -> deleteApartment(sc);
                        case "4" -> changeApartment(sc);
                        case "5" -> viewApartments(sc);
                        default -> {
                            return;
                        }
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS flats");
            st.execute("CREATE TABLE flats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + " district VARCHAR(128) DEFAULT NULL,"
                    + " address VARCHAR(128) DEFAULT NULL,"
                    + " area INT DEFAULT NULL,"
                    + " rooms INT DEFAULT NULL,"
                    + " price INT DEFAULT NULL)");
        } finally {
            st.close();
        }
    }

    private static void addApartment(Scanner sc) throws SQLException {
        System.out.print("Enter apartment district: ");
        String district = sc.nextLine();
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter apartment area: ");
        String sArea = sc.nextLine();
        int area = Integer.parseInt(sArea);
        System.out.print("Enter number of rooms: ");
        String sRooms = sc.nextLine();
        int rooms = Integer.parseInt(sRooms);
        System.out.print("Enter apartment price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        PreparedStatement ps = conn.prepareStatement("INSERT INTO flats (district, address, area, rooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setInt(3, area);
            ps.setInt(4, rooms);
            ps.setInt(5, price);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void deleteApartment(Scanner sc) throws SQLException {
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM flats WHERE address = ?");
        try {
            ps.setString(1, address);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void changeApartment(Scanner sc) throws SQLException {
        System.out.println("\t1: change district");
        System.out.println("\t2: change area");
        System.out.println("\t3: change number of rooms");
        System.out.println("\t4: change price");
        System.out.println("\t5: change address");
        System.out.print("\t-> ");

        String s = sc.nextLine();
        switch (s) {
            case "1" -> changeApartmentDistrict(sc);
            case "2" -> changeApartmentArea(sc);
            case "3" -> changeApartmentRooms(sc);
            case "4" -> changeApartmentPrice(sc);
            case "5" -> changeApartmentAddress(sc);
        }
    }

    private static void changeApartmentDistrict(Scanner sc) throws SQLException {
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter new apartment district: ");
        String district = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("UPDATE flats SET district = ? WHERE address = ?");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void changeApartmentArea(Scanner sc) throws SQLException {
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter new apartment area: ");
        String sArea = sc.nextLine();
        int area = Integer.parseInt(sArea);

        PreparedStatement ps = conn.prepareStatement("UPDATE flats SET area = ? WHERE address = ?");
        try {
            ps.setInt(1, area);
            ps.setString(2, address);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void changeApartmentRooms(Scanner sc) throws SQLException {
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter new number of rooms: ");
        String sRooms = sc.nextLine();
        int rooms = Integer.parseInt(sRooms);

        PreparedStatement ps = conn.prepareStatement("UPDATE flats SET rooms = ? WHERE address = ?");
        try {
            ps.setInt(1, rooms);
            ps.setString(2, address);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void changeApartmentAddress(Scanner sc) throws SQLException {
        System.out.print("Enter new apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter apartment id: ");
        String sId = sc.nextLine();
        int id = Integer.parseInt(sId);

        PreparedStatement ps = conn.prepareStatement("UPDATE flats SET address = ? WHERE id = ?");
        try {
            ps.setString(1, address);
            ps.setInt(2, id);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void changeApartmentPrice(Scanner sc) throws SQLException {
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter new apartment price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        PreparedStatement ps = conn.prepareStatement("UPDATE flats SET price = ? WHERE address = ?");
        try {
            ps.setInt(1, price);
            ps.setString(2, address);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void insertRandomApartments(Scanner sc) throws SQLException {
        System.out.print("Enter apartment count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);
        Random rnd = new Random();

        conn.setAutoCommit(false);
        try {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO flats (district, address, area, rooms, price) VALUES(?, ?, ?, ?, ?)");
                try {
                    for (int i = 0; i < count; i++) {
                        ps.setString(1, "district");
                        ps.setString(2, "address" + i);
                        ps.setInt(3, rnd.nextInt(15, 150));
                        ps.setInt(4, rnd.nextInt(1, 5));
                        ps.setInt(5, rnd.nextInt(30000, 100000));
                        ps.executeUpdate();
                    }
                    conn.commit();
                } finally {
                    ps.close();
                }
            } catch (Exception ex) {
                conn.rollback();
            }
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private static void viewApartments(Scanner sc) throws SQLException {
        String param = "";
        boolean selection = true;
        while (selection) {
            System.out.println("\t1: view selected");
            System.out.println("\t2: select min area");
            System.out.println("\t3: select max area");
            System.out.println("\t4: select min price");
            System.out.println("\t5: select max price");
            System.out.println("\t6: select district");
            System.out.println("\t7: select min rooms");
            System.out.println("\t8: select max rooms");
            System.out.print("\t-> ");

            String s = sc.nextLine();

            switch (s) {
                case "1" -> {
                    selection = false;
                    viewAllApartments(param);
                }
                case "2" -> param = paramMinAreaInitializer(param, sc);
                case "3" -> param = paramMaxAreaInitializer(param, sc);
                case "4" -> param = paramMinPriceInitializer(param, sc);
                case "5" -> param = paramMaxPriceInitializer(param, sc);
                case "6" -> param = paramDistrictInitializer(param, sc);
                case "7" -> param = paramMinRoomInitializer(param, sc);
                case "8" -> param = paramMaxRoomInitializer(param, sc);
            }
        }
    }

    private static String paramMinAreaInitializer(String param, Scanner sc){
        System.out.println("Enter min area");
        int minArea = sc.nextInt();
        if (param.equals("")) param += "WHERE area >= " + minArea;
        else param += " AND area >= " + minArea;
        return param;
    }

    private static String paramMaxAreaInitializer(String param, Scanner sc){
        System.out.println("Enter max area");
        int maxArea = sc.nextInt();
        if (param.equals("")) param += "WHERE area <= " + maxArea;
        else param += " AND area <= " + maxArea;
        return param;
    }

    private static String paramMinPriceInitializer(String param, Scanner sc){
        System.out.println("Enter min price");
        int minPrice = sc.nextInt();
        if (param.equals("")) param += "WHERE price >= " + minPrice;
        else param += " AND price >= " + minPrice;
        return param;
    }

    private static String paramMaxPriceInitializer(String param, Scanner sc){
        System.out.println("Enter max price");
        int maxPrice = sc.nextInt();
        if (param.equals("")) param += "WHERE price <= " + maxPrice;
        else param += " AND price <= " + maxPrice;
        return param;
    }

    private static String paramDistrictInitializer(String param, Scanner sc){
        System.out.println("Enter district");
        String district = sc.nextLine();
        if (param.equals("")) param += "WHERE district = '" + district + "'";
        else param += " AND district = '" + district + "'";
        return param;
    }

    private static String paramMinRoomInitializer(String param, Scanner sc){
        System.out.println("Enter min rooms");
        int minRooms = sc.nextInt();
        if (param.equals("")) param += "WHERE rooms >= " + minRooms;
        else param += " AND rooms >= " + minRooms;
        return param;
    }

    private static String paramMaxRoomInitializer(String param, Scanner sc){
        System.out.println("Enter max rooms");
        int maxRooms = sc.nextInt();
        if (param.equals("")) param += "WHERE rooms <= " + maxRooms;
        else param += " AND rooms <= " + maxRooms;
        return param;
    }

    private static void viewAllApartments(String param) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM flats " + param);
        try {
            ResultSet rs = ps.executeQuery();

            try {
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }
}
