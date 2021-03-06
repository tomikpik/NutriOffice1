package cz.fel.ds.database.dao;

import cz.fel.ds.database.model.MealSchedule;
import cz.fel.ds.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Query;

/**
 * Created by Barush on 15. 5. 2015.
 */
public class MealScheduleDAO {
    public int create(MealSchedule mealSchedule) {
        HibernateUtil.getSession().beginTransaction();


        Query q = null;
        q = HibernateUtil.getSession().createQuery("DELETE from MealSchedule ms where ms.diet.dietId=:a and ms.mealType.mealTypeId=:b and ms.order=:c");
        q.setParameter("a", mealSchedule.getDiet().getDietId());
        q.setParameter("b", mealSchedule.getMealType().getMealTypeId());
        q.setParameter("c", mealSchedule.getOrder());
        q.executeUpdate();


        HibernateUtil.getSession().save(mealSchedule);
        HibernateUtil.getSession().flush();
        HibernateUtil.getSession().getTransaction().commit();
        return mealSchedule.getMealScheduleId();
    }

    public MealSchedule read(int id) {
        HibernateUtil.getSession().beginTransaction();
        MealSchedule mealSchedule;
        mealSchedule = (MealSchedule) HibernateUtil.getSession().get(MealSchedule.class, id);
        HibernateUtil.getSession().flush();
        HibernateUtil.getSession().getTransaction().commit();
        return mealSchedule;
    }

    public boolean update(MealSchedule mealSchedule) {
        HibernateUtil.getSession().beginTransaction();
        try {
            HibernateUtil.getSession().update(mealSchedule);
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().getTransaction().commit();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean delete(MealSchedule mealSchedule) {
        System.out.println(mealSchedule);
        HibernateUtil.getSession().beginTransaction();
        try {
            HibernateUtil.getSession().delete(mealSchedule);
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ah���");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public ObservableList<MealSchedule> selectObjectsTo(String type, Object value) {
        HibernateUtil.getSession().beginTransaction();
        Query q = null;
        switch (type) {
            case "mealScheduleId":
                q = HibernateUtil.getSession().createQuery("SELECT c from MealSchedule c where c.mealScheduleId=:mealScheduleId");
                break;
            case "order":
                q = HibernateUtil.getSession().createQuery("SELECT c from MealSchedule c where c.order=:order");
                break;
            case "mealType":
                q = HibernateUtil.getSession().createQuery("SELECT c from MealSchedule c where c.mealType=:mealType");
                break;
            case "meal":
                q = HibernateUtil.getSession().createQuery("SELECT c from MealSchedule c where c.meal=:meal");
                break;
            case "diet":
                q = HibernateUtil.getSession().createQuery("SELECT c from MealSchedule c where c.diet=:diet");
                break;
            default:
                q = HibernateUtil.getSession().createQuery("SELECT c from MealSchedule c");
                ObservableList<MealSchedule> listOfMealSchedules = FXCollections.observableList(q.list());
                HibernateUtil.getSession().flush();
                HibernateUtil.getSession().getTransaction().commit();
                return listOfMealSchedules;
        }
        q.setParameter(type, value);
        ObservableList<MealSchedule> listOfMealSchedules = FXCollections.observableList(q.list());
        HibernateUtil.getSession().flush();
        HibernateUtil.getSession().getTransaction().commit();
        return listOfMealSchedules;
    }

}
