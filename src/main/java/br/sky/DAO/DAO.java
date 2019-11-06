package br.sky.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class DAO<T, I extends Serializable> {

    private Class<T> persistedClass;

    protected DAO() {
    }

    protected DAO(Class<T> persistedClass) {
        this();
        this.persistedClass = persistedClass;
    }

    public T add(T entity, EntityManager entityManager) {
        try {
            EntityTransaction t = entityManager.getTransaction();
            t.begin();

            entityManager.persist(entity);
            entityManager.flush();
            t.commit();
            return entity;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }

    public ArrayList<T> addAll(ArrayList<T> entities, EntityManager entityManager) {
        try {
            EntityTransaction t = entityManager.getTransaction();
            t.begin();
            
            for (T entity : entities) {
                entityManager.persist(entity);
            }
            
            entityManager.flush();
            t.commit();
            return entities;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }

    public ArrayList<T> updateAll(ArrayList<T> entities, EntityManager entityManager) {
        try {
            EntityTransaction t = entityManager.getTransaction();
            t.begin();
            for (T entity : entities) {
                entityManager.merge(entity);
            }
            entityManager.flush();
            t.commit();
            return entities;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public ObservableList<T> updateAll(ObservableList<T> entities, EntityManager entityManager) {
        try {
            EntityTransaction t = entityManager.getTransaction();
            t.begin();
            for (T entity : entities) {
                entityManager.merge(entity);
            }
            entityManager.flush();
            t.commit();
            return entities;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public T update(T entity, EntityManager entityManager) {
        try {
            EntityTransaction t = entityManager.getTransaction();
            t.begin();
            entityManager.merge(entity);
            entityManager.flush();
            t.commit();
            return entity;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public void delete(T entity, EntityManager entityManager) {
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            entityManager.remove(entity);
            entityManager.flush();
            tx.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public void delete(I id, EntityManager entityManager) {
        try {
            T entity = find(id, entityManager);
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            T mergedEntity = entityManager.merge(entity);
            entityManager.remove(mergedEntity);
            entityManager.flush();
            tx.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public ArrayList<T> getList(EntityManager entityManager) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(persistedClass);
            query.from(persistedClass);
            
            return new ArrayList<>(entityManager.createQuery(query).getResultList());
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public T find(I id, EntityManager entityManager) {
        try {
            return entityManager.find(persistedClass, id);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

}
