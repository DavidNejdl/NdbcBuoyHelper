package org.nightra1n.ndbcbuoyhelper.NDBC;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public class NDBCFloatFormatter implements FixedFormatter<Float> {

    @Override
    public Float parse(String value, FormatInstructions instructions) throws FixedFormatException {
        float result;
        if (value.trim().equals("MM")) {
            result = 0f;
        } else {
            try {
                result = Float.parseFloat(value.trim());
            } catch (NumberFormatException e) {
                throw new FixedFormatException("Could not parse value[" + value + "]");
            }

        }
        return result;
    }

    @Override
    public String format(Float value, FormatInstructions instructions) throws FixedFormatException {
        String result;
        if (value == 0) {
            result = "MM";
        } else {
            result = Float.toString(value);
        }

        return result;
    }

}
