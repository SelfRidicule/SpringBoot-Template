package com.zhongzhou.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrTokenizer;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @ClassName IpUtil
 * @Description 获取真实IP
 * @Date 2020/3/22 22:07
 * @Author
 */
@Slf4j
public class IpUtil implements Serializable {
    private static final long serialVersionUID = 6197427416633920881L;
    public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    public static final Pattern PATTERN = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

    /**
     * 无代理，获取IP地址
     *
     * @param request HttpServletRequest request
     * @return String IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String longToIpV4(long longIp) {
        int octet3 = (int) ((longIp >> 24) % 256);
        int octet2 = (int) ((longIp >> 16) % 256);
        int octet1 = (int) ((longIp >> 8) % 256);
        int octet0 = (int) ((longIp) % 256);
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
    }

    public static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    public static boolean isIPv4Private(String ip) {
        long longIp = ipV4ToLong(ip);
        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
                || (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
                || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
    }

    public static boolean isIPv4Valid(String ip) {
        return PATTERN.matcher(ip).matches();
    }

    /**
     * 获取IP地址
     *
     * @param request HttpServletRequest
     * @return String IP地址
     */
    public static String getIpFromRequest(HttpServletRequest request) {
        String ip;
        boolean found = false;
        if ((ip = request.getHeader("x-forwarded-for")) != null) {
            StrTokenizer tokenizer = new StrTokenizer(ip, ",");
            while (tokenizer.hasNext()) {
                ip = tokenizer.nextToken().trim();
                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 验证ip是否在允许范围内
     *
     * @param ipAddr      被验证的IP(可能为Nginx转发的IP)
     * @param startIpAddr 开始IP
     * @param endIpAddr   结束IP
     * @return true：允许，false：未允许
     */
    public static boolean rangValidate(String ipAddr, String startIpAddr, String endIpAddr) {
        boolean ipFlag;
        if (ipAddr.indexOf(",") > 0) {
            //如果客户端IP是经过Nginx转发，则获取其真实IP
            String[] ipSplit = ipAddr.split(",");
            String realIpAddr = ipSplit[0].trim();
            ipFlag = realIpRangValidate(realIpAddr, startIpAddr, endIpAddr);
        } else {
            //如果客户端IP是未经过Nginx转发
            ipFlag = realIpRangValidate(ipAddr, startIpAddr, endIpAddr);
        }
        return ipFlag;
    }

    /**
     * 验证ip是否在允许范围内
     *
     * @param ipAddr      真实IP
     * @param startIpAddr 开始IP
     * @param endIpAddr   结束IP
     * @return true：允许，false：未允许
     */
    public static boolean realIpRangValidate(String ipAddr, String startIpAddr, String endIpAddr) {
        boolean ipFlag = false;
        log.info("real ip address is:{}", ipAddr);
        log.info("start ip address is:{}", startIpAddr);
        log.info("end ip address is:{}", endIpAddr);
        if ("0.0.0.0".equals(startIpAddr) && "0.0.0.0".equals(endIpAddr)) {
            ipFlag = true;
        } else {
            String[] ipStartArr = startIpAddr.split("\\.");
            String[] ipEndArr = endIpAddr.split("\\.");
            String[] ipAddrArr = ipAddr.split("\\.");
            if (Integer.parseInt(ipAddrArr[0]) >= Integer.parseInt(ipStartArr[0]) && Integer.parseInt(ipAddrArr[0]) <= Integer.parseInt(ipEndArr[0])) {
                if (Integer.parseInt(ipAddrArr[1]) >= Integer.parseInt(ipStartArr[1]) && Integer.parseInt(ipAddrArr[1]) <= Integer.parseInt(ipEndArr[1])) {
                    if (Integer.parseInt(ipAddrArr[2]) >= Integer.parseInt(ipStartArr[2]) && Integer.parseInt(ipAddrArr[2]) <= Integer.parseInt(ipEndArr[2])) {
                        ipFlag = true;
                        log.info("IP合法并且在允许范围内");
                    } else {
                        log.info("IP不在允许范围内");
                    }
                } else {
                    log.info("IP不在允许范围内");
                }
            } else {
                log.info("IP不在允许范围内");
            }
        }
        return ipFlag;
    }
}
