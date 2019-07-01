package dao.impl;
import dao.CallService;
import dao.NetService;
import dao.SendService;
import entity.Common;
import entity.MobileCard;
import entity.ServicePackage;

public class SuperPackage extends ServicePackage implements
        CallService, SendService, NetService {
    private int talkTime;
    private int smsCount;
    private int flow;

    public SuperPackage() {
        this.talkTime=200;
        this.flow=1024*1;
        this.smsCount=50;
        this.price=78;
    }

    public int getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(int talkTime) {
        this.talkTime = talkTime;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    @Override
    public int call(int minCount, MobileCard card) throws Exception {
        int temp = minCount;
        for(int i=0;i<minCount;i++){
            if(this.talkTime-card.getRealTalkTime()>=1){
                //第一种情况：套餐剩余通话时长可以付1分钟通话
                card.setRealTalkTime(card.getRealTalkTime()+1); //实际通话数据加1
            }else if(card.getMoney()>=0.2){
                //第二种情况:套餐通话时长已用完，账户余额可以支付1分钟通话，使用账户余额支付
                card.setRealTalkTime(card.getRealTalkTime()+1); //实际使用通话时长1分钟
                card.setMoney(Common.sub(card.getMoney(),0.2)); //账户余额消费0.2元（1分钟额外通话）
                card.setConsumAmount(card.getConsumAmount() + 0.2);
            }else{
                temp = i; //记录实现通话分钟数
                throw new Exception("本次已通话"+i+"分钟,您的余额不足，请充值后再使用！");
            }
        }
        return temp;
    }

    @Override
    public int netPlay(int flow, MobileCard card) throws Exception {
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
    public int send(int count, MobileCard card) throws Exception {
        int temp=count;
        for (int i = 0; i < count; i++) {
            if(this.smsCount-card.getRealSMSCount()>=1){
                card.setRealSMSCount(card.getRealSMSCount()+1);
            }else if (card.getMoney()>0.1){
                card.setRealSMSCount(card.getRealSMSCount()+1);
                card.setMoney(Common.sub(card.getMoney(),0.1));
                card.setConsumAmount(card.getConsumAmount()+0.1);
            }else{
                temp=i;
                throw new Exception("本次已发送"+temp+"条，您的余额不足，请充值后再使用1");
            }
        }
        return temp;
    }

    @Override
    public void showInfo() {
        System.out.println("超人套餐：通话时长为"+this.talkTime+"分钟/月，短信条数为"+
                this.smsCount+"条/月，上网流量为"+this.flow/1024+"GB/月。");
    }
}
