package ir.ac.kntu.handlers;

import ir.ac.kntu.models.enums.MenuType;
import ir.ac.kntu.models.errors.MenuNotFoundError;

import java.util.HashMap;
import java.util.Map;

public class MenuHandler {

    private static MenuHandler instance;
    private Map<MenuType, Menu> menus;

    private MenuHandler() {
        menus = new HashMap<>();
        menus.put(MenuType.MAIN_MENU, new MainMenu());
    }

    public static MenuHandler getInstance() {
        if (instance == null) {
            instance = new MenuHandler();
        }
        return instance;
    }


    public void loadMenu(MenuType menuType) {
        if (menus.containsKey(menuType)) {
            menus.get(menuType).enterMenu();
        }
        throw new MenuNotFoundError("Menu " + menuType + " not found!");
    }
}
