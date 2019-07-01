package util;
import dao.CallService;
import dao.NetService;
import dao.SendService;
import dao.impl.NetPackage;
import dao.impl.SuperPackage;
import dao.impl.TalkPackage;
import entity.*;

import java.io.*;
import java.util.*;

public class CardUtil {

    public static Map<String, MobileCard> cards=new HashMap<String, MobileCard>();
    public static Map<String, List<ConsumInfo>> consumInfos=new HashMap<String, List<ConsumInfo>>();
    List<Scene> scenes=new ArrayList<>();

    public void initScene(){
        MobileCard card1=new MobileCard("阿球","1234","13912345679",new SuperPackage(),78,72);
        MobileCard card2=new MobileCard("阿鑫","1235","13912345678",new TalkPackage(),58,42);
        MobileCard card3=new MobileCard("阿飞","1236","13987654321",new NetPackage(),68,32);
        card2.setConsumAmount(98);
        card2.setRealTalkTime(500);
        card2.setRealSMSCount(100);
        cards.put("13912345679",card1);
        cards.put("13912345678",card2);
        cards.put("13987654321",card3);
        scenes.add(new Scene("通话",90,"给女朋友打电话，90分钟"));
        scenes.add(new Scene("短信",50,"与神秘小姐姐短信互动 发送短线50条"));
        scenes.add(new Scene("上网",2*1024,"看健康的小视频 使用流量2G"));
        System.out.println(cards);
        System.out.println(scenes);
        System.out.println(consumInfos);
    }

    /**
     * 登录验证。。
     * @param number
     * @param passWord
     * @return 布尔类型
     */
    public boolean isExistCard(String number,String passWord){
        Set<String> numkey=cards.keySet();
        Iterator<String> it=numkey.iterator();
        while (it.hasNext()){
            String serchnum=it.next();
            if(serchnum.equals(number)&&(cards.get(serchnum)).getPassWord().equals(passWord)){
                return true;
            }
        }
        return false;
    }
    public boolean isExitCard(String number){
        Set<String> numkey=cards.keySet();
        for(String num:numkey){
            if(num.equals(number)){
                return true;
            }
        }
        return false;
    }
    public String createNumber(){
        Random random=new Random();
        boolean isExist=false;// 记录现有用户中是否存在此卡号用户 是：true 否：false
        String number=null;
        int temp=0;
        do{
            isExist=false;
            do{
                temp=random.nextInt(100000000);
            }while (temp<10000000);
            number="139"+temp;
            Set<String> cardNumbers=cards.keySet();
            for(String cardNum:cardNumbers){
                if(number.equals(cardNum)){
                    isExist=true;break;
                }
            }
        }while (isExist);
        return number;
    }

    public String[] getNewNumber(int count){
        String[] numbers=new String[count];
        for(int i=0;i<count;i++){
            numbers[i]=createNumber();
        }
        return numbers;
    }
    public void addCard(MobileCard card){
        cards.put(card.getCardNumber(),card);
        System.out.println("注册成功！");
        card.showMeg();
    }
    public void delCard(String number){
        Set<String> numberKeys=cards.keySet();
        if(numberKeys.contains(number)){
            cards.remove(number);
            System.out.println("卡号："+number+"办理退网成功");
        }else {
            System.out.println("没有卡号，销卡失败！");
        }
    }

