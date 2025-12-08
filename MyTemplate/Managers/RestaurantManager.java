package Managers;

import Models.Restaurant;

import java.util.ArrayList;

public class RestaurantManager
{
    private static RestaurantManager instance = null;
    private ArrayList<Restaurant> restaurantArrayList;

    private RestaurantManager()
    {
        restaurantArrayList = new ArrayList<>();
    }

    public static RestaurantManager getInstance()
    {
        if (instance == null)
        {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void addRestaurant(Restaurant restaurant)
    {
        restaurantArrayList.add(restaurant);
    }

    public Restaurant[] getRestaurantList()
    {
        return restaurantArrayList.toArray(new Restaurant[0]);
    }
}
