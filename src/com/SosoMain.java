package com;

import entity.MobileCard;
import entity.ServicePackage;
import util.CardUtil;

import java.util.Scanner;

public class SosoMain {
    Scanner input=new Scanner(System.in);
    CardUtil utils=new CardUtil();

    public static void main(String[] args) {
        SosoMain soso=new SosoMain();
        soso.mainMenu();
        System.out.println("谢谢使用！");
    }

    private void mainMenu() {
        int menuChoose=0;
        String mobileNum=null;
        String password=null;
        utils.initScene();
        do{
            System.out.println("\n***************欢迎使用嗖嗖移动业务大厅***************");
            System.out.println("1.用户登录  2.用户注册  3.使用嗖嗖  4.话费充值  5.资费说明  6.退出系统");
            System.out.print("请选择：");
            menuChoose=input.nextInt();
            switch (menuChoose){
                case 1:
                    System.out.print("请输入手机卡号：");
                    mobileNum=input.next();
                    System.out.print("请输入密码：");
                    password=input.next();
                    if(utils.isExistCard(mobileNum,password)){
                        System.out.println("登录成功，SOSO移动欢迎您！");
                        cardMenu(mobileNum);
                    }else {
                        System.out.println("对不起，您输入信息有误，登录失败！");
                    }
                    continue;
                case 2:
                    registCard();
                    continue;
                case 3:
                    System.out.print("请输入手机卡号：");
                    mobileNum=input.next();
                    if(utils.isExitCard(mobileNum)){
                        utils.useSoso(mobileNum);
                    }else {
                        System.out.println("对不起，该卡号为注册，无法使用!");
                    }
                    continue;
                case 4:
                    System.out.println("请输入充值卡号：");
                    mobileNum=input.next();
                    if(utils.isExitCard(mobileNum)){
                        System.out.println("请输入充值金额：");
                        double money=input.nextDouble();
                        utils.changeMoney(mobileNum,money);
                    }else {
                        System.out.println("对不起，要充的卡号未注册，无法充值！");
                    }
                    continue;
                case 5:
                    System.out.println();
                    utils.showDescription();
                    continue;
                case 6:
                    break;
                    default:
                        break;
            }
            break;
        }while(true);
    }
    private void registCard() {
        String[] numbers=utils.getNewNumber(9);
        System.out.println("*****可选择的卡号*****");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print((i+1)+"."+numbers[i]+"\t\t");
            if(i%3==0){
                System.out.println();
            }
        }
        System.out.println("\n请选择卡号（输入1-9的序号）：");
        String number=numbers[input.nextInt()-1];
        System.out.print("1.话痨套餐  2.网虫套餐  3.超人套餐  ，请选择套餐（输入序号）：");
        ServicePackage pack=utils.createPack(input.nextInt());
        System.out.println("请输入姓名：");
        String name=input.next();
        System.out.println("请输入密码：");
        String password=input.next();
        double money=0;
        System.out.println("请输入预存话费金额：");
        money=input.nextDouble();
        if(money<pack.getPrice()){
            System.out.println("您预存的话费金额不足以支付本月固定套餐资费，请重新充值：");
            money=input.nextDouble();
        }
        MobileCard ncard=new MobileCard(name,password,number,pack,pack.getPrice(),(money-pack.getPrice()));
        utils.addCard(ncard);
    }

    private int cardMenu(String mobileNum) {
        int menuChoose=0;
        do{
            System.out.println("\n*****嗖嗖移动用户菜单*****");
            System.out.println("1.本月账单查询");
            System.out.println("2.套餐余量查询");
            System.out.println("3.打印消费详单");
            System.out.println("4.套餐变更");
            System.out.println("5.办理退网");
            System.out.print("请选择（输入1-5选择功能，其他建返回上一级）：");
            menuChoose=input.nextInt();
            switch (menuChoose){
                case 1:
                    System.out.println("\n*****本月账单查询*****");
                    utils.showAmountDetail(mobileNum);
                    continue;
                case 2:
                    System.out.println("\n*****套餐余量查询*****");
                    utils.showRemainDetail(mobileNum);
                    continue;
                case 3:
                    System.out.println("\n*****消费详单查询*****");
                    utils.printConsumInfo(mobileNum);
                    continue;
                case 4:
                    System.out.println("\n*****套餐变更*****");
                    System.out.print("1.话痨套餐  2.网虫套餐  3.超人套餐  请选择（输入1-3选择功能，其他键返回上一级）：");
                    utils.changingPack(mobileNum,input.next());
                    continue;
                case 5:
                    System.out.println("\n*****办理退网*****");
                    utils.delCard(mobileNum);
                    System.out.println("谢谢使用");
                    System.exit(1);
            }break;
        }while (true);
        return menuChoose;
    }
}