    /**
     * 查询套餐余量
     * @param number
     */
    public void showRemainDetail(String number){
        MobileCard card;
        int remainTalkTime;
        int remainSmsCount;
        int remainFlow;
        StringBuffer meg=new StringBuffer();
        card=cards.get(number);
        meg.append("卡号："+number+",套餐剩余：\n");
        ServicePackage pack=card.getSerPackage();
        if(pack instanceof TalkPackage){
            TalkPackage cardPack=(TalkPackage)pack;
            remainTalkTime=cardPack.getTalkTime()>card.getRealTalkTime()?
                    cardPack.getTalkTime()-card.getRealTalkTime():0;
            meg.append("通话时长："+remainTalkTime+"分钟\n");
            remainSmsCount=cardPack.getSmsCount()>card.getRealSMSCount()?
                    cardPack.getSmsCount()-card.getRealSMSCount():0;
            meg.append("短信条数："+remainSmsCount+"条");
        }else if(pack instanceof NetPackage){
            NetPackage cardPack=(NetPackage)pack;
            remainFlow=cardPack.getFlow()>card.getRealFlow()?
                    cardPack.getFlow()-card.getRealFlow():0;
            meg.append("上网流量："+ Common.dataFormat(remainFlow*1.0/1024) +"GB");
        }else if(pack instanceof SuperPackage){
            SuperPackage cardPack=(SuperPackage)pack;
            remainTalkTime=cardPack.getTalkTime()>card.getRealTalkTime()?
                    cardPack.getTalkTime()-card.getRealTalkTime():0;
            meg.append("通话时长："+remainTalkTime+"分钟\n");
            remainSmsCount=cardPack.getSmsCount()>card.getRealSMSCount()?
                    cardPack.getSmsCount()-card.getRealSMSCount():0;
            meg.append("短信条数："+remainSmsCount+"条");
            remainFlow=cardPack.getFlow()>card.getRealFlow()?
                    cardPack.getFlow()-card.getRealFlow():0;
            meg.append("上网流量："+ Common.dataFormat(remainFlow*1.0/1024) +"GB");
        }
        System.out.println(meg);
    }

    /**
     * 查询指定卡号的消费详情
     * @param number
     */
    public void showAmountDetail(String number){
        MobileCard card;
        StringBuffer meg=new StringBuffer();
        card=cards.get(number);
        meg.append("您的卡号："+card.getCardNumber()+",当月账单：\n");
        meg.append("套餐资费："+card.getSerPackage().getPrice()+"元\n");
        meg.append("合计："+Common.dataFormat(card.getConsumAmount())+"元\n");
        meg.append("账户余额："+Common.dataFormat(card.getMoney())+"元\n");
        System.out.println(meg);
    }

    /**
     * 添加一条指定卡的消费记录
     * @param number 卡号
     * @param info 记录
     */
    public void addConsumInfo(String number,ConsumInfo info){
        Set<String> numbers=consumInfos.keySet();
        Iterator<String> it=numbers.iterator();
        List<ConsumInfo> infos=new ArrayList<>();
        boolean isExist=false;
        while (it.hasNext()){
            infos=consumInfos.get(number);
            infos.add(info);
            isExist=true;
            System.out.println("已添加一条消费记录。");
            break;
        }
        if(!isExist){
            infos.add(info);
            consumInfos.put(number,infos);
            System.out.println("不存在此卡的消费记录，已添加一条消费记录。");
        }
    }

