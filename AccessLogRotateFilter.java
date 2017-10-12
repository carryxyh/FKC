package com.twodfire.log.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 记录Service的Access Log。
 * <p>
 * 使用的Logger key是<code><b>dubbo.accesslog</b></code>。
 * 如果想要配置Access Log只出现在指定的Appender中，可以在Log4j中注意配置上additivity。配置示例:
 * <code>
 * <pre>
 * &lt;logger name="<b>dubbo.accesslog</b>" <font color="red">additivity="false"</font>&gt;
 *    &lt;level value="info" /&gt;
 *    &lt;appender-ref ref="foo" /&gt;
 * &lt;/logger&gt;
 * </pre></code>
 */
@Activate(group = Constants.PROVIDER, value = Constants.ACCESS_LOG_KEY)
public class AccessLogRotateFilter implements Filter {
    private static final Logger logger            = LoggerFactory.getLogger(AccessLogRotateFilter.class);
    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(inv);
        long elapsed = System.currentTimeMillis() - start;
        if (ConfigUtils.isNotEmpty(invoker.getUrl().getParameter(Constants.ACCESS_LOG_KEY))) {
            RpcContext context = RpcContext.getContext();
            String serviceName = invoker.getInterface().getName();
            String version = invoker.getUrl().getParameter(Constants.VERSION_KEY);
            String group = invoker.getUrl().getParameter(Constants.GROUP_KEY);
            StringBuilder sn = new StringBuilder();
            sn.append(context.getRemoteHost()).append(":").append(context.getRemotePort())
                    .append(" -> ").append(context.getLocalHost()).append(":").append(context.getLocalPort())
                    .append(" - ");
            if (null != group && group.length() > 0) {
                sn.append(group).append("/");
            }
            sn.append(serviceName);
            if (null != version && version.length() > 0) {
                sn.append(":").append(version);
            }
            sn.append(" ");
            sn.append(inv.getMethodName());
            sn.append("(");
            Class<?>[] types = inv.getParameterTypes();
            if (types != null && types.length > 0) {
                boolean first = true;
                for (Class<?> type : types) {
                    if (first) {
                        first = false;
                    } else {
                        sn.append(",");
                    }
                    sn.append(type.getName());
                }
            }
            sn.append(") ");
            Object[] args = inv.getArguments();
            if (args != null && args.length > 0) {
                try {
                    sn.append(JSON.json(args));
                } catch (IOException e) {
                }
            }
            sn.append(" - ").append(elapsed);
            logger.info(sn.toString());
        }
        return result;
    }

}