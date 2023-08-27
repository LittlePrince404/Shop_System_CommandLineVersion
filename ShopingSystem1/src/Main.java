package org.example;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;


/*
 * 商品信息类
 */
class FruitItem {
    int ID;         // 商品编号
    String name;    // 商品名称
    double price;   // 商品价格
    int num;        // 商品数量
    double money;   // 商品总额
}

/*
 * 用户类
 */
class User {
    String username;
    String password;
    List<FruitItem> cart;
    List<FruitItem> purchaseHistory;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.cart = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }
}

/*
 * 管理员类
 */
class Admin {
    String username;
    String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

/*
 * 超市管理系统的启动类
 * 实现基本功能
 *    增加商品
 *    删除商品
 *    修改商品
 *    查询商品
 *    用户注册
 *    用户登录
 *    修改密码
 *    用户购物
 *    退出登录
 *    管理员登录
 *    管理员密码管理
 *    客户管理
 *    商品管理
 */
public class Main {
    private static List<FruitItem> items = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static User currentUser;
    private static Admin currentAdmin;

    //MD5
    public static String MD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        init();

        while (true) {
            mainMenu();

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    userLogin();
                    break;
                case 2:
                    adminLogin();
                    break;
                case 3:
                    System.out.println("谢谢使用！再见！");
                    scanner.close();
                    return;
                default:
                    System.out.println("无效的操作编号，请重新输入！");
                    break;
            }

            while (currentUser != null) {
                userMenu();

                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        changeUserPassword();
                        break;
                    case 2:
                        addToCart();
                        break;
                    case 3:
                        removeFromCart();
                        break;
                    case 4:
                        updateCart();
                        break;
                    case 5:
                        checkout();
                        break;
                    case 6:
                        viewPurchaseHistory();
                        break;
                    case 7:
                        logoutUser();
                        break;
                    default:
                        System.out.println("无效的操作编号，请重新输入！");
                        break;
                }
            }

