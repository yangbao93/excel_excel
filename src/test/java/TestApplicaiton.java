import com.alibaba.fastjson.JSONObject;
import com.yonyou.entity.InvoiceCollection;

import org.junit.Test;

import java.io.File;

/**
 * 用于解析所有的
 * Created by yangbao on 2017/12/8.
 */
public class TestApplicaiton {
  private static int temp = 0;

  public static int fib(int n) {
    System.out.println(++temp + "次数");
    System.out.println(n);
    if (n == 0) {
      return 0;
    }
    if (n == 1) {
      return 1;
    }
    return fib(n - 1) + fib(n - 2);
  }


  public static void main(String[] args) {
      String a = "{\"accountNote\":\"00\",\"accountTime\":\"\",\"accountUser\":\"冠益乳护手\",\"corpid\":\"\",\"fpDm\":\"150003522222\",\"fpHm\":\"87976666\",\"fplx\":\"1\",\"id\":659,\"jshj\":\"2666.00\",\"kplx\":\"0\",\"kprq\":\"2017-12-08\",\"orgid\":\"\",\"pk_invoice\":90807780,\"purchaser\":\"d\",\"purchaserstatus\":37,\"reimburse_date\":\"\",\"reimburse_money\":\"\",\"reimburse_user\":\"\",\"vnote\":\"dd\",\"voucherid\":\"00\",\"xsfMc\":\"测试3\"}";
    String toJSONString = JSONObject.toJSONString(a);
    System.out.println(toJSONString);
    InvoiceCollection collection = JSONObject.parseObject(a, InvoiceCollection.class);
  }

    @Test
    public void testCreateFile() {
        File file = new File("D:\\TXADAFA.txt");
        file.mkdir();
    }
}
