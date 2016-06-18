package net.weweave.tubewarder.test;

import net.weweave.tubewarder.util.PasswordPolicy;
import org.junit.Assert;
import org.junit.Test;

public class TestPasswordPolicy {
    @Test
    public void testOkayMinLength() {
        Assert.assertTrue(PasswordPolicy.matches("abcABC12"));
    }

    @Test
    public void testOkayLong() {
        Assert.assertTrue(PasswordPolicy.matches("1ac5BD02aBC830987ABC"));
    }

    @Test
    public void testEmpty() {
        Assert.assertFalse(PasswordPolicy.matches(""));
    }

    @Test
    public void testNull() {
        Assert.assertFalse(PasswordPolicy.matches(null));
    }

    @Test
    public void testTooShort() {
        Assert.assertFalse(PasswordPolicy.matches("abcABC1"));
    }

    @Test
    public void testMissingNumber() {
        Assert.assertFalse(PasswordPolicy.matches("abcdefGH"));
    }

    @Test
    public void testMissingUppercase() {
        Assert.assertFalse(PasswordPolicy.matches("abcd1234"));
    }

    @Test
    public void testMissingLowercase() {
        Assert.assertFalse(PasswordPolicy.matches("ABCDEFG1"));
    }
}
