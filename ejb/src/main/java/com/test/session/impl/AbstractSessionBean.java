package com.test.session.impl;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/13/15
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */

import com.test.session.GenericSessionBean;
import com.test.utils.HibernateUtil;

import javax.ejb.DuplicateKeyException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractSessionBean<T, ID extends Serializable> implements GenericSessionBean<T, ID> {
    private transient static Logger logger = Logger.getLogger("SessionBean");

    private Class<T> persistentClass;

    public AbstractSessionBean() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }
    public String getPersistentClassName() {
        return persistentClass.getSimpleName();
    }

    protected AbstractSessionBean(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @PersistenceContext(unitName = "core-data")
    protected EntityManager entityManager;

    public T save(T entity) throws DuplicateKeyException {
        logger.log(Level.INFO, "saving T instance");
        try {
            entityManager.persist(entity);
            logger.log(Level.INFO, "save successful");
            return entity;
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "save failed", re);
            throw re;
        }
    }

    public void delete(ID id) throws RemoveException {
        logger.log(Level.INFO, "deleting T instance");
        try {
            T entity = entityManager.getReference(getPersistentClass(), id);
            entityManager.remove(entity);
            logger.log(Level.INFO, "delete successful");
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "delete failed", re);
            throw re;
        }
    }

    public void delete(T entity) throws RemoveException {
        logger.log(Level.INFO, "deleting T instance");
        try {
            entityManager.remove(entity);
            entityManager.flush();
            logger.log(Level.INFO, "delete successful");
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "delete failed", re);
            throw re;
        }
    }

    public Integer delete(ID[] ids) throws RemoveException {
        logger.log(Level.INFO, "deleting T instances");
        Integer res = 0;
        for (ID id : ids) {
            try {

                T entity = entityManager.getReference(getPersistentClass(), id);
                entityManager.remove(entity);
                res++;
            } catch (RuntimeException re) {
                logger.log(Level.SEVERE, "delete failed " + id, re);
                throw re;
            }
        }
        return res;
    }

    public void deleteAll(List<T> entities) {
        try{
            for (T entity : entities) {
                entityManager.remove(entity);
            }
            entityManager.flush();
        }catch (RuntimeException re) {
            throw re;
        }
    }

    public T update(T entity) throws DuplicateKeyException {
        logger.log(Level.INFO, "updating T instance");
        try {
            T result = entityManager.merge(entity);
            logger.log(Level.INFO, "update successful");
            return result;
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "update failed", re);
            throw re;
        }
    }

    public T findById(ID id) throws ObjectNotFoundException {
        logger.log(Level.INFO, "finding T instance with id: " + id);
        try {
            T instance = entityManager.find(getPersistentClass(), id);
            if (instance == null) {
                throw new ObjectNotFoundException("Not found T " + id);
            }
            return instance;
        } catch (RuntimeException re) {
            //logger.log(Level.SEVERE, "find failed", re);
            throw new ObjectNotFoundException("Not found T " + id);
        }
    }

    public T findEqualUnique(String propertyName, Object value) throws ObjectNotFoundException {
        logger.log(Level.INFO, "finding unique T instance with property: " + propertyName
                + ", value: " + value);
        try {
            final String queryString = "select model from " + getPersistentClassName() + " model where model."
                    + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);

            return (T)query.getSingleResult();
        } catch (NoResultException re) {
            //logger.log(Level.SEVERE, "find by property name failed", re);
            throw new ObjectNotFoundException("Not found object " + propertyName + " with value " + value);
        }
    }

    public T findEqualUniqueCaseSensitive(String propertyName, Object value) throws ObjectNotFoundException {
        logger.log(Level.INFO, "finding unique T instance with property: " + propertyName
                + ", value: " + value);
        try {
            final String queryString = "select model from " + getPersistentClassName() + " model where LOWER(model."
                    + propertyName + " ) = LOWER(:propertyValue)";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);

            return (T)query.getSingleResult();
        } catch (NoResultException re) {
            //logger.log(Level.SEVERE, "find by property name failed", re);
            throw new ObjectNotFoundException("Not found object " + propertyName + " with value " + value);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findByProperty(String propertyName, final Object value,
                                  final int... rowStartIdxAndCount) {
        logger.log(Level.INFO, "finding T instance with property: " + propertyName
                + ", value: " + value);
        try {
            final String queryString = "select model from " + getPersistentClassName() + " model where model."
                    + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }

                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public List<T> findProperty(String propertyName, final Object value) {
        logger.log(Level.INFO, "finding all T instance with property: " + propertyName
                + ", value: " + value);
        try {
            final String queryString = "select model from " + getPersistentClassName() + " model where model."
                    + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);

            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
    }


    @SuppressWarnings("unchecked")
    public List<T> findAll(final int... rowStartIdxAndCount) {
        logger.log(Level.INFO, "finding all T instances " + getPersistentClassName());
        try {
            final String queryString = "select model from " + getPersistentClassName() + " model";
            Query query = entityManager.createQuery(queryString);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }

                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public Object[] searchByProperties(Map<String, Object> properties,
                                       String sortExpression, String sortDirection, Integer offset,
                                       Integer limit) {
        try {
            final Object[] nameQuery = HibernateUtil.buildNameQuery(getPersistentClass(), properties, null, sortExpression,
                    sortDirection, true, false, null, true);

            String queryString = "select A " + nameQuery[0] + nameQuery[1];
            //System.out.println(queryString);
            Query query = entityManager.createQuery(queryString);
            if (nameQuery.length == 4) {
                String[] params = (String[]) nameQuery[2];
                Object[] values = (Object[]) nameQuery[3];
                for (int i = 0; i < params.length; i++) {

                    query.setParameter(params[i], values[i]);
                }
            }
            if (offset != null && offset >= 0) {
                query.setFirstResult(offset);
            }
            if (limit != null && limit > 0) {
                query.setMaxResults(limit);
            }
            List<T> res = query.getResultList();

            Object totalItem = 0;
            String queryTotal = "SELECT COUNT(*) " + nameQuery[0];

            Query query2 = entityManager.createQuery(queryTotal);
            if (nameQuery.length == 4) {
                String[] params = (String[]) nameQuery[2];
                Object[] values = (Object[]) nameQuery[3];
                for (int i = 0; i < params.length; i++) {

                    query2.setParameter(params[i], values[i]);
                }
            }
            totalItem = query2.getSingleResult();
            return new Object[]{totalItem, res};
        } catch (RuntimeException re) {
            //logger.log(Level.SEVERE, "find all failed", re);
        }
        return new Object[]{0, new ArrayList<T>()};
    }

    public Object[] searchByProperties(Map<String, Object> properties,
                                       String sortExpression, String sortDirection, Integer offset,
                                       Integer limit, String whereClause) {
        try {
            final Object[] nameQuery = HibernateUtil.buildNameQuery(getPersistentClass(), properties, null, sortExpression,
                    sortDirection, true, false, whereClause, true);

            String queryString = "select A " + nameQuery[0] + nameQuery[1];
            //System.out.println(queryString);
            Query query = entityManager.createQuery(queryString);
            if (nameQuery.length == 4) {
                String[] params = (String[]) nameQuery[2];
                Object[] values = (Object[]) nameQuery[3];
                for (int i = 0; i < params.length; i++) {

                    query.setParameter(params[i], values[i]);
                }
            }
            if (offset != null && offset >= 0) {
                query.setFirstResult(offset);
            }
            if (limit != null && limit > 0) {
                query.setMaxResults(limit);
            }
            List<T> res = query.getResultList();
            Object totalItem = 0;
            String queryTotal = "SELECT COUNT(*) " + nameQuery[0];

            Query query2 = entityManager.createQuery(queryTotal);
            if (nameQuery.length == 4) {
                String[] params = (String[]) nameQuery[2];
                Object[] values = (Object[]) nameQuery[3];
                for (int i = 0; i < params.length; i++) {

                    query2.setParameter(params[i], values[i]);
                }
            }
            totalItem = query2.getSingleResult();
            return new Object[]{totalItem, res};
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "find all failed", re);
        }
        return new Object[]{0, new ArrayList<T>()};
    }

    public List<T> findProperties(Map<String, Object> properties) {
        try {
            final Object[] nameQuery = HibernateUtil.buildNameQuery(getPersistentClass(), properties, null, null,
                    null, true, true, null, false);

            String queryString = "select A " + nameQuery[0] + nameQuery[1];
            //System.out.println(queryString);
            Query query = entityManager.createQuery(queryString);
            if (nameQuery.length == 4) {
                String[] params = (String[]) nameQuery[2];
                Object[] values = (Object[]) nameQuery[3];
                for (int i = 0; i < params.length; i++) {

                    query.setParameter(params[i], values[i]);
                }
            }

            List<T> res = query.getResultList();
            return  res;
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "find all failed", re);
        }
        return new ArrayList<T>();
    }

    public Object findByProperties(Map<String, Object> properties,
                                   String sortExpression, String sortDirection, Integer offset,
                                   Integer limit) {
        try {
            final Object[] nameQuery = HibernateUtil.buildNameQuery(getPersistentClass(), properties, null, sortExpression,
                    sortDirection, true, true, null, false);

            String queryString = "select A " + nameQuery[0] + nameQuery[1];
            //System.out.println(queryString);
            Query query = entityManager.createQuery(queryString);
            if (nameQuery.length == 4) {
                String[] params = (String[]) nameQuery[2];
                Object[] values = (Object[]) nameQuery[3];
                for (int i = 0; i < params.length; i++) {

                    query.setParameter(params[i], values[i]);
                }
            }
            if (offset != null && offset >= 0) {
                query.setFirstResult(offset);
            }
            if (limit != null && limit > 0) {
                query.setMaxResults(limit);
            }
            List<T> res = query.getResultList();
            return  res;
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "find all failed", re);
        }
        return new ArrayList<T>();
    }

    public Long countByProperties(Map<String, Object> properties) {
        Long totalItem = 0L;
        try {
            final Object[] nameQuery = HibernateUtil.buildNameQuery(getPersistentClass(), properties, null, null,
                    null, true, false, null, true);
            String queryTotal = "SELECT COUNT(*) " + nameQuery[0] + nameQuery[1];
            Query query = entityManager.createQuery(queryTotal);
            if (nameQuery.length == 4) {
                String[] params = (String[]) nameQuery[2];
                Object[] values = (Object[]) nameQuery[3];
                for (int i = 0; i < params.length; i++) {

                    query.setParameter(params[i], values[i]);
                }
            }
            totalItem = (Long)query.getSingleResult();
        } catch (RuntimeException re) {
            logger.log(Level.SEVERE, "count by properties failed", re);
        }
        return totalItem;
    }

}
