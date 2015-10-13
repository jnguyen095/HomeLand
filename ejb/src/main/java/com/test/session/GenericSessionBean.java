package com.test.session;

import javax.ejb.DuplicateKeyException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/13/15
 * Time: 9:52 AM
 * To change this template use File | Settings | File Templates.
 */
public interface GenericSessionBean<T, ID extends Serializable> {

    public T save(T entity) throws DuplicateKeyException;
    public void delete(ID id) throws RemoveException;
    public void delete(T entity) throws RemoveException;
    public Integer delete(ID[] ids) throws RemoveException;
    public T update(T entity) throws DuplicateKeyException;
    public T findById(ID id) throws ObjectNotFoundException;
    public T findEqualUnique(String property, Object value) throws ObjectNotFoundException;
    public T findEqualUniqueCaseSensitive(String property, Object value) throws ObjectNotFoundException;
    public List<T> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);
    public List<T> findAll(int... rowStartIdxAndCount);
    public Object[] searchByProperties(Map<String, Object> properties,
                                       String sortExpression, String sortDirection, Integer offset,
                                       Integer limit, String whereClause);
    public Object[] searchByProperties(Map<String, Object> properties,
                                       String sortExpression, String sortDirection, Integer offset,
                                       Integer limit);
    public Object findByProperties(Map<String, Object> properties,
                                   String sortExpression, String sortDirection, Integer offset,
                                   Integer limit);
    public Long countByProperties(Map<String, Object> properties);
    public List<T> findProperty(String property, Object value);
    void deleteAll(List<T> entities);
    List<T> findProperties(Map<String, Object> properties);
}
