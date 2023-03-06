package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Properties;

public final class Util {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final SessionFactory sessionFactory;
    private static final Properties PROPERTIES = new Properties();

    private Util() {
    }

    static {
        try {
            PROPERTIES.setProperty("hibernate.connection.url", HelperUtil.get(URL_KEY));
            PROPERTIES.setProperty("hibernate.connection.username", HelperUtil.get(USERNAME_KEY));
            PROPERTIES.setProperty("hibernate.connection.password", HelperUtil.get(PASSWORD_KEY));
            PROPERTIES.setProperty("hibernate.hbm2ddl.auto","update");

            sessionFactory = new org.hibernate.cfg.Configuration()
                    .addProperties(PROPERTIES)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Exception err) {
            throw new ExceptionInInitializerError(err);
        }
    }

    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }


    /* JDBC connection

    public static Connection openConnection() {
        try {
            return DriverManager.getConnection(
                    HelperUtil.get(URL_KEY),
                    HelperUtil.get(USERNAME_KEY),
                    HelperUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     */
}

