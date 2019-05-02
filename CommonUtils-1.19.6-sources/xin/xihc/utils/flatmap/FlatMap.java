package xin.xihc.utils.flatmap;

import xin.xihc.utils.common.CommonUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 扁平化Map
 *
 * @Author Leo.Xi
 * @Date 2019/1/18 14:29
 * @Version 1.0
 **/
public class FlatMap {

    private LinkedHashMap<String, Object> _data_new = new LinkedHashMap(16);
    private LinkedHashMap<String, FlatMapType> key2Type = new LinkedHashMap(16);

    public FlatMap(Object val) {
        if (val instanceof List) {
            parseListType(null, (List) val);
        } else {
            parseMapType(null, (Map) val);
        }
    }

    /**
     * 获取对应的类型
     *
     * @param obj
     * @return
     */
    private FlatMapType getType(Object obj) {
        if (null == obj) {
            return FlatMapType.OBJECT;
        }
        if (obj instanceof List) {
            return FlatMapType.LIST;
        } else if (obj instanceof String) {
            return FlatMapType.STRING;
        } else if (obj instanceof Integer) {
            return FlatMapType.INTEGER;
        } else if (obj instanceof Long) {
            return FlatMapType.LONG;
        } else if (obj instanceof Boolean) {
            return FlatMapType.BOOLEAN;
        } else if (obj instanceof Double) {
            return FlatMapType.DOUBLE;
        } else if (obj instanceof Map) {
            return FlatMapType.MAP;
        }
        return FlatMapType.OBJECT;
    }

    /**
     * 获取对应的类型
     *
     * @param type
     * @return
     */
    private Class toClass(FlatMapType type) {
        if (null == type) {
            return Object.class;
        }
        switch (type) {
            case OBJECT:
                return Object.class;
            case MAP:
                return LinkedHashMap.class;
            case INTEGER:
                return Integer.class;
            case LONG:
                return Long.class;
            case STRING:
                return String.class;
            case LIST:
                return List.class;
            case DOUBLE:
                return Double.class;
            case BOOLEAN:
                return Boolean.class;
            default:
                break;
        }
        return Object.class;
    }

    /**
     * 分析map数据类型
     *
     * @param prefix
     * @param map
     */
    public void parseMapType(String prefix, Map<String, Object> map) {
        if (null == map) {
            return;
        }
        for (String key : map.keySet()) {
            Object val = map.get(key);
            FlatMapType myType = getType(val);
            String putKey = CommonUtil.isNullEmpty(prefix) ? key : prefix + "." + key;
            this.key2Type.put(putKey, myType);
            this._data_new.put(putKey, val);
            if (null != val) {
                switch (myType) {
                    case OBJECT:
                    case MAP:
                        parseMapType(putKey, (Map) val);
                        break;
                    case LIST:
                        parseListType(putKey, (List) val);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 分析列表里的数据类型
     *
     * @param prefix
     * @param list
     */
    public void parseListType(String prefix, List<Object> list) {
        if (null == list) {
            return;
        }
        this.key2Type.put(CommonUtil.isNullEmpty(prefix) ? "data" : prefix, FlatMapType.LIST);
        this._data_new.put(CommonUtil.isNullEmpty(prefix) ? "data" : prefix, list);
        for (int i = 0, j = list.size(); i < j; i++) {
            String putKey = CommonUtil.isNullEmpty(prefix) ? i + "" : prefix + "." + i;
            FlatMapType myType = getType(list.get(i));
            this.key2Type.put(putKey, myType);
            this._data_new.put(putKey, list.get(i));

            switch (myType) {
                case OBJECT:
                case MAP:
                    parseMapType(putKey, (Map) list.get(i));
                    break;
                case LIST:
                    parseListType(putKey, (List) list.get(i));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取Key-Value
     *
     * @param key
     * @return
     */
    public Optional getOptional(String key) {
        Object val = this._data_new.get(key);
        return Optional.ofNullable(val);
    }

    /**
     * 取值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        Object val = this._data_new.get(key);
        return val;
    }

    @Override
    public String toString() {
        return "FlatMap{" +
                "_data_new=" + _data_new +
                ", key2Type=" + key2Type +
                '}';
    }

    public LinkedHashMap<String, Object> get_data_new() {
        return _data_new;
    }

    public LinkedHashMap<String, FlatMapType> getKey2Type() {
        return key2Type;
    }
}
