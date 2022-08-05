import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class BaseN {

    String[] list;
    public BaseN(String[] list){
        if(checkList(list)){
            this.list = list;
        }
        else {
            try {
                throw new BaseNException("리스트에 중복이 있겄나 2글자 이상이 있습니다.");
            } catch (BaseNException e) {
                e.printStackTrace();
            }
        }
    }

    public int getN(){
        return list.length;
    }

    public String BaseNToDecimal(String str){
        BigInteger de = new BigInteger("0");
        String[] list = str.split("");

        for(int i = list.length-1;i>=0;i--){
            int index = list.length-i-1;
            if(contain(list[index])){
                BigInteger pow = new BigInteger(String.valueOf(getN()));
                pow = pow.pow(i);
                BigInteger num = new BigInteger(String.valueOf(getNum(list[index])));
                de = de.add(pow.multiply(num));
                //System.out.println(pow.multiply(num)+" "+de);
            }
            else {
                try {
                    throw new BaseNException("리스트에 포함되어있지 않습니다.");
                } catch (BaseNException e) {
                    e.printStackTrace();
                    return "error";
                }
            }
        }

        return de.toString();
    }


    public String DecimalToBaseN(BigInteger bi){
        if(bi.min(BigInteger.ZERO.subtract(BigInteger.ONE)).equals(bi)){
            try {
                throw new BaseNException("0을 포함한 자연수 만 가능 합니다.");
            } catch (BaseNException e) {
                e.printStackTrace();
            }
            return "error";
        }
        if(bi.equals(BigInteger.ZERO)){
            return list[0];
        }
        StringBuilder sb = new StringBuilder();
        BigInteger na = bi;
        BigInteger a = findA(bi);
        BigInteger n = new BigInteger(String.valueOf(getN()));
        while (true){
            BigInteger m = na.divide(n.pow(a.intValue()));
            sb.append(list[m.intValue()]);
            na = na.remainder(n.pow(a.intValue()));
            a =a.subtract(BigInteger.ONE);
            if(a.intValue() <0){
                return sb.toString();
            }
        }

    }

    public String DecimalToBaseN(int i){
        return DecimalToBaseN(new BigInteger(String.valueOf(i)));
    }

    public String DecimalToBaseN(String s){
        return DecimalToBaseN(new BigInteger(s));
    }

    private BigInteger findA(BigInteger b){
        BigInteger a = new BigInteger("0");
        while (true){
            boolean min = b.max(new BigInteger(String.valueOf(getN())).pow(a.intValue())).equals(b);
            boolean max = b.min(new BigInteger(String.valueOf(getN())).pow(a.intValue()+1)).equals(b);
            if(min && max){
                return a;
            }
            a = a.add(BigInteger.ONE);
        }
    }

    private boolean contain(String s){
        return Arrays.asList(list).contains(s);
    }

    private int getNum(String s){
        ArrayList<String> list = new ArrayList<>(Arrays.asList(this.list));
        return list.indexOf(s);
    }

    private boolean checkList(String[] list){
        Set<String> list2 = new HashSet<>(Arrays.asList(list));
        if(list.length != list2.size()) return false;
        for(String s:list){
            if(s.length() != 1) return false;
        }
        return true;
    }

    private boolean isInt(Double d){
        BigDecimal bd = new BigDecimal(d.toString());
        bd = bd.add(BigDecimal.ZERO);
        try {
            BigInteger integer = new BigInteger(bd.toString().replace(".0",""));
            return integer.doubleValue() == bd.doubleValue();
        }catch (Exception e){
            return false;
        }
    }

    static class BaseNException extends Exception{
        BaseNException(String msg){
            super(msg);
        }
    }
}
