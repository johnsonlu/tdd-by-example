package guru.springframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MoneyTest {

    @Test
    void testMultiplication() {
        Money five = Money.dollar(5);

        assertEquals(Money.dollar(10), five.times(2));
        assertEquals(Money.dollar(15), five.times(3));
    }

    @Test
    void testEquality() {
        assertEquals(Money.dollar(5), Money.dollar(5));
        assertNotEquals(Money.dollar(5), null);
        assertNotEquals(Money.dollar(8), Money.dollar(10));
        assertNotEquals(Money.dollar(5),Money.franc(5));
    }

    @Test
    void testHashCode() {
        assertEquals(Money.dollar(5), Money.dollar(5));
        assertNotEquals(Money.dollar(5), Money.franc(5));
    }

    @Test
    void testCurrency() {
        assertEquals("USD", Money.dollar(1).currency());
        assertEquals("CHF", Money.franc(1).currency());
    }

    @Test
    void testSimpleAddition() {
        Money five = Money.dollar(5);
        Expression sum = five.plus(five);
        Bank bank = new Bank();
        Money reduced = sum.reduce(bank, "USD");
        assertEquals(Money.dollar(10), reduced);
    }

    @Test
    void testPlusReturnsSum() {
        Money five = Money.dollar(5);
        Expression result = five.plus(five);
        Sum sum = (Sum) result;
        assertEquals(five, sum.getAugend());
        assertEquals(five, sum.getAddend());
    }

    @Test
    void testReduceSum() {
        Expression sum = new Sum(Money.dollar(3), Money.dollar(4));
        Bank bank = new Bank();
        Money result = sum.reduce(bank, "USD");
        assertEquals(Money.dollar(7), result);
    }

    @Test
    void testReduceMoney() {
        Bank bank = new Bank();
        Money result = Money.dollar(1).reduce(bank, "USD");
        assertEquals(Money.dollar(1), result);
    }

    @Test
    void testReduceMoneyDifferentCurrency() {
        Bank bank = new Bank();
        assertEquals(Money.dollar(1), Money.dollar(1).reduce(bank, "USD"));
    }

    @Test
    void testReduceSumDifferentCurrency() {
        Money fiveDollar = Money.dollar(5);
        Money tenFranc = Money.franc(10);
        Expression sum = fiveDollar.plus(tenFranc);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        assertEquals(Money.dollar(10), sum.reduce(bank, "USD"));
    }

    @Test
    void testIdentityRate() {
        assertEquals(1, new Bank().rate("USD", "USD"));
        assertEquals(1, new Bank().rate("CHF", "CHF"));
    }

    @Test
    void testReduceMultipleSumDifferentCurrency() {
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        assertEquals(Money.dollar(10),
                Money.dollar(1)
                        .plus(Money.dollar(4))
                        .plus(Money.franc(10)) // 5 USD
                        .reduce(bank, "USD"));
    }

    @Test
    void testSumThenTimes() {
        Bank bank = new Bank();

        Expression result = Money.dollar(1).plus(Money.dollar(3)).times(2);
        assertEquals(Money.dollar(8), result.reduce(bank, "USD"));
    }
}
