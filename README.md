# Foodli - Food Delivery System 🍕

A comprehensive food delivery platform built in Java that connects customers, restaurants, and support staff through an interactive console-based application.

## 📋 Table of Contents
- [Features](#-features)
- [Architecture](#-architecture)
- [Technologies Used](#-technologies-used)
- [Getting Started](#-getting-started)
- [User Roles](#-user-roles)
- [Project Structure](#-project-structure)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

## 🚀 Features

### Core Functionality
- **Multi-User System**: Support for Customers, Restaurant Managers, and Support Staff
- **Restaurant Management**: Complete CRUD operations for restaurants and menus
- **Order Processing**: Full order lifecycle from placement to delivery
- **Payment System**: Wallet-based payments with balance management
- **Advanced Search**: Text similarity search with fuzzy matching
- **Reporting**: HTML reports with interactive charts
- **Data Persistence**: CSV-based storage with backup/restore functionality

### Advanced Features
- **Pagination**: 10 results per page with navigation controls
- **Colored Output**: ANSI color-coded console interface
- **Text Similarity Search**: Levenshtein + Jaccard algorithms
- **HTML Reports**: Interactive charts using Chart.js
- **Unit Testing**: Comprehensive test suite with JUnit 5
- **Database Foundation**: SQLite JDBC integration ready
- **Backup/Restore**: Complete data backup and restoration system

## 🏗️ Architecture

### Design Patterns
- **Singleton Pattern**: Used for all manager classes (UserManager, RestaurantManager, etc.)
- **MVC Pattern**: Clear separation between models, handlers, and utilities
- **Factory Pattern**: Menu item creation and management

### Key Components
- **Models**: Core business entities (User, Restaurant, Order, Food, etc.)
- **Managers**: Business logic layer for each domain
- **Handlers**: User interface and menu management
- **Utilities**: Helper classes for common functionality

## 🛠️ Technologies Used

- **Language**: Java 11+
- **Build Tool**: Gradle
- **Database**: SQLite (JDBC)
- **Testing**: JUnit 5
- **Code Quality**: Checkstyle, PMD
- **Documentation**: JavaDoc

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Gradle (wrapper included)

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Foodli
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew run
   ```

4. **Run tests**
   ```bash
   ./gradlew test
   ```

### Development Setup

1. **Import project in your IDE**
   - Import as Gradle project
   - Ensure Java 11+ is configured

2. **Run with hot-reload**
   ```bash
   ./gradlew compileJava --continuous
   ```

## 👥 User Roles

### Customer
- Browse and search restaurants
- Add items to shopping cart
- Place and track orders
- Manage wallet balance
- Rate and review delivered orders
- Manage delivery addresses

### Restaurant Manager
- Register and manage restaurant
- Create and manage menu items
- Process incoming orders
- Update order status
- View wallet balance
- Generate financial reports
- Set delivery pricing

### Support Staff
- Approve/reject restaurant registrations
- Manage user accounts
- View system statistics
- Generate system reports
- Backup and restore data

## 📁 Project Structure

```
src/main/java/ir/ac/kntu/
├── handlers/          # User interface handlers
│   ├── CustomerMenu.java
│   ├── RestaurantManagerMenu.java
│   ├── SupportMenu.java
│   └── MainMenu.java
├── managers/          # Business logic managers
│   ├── UserManager.java
│   ├── RestaurantManager.java
│   ├── OrderManager.java
│   └── CartManager.java
├── models/            # Data models and enums
│   ├── User.java
│   ├── Restaurant.java
│   ├── Order.java
│   └── enums/
├── utilities/         # Helper utilities
│   ├── DatabaseManager.java
│   ├── HTMLReportGenerator.java
│   ├── TextSimilarity.java
│   └── PaginationUtility.java
└── Main.java          # Application entry point

src/test/java/ir/ac/kntu/
├── FoodliTest.java
├── utilities/
│   ├── PasswordUtilsTest.java
│   └── TextSimilarityTest.java
└── style/
    ├── CheckStyleTest.java
    └── CheckPMDTest.java
```

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Code Quality Checks
```bash
./gradlew checkstyleMain
./gradlew pmdMain
```

### Test Coverage
- Password validation utilities
- Text similarity algorithms
- Core business logic
- Code style compliance

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow existing code style and patterns
- Add unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📊 Project Status

- **Completion**: ~95%
- **Core Features**: ✅ Complete
- **Bonus Features**: ✅ Mostly Complete
- **Code Quality**: ✅ High Standards
- **Testing**: ✅ Comprehensive

## 📈 Future Enhancements

- Complete JDBC CRUD operations
- Web-based user interface
- Mobile application
- Real-time notifications
- Advanced analytics dashboard
- Multi-language support

## 📄 License

Copyright © 2025 Aghaaminegol.ir

This project is proprietary software. All rights reserved.

## 👨‍💻 Authors

- **Amin** - *Advanced Programming Course Project*

## 🙏 Acknowledgments

- Built as part of Advanced Programming course
- Implements modern software engineering practices
- Demonstrates comprehensive Java application development

---

**Built with ❤️ using Java & Gradle**
