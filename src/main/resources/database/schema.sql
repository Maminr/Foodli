-- Foodli Database Schema
-- This schema supports all entities in the Foodli system

-- Users table (base table for all user types)
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(11) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK(role IN ('CUSTOMER', 'RESTAURANT_MANAGER', 'SUPPORT')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customers table (extends users)
CREATE TABLE IF NOT EXISTS customers (
    user_id INTEGER PRIMARY KEY,
    wallet_balance DOUBLE DEFAULT 0.0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Managers table (extends users)
CREATE TABLE IF NOT EXISTS managers (
    user_id INTEGER PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Support table (extends users)
CREATE TABLE IF NOT EXISTS support (
    user_id INTEGER PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Addresses table
CREATE TABLE IF NOT EXISTS addresses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER NOT NULL,
    description TEXT NOT NULL,
    zone_number INTEGER NOT NULL CHECK(zone_number >= 1 AND zone_number <= 20),
    FOREIGN KEY (customer_id) REFERENCES customers(user_id) ON DELETE CASCADE
);

-- Restaurants table
CREATE TABLE IF NOT EXISTS restaurants (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    manager_id INTEGER NOT NULL,
    address TEXT NOT NULL,
    zone_number INTEGER NOT NULL CHECK(zone_number >= 1 AND zone_number <= 20),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_REVIEW' CHECK(status IN ('PENDING_REVIEW', 'APPROVED', 'REJECTED')),
    rejection_reason TEXT,
    rating DOUBLE DEFAULT 0.0,
    wallet_balance DOUBLE DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (manager_id) REFERENCES managers(user_id) ON DELETE CASCADE
);

-- Restaurant food types (many-to-many relationship)
CREATE TABLE IF NOT EXISTS restaurant_food_types (
    restaurant_id INTEGER NOT NULL,
    food_type VARCHAR(20) NOT NULL CHECK(food_type IN ('IRANIAN', 'FAST_FOOD', 'SEAFOOD', 'INTERNATIONAL', 'CAFE')),
    PRIMARY KEY (restaurant_id, food_type),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

-- Foods table
CREATE TABLE IF NOT EXISTS foods (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    restaurant_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL CHECK(price >= 0),
    category VARCHAR(20) NOT NULL CHECK(category IN ('MAIN_DISH', 'APPETIZER', 'BEVERAGE')),
    available BOOLEAN DEFAULT TRUE,
    -- Main dish attributes
    ingredients TEXT,
    cooking_time INTEGER,
    serving_type VARCHAR(20) CHECK(serving_type IN ('PLATED', 'SANDWICH')),
    -- Appetizer attributes
    pieces_per_serving INTEGER,
    portion_size VARCHAR(20) CHECK(portion_size IN ('SMALL', 'MEDIUM', 'LARGE')),
    -- Beverage attributes
    volume INTEGER,
    packaging VARCHAR(20) CHECK(packaging IN ('CAN', 'BOTTLE', 'CUP')),
    sugar_status VARCHAR(20) CHECK(sugar_status IN ('DIET', 'REGULAR')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER NOT NULL,
    restaurant_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'REGISTERED' CHECK(status IN ('REGISTERED', 'PREPARING', 'SENT', 'DELIVERED', 'CANCELLED')),
    items_total DOUBLE NOT NULL CHECK(items_total >= 0),
    delivery_cost DOUBLE NOT NULL CHECK(delivery_cost >= 0),
    final_amount DOUBLE NOT NULL CHECK(final_amount >= 0),
    delivery_address TEXT NOT NULL,
    delivery_zone INTEGER NOT NULL,
    review_rating INTEGER CHECK(review_rating >= 0 AND review_rating <= 5),
    review_comment TEXT,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(user_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

-- Order items table
CREATE TABLE IF NOT EXISTS order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    food_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL CHECK(quantity > 0),
    unit_price DOUBLE NOT NULL CHECK(unit_price >= 0),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE
);

-- Indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone_number);
CREATE INDEX IF NOT EXISTS idx_restaurants_manager ON restaurants(manager_id);
CREATE INDEX IF NOT EXISTS idx_restaurants_status ON restaurants(status);
CREATE INDEX IF NOT EXISTS idx_foods_restaurant ON foods(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_restaurant ON orders(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_addresses_customer ON addresses(customer_id);

