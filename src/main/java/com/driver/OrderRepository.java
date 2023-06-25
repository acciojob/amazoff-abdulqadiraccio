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
          orderPartnerMap.put(orderId, partnerId);

          List<String> listOfOrder = new ArrayList<>();

          if (partnerOrderMap.containsKey(partnerId)) {
              listOfOrder = partnerOrderMap.get(partnerId);
                            }

              listOfOrder.add(orderId);
              partnerOrderMap.put(partnerId, listOfOrder);

          DeliveryPartner partner = partnerMap.get(partnerId);
          partner.setNumberOfOrders(listOfOrder.size());
      }

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

    public List<String> getListOfOrders(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllOrderList() {
        List<String> orderlist=new ArrayList<>();
        for(String X: orderMap.keySet()){
            orderlist.add(X);
        }
        return orderlist;
    }

    public int getCountOfUnassignedOrder() {

        return orderMap.size()-orderPartnerMap.size();
    }

    public Integer getCountOfOrderLeftAfterGivenTime(String time, String partnerId) {
        int count = 0;
        int givenTime = (Integer.parseInt(time.substring(0, 2))) * 60 + (Integer.parseInt(time.substring(3)));
        List<String> list = partnerOrderMap.get(partnerId);

        for (int i = 0; i < list.size(); i++) {
            int deliveryTime = orderMap.get(list.get(i)).getDeliveryTime();

            if (givenTime < deliveryTime) {
                count++;
            }

        }
        return count;
    }

    public String  getLastDeliveryTime(String partnerId){
        List<String> list = partnerOrderMap.get(partnerId);

         int maxTime=0;
        for (int i = 0; i < list.size(); i++) {
            int delTime = orderMap.get(list.get(i)).getDeliveryTime();
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





