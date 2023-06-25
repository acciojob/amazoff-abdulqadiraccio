package com.driver;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {



    private HashMap<String, Order> orderMap = new HashMap<>();
    private HashMap<String, DeliveryPartner> partnerMap = new HashMap<>();
    private HashMap<String, String> orderPartnerMap = new HashMap<>();
    private HashMap<String, List<String>> partnerOrderMap = new HashMap<>();


    public void saveOrder(Order order) {

        orderMap.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);

        partnerMap.put(partnerId, deliveryPartner);
    }


    public void addOrderPartnerPair(String orderId, String partnerId) {
      if(orderMap.containsKey(orderId)&&partnerMap.containsKey(partnerId)) {
          if (partnerOrderMap.containsKey(partnerId)) {
              List<String> orderList = partnerOrderMap.get(partnerId);
              orderList.add(orderId);
              partnerOrderMap.put(partnerId, orderList);
          } else {
              List<String> listOfOrder = new ArrayList<>();
              listOfOrder.add(orderId);
              partnerOrderMap.put(partnerId, listOfOrder);
          }
      }
          int numberOfOrder= partnerOrderMap.get(partnerId).size();
           DeliveryPartner partner = partnerMap.get(partnerId);
           partner.setNumberOfOrders(numberOfOrder);
          orderPartnerMap.put(orderId,partnerId);

    }

    public Order getOrder(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartner(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public int getOrderCount(String partnerId) {
        return partnerOrderMap.get(partnerId).size();
    }

    public List getListOfOrders(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List getAllOrderList() {
        return l;
    }

    public int getCountOfUnassignedOrder() {
        int count = 0;
        for (String orderId:orderMap.keySet()){
            if(orderMap.containsKey(orderId) && !orderPartnerMap.containsKey(orderId)){
                count++;
            }
        }
        return count;
    }

    public Integer getCountOfOrderLeftAfterGivenTime(String time, String partnerId) {
        int count = 0;
        int givenTime = (Integer.parseInt(time.substring(0, 2))) * 60 + (Integer.parseInt(time.substring(3)));
        List<String> list = partnerOrderMap.get(partnerId);

        for (int i = 0; i < list.size(); i++) {
            String orderTime = orderMap.get(list.get(i)).getDeliveryTime();
            int delTime = (Integer.parseInt(orderTime.substring(0, 2))) * 60 + (Integer.parseInt(orderTime.substring(3)));
            if (givenTime < delTime) {
                count++;
            }

        }
        return count;
    }

    public String  getLastDeliveryTime(String partnerId){
        List<String> list = partnerOrderMap.get(partnerId);

         int maxTime=0;
        for (int i = 0; i < list.size(); i++) {
            String orderTime = orderMap.get(list.get(i)).getDeliveryTime();
            int delTime= (Integer.parseInt(orderTime.substring(0, 2))) * 60 + (Integer.parseInt(orderTime.substring(3)));
            if(delTime>=maxTime){
                maxTime=delTime;
            }

        }


      String s1 =   Integer.toString(maxTime/60)  ;
        if(s1.length()<2){
                         s1="0"+s1;
        }
        String s2= Integer.toString(maxTime%60);
        if(s2.length()<2){
            s2="0"+s2;
        }
           return s1+":"+s2;
    }



    public void deletePartnerById( String partnerId){
        List <String> listToDel= partnerOrderMap.get(partnerId);

        for(String orderId : listToDel){
            if (orderPartnerMap.containsKey(orderId)){
                orderPartnerMap.remove(orderId);
            }
        }
        partnerOrderMap.remove(partnerId);
        partnerMap.remove(partnerId);
    }

    public void deleteOrderById( String orderId){
     String partnerId=   orderPartnerMap.get(orderId);
      partnerOrderMap.get(partnerId).remove(orderId);
      partnerMap.get(partnerId).setNumberOfOrders(partnerOrderMap.get(partnerId).size());

       orderMap.remove(orderId);
      orderPartnerMap.remove(orderId);

        }


    }





