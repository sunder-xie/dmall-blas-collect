package com.dmall.blas.collect.service;


import com.dmall.blas.collect.domain.EventDataQuery;
import com.dmall.tool.basic.Page;

import java.util.List;
import java.util.Map;

/**
 * 处理ES中索引为dmall_dm_userprofile，类型为evt_data的数据
 *
 * @Author qiang.shao
 * @Email qiang.shao@dmall.com
 * @Date 2017/3/30
 */
public interface CollectDataService {
    /**
     * 异步插入ES库
     *
     * @param eventParsedData
     */
    void asynSaveParsedData(Map<String, Object> eventParsedData);

    /**
     * 异步批量保存解析后的事件数据
     *
     * @param eventParsedData
     */
    void asynBatchSaveParsedData(List<Map<String, Object>> eventParsedData);

    /**
     * 分页查询所被解析后的事件数据
     *
     * @param condition 查询条件
     * @param page      分页信息，包括当前页号和页大小
     * @return 分页返回解析后的采集数据
     */
//    Page<Map<String, Object>> selectParsedDataPage(EventDataQuery condition, Page<Map<String, Object>> page);


    /**
     * 根据指定查询条件，对event code做统计
     *
     * @param condition 指定查询条件
     * @return 返回Map对象，key为事件类型，value为事件的统计数量
     */
//    List<Map<String, Object>> eventsCount(EventDataQuery condition);

}
