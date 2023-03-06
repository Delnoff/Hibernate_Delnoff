package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;

public class Main {
    public static void main(String[] args) {

        UserDaoHibernateImpl daoHibernate = new UserDaoHibernateImpl();

        daoHibernate.dropUsersTable();

        daoHibernate.createUsersTable();

        daoHibernate.saveUser("Romka","Doma", (byte) 33);

        daoHibernate.getAllUsers();

        daoHibernate.cleanUsersTable();

        daoHibernate.removeUserById(1);


    }
}
