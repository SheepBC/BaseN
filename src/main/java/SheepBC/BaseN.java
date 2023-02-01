package SheepBC;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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



    public static String toSerialize(Object object){

        byte[] serializeMember;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            serializeMember = bos.toByteArray();
            String b64 = Base64.getEncoder().encodeToString(serializeMember);

            BaseN base64 = new BaseN(getList64());
            BaseN baseM = new BaseN(getListM());

            String num = base64.BaseNToDecimal(b64);
            return baseM.DecimalToBaseN(num);


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object toObject(String Serialize){

        BaseN baseM = new BaseN(getListM());
        BaseN base64 = new BaseN(getList64());
        String sm = baseM.BaseNToDecimal(Serialize);
        String bm = base64.DecimalToBaseN(sm);

        byte[] serializeMember = Base64.getDecoder().decode(bm);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(serializeMember);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return  ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    private static String[] getList64(){
        String b6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        return b6.split("");
    }

    private static String[] getListM(){

        ArrayList<String> list = new ArrayList<>();
        String b6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        list.addAll(List.of(b6.split("")));

        String[] CHO = {"ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ",
                "ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};

        String[] JOONG = {"ㅏ","ㅐ","ㅑ","ㅒ","ㅓ","ㅔ","ㅕ","ㅖ","ㅗ","ㅘ",
                "ㅙ","ㅚ","ㅛ","ㅜ","ㅝ","ㅞ","ㅟ","ㅠ","ㅡ","ㅢ","ㅣ"};

        String[] JONG = {"","ㄱ","ㄲ","ㄳ","ㄴ","ㄵ","ㄶ","ㄷ","ㄹ","ㄺ","ㄻ","ㄼ",
                "ㄽ","ㄾ","ㄿ","ㅀ","ㅁ","ㅂ","ㅄ","ㅅ","ㅆ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
        list.addAll(List.of(CHO));
        list.addAll(List.of(JOONG));

        for(int i = 0;i< CHO.length;i++){
            for(int i1 = 0;i1< JOONG.length;i1++){
                for(int i2 = 0;i2< JONG.length;i2++){
                    char c = (char) ((i*21+i1)*28+i2+0xAC00);
                    list.add(String.valueOf(c));
                }
            }
        }

        return list.toArray(new String[0]);
    }

}

