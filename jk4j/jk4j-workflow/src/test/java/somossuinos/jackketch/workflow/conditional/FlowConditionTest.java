package somossuinos.jackketch.workflow.conditional;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FlowConditionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreate_With_Attribute_Null_Fail() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"attribute\" must not be empty");

        new FlowCondition(null, null, null);
    }

    @Test
    public void testCreate_With_Attribute_Empty_Fail() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"attribute\" must not be empty");

        new FlowCondition(" ", null, null);
    }

    @Test
    public void testCreate_With_ConditionType_Null_Fail() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("conditionType cannot be null");

        new FlowCondition("attr", null, null);
    }

    @Test
    public void testCreate_With_Value_Null_Fail() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"value\" must not be empty");

        new FlowCondition("attr", ConditionType.EQ, null);
    }

    @Test
    public void testCreate_With_Value_Blank_Fail() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("\"value\" must not be empty");

        new FlowCondition("attr", ConditionType.EQ, " ");
    }

    @Test
    public void testGetAttribute_Success() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.EQ, "val");

        Assert.assertEquals("attr", fc.getAttribute());
    }

    @Test
    public void testIsValid_With_Null_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.EQ, "val");

        Assert.assertFalse(fc.isValid(null));
    }

    @Test
    public void testIsValid_Attr_Eq_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.EQ, "val");

        Assert.assertTrue(fc.isValid("val"));
    }

    @Test
    public void testIsValid_Attr_Eq_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.EQ, "val");

        Assert.assertFalse(fc.isValid("wrongVal"));
    }

    @Test
    public void testIsValid_Attr_Neq_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.NEQ, "val");

        Assert.assertTrue(fc.isValid("diffVal"));
    }

    @Test
    public void testIsValid_Attr_Neq_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.NEQ, "val");

        Assert.assertFalse(fc.isValid("val"));
    }

    @Test
    public void testIsValid_Attr_Gt_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.GT, "1");

        Assert.assertTrue(fc.isValid("2"));
    }

    @Test
    public void testIsValid_Attr_Gt_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.GT, "1");

        Assert.assertFalse(fc.isValid("0"));
    }

    @Test
    public void testIsValid_Attr_Geqt_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.GEQT, "1");

        Assert.assertTrue(fc.isValid("1") && fc.isValid("2"));
    }

    @Test
    public void testIsValid_Attr_Geqt_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.GT, "1");

        Assert.assertFalse(fc.isValid("0"));
    }

    @Test
    public void testIsValid_Attr_Lt_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.LT, "1");

        Assert.assertTrue(fc.isValid("0"));
    }

    @Test
    public void testIsValid_Attr_Lt_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.LT, "1");

        Assert.assertFalse(fc.isValid("1"));
    }

    @Test
    public void testIsValid_Attr_Leqt_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.LEQT, "1");

        Assert.assertTrue(fc.isValid("0") && fc.isValid("1"));
    }

    @Test
    public void testIsValid_Attr_Leqt_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.LT, "1");

        Assert.assertFalse(fc.isValid("2"));
    }

    @Test
    public void testIsValid_Attr_In_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.IN, "Attribute inside the value");

        Assert.assertTrue(fc.isValid("IN"));
    }

    @Test
    public void testIsValid_Attr_In_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.IN, "Attribute not inside the value");

        Assert.assertFalse(fc.isValid("OUT"));
    }

    @Test
    public void testIsValid_Attr_Ends_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.ENDS, "Attribute ends with value");

        Assert.assertTrue(fc.isValid("LUE"));
    }

    @Test
    public void testIsValid_Attr_Ends_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.ENDS, "Does attribute ends with value?");

        Assert.assertFalse(fc.isValid("LUE"));
    }

    @Test
    public void testIsValid_Attr_Starts_Value_Returns_True() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.STARTS, "Attribute starts with value");

        Assert.assertTrue(fc.isValid("ATTR"));
    }

    @Test
    public void testIsValid_Attr_Starts_Value_Returns_False() {
        final FlowCondition fc = new FlowCondition("attr", ConditionType.ENDS, "Does attribute starts with value?");

        Assert.assertFalse(fc.isValid("ATTR"));
    }

}
