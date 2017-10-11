package com.yxkj.processer;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class ProcessorWatcher {
    private List<IProcessor> iProcessorList = new ArrayList<>();

    /**
     * 处理业务
     * @param msg
     */
    public void process(ChannelHandlerContext ctx, String msg) {
        iProcessorList.forEach((iProcessor -> {
            iProcessor.process(ctx,msg);
        }));
    }

    /**
     * 添加监听
     * @param iProcessor
     */
    public void addProcessor(IProcessor iProcessor) {
        iProcessorList.add(iProcessor);
    }
}
