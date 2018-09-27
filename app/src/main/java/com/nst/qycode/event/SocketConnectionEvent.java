package com.nst.qycode.event;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/10 下午5:05
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SocketConnectionEvent {

    public int state;

    public SocketConnectionEvent(int state) {
        this.state = state;
    }
}
