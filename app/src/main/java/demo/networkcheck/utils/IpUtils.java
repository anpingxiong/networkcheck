package demo.networkcheck.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anping on 15-6-10.
 */
public class IpUtils {
    static ArrayList<String> ips = new ArrayList<>();
    static ArrayList<Integer> ports = new ArrayList<>();

    public static List<String> getIps() {
        if (ips.size() == 0) {
            ips.add("www.baidu.com");
            ips.add("www.sina.com.cn");
            ips.add("www.360.com");
            ips.add("www.qq.com");
        }
        return ips;
    }

    public static List<String> getIps1() {
        List<String> ip1 = new ArrayList<String>();

        ip1.add("www.baidu.com");
        ip1.add("www.sina.com.cn");

        return ip1;
    }

    public static List<String> getIps2() {
        List<String> ip1 = new ArrayList<String>();

        ip1.add("www.360.com");
        ip1.add("www.qq.com");

        return ip1;
    }

    public static List<Integer> getPort() {
        if (ports.size() == 0) {
            ports.add(80);
        }
        return ports;
    }


}
