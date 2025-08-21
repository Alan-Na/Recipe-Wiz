package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接管理器，负责SQLite数据库的连接和初始化
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:recipe_wiz.db";
    private static Connection connection;

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 如果连接失败
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            // 启用外键约束
            connection.createStatement().execute("PRAGMA foreign_keys = ON;");
        }
        return connection;
    }

    /**
     * 初始化数据库表结构
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 创建用户表
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY,
                    username TEXT NOT NULL
                )
            """);

            // 创建食谱表
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS recipes (
                    recipe_id INTEGER PRIMARY KEY,
                    title TEXT NOT NULL,
                    description TEXT,
                    instructions TEXT,
                    servings INTEGER DEFAULT 1,
                    ingredient_lines TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // 创建保存的食谱表
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS saved_recipes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    recipe_id INTEGER,
                    recipe_data TEXT,
                    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id),
                    UNIQUE(user_id, recipe_id)
                )
            """);

            // 创建餐饮计划表
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS meal_plan_entries (
                    entry_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    recipe_id INTEGER,
                    recipe_data TEXT,
                    meal_date DATE,
                    meal_type TEXT,
                    status TEXT DEFAULT 'planned',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                )
            """);

            // 创建索引以提高查询性能
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_saved_recipes_user_id ON saved_recipes(user_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_meal_plan_user_date ON meal_plan_entries(user_id, meal_date)");
            
            System.out.println("数据库初始化完成");
            
        } catch (SQLException e) {
            System.err.println("数据库初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("关闭数据库连接失败: " + e.getMessage());
        }
    }
}