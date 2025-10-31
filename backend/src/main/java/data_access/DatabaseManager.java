package data_access;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 数据库连接管理器，负责SQLite数据库的连接和初始化
 */
@Component
public class DatabaseManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);

    private final DataSource dataSource;

    public DatabaseManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 如果连接失败
     */
    public Connection getConnection() throws SQLException {
        final Connection connection = dataSource.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON;");
        }
        return connection;
    }

    /**
     * 初始化数据库表结构
     */
    @PostConstruct
    public void initializeDatabase() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY,
                    username TEXT NOT NULL
                )
            """);

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

            stmt.execute("CREATE INDEX IF NOT EXISTS idx_saved_recipes_user_id ON saved_recipes(user_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_meal_plan_user_date ON meal_plan_entries(user_id, meal_date)");

            LOGGER.info("数据库初始化完成");
        } catch (SQLException e) {
            LOGGER.error("数据库初始化失败: {}", e.getMessage(), e);
        }
    }
}
