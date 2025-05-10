import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Database {
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MonthlyData.class)
                .buildSessionFactory();
    }

    public Database() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS electricity").executeUpdate();
            session.createNativeQuery("""
                CREATE TABLE IF NOT EXISTS electricity (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    month TEXT NOT NULL,
                    year INTEGER NOT NULL,
                    previous REAL,
                    current REAL,
                    used REAL,
                    price REAL,
                    cost REAL
                )
            """).executeUpdate();
            transaction.commit();
        }
    }

    public List<MonthlyData> loadAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM MonthlyData", MonthlyData.class).list();
        }
    }

    public void saveAll(List<MonthlyData> dataList) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM MonthlyData").executeUpdate();
            for (MonthlyData data : dataList) {
                session.persist(data);
            }
            transaction.commit();
        }
    }
}