package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDaoHibernateImpl implements UserDao {

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

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (var session = Util.getSession()) {
            session.beginTransaction();
            session.createSQLQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица была успешно создана");
        } catch (Exception ignored) {
            log.error("Такая таблица была уже создана");
        }
    }

    @Override
    public void dropUsersTable() {
        try (var session = Util.getSession()) {
            session.beginTransaction();
            session.createSQLQuery(DROP_TABLE).executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица была успешно удалена");
        } catch (Exception ignored) {
            log.error("Такой таблицы для удаления не существует");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (var session = Util.getSession()) {
            try {
                session.beginTransaction();
                session.persist(new User(name, lastName, age));
                session.getTransaction().commit();
                log.info("Пользователь ".concat(name).concat(" - был успешно добавлен"));
            } catch (Exception exception) {
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (var session = Util.getSession()) {
            try {
                session.beginTransaction();
                session.createQuery("delete from User where id = :id")
                        .setParameter("id", id)
                        .executeUpdate();
                session.flush();
                session.getTransaction().commit();
                log.info("Пользователь с id = " + id + ", был успешно удален");
            } catch (Exception exception) {
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (var session = Util.getSession()) {
            try {
                session.beginTransaction();
                list = session.createQuery("select p from User p", User.class).getResultList();
                session.getTransaction().commit();
                log.info("Список всех пользователей был успешно получен");
            } catch (Exception exception) {
                session.getTransaction().rollback();
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (var session = Util.getSession()) {
            try {
                session.beginTransaction();
                session.createSQLQuery("TRUNCATE TABLE USERS").executeUpdate();
                session.getTransaction().commit();
                log.info("Таблица была успешно очищена");
            } catch (Exception exception) {
                session.getTransaction().rollback();
            }
        }
    }
}
