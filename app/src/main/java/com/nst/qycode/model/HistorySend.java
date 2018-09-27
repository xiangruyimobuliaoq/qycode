package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/9 下午6:17
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HistorySend extends DataSend {

    public HistoryModel HistoryModel = new HistoryModel();

    public class HistoryModel {
        public String BeginDate;
        public String EndDate;
    }
}
