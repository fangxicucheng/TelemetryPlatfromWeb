package com.fang.utils;

import javax.swing.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class  NetWorkInfoUtils {

  public static List<InetAddress> getMachineIpAddress(){

      List<InetAddress> result=new ArrayList<>();
      try {
          // 获取所有网络接口
          Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

          while (interfaces.hasMoreElements()) {
              NetworkInterface networkInterface = interfaces.nextElement();
              System.out.println("网络接口: " + networkInterface.getName());

              // 获取当前网络接口上的所有 IP 地址
              Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
              while (inetAddresses.hasMoreElements()) {
                  InetAddress inetAddress = inetAddresses.nextElement();
                  if(inetAddress instanceof Inet4Address&& !inetAddress.isLoopbackAddress())
                  result.add(inetAddress);
              }
          }
      } catch (Exception e) {
          e.printStackTrace();
      }


      return result;

  }

  public static List<String> getIpAddressList()  {
      List<String> ipAddressList=new ArrayList<>();

      try {
        List<String>list=  Collections.list(NetworkInterface.getNetworkInterfaces())
                  .stream()
                  .filter(ni -> {
                      try {
                          return ni.isUp();
                      } catch (SocketException e) {
                          throw new RuntimeException(e);
                      }
                  }) // 只考虑正在使用的网络接口
                  .flatMap(ni -> Collections.list(ni.getInetAddresses()).stream())
                  .map(InetAddress::getHostAddress) // 获取IP地址字符串
                  .collect(Collectors.toList());
        ipAddressList.addAll(list);
      } catch (Exception e) {
          //throw new RuntimeException(e);
          e.printStackTrace();
      }


      return ipAddressList;
  }

}