    /**
     * 使用SOSO
     * @param number
     */
    public void useSoso(String number){
        MobileCard card=cards.get(number);
        ServicePackage pack=card.getSerPackage();
        Random random=new Random();
        int ranNum=0;int temp=0;//记录各场景中实际消费数据
        do{
            ranNum=random.nextInt(3);
            Scene scene=scenes.get(ranNum);
            switch (ranNum){
                case 0:
                    // 判断该卡所属套餐是否支持通话功能
                    if(pack instanceof CallService){
                        System.out.println(scene.getDescription());
                        CallService callService=(CallService) pack;
                        try {
                            temp=callService.call(scene.getData(),card);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 添加一条消费记录
                        addConsumInfo(number,new ConsumInfo(number,scene.getType(),temp));
                        break;
                    }else {
                        // 如果该卡套餐不支持通话功能，则重新生成随机数选择其他场景
                        continue;
                    }
                case 1:
                    if(pack instanceof SendService){
                        System.out.println(scene.getDescription());
                        SendService sendService=(SendService) pack;
                        try {
                            temp=sendService.send(scene.getData(),cards.get(number));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        addConsumInfo(number,new ConsumInfo(number,scene.getType(),temp));
                        break;
                    }else {
                        continue;
                    }
                case 2:
                    if(pack instanceof NetService){
                        System.out.println(scene.getDescription());
                        NetService netService=(NetService)pack;
                        try {
                            temp=netService.netPlay(scene.getData(),cards.get(number));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        addConsumInfo(number,new ConsumInfo(number,scene.getType(),temp));
                        break;
                    }else {
                        continue;
                    }
            }
            break;
        }while(true);
    }

    /**
     * 显示资费说明。。
     */
    public void showDescription(){
        Reader re=null;
        try {
            re=new FileReader("资费说明.txt");
            char[] content=new char[1024];
            int len=0;
            StringBuffer sb=new StringBuffer();
            while ((len=re.read(content))!=-1){
                sb.append(content,0,len);
            }
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改套餐
     * @param number
     * @param packNum
     */
    public void changingPack(String number,String packNum){
        MobileCard card;ServicePackage pack;
        if(isExitCard(number)){
            card=cards.get(number);
            switch (packNum){
                case "1":
                    pack=new TalkPackage();break;
                case "2":
                    pack=new NetPackage();break;
                default:
                    pack=new SuperPackage();break;
            }
            if(card.getSerPackage().getClass().getName().equals(pack.getClass().getName())){
                System.out.println("对不起你已经是该套餐用户，无需变更套餐！");
            }else{
                if(card.getMoney()>pack.getPrice()){
                    card.setMoney(card.getMoney()-pack.getPrice());
                    card.setSerPackage(pack);
                    card.setRealTalkTime(0);
                    card.setRealFlow(0);
                    card.setRealSMSCount(0);
                    card.setConsumAmount(pack.getPrice());
                    System.out.println("更换套餐成功!");
                    pack.showInfo();
                }else {
                    System.out.println("对不起，您卡上余额不足以支付新套餐本月资费，请充值后再办理套餐更换业务!");
                    return;
                }
            }
        }else{
            System.out.println("对不起，该卡号未注册，无法更换套餐！");
        }
    }

    /**
     * 打印消费记录。。
     * @param number
     */
    public void printConsumInfo(String number){
        Writer fileWriter=null;
        try{
            fileWriter=new FileWriter(number+"消费记录.txt");
            Set<String> numbers=consumInfos.keySet();
            Iterator<String> it=numbers.iterator();
            List<ConsumInfo> infors=new ArrayList<ConsumInfo>();
            boolean isExist=false;
            while (it.hasNext()){
                if(it.next().equals(number)){
                    infors=consumInfos.get(number);
                    isExist=true;
                    break;
                }
            }
            if(isExist){
                StringBuffer context=new StringBuffer();
                context.append("序号\t类型\t数据（通话（条）/上网（MB）/短信（条））\n");
                for(int i=0;i<infors.size();i++){
                    ConsumInfo info=infors.get(i);
                    context.append((i+1)+".\t"+info.getType()+"\t"+info.getConsumData()+"\n");
                }
                fileWriter.write(context.toString());
                fileWriter.flush();
                System.out.println("消费记录打印完毕！");
            }else {
                System.out.println("对不起，不存在此号码的消费记录，无法打印！");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fileWriter!=null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 话费充值。。
     * @param number
     * @param money
     */
    public void changeMoney(String number,double money){
        MobileCard card;
        if(money<50){
            System.out.println("对不起。最低充值50元！");
            return;
        }
        card=cards.get(number);
        card.setMoney(card.getMoney()+money);
        System.out.println("充值成功，当前余额"+Common.dataFormat(card.getMoney())+"元。");
    }

    /**
     *根据传入的id来创建package
     * @param id
     * @return
     */
    public ServicePackage createPack(int id) {
        ServicePackage pack=null;
        switch (id){
            case 1:
                pack=new TalkPackage();break;
            case 2:
                pack=new NetPackage();break;
            case 3:
                pack=new SuperPackage();break;
        }
        return pack;
    }
}
