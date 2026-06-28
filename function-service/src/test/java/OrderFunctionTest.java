import com.food_delivery_platform.function_service.FunctionServiceApplication;
import com.food_delivery_platform.function_service.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.function.Function;

@SpringBootTest(classes = FunctionServiceApplication.class)
public class OrderFunctionTest {

    @Autowired
    private Function<Order, Map<String, String>> sendOrderConfirmation;

    @Test
    public void testLocalEmailExecution() {
        // Prepare your test order data
        Order mockOrder = new Order();
        mockOrder.setId(111L);
        mockOrder.setCustomerEmail("sagarraju69@gmail.com");

        System.out.println(">>> Starting local SES email test...");

        // Execute the function directly (No HTTP server or curl needed!)
        Map<String, String> result = sendOrderConfirmation.apply(mockOrder);

        System.out.println(">>> SES API response: " + result.get("result"));
    }
}