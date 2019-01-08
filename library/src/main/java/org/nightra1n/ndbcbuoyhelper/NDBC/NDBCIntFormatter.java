package org.nightra1n.ndbcbuoyhelper.NDBC;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public class NDBCIntFormatter implements FixedFormatter<Integer> {
    @Override
    public Integer parse(String value, FormatInstructions instructions) throws FixedFormatException {
        int result;
        if (value.trim().equals("MM")) {
            result = 0;
        } else {
            try {
                result = Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                throw new FixedFormatException("Could not parse value[" + value + "]");
            }

        }
        return result;
    }

    @Override
    public String format(Integer value, FormatInstructions instructions) throws FixedFormatException {
        String result;
        if (value == 0) {
            result = "MM";
        } else {
            result = Integer.toString(value);
        }

        return result;
    }
}
