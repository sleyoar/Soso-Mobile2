package entity;
public abstract class ServicePackage {
    //三个套餐的共同属性就是每月要交月租，不交钱还想用卡？
    protected double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //定义一个抽象的控制台输出方法，三个套餐子类会用到
    public abstract void showInfo();
}