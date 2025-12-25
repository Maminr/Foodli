package ir.ac.kntu.handlers;

import ir.ac.kntu.models.enums.MenuType;
import ir.ac.kntu.models.errors.MenuNotFoundError;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MenuHandler {

    private static MenuHandler instance;
    private Map<MenuType, Menu> menus;
    private Stack<MenuType> menuStack;

    private MenuHandler() {
        menus = new HashMap<>();
        menuStack = new Stack<>();
        menus.put(MenuType.MAIN_MENU, new MainMenu());
        menus.put(MenuType.CUSTOMER_MENU, new CustomerMenu());
        menus.put(MenuType.RESTAURANT_MANAGER_MENU, new RestaurantManagerMenu());
        menus.put(MenuType.SUPPORT_MENU, new SupportMenu());
        menus.put(MenuType.ABOUT_US_MENU, new AboutUsMenu());
    }

    public static MenuHandler getInstance() {
        if (instance == null) {
            instance = new MenuHandler();
        }
        return instance;
    }


    public void loadMenu(MenuType menuType) {
        if (!menus.containsKey(menuType)) {
            throw new MenuNotFoundError("Menu " + menuType + " not found!");
        }
        if (menuType == MenuType.MAIN_MENU) {
            menuStack.clear();
        }
        menuStack.push(menuType);
        menus.get(menuType).enterMenu();
    }

    public void goBack() {
        if (menuStack.size() > 1) {
            menuStack.pop();
            MenuType previousMenu = menuStack.peek();
            menus.get(previousMenu).enterMenu();
        }
    }

    public boolean canGoBack() {
        return menuStack.size() > 1;
    }
}
