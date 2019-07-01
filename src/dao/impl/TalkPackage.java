package dao.impl;

import dao.CallService;
import dao.SendService;
import entity.Common;
import entity.MobileCard;
import entity.ServicePackage;

public class TalkPackage extends ServicePackage implements
        CallService, SendService {
    private int talkTime;
    private int smsCount;

    public TalkPackage() {
        this.talkTime=500;
        this.smsCount=30;
        this.price=58;
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

    @Override
    public int call(int minCount, MobileCard card) throws Exception {
        int temp = minCount;
        for(int i=0;i<minCount;i++){
            if(this.talkTime-card.getRealTalkTime()>=1){
                //第一种情况：套餐剩余通话时长可以付1分钟通话
                card.setRealTalkTime(card.getRealTalkTime()+1); //实际通话时间+1
            }else if(card.getMoney()>=0.2){
                //第二种情况:套餐通话时长已用完，账户余额可以支付1分钟通话，使用账户余额支付
                card.setRealTalkTime(card.getRealTalkTime()+1); //实际使用通话时长1分钟
                card.setMoney(Common.sub(card.getMoney(),0.2)); //账户余额消费0.2元（1M流量费用）
                card.setConsumAmount(card.getConsumAmount() + 0.2);
            }else{
                temp = i; //记录实现通话分钟数
                throw new Exception("本次已通话"+i+"分钟,您的余额不足，请充值后再使用！");
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
                throw new Exception("本次已发送"+temp+"条，您的余额不足，请充值后再使用");
            }
        }
        return temp;
    }

    @Override
    public void showInfo() {
        System.out.println("话唠套餐：通话时长为" + this.talkTime + "分钟/月，短信条数为"
                + this.smsCount + "条/月，资费为" + this.price + "元/月。");
    }
}
