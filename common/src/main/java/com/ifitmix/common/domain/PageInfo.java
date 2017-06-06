package com.ifitmix.common.domain;

import com.ifitmix.utils.DateUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 分页对象
 */
public class PageInfo<T> {

    /**
     * 默认每页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     *  如果 size 传 -1 则不分页
     */
    public static final int NO_PAGE = -1;

    /**
     * 排序 - 升序
     */
    public static final String ASC = "asc";
    /**
     * 排序 - 升序
     */
    public static final String DESC = "desc";

    /**
     * 第几页.默认从第一页开始
     */
    private int pageNo = 1;
    /**
     * 每页条数
     */
    private int size = DEFAULT_PAGE_SIZE;
    /**
     * 排序的顺序
     */
    private String orderBy;
    /**
     * 排序的属性
     */
    private String order = null;
    /**
     * 当前页从第几条开始
     */
    private int index = -1;
    /**
     * 结果集
     */
    private List<T> result = Collections.emptyList();
    /**
     * 总条数
     */
    private long total = -1;
    /**
     * 过滤条件集合
     */
    protected LinkedHashMap<String, Object> filter = new LinkedHashMap<>();

    //-- 构造函数 --//

    /**
     * 分页对象
     */
    public PageInfo() {
    }

    /**
     * 分页对象
     *
     * @param pageSize 每页条数
     */
    public PageInfo(int pageSize) {
        this.size = pageSize;
    }

    //-- 分页参数访问函数 --//

    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
     */
    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    /**
     * 返回Page对象自身的setPageNo函数,可用于连续设置。
     */
    public PageInfo<T> pageNo(final int thePageNo) {
        setPageNo(thePageNo);
        return this;
    }

    /**
     * 获得每页的记录数量, 默认为-1.
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置每页的记录数量.
     */
    public void setSize(final int size) {
        this.size = size;
    }

    /**
     * 返回Page对象自身的setPageSize函数,可用于连续设置。
     */
    public PageInfo<T> pageSize(final int thePageSize) {
        setSize(thePageSize);
        return this;
    }

    /**
     * @return 当前页记录开始位置
     */
    public int getIndex() {
        return index == -1 ? 1 : index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 获得排序字段,无默认值. 多个排序字段时用','分隔.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段,多个排序字段时用','分隔.
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 返回Page对象自身的setOrderBy函数,可用于连续设置。
     */
    public PageInfo<T> orderBy(final String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    //-- 访问查询结果函数 --//

    /**
     * 获得页内的记录列表.
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setResult(final List<T> result) {
        this.result = result;
    }

    /**
     * 获得总记录数, 默认值为-1.
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数.
     */
    public void setTotal(final long total) {
        this.total = total;
    }

    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public long getTotalPages() {
        if (total < 0) {
            return -1;
        }

        long count = total / size;
        if (total % size > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPages());
    }

    /**
     * 取得下页的页号, 序号从1开始.
     * 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 取得上页的页号, 序号从1开始.
     * 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }

    /**
     * @return 过滤条件集合
     */
    public LinkedHashMap<String, Object> getFilter() {
        return filter;
    }

    /**
     * 设置过滤条件集合
     *
     * @param filter 过滤条件集合
     */
    public void setFilter(LinkedHashMap<String, Object> filter) {
        this.filter = filter;
    }

    /**
     * 设置过滤条件集合
     *
     * @param filter 过滤条件集合
     */
    public void setF(LinkedHashMap<String, Object> filter) {
        this.filter = filter;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(final String order) {
        this.order = order;
    }

    /**
     * 将过滤条件里的对应的key转换成int型。若无法转换，则移除
     *
     * @param key 过滤条件里的key
     */
    public PageInfo convertInt(String key) {
        String value = (String) filter.remove(key);
        if (NumberUtils.isNumber(value)) {
            filter.put(key, Integer.valueOf(value));
        }
        return this;
    }

    /**
     * 将过滤条件里的对应的key转换成int型。若无法转换，则移除
     *
     * @param keys 过滤条件里的key数组
     */
    public PageInfo convertInt(String... keys) {
        if (keys.length > 0) {
            for (String key : keys) {
                convertInt(key);
            }
        }
        return this;
    }

    /**
     * 转换集合 int
     * @param keys
     */
    public PageInfo convertCollInt (String... keys) {
        if (keys.length > 0) {
            for (String key : keys) {
                convertCollInt(key);
            }
        }
        return this;
    }

    /**
     * 转换集合 int
     * @param key
     */
    public PageInfo convertCollInt (String key) {
        Object object = filter.remove(key);
        if (object != null) {
            if (object instanceof String[]) {
                String[] strings = (String[]) object;
                Integer[] is = new Integer[strings.length];
                for (int i = 0; i < strings.length; i++) {
                    is[i] = Integer.valueOf(strings[i]);
                }
                filter.put(key, is);
            }
            else {
                filter.put(key, Integer.valueOf(object.toString()));
            }
        }
        return this;
    }
    /**
     * 将过滤条件里的对应的key转换成long型。若无法转换，则移除
     *
     * @param key 过滤条件里的key
     */
    public PageInfo convertLong(String key) {
        String value = (String) filter.remove(key);
        if (NumberUtils.isNumber(value)) {
            filter.put(key, Long.valueOf(value));
        }
        return this;
    }

    public PageInfo convertDate(String... keys) {
        if (keys.length > 0) {
            for (String key : keys) {
                convertDate(key);
            }
        }
        return this;
    }


    public PageInfo convertDate2(String key, String newKey, String format) {
        String value = (String) filter.get(key);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            filter.put(newKey, sdf.parse(value));
        } catch (Exception ignore) {
        }
        return this;
    }

    /**
     * 将过滤条件里的对应的key转换成Date型。若无法转换，则移除
     *
     * @param key 过滤条件里的key
     */
    public PageInfo convertDate(String key) {
        String value = (String) filter.remove(key);
        if (value == null) {
            return this;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            filter.put(key, sdf.parse(value));
        } catch (Exception ignore) {
        }
        return this;
    }

    /**
     * 清理掉filter中的空值查询条件
     * @param keys 过滤条件里的key
     * @return
     */
    public PageInfo removeEmptys(String...keys) {
        if (keys.length == 0) {
            return this;
        }
        for (String key : keys) {
            removeEmpty(key);
        }
        return this;
    }

    /**
     * 清理掉filter中的空值查询条件
     * @param key 过滤条件里的key
     * @return
     */
    public PageInfo removeEmpty(String key) {
        if (StringUtils.isEmpty(filter.get(key))) {
            filter.remove(key);
        }
        return this;
    }

    /**
     * 根据 key Date 转换 Long
     * @param key
     * @return
     */
    public Long parseBeginTime (String key) {
        String value = (String)filter.get(key);
        return DateUtil.getDayBegin(DateUtil.parse(value, "yyyy-MM-dd")).getTime();
    }

    /**
     * 根据 key Date 转换 Long
     * @param key
     * @return
     */
    public Long parseEndTime (String key) {
        String value = (String)filter.get(key);
        return DateUtil.getDayEnd(DateUtil.parse(value, "yyyy-MM-dd")).getTime();
    }
}