            while (currentAdmin != null) {
                adminMenu();

                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        changeAdminPassword();
                        break;
                    case 2:
                        resetUserPassword();
                        break;
                    case 3:
                        manageCustomers();
                        break;
                    case 4:
                        manageItems();
                        break;
                    case 5:
                        logoutAdmin();
                        break;
                    default:
                        System.out.println("无效的操作编号，请重新输入！");
                        break;
                }
            }
        }
    }

    /*
     * 初始化商品和用户数据
     */
    public static void init() {
        FruitItem f1 = new FruitItem();
        f1.ID = 1000;
        f1.name = "笔记本";
        f1.price = 10.0;

        FruitItem f2 = new FruitItem();
        f2.ID = 1001;
        f2.name = "西红柿";
        f2.price = 2.0;

        FruitItem f3 = new FruitItem();
        f3.ID = 1002;
        f3.name = "辣条";
        f3.price = 5.0;

        items.add(f1);
        items.add(f2);
        items.add(f3);

        //添加初始用户
        User user1 = new User("user1", "123456");
        User user2 = new User("user2", "abcdef");
        users.add(user1);
        users.add(user2);
    }

    /*
     * 主菜单
     */
    public static void mainMenu() {
        System.out.println();
        System.out.println("==========超市管理系统===========");
        System.out.println("1:用户登录  2:管理员登录  3:退出系统");
        System.out.println("请输入操作编号：");
    }

    /*
     * 用户菜单
     */
    public static void userMenu() {
        System.out.println();
        System.out.println("==========用户菜单===========");
        System.out.println("1:密码管理");
        System.out.println("2:购物");
        System.out.println("3:移除购物车中的商品");
        System.out.println("4:修改购物车中的商品");
        System.out.println("5:模拟结账");
        System.out.println("6:查看购物历史");
        System.out.println("7:退出登录");
        System.out.println("请输入操作编号：");
    }

    /*
     * 管理员菜单
     */
    public static void adminMenu() {
        System.out.println();
        System.out.println("==========管理员菜单===========");
        System.out.println("1:密码管理");
        System.out.println("2:重置用户密码");
        System.out.println("3:客户管理");
        System.out.println("4:商品管理");
        System.out.println("5:退出登录");
        System.out.println("请输入操作编号：");
    }

    /*
     * 用户登录
     */
    public static void userLogin() {
        System.out.println();
        System.out.println("==========用户登录===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入用户名：");
        String username = scanner.nextLine();

        System.out.println("请输入密码：");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username) && MD5(user.password).equals(MD5(password))) {
                currentUser = user;
                System.out.println("登录成功！");
                return;
            }
        }

        System.out.println("用户名或密码错误！");
    }

    /*
     * 管理员登录
     */
    public static void adminLogin() {
        System.out.println();
        System.out.println("==========管理员登录===========");
        String oldPassword = MD5("admin");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入用户名：");
        String username = scanner.nextLine();

        System.out.println("请输入密码：");
        String password = scanner.nextLine();

        if (username.equals("admin") && MD5(password).equals(oldPassword)) {
            currentAdmin = new Admin(username, password);
            System.out.println("管理员登录成功！");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }

    /*
     * 密码管理 - 用户
     */
    public static void changeUserPassword() {
        System.out.println();
        System.out.println("==========密码管理===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入旧密码：");
        String oldPassword = scanner.nextLine();

        if (!currentUser.password.equals(oldPassword)) {
            System.out.println("旧密码错误！");
            return;
        }

        System.out.println("请输入新密码：");
        String newPassword = scanner.nextLine();

        currentUser.password = newPassword;
        System.out.println("密码修改成功！");
    }

    /*
     * 密码管理 - 管理员
     */
    public static void changeAdminPassword() {
        System.out.println();
        System.out.println("==========密码管理===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入旧密码：");
        String oldPassword = scanner.nextLine();

        if (!currentAdmin.password.equals(oldPassword)) {
            System.out.println("旧密码错误！");
            return;
        }

        System.out.println("请输入新密码：");
        String newPassword = scanner.nextLine();

        currentAdmin.password = newPassword;
        System.out.println("密码修改成功！");
    }

    /*
     * 重置用户密码 - 管理员
     */
    public static void resetUserPassword() {
        System.out.println();
        System.out.println("==========重置用户密码===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要重置密码的用户名：");
        String username = scanner.nextLine();

        boolean found = false;
        for (User user : users) {
            if (user.username.equals(username)) {
                System.out.println("请输入新密码：");
                String newPassword = scanner.nextLine();

                user.password = newPassword;
                System.out.println("用户密码重置成功！");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("未找到该用户！");
        }
    }

    /*
     * 客户管理 - 管理员
     */
    public static void manageCustomers() {
        System.out.println();
        System.out.println("==========客户管理===========");
        System.out.println("1:列出所有客户信息");
        System.out.println("2:删除客户信息");
        System.out.println("3:查询客户信息");
        System.out.println("请输入操作编号：");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                listCustomers();
                break;
            case 2:
                deleteCustomer();
                break;
            case 3:
                searchCustomer();
                break;
            default:
                System.out.println("无效的操作编号，请重新输入！");
                break;
        }
    }

    /*
     * 列出所有客户信息 - 管理员
     */
    public static void listCustomers() {
        System.out.println();
        System.out.println("==========列出所有客户信息===========");

        for (User user : users) {
            System.out.println("用户名：" + user.username);
        }
    }

    /*
     * 删除客户信息 - 管理员
     */
    public static void deleteCustomer() {
        System.out.println();
        System.out.println("==========删除客户信息===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要删除的用户名：");
        String username = scanner.nextLine();

        User toBeDeleted = null;
        for (User user : users) {
            if (user.username.equals(username)) {
                toBeDeleted = user;
                break;
            }
        }

        if (toBeDeleted != null) {
            users.remove(toBeDeleted);
            System.out.println("用户信息删除成功！");
        } else {
            System.out.println("未找到该用户！");
        }
    }

    /*
     * 查询客户信息 - 管理员
     */
    public static void searchCustomer() {
        System.out.println();
        System.out.println("==========查询客户信息===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要查询的用户名：");
        String username = scanner.nextLine();

        boolean found = false;
        for (User user : users) {
            if (user.username.equals(username)) {
                System.out.println("用户名：" + user.username);
                System.out.println("密码：" + user.password);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("未找到该用户！");
        }
    }

    /*
     * 商品管理 - 管理员
     */
    public static void manageItems() {
        System.out.println();
        System.out.println("==========商品管理===========");
        System.out.println("1:列出所有商品信息");
        System.out.println("2:添加商品信息");
        System.out.println("3:修改商品信息");
        System.out.println("4:删除商品信息");
        System.out.println("5:查询商品信息");
        System.out.println("请输入操作编号：");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                listItems();
                break;
            case 2:
                addItem();
                break;
            case 3:
                updateItem();
                break;
            case 4:
                deleteItem();
                break;
            case 5:
                searchItem();
                break;
            default:
                System.out.println("无效的操作编号，请重新输入！");
                break;
        }
    }

    /*
     * 列出所有商品信息 - 管理员
     */
    public static void listItems() {
        System.out.println();
        System.out.println("==========列出所有商品信息===========");

        for (FruitItem item : items) {
            System.out.println("编号：" + item.ID);
            System.out.println("名称：" + item.name);
            System.out.println("价格：" + item.price);
            System.out.println("数量：" + item.num);
            System.out.println("总额：" + item.money);
        }
    }

    /*
     * 添加商品信息 - 管理员
     */
    public static void addItem() {
        System.out.println();
        System.out.println("==========添加商品信息===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入商品编号：");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("请输入商品名称：");
        String name = scanner.nextLine();

        System.out.println("请输入商品价格：");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("请输入商品数量：");
        int num = scanner.nextInt();
        scanner.nextLine();

        System.out.println("请输入商品总额：");
        double money = scanner.nextDouble();
        scanner.nextLine();

        FruitItem newItem = new FruitItem();
        newItem.ID = id;
        newItem.name = name;
        newItem.price = price;
        newItem.num = num;
        newItem.money = money;

        items.add(newItem);
        System.out.println("商品信息添加成功！");
    }

    /*
     * 修改商品信息 - 管理员
     */
    public static void updateItem() {
        System.out.println();
        System.out.println("==========修改商品信息===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要修改的商品编号：");
        int id = scanner.nextInt();
        scanner.nextLine();

        FruitItem toBeUpdated = null;
        for (FruitItem item : items) {
            if (item.ID == id) {
                toBeUpdated = item;
                break;
            }
        }

        if (toBeUpdated != null) {
            System.out.println("请输入新的商品名称：");
            String newName = scanner.nextLine();
            toBeUpdated.name = newName;

            System.out.println("请输入新的商品价格：");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();
            toBeUpdated.price = newPrice;

            System.out.println("请输入新的商品数量：");
            int newNum = scanner.nextInt();
            scanner.nextLine();
            toBeUpdated.num = newNum;

            System.out.println("请输入新的商品总额：");
            double newMoney = scanner.nextDouble();
            scanner.nextLine();
            toBeUpdated.money = newMoney;

            System.out.println("商品信息修改成功！");
        } else {
            System.out.println("未找到该商品！");
        }
    }

    /*
     * 删除商品信息 - 管理员
     */
    public static void deleteItem() {
        System.out.println();
        System.out.println("==========删除商品信息===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要删除的商品编号：");
        int id = scanner.nextInt();
        scanner.nextLine();

        FruitItem toBeDeleted = null;
        for (FruitItem item : items) {
            if (item.ID == id) {
                toBeDeleted = item;
                break;
            }
        }

        if (toBeDeleted != null) {
            items.remove(toBeDeleted);
            System.out.println("商品信息删除成功！");
        } else {
            System.out.println("未找到该商品！");
        }
    }

    /*
     * 查询商品信息 - 管理员
     */
    public static void searchItem() {
        System.out.println();
        System.out.println("==========查询商品信息===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要查询的商品编号：");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean found = false;
        for (FruitItem item : items) {
            if (item.ID == id) {
                System.out.println("编号：" + item.ID);
                System.out.println("名称：" + item.name);
                System.out.println("价格：" + item.price);
                System.out.println("数量：" + item.num);
                System.out.println("总额：" + item.money);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("未找到该商品！");
        }
    }

    /*
     * 用户注销
     */
    public static void logoutUser() {
        System.out.println();
        System.out.println("用户已注销！");
        currentUser = null;
    }

    /*
     * 管理员注销
     */
    public static void logoutAdmin() {
        System.out.println();
        System.out.println("管理员已注销！");
        currentAdmin = null;
    }

    /*
     * 添加商品到购物车
     */
    public static void addToCart() {
        System.out.println();
        System.out.println("==========添加商品到购物车===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要添加的商品编号：");
        int id = scanner.nextInt();
        scanner.nextLine();

        FruitItem toBeAdded = null;
        for (FruitItem item : items) {
            if (item.ID == id) {
                toBeAdded = item;
                break;
            }
        }

        if (toBeAdded != null) {
            currentUser.cart.add(toBeAdded);
            System.out.println("商品已添加到购物车！");
        } else {
            System.out.println("未找到该商品！");
        }
    }

    /*
     * 从购物车移除商品
     */
    public static void removeFromCart() {
        System.out.println();
        System.out.println("==========从购物车移除商品===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要移除的商品编号：");
        int id = scanner.nextInt();
        scanner.nextLine();

        FruitItem toBeRemoved = null;
        for (FruitItem item : currentUser.cart) {
            if (item.ID == id) {
                toBeRemoved = item;
                break;
            }
        }

        if (toBeRemoved != null) {
            currentUser.cart.remove(toBeRemoved);
            System.out.println("商品已从购物车移除！");
        } else {
            System.out.println("未找到该商品！");
        }
    }

    /*
     * 修改购物车中的商品
     */
    public static void updateCart() {
        System.out.println();
        System.out.println("==========修改购物车中的商品===========");
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要修改的商品编号：");
        int id = scanner.nextInt();
        scanner.nextLine();

        FruitItem toBeUpdated = null;
        for (FruitItem item : currentUser.cart) {
            if (item.ID == id) {
                toBeUpdated = item;
                break;
            }
        }

        if (toBeUpdated != null) {
            System.out.println("请输入新的商品名称：");
            String newName = scanner.nextLine();
            toBeUpdated.name = newName;

            System.out.println("请输入新的商品价格：");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();
            toBeUpdated.price = newPrice;

            System.out.println("请输入新的商品数量：");
            int newNum = scanner.nextInt();
            scanner.nextLine();
            toBeUpdated.num = newNum;

            System.out.println("请输入新的商品总额：");
            double newMoney = scanner.nextDouble();
            scanner.nextLine();
            toBeUpdated.money = newMoney;

            System.out.println("购物车中的商品信息修改成功！");
        } else {
            System.out.println("未找到该商品！");
        }
    }

    /*
     * 模拟结账
     */
    public static void checkout() {
        System.out.println();
        System.out.println("==========模拟结账===========");
        System.out.println("购物车中的商品清单：");

        double total = 0.0;
        for (FruitItem item : currentUser.cart) {
            System.out.println("商品编号：" + item.ID);
            System.out.println("商品名称：" + item.name);
            System.out.println("商品价格：" + item.price);
            System.out.println("商品数量：" + item.num);
            System.out.println("商品总额：" + item.money);
            System.out.println("--------------------------------------");
            total += item.money;
        }

        System.out.println("总计：" + total);
        System.out.println("是否确认结账？(Y/N)");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("Y")) {
            for (FruitItem item : currentUser.cart) {
                item.num -= 1;
                item.money -= item.price;
            }

            currentUser.purchaseHistory.addAll(currentUser.cart);
            currentUser.cart.clear();
            System.out.println("结账成功！");
        } else {
            System.out.println("结账取消！");
        }
    }

    /*
     * 查看购物历史
     */
    public static void viewPurchaseHistory() {
        System.out.println();
        System.out.println("==========购物历史===========");
        System.out.println("购物历史清单：");

        for (FruitItem item : currentUser.purchaseHistory) {
            System.out.println("商品编号：" + item.ID);
            System.out.println("商品名称：" + item.name);
            System.out.println("商品价格：" + item.price);
            System.out.println("商品数量：" + item.num);
            System.out.println("商品总额：" + item.money);
            System.out.println("--------------------------------------");
        }
    }
}