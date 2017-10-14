package com.test.testclass;

/**
 * Created by xiaohao on 17-10-14.
 */

interface GasListener {
    public void offerGas(String msg);
}

class GasCompany implements GasListener{

    @Override
    public void offerGas(String msg) {
        System.out.println("公司收到买煤气的请求");

    }
    public void  advertiseTo(IndoorMan man){
        System.out.println("煤气公司：这是我们的联系方式，欢迎来电");
        man.setGasListener(this);

    }


}

class IndoorMan{
    GasListener gasListener;
    public void prepareCook(){
        System.out.println("宅男：准备下厨做几个花式大菜！");
        System.out.println("宅男：进厨房，烧菜...");
        System.out.println("宅男：刚开火，就发现煤气不足，没办法，只能打电话叫煤气。");
        gasListener.offerGas("送煤气过来");

    }
    public void setGasListener(GasListener gasListener){
        this.gasListener=gasListener;
    }
}


public class TestClass {
    public static void main(String agrs[]){
        IndoorMan indoorMan=new IndoorMan();
        GasCompany company=new GasCompany();
        company.advertiseTo(indoorMan);
        indoorMan.prepareCook();
    }
}
