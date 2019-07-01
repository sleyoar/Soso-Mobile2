package dao.impl;

import dao.NetService;
import entity.Common;
import entity.MobileCard;
import entity.ServicePackage;

public class NetPackage extends ServicePackage implements NetService {
    private int flow;

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public NetPackage() {
        this.flow = 1024*3;
        this.price=68.0;
    }

    public NetPackage(int flow) {
        super();
        this.flow = flow;
    }

    @Override
    public int netPlay(int flow, MobileCard card) throws Exception{
        int temp = flow;
        for(int i=0;i<flow;i++){
            if(this.flow-card.getRealFlow()>=1){
                //第一种情况：套餐剩余流量可以支持使用1M流量
                card.setRealFlow(card.getRealFlow()+1); //实际使用流量加1MB
                }else if(card.getMoney()>=0.1){
                //第二种情况:套餐流量已用完，账户余额可以支付1M流量，使用账户余额支付
                card.setRealFlow(card.getRealFlow()+1); //实际使用流量加1MB
                card.setMoney(Common.sub(card.getMoney(),0.1)); //账户余额消费0.1元（1M流量费用）
                card.setConsumAmount(card.getConsumAmount() + 0.1);
                }else{
                temp = i;
                throw new Exception("本次已使用流量"+i+"MB,您的余额不足，请充值后再使用！");
                }
             }
        return temp;
        }

    @Override
    public void showInfo() {
        System.out.println("网虫套餐：上网流量是" + flow / 1024 + "GB/月,月资费是"
                + this.price + "元/月。");
    }
}
