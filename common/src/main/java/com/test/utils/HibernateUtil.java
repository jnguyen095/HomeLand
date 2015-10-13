package com.test.utils;

import com.test.Constants;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/13/15
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class HibernateUtil {
    enum PropertyType {
        STRING, DATE, BOOLEAN, NUMBER
    };

    private static final String alias = " A";
    private static final String aliasDot = " A.";
    private static final String like = " like ";
    public static final String eq = " = ";
    public static final String noteq = " != ";
    public static final String notIn = " not in ";
    public static final String less = " < ";
    public static final String greater = " > ";
    public static final String in = " in ";
    private static final String and = " AND ";
    private static final String or = " OR ";
    private static final String from = " from ";
    private static final String where = " where ";
    private static final String fquote = " '";
    private static final String lquote = "'";
    private static final String percentQuote = "%'";

    /**
     * The method that create Criterion array from map of names and values.
     *
     * @param propertyNameValues
     *            map of property name and values
     * @param includeNull
     *            if it's true the property with null value will be created with
     *            Restrictions.isNull. Otherwise, it will be ignored.
     * @return
     */
    public static Criterion[] createCriterion(
            Map<String, String> propertyNameValues, boolean includeNull) {
        if (propertyNameValues == null) {
            return null;
        }
        Criterion[] criterion = new Criterion[propertyNameValues.size()];
        String value;
        int i = 0;
        for (String key : propertyNameValues.keySet()) {
            value = propertyNameValues.get(key);
            if (value == null && includeNull) {
                criterion[i] = Restrictions.isNull(key);
            } else if (value != null) {
                criterion[i] = Restrictions.ilike(key, value + "%");
            }

            i++;
        }
        return criterion;
    }

    private static String getNoDotProperty(String property) {
        if (property.indexOf(Constants.ALPHABET_SEARCH_PREFIX) == 0) {
            property = property.substring(Constants.ALPHABET_SEARCH_PREFIX
                    .length());
        }
        return property.replace('.', 'X');
    }

    private static String getListValue(List values) {
        StringBuilder buf = new StringBuilder(20);
        int i = 0;
        for (Object o : values) {
            if (i > 0) {
                buf.append(",");
            }
            if (o instanceof String) {
                buf.append("'");
                buf.append(o.toString());
                buf.append("'");
            } else {
                buf.append(o.toString());
            }
            i++;
        }
        return buf.toString();
    }

    /**
     * Return correct HSQL value based on value type. Value type is first
     * checked against PropertyType. If PropertyType is null it will use
     * instanceof to find out value type value[0] is operator and value[1] is
     * value
     *
     * @param additionalTypeInfo
     * @param value
     * @return HSQL value base the on the type
     */
    private static String getNameCriteria(PropertyType additionalTypeInfo,
                                          String property, Object value[], boolean exactMatch,
                                          boolean ignoreCase) {
        StringBuilder buffer = new StringBuilder();
        if (value[1] instanceof List) {
            buffer.append(aliasDot).append(property);
            if (HibernateUtil.notIn.equals(value[0])) {
                buffer.append(notIn);
            } else {
                buffer.append(in);
            }
            buffer.append("(");
            buffer.append(getListValue((List)value[1]));
            buffer.append(")");
        }else if ((additionalTypeInfo != null && additionalTypeInfo == PropertyType.STRING)
                || value[1] instanceof String) {
            if (exactMatch) {
                if (ignoreCase) {

                    buffer.append("upper(").append(aliasDot).append(property).append(")").append(value[0])
                            .append("upper(").append(":").append(getNoDotProperty(property)).append(")");
                } else {
                    buffer.append(aliasDot).append(property).append(value[0]).append(":").append(getNoDotProperty(property));
                }
            } else {
                if (HibernateUtil.notIn.equals(value[0])) {
                    buffer.append(aliasDot).append(property).append(notIn).append("(:").append(getNoDotProperty(property))
                            .append(")");
                }else if (HibernateUtil.in.equals(value[0])) {
                    buffer.append(aliasDot).append(property).append(in).append("(:").append(getNoDotProperty(property))
                            .append(")");
                } else if (property.indexOf(Constants.ALPHABET_SEARCH_PREFIX) == 0) {
                    // this is alphabet search
                    property = property.substring(Constants.ALPHABET_SEARCH_PREFIX
                            .length());

                    buffer.append("upper(").append(aliasDot).append(property).append(") like upper(:")
                            .append(getNoDotProperty(property)).append(" ||'%')");
                } else {
                    buffer.append("upper(").append(aliasDot).append(property)
                            .append(") like upper('%'|| :")
                            .append(getNoDotProperty(property)).append(" ||'%')");
                }
            }
        }else {
            buffer.append(aliasDot).append(property).append(value[0]).append(":").append(getNoDotProperty(property));
        }

        return buffer.toString();

    }

    private static String getNameCriteria(String property, Object[] value,
                                          Map<String, PropertyType> additionalTypeInfo, boolean exactMatch,
                                          boolean ignoreCase) {
        if (additionalTypeInfo != null
                && additionalTypeInfo.get(property) != null) {
            return getNameCriteria(additionalTypeInfo.get(property), property,
                    value, exactMatch, ignoreCase);
        }

        return getNameCriteria(null, property, value, exactMatch, ignoreCase);

    }

    /**
     * Build named query based on instance of Class and map of properties and
     * values Type of values can be specified in additionalTypeInfo for nested
     * properties If type is not specified in the map it will use instanceof to
     * determine the type for the property.
     *
     * @param clazz
     * @param propertyNameValues
     * @param additionalTypeInfo
     * @return
     */
    public static Object[] buildNameQuery(Class clazz,
                                          Map<String, Object> propertyNameValues,
                                          Map<String, PropertyType> additionalTypeInfo, String orderBy,
                                          String orderDirection, boolean andSearch, boolean exactMatch,
                                          String whereClause, boolean ignoreCase) {

        Object[] value;
        Object temp;
        StringBuilder buffer = new StringBuilder(100);
        StringBuilder orderByBuffer = new StringBuilder();
        buffer.append(from).append(clazz.getName()).append(alias);
        if (propertyNameValues.size() > 0) {
            buffer.append(where);
            if (whereClause != null && whereClause.trim().length() > 0) {
                buffer.append(whereClause);
                if (andSearch) {
                    buffer.append(and);
                } else {
                    buffer.append(or);
                }
            }
            int i = 0;
            StringBuilder paramNames = new StringBuilder();
            ArrayList<Object> values = new ArrayList<Object>();
            Map<String, Object> propertyNameValuesEx = new HashMap<String, Object>();

            for (String property : propertyNameValues.keySet()) {
                temp = propertyNameValues.get(property);
                if (temp instanceof ArrayList) {
                    propertyNameValuesEx.put(property, temp);
                    continue;
                } else if (temp instanceof Object[]) {
                    value = (Object[]) temp;
                } else {
                    value = new Object[2];
                    value[0] = eq;
                    value[1] = temp;
                }
                if (i > 0) {
                    if (andSearch) {
                        buffer.append(and);
                    } else {
                        buffer.append(or);
                    }
                }
                String str = getNameCriteria(property, value,
                        additionalTypeInfo, exactMatch, ignoreCase);
                buffer.append(str);
                if (str.indexOf(":") > 0) {
                    if (i > 0) {
                        paramNames.append(",");
                    }
                    paramNames.append(getNoDotProperty(property));
                    values.add(value[1]);
                    i++;
                }
            }
            ArrayList propertyNameValueList;
            for (String property : propertyNameValuesEx.keySet()) {
                temp = propertyNameValuesEx.get(property);

                if (temp instanceof ArrayList) {
                    propertyNameValueList = (ArrayList) temp;
                    for (int j = 0; j < propertyNameValueList.size(); j++) {
                        temp = propertyNameValueList.get(j);
                        if (temp instanceof Object[]) {
                            value = (Object[]) temp;
                            if (i > 0) {
                                buffer.append(and);
                            }
                            String str = getNameCriteria(property, value, j);
                            buffer.append(str);
                            if (str.indexOf(":") > 0) {
                                if (i > 0) {
                                    paramNames.append(",");
                                }
                                paramNames
                                        .append(getNoDotProperty(property + j));
                                values.add(value[1]);
                                i++;
                            }
                        }
                    }
                }

            }
            if (orderBy != null && !"".equals(orderBy)) {
                orderByBuffer.append(" order by A.");
                orderByBuffer.append(orderBy);

                if (orderDirection != null && !"".equals(orderDirection)) {
                    orderByBuffer.append(" " + (orderDirection.equals(Constants.SORT_ASC) ? "asc" : "desc"));
                }
            }

            if (values.size() > 0) {
                return new Object[] { buffer.toString(), orderByBuffer.toString(),
                        paramNames.toString().split("[,]"), values.toArray() };
            } else {
                return new Object[] { buffer.toString(), orderByBuffer.toString() };
            }
        }else if (whereClause != null && whereClause.trim().length() > 0) {
            buffer.append(where);
            buffer.append(whereClause);
        }

        if (orderBy != null && !"".equals(orderBy)) {
            orderByBuffer.append(" order by A.");
            orderByBuffer.append(orderBy);

            if (orderDirection != null && !"".equals(orderDirection)) {
                orderByBuffer.append((orderDirection.equals(Constants.SORT_ASC) ? " asc" : " desc"));
            }
        }

        return new Object[] { buffer.toString(), orderByBuffer.toString() };
    }

    private static String getNameCriteria(String property, Object value[],
                                          int index) {
        return aliasDot + property + value[0] + ":"
                + (getNoDotProperty(property) + index);
    }

    public static void populateProperty(String[] properties,
                                        Object persistentBean) throws Exception {
        Object o = null;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].indexOf(PropertyUtils.NESTED_DELIM) > 0) {
                String[] p = (properties[i]).split("[" + PropertyUtils.NESTED_DELIM + "]");
                Object temp = persistentBean;
                // try to populate not null property
                for (int j = 0; j < p.length; j++) {
                    o = PropertyUtils.getProperty(temp, p[j]);
                    if (o == null) {
                        break;
                    }
                    temp = o;
                }
            } else {
                o = PropertyUtils.getProperty(persistentBean, properties[i]);
            }

            if (o != null && (o instanceof Collection || o instanceof List)) {
                Collection c = (Collection) o;
                //for (Object temp : c) {
                // do nothing, just populate lazy
                //}
            }
        }
    }

    public static Object[] buildNameQuery(Class clazz,
                                          Map<String, Object> propertyNameValues,
                                          Map<String, PropertyType> additionalTypeInfo, String orderBy,
                                          String orderDirection, boolean andSearch, boolean exactMatch,
                                          String whereClause, boolean ignoreCase, String selectFromClause) {

        Object[] value;
        Object temp;
        StringBuilder buffer = new StringBuilder(100);
        if(StringUtils.isNotBlank(selectFromClause)) {
            buffer.append(selectFromClause);
            buffer.append(from).append(clazz.getName()).append(alias);
        } else {
            buffer.append(selectFromClause);
        }
        if (propertyNameValues.size() > 0) {
            buffer.append(where);
            if (whereClause != null && whereClause.trim().length() > 0) {
                buffer.append(whereClause);
                if (andSearch) {
                    buffer.append(and);
                } else {
                    buffer.append(or);
                }
            }
            int i = 0;
            StringBuilder paramNames = new StringBuilder();
            ArrayList<Object> values = new ArrayList<Object>();
            Map<String, Object> propertyNameValuesEx = new HashMap<String, Object>();

            for (String property : propertyNameValues.keySet()) {
                temp = propertyNameValues.get(property);
                if (temp instanceof ArrayList) {
                    propertyNameValuesEx.put(property, temp);
                    continue;
                } else if (temp instanceof Object[]) {
                    value = (Object[]) temp;
                } else {
                    value = new Object[2];
                    value[0] = eq;
                    value[1] = temp;
                }
                if (i > 0) {
                    if (andSearch) {
                        buffer.append(and);
                    } else {
                        buffer.append(or);
                    }
                }
                String str = getNameCriteria(property, value,
                        additionalTypeInfo, exactMatch, ignoreCase);
                buffer.append(str);
                if (str.indexOf(":") > 0) {
                    if (i > 0) {
                        paramNames.append(",");
                    }
                    paramNames.append(getNoDotProperty(property));
                    values.add(value[1]);
                    i++;
                }
            }
            ArrayList propertyNameValueList;
            for (String property : propertyNameValuesEx.keySet()) {
                temp = propertyNameValuesEx.get(property);

                if (temp instanceof ArrayList) {
                    propertyNameValueList = (ArrayList) temp;
                    for (int j = 0; j < propertyNameValueList.size(); j++) {
                        temp = propertyNameValueList.get(j);
                        if (temp instanceof Object[]) {
                            value = (Object[]) temp;
                            if (i > 0) {
                                buffer.append(and);
                            }
                            String str = getNameCriteria(property, value, j);
                            buffer.append(str);
                            if (str.indexOf(":") > 0) {
                                if (i > 0) {
                                    paramNames.append(",");
                                }
                                paramNames
                                        .append(getNoDotProperty(property + j));
                                values.add(value[1]);
                                i++;
                            }
                        }
                    }
                }

            }
            // //end

            if (orderBy != null && !"".equals(orderBy)) {
                buffer.append(" order by A.").append(orderBy);

                if (orderDirection != null && !"".equals(orderDirection)) {
                    buffer.append((orderDirection.equals(Constants.SORT_ASC) ? " asc" : " desc"));
                }
            }

            if (values.size() > 0) {
                return new Object[] { buffer.toString(),
                        paramNames.toString().split("[,]"), values.toArray() };
            } else {
                return new Object[] { buffer.toString() };
            }
        }else if (whereClause != null && whereClause.trim().length() > 0) {
            buffer.append(where).append(whereClause);
        }

        if (orderBy != null && !"".equals(orderBy)) {
            buffer.append(" order by A.").append(orderBy);

            if (orderDirection != null && !"".equals(orderDirection)) {
                buffer.append((orderDirection.equals(Constants.SORT_ASC) ? " asc" : " desc"));
            }

        }

        return new Object[] { buffer.toString() };
    }
}
