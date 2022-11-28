package com.csc.common.utils.spring;

import com.csc.common.exception.BusinessException;
import com.csc.common.enums.ApplicationStatus;
import com.csc.common.exception.PrintExceptionInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: HttpServlet
 * @Author: csc
 * @create: 2022/11/24
 * @Version: 1.0
 */
public class RequestUtil {
    /**
     * unknown
     */
    private static final String UNKNOWN = "unknown";
    /**
     * 本机IP
     */
    private static final String LOCAL_IP = "127.0.0.1";
    /**
     * 服务器端IP
     */
    private static String SERVER_IP = null;

    /**
     * 获取客户端IP
     *
     * @return
     */
    public static String getClientIp() {
        if (isServletContext()) {
            return getClientIp(RequestUtil.getRequest());
        }
        return LOCAL_IP;
    }

    /**
     * @Description 获取客户单IP地址
     * @Author: csc
     * @create: 2022/11/24
     * @Version 1.0
     */
    public static String getClientIp(HttpServletRequest request) {
        try {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (StringUtils.equalsIgnoreCase("0:0:0:0:0:0:0:1", ip)) {
                ip = LOCAL_IP;
            }
            return ip;
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * @Description 判断请求IP是否是内网IP
     * @Author: csc
     * @create: 2022/11/24
     * @Version 1.0
     */
    @Deprecated
    public static boolean isInnerIp(String ip) {
        String reg = "((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
                + "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
                + "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        return matcher.find();
    }

    /**
     * @param ip
     * @return
     */
    public static boolean noInternet(String ip) {
        return !isInternet(ip);
    }

    /**
     * 判定是否是内网地址
     *
     * @param ip
     * @return
     */
    public static boolean isInternet(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        if (StringUtils.equals("0:0:0:0:0:0:0:1", ip)) {
            return true;
        }
        Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
        Matcher match = reg.matcher(ip);
        return match.find();
    }

    /**
     * @Description 获取服务器端的IP
     * @Author: csc
     * @create: 2022/11/24
     * @Version 1.0
     */
    public static String getServerIp() {
        if (StringUtils.isNotEmpty(SERVER_IP)) {
            return SERVER_IP;
        }
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                String name = netInterface.getName();
                if (!StringUtils.contains(name, "docker") && !StringUtils.contains(name, "lo")) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ip = addresses.nextElement();
                        //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                        if (ip != null
                                && ip instanceof Inet4Address
                                && !ip.isLoopbackAddress()
                                && ip.getHostAddress().indexOf(":") == -1) {
                            SERVER_IP = ip.getHostAddress();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            SERVER_IP = LOCAL_IP;
        }
        return SERVER_IP;
    }

    public static void setServerIp(String serverIp) {
        SERVER_IP = serverIp;
    }

    /**
     * 是否存在servlet上下文
     *
     * @return
     */
    public static boolean isServletContext() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    /**
     * 获取用户当前请求的HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            return attributes.getRequest();
        } catch (Exception ex) {
            throw new BusinessException(ApplicationStatus.ILLEGAL_ACCESS.getCode(), PrintExceptionInfo.printErrorInfo(ex));
        }
    }

    /**
     * 获取当前请求的HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            return attributes.getResponse();
        } catch (Exception ex) {
            throw new BusinessException(ApplicationStatus.ILLEGAL_ACCESS.getCode(), PrintExceptionInfo.printErrorInfo(ex));
        }
    }

    /**
     * 开启请求时间记录
     */
    public static void startRequest() {
        if (!isServletContext()) {
            return;
        }
        //设置业务请求开始时间
        getRequest().setAttribute("startTime", System.currentTimeMillis());
    }

    /**
     * 获取请求开始到当前耗时
     *
     * @return
     */
    public static long getSpentTime() {
        if (!isServletContext()) {
            return 0;
        }
        if (getRequest().getAttribute("startTime") == null) {
            return 0;
        }
        return System.currentTimeMillis() - Long.parseLong(getRequest().getAttribute("startTime").toString());
    }
}
