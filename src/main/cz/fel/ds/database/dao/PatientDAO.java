package cz.fel.ds.database.dao;

import cz.fel.ds.database.model.Meal;
import cz.fel.ds.database.model.Patient;
import cz.fel.ds.database.model.TrainingProgram;
import cz.fel.ds.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Tom on 15. 5. 2015.
 */
public class PatientDAO {
    public ObservableList<Patient> getWeightLossPeople() {
        String sql = " select * from patients,\n" +
                "(\n" +
                " select pid from (select patient_id as pid,count(*) as count from medicalrecords mr group by mr.patient_id) as table1 where table1.count>1 AND\n" +
                "(select weight from medicalrecords mr where  mr.patient_id=pid order by medical_record_date desc limit 1) <\n" +
                "(select weight from medicalrecords mr2 where mr2.patient_id=pid order by medical_record_date desc offset 1 limit 1 )\n" +
                ") as passed\n" +
                "\n" +
                "where passed.pid=patients.patient_id;";
        SQLQuery query = HibernateUtil.getSession().createSQLQuery(sql);
        query.addEntity(Patient.class);
        List<Patient> l= query.list();
        ObservableList<Patient> list = FXCollections.observableList(l);
        return list;

    }

    public int create(Patient patient) {

        HibernateUtil.getSession().beginTransaction();
        HibernateUtil.getSession().save(patient);
        HibernateUtil.getSession().getTransaction().commit();
        return patient.getPatientId();
    }

    public Patient read(int id) {
        HibernateUtil.getSession().beginTransaction();
        Patient patient = (Patient) HibernateUtil.getSession().get(Patient.class, id);
        HibernateUtil.getSession().getTransaction().commit();
        return patient;
    }


    public boolean update(Patient patient) {
        HibernateUtil.getSession().beginTransaction();
        try {
            HibernateUtil.getSession().update(patient);
            HibernateUtil.getSession().getTransaction().commit();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean delete(Patient patient) {
        HibernateUtil.getSession().beginTransaction();
        try {

            Query q = null;
            q = HibernateUtil.getSession().createQuery("DELETE from MedicalRecord mr where mr.patient.patientId=:a ");
            q.setParameter("a", patient.getPatientId());
            q.executeUpdate();
            q = HibernateUtil.getSession().createQuery("DELETE from TrainingProgramToPatient tptp where tptp.patient.patientId=:a");
            q.setParameter("a", patient.getPatientId());
            q.executeUpdate();

            HibernateUtil.getSession().delete(patient);
            HibernateUtil.getSession().getTransaction().commit();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public ObservableList<Patient> selectObjectsTo(String type, Object value) {
        HibernateUtil.getSession().beginTransaction();
        Query q = null;
        ObservableList<Patient> listOfPatients;
        switch (type) {
            case "patientId":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.patientId=:patientId");
                break;
            case "firstName":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.firstName=:firstName");
                break;
            case "lastName":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.lastName=:lastName");
                break;

            case "nameStartsWith":
                Criteria crit;
                try {
                    int i = Integer.parseInt((String) value);
                    crit = HibernateUtil.getSession().createCriteria(Patient.class);
                    crit.add(Restrictions.or(Restrictions.or(Restrictions.ilike("lastName", value + "%"), Restrictions.ilike("firstName", value + "%")), Restrictions.eq("patientId", i)));
                } catch (Exception e) {
                    crit = HibernateUtil.getSession().createCriteria(Patient.class);
                    crit.add(Restrictions.or(Restrictions.ilike("lastName", value + "%"), Restrictions.ilike("firstName", value + "%")));
                }
                //sort podle patient id
                crit.addOrder(Order.asc("patientId"));


                listOfPatients = FXCollections.observableList(crit.list());
                HibernateUtil.getSession().getTransaction().commit();
                return listOfPatients;
            //q =  HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.lastName like concat(:nameStartsWith, '%') ");
            //break;

            case "gender":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.gender=:gender");
                break;
            case "birthdate":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.birthdate=:birthdate");
                break;
            case "nationalId":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.nationalId=:nationalId");
                break;
            case "email":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.email=:email");
                break;
            case "phone":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.phone=:phone");
                break;
            case "dietStarted":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.dietStarted=:dietStarted");
                break;
            case "diet":
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c where c.diet=:diet");
                break;
            default:
                q = HibernateUtil.getSession().createQuery("SELECT c from Patient c");
                listOfPatients = FXCollections.observableList(q.list());
                HibernateUtil.getSession().getTransaction().commit();
                return listOfPatients;
        }
        q.setParameter(type, value);
        listOfPatients = FXCollections.observableList(q.list());
        HibernateUtil.getSession().getTransaction().commit();
        return listOfPatients;
    }

}
