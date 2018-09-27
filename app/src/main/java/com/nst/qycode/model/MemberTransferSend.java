package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/25 下午8:03
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MemberTransferSend extends DataSend {
    public MemberTransfer MemberTransferModel = new MemberTransfer();

    public class MemberTransfer {
        public int FromGame;
        public int ToGame;
        public double TransferMoney;
    }


}
