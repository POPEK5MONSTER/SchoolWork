/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Calc {

    private final Map<String, BiFunction<BigDecimal, BigDecimal, BigDecimal>> operators;

    public Calc() {
        operators = new HashMap<>();
        operators.put("+", BigDecimal::add);
        operators.put("-", BigDecimal::subtract);
        operators.put("*", BigDecimal::multiply);
        operators.put("/", (x, y) -> x.divide(y, 7, RoundingMode.HALF_UP));
    }

    public String doCalc(String cmd) {
        try {
            String[] tokens = cmd.split("\\s+");

            BigDecimal num1 = new BigDecimal(tokens[0]);
            BigDecimal num2 = new BigDecimal(tokens[2]);

            BiFunction<BigDecimal, BigDecimal, BigDecimal> operator = getOperatorMultiplier(tokens[1]);
            BigDecimal result = operator.apply(num1, num2);

            return result.stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return "Invalid command to calc";
        }
    }

    private BiFunction<BigDecimal, BigDecimal, BigDecimal> getOperatorMultiplier(String operator) {
        return operators.get(operator);
    }
}
