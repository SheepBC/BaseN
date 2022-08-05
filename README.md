# BaseN

BaseN은[디스코드 봇](https://discord.com/oauth2/authorize?client_id=852699897924026378&permissions=3533904&scope=bot)에서 전달내용을 압축하기 위해 만들어졌습니다.

# 사용법


## 예시 

```java
class Main{
    public static void main(String[] args){
        String[] list = {"1","@","a","아"};
        BaseN baseN = new BaseN(list);
        String bN = baseN.BaseNToDecimal("@@a아1"); //bN = 364
        String De = baseN.DecimalToBaseN("10123"); // De = a@아a1a아
        System.out.println(bN+" "+De);
    }
}
```
## 주의점

* List는 중복없이 한글자로만 이루어져야한다.
* BaseNToDecimal로 나온 String을 Double로 바꿀 경우 뒤쪽이 0 으로바꾸는 오류가있다 
