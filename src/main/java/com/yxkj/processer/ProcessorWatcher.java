package com.yxkj.processer;

import com.yxkj.beans.NotifyMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class ProcessorWatcher {
    private List<IProcessor> iProcessorList = new ArrayList<>();

    /**
     * 处理业务
     *
     * @param msg
     */
    public void process(ChannelHandlerContext ctx, NotifyMessage msg) {
        iProcessorList.forEach((iProcessor -> {
            if (iProcessor.validateProcessor(msg))
                iProcessor.process(ctx, msg);
        }));
    }

    /**
     * 添加监听
     *
     * @param iProcessor
     */
    public void addProcessor(IProcessor iProcessor) {
        iProcessorList.add(iProcessor);
    }
}
