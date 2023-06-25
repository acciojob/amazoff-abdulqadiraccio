package com.driver;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    public Map<String,Order>ordersDb=new HashMap<>();
    public Map<String,DeliveryPartner>deliveryPartnerDb=new HashMap<>();
    public Map<String,String>orderPartnerDb=new HashMap<>();

    public Map<String, List<String>>partnerDb=new HashMap<>();


    public void addOrder(Order order){
        ordersDb.put(order.getId(),order);
    }
    public void addPartner(String partnerId){
        deliveryPartnerDb.put(partnerId,new DeliveryPartner(partnerId));
    }
    public void addOrderPartnerPair(String orderId, String partnerId){
        if(ordersDb.containsKey(orderId) && deliveryPartnerDb.containsKey(partnerId)){
            orderPartnerDb.put(orderId,partnerId);
            List<String>currOrder=new ArrayList<>();
            if(partnerDb.containsKey(partnerId)){
                currOrder=partnerDb.get(partnerId);
            }
            currOrder.add(orderId);
            partnerDb.put(partnerId,currOrder);
            DeliveryPartner deliveryPartner=deliveryPartnerDb.get(partnerId);
            deliveryPartner.setNumberOfOrders(currOrder.size());

        }
    }
    public Order getOrderById(String orderId){
        return ordersDb.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerDb.get(partnerId);
    }
    public int getOrderCountByPartnerId(String partnerId){
        int count=0;
        for (String orderId:orderPartnerDb.keySet()) {
            if(orderPartnerDb.get(orderId).equals(partnerId)){
                count++;
            }
        }
        return count;
        // return partnerDb.get(partnerId).size();
    }
    public List<String> getOrdersByPartnerId(String partnerId){
//        List<String>order=new ArrayList<>();
//        for(String orderId:orderPartnerDb.keySet()){
//            if(orderPartnerDb.get(orderId).equals(partnerId)){
//                order.add(orderPartnerDb.get(orderId));
//            }
//        }
        return partnerDb.get(partnerId);
    }
    public List<String> getAllOrders() {
        List<String>orders=new ArrayList<>();
        for(String s:ordersDb.keySet()){
            orders.add(s);
        }

        return orders;
    }
    public int getCountOfUnassignedOrders(){
        int count=0;
        for (String orderId:ordersDb.keySet()){
            if(ordersDb.containsKey(orderId) && !orderPartnerDb.containsKey(orderId)){
                count++;
            }
        }
        return count;
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId){
        int count=0;
        List<String>orders=partnerDb.get(partnerId);
        for(String orderId:orders){
            int deliveryTime=ordersDb.get(orderId).getDeliveryTime();
            if(deliveryTime>time){
                count++;
            }
        }
        return count;
    }
    public int getLastDeliveryTimeByPartnerId(String partnerId){
        int lastDeliveryTime=0;
        List<String>orders=partnerDb.get(partnerId);
        for(String orderId:orders){
            if(orderPartnerDb.get(orderId).equals(partnerId)){
                int currTime=ordersDb.get(orderId).getDeliveryTime();
                lastDeliveryTime=Math.max(lastDeliveryTime,currTime);
            }
        }
        return lastDeliveryTime;
    }
    public void deletePartnerById(String partnerId){
        deliveryPartnerDb.remove(partnerId);
        List<String>list=partnerDb.get(partnerId);
        partnerDb.remove(partnerId);
        for(String orderId:list){
            orderPartnerDb.remove(orderId);
        }
    }
    public void deleteOrderById(String orderId){
        ordersDb.remove(orderId);
        String partnerId=orderPartnerDb.get(orderId);
        orderPartnerDb.remove(orderId);

        partnerDb.get(partnerId).remove(orderId);
        deliveryPartnerDb.get(partnerId).setNumberOfOrders(partnerDb.get(partnerId).size());
    }
}