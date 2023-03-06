package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
    }

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS USERS (
                user_id int auto_increment primary key,
                user_name varchar (32),
                user_lastname varchar (32),
                user_age integer check (user_age > 0 AND user_age < 100)
            )
            """;

    private static final String DROP_TABLE = """
            DROP TABLE IF EXISTS USERS
            """;

    private static final String SAVE_USER = """
            INSERT INTO USERS (USER_NAME,USER_LASTNAME,USER_AGE)
            VALUES (?,?,?)
            """;

    private static final String REMOVE_USER = """
            DELETE FROM USERS
            WHERE USER_ID = ?
            """;

    private static final String GET_ALL_USERS = """
            SELECT USER_ID, USER_NAME, USER_LASTNAME, USER_AGE
            FROM USERS
            """;

    private static final String CLEAN_ALL_TABLE = """
            TRUNCATE TABLE USERS
            """;


    public void createUsersTable() {
        try (var connection = Util.openConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE);
            LOGGER.log(Level.INFO,"Таблица 'USER' - была успешно создана");
        } catch (SQLException message) {
            LOGGER.log(Level.WARNING,"Такая таблица уже существует");
        }
    }

    public void dropUsersTable() {
        try (var connection = Util.openConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE);
            LOGGER.log(Level.INFO,"Таблица была успешно удалена");
        } catch (SQLException message) {
            LOGGER.log(Level.WARNING, "Таблицы для удаления не существует");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(SAVE_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO,"User с именем - ".concat("<" + name + ">").concat(" был добавлен в базу данных"));
        } catch (SQLException e) {
            LOGGER.log(Level.INFO,"Возраст пользователя ".concat("<" + name + ">").concat(" - был введен некорректно, повторите попытку"));
        }
    }

    public void removeUserById(long id) {
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(REMOVE_USER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO,"User с id {" + id + "} - был удален из таблицы");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "User с таким id был уже удален или отсутствует");
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(GET_ALL_USERS)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long user_id = resultSet.getLong("USER_ID");
                String user_name = resultSet.getString("USER_NAME");
                String user_lastname = resultSet.getString("USER_LASTNAME");
                byte user_age = resultSet.getByte("USER_AGE");
                userList.add(new User(user_id,user_name, user_lastname, user_age));
            }
            for (User str : userList) {
                System.out.println(str.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (var connection = Util.openConnection();
             var preparedStatement = connection.prepareStatement(CLEAN_ALL_TABLE)) {
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO,"Таблица была успешно очищена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
