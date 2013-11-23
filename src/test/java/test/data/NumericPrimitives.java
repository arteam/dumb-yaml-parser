package test.data;

import annotation.Name;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
public class NumericPrimitives {

    private final long long1;
    private final Long long2;
    private final int int1;
    private final Integer int2;
    private final double double1;
    private final Double double2;
    private final float float1;
    private final Float float2;
    private final boolean bool1;
    private final Boolean bool2;

    public NumericPrimitives(@Name("long1") long long1, @Name("long2") Long long2, @Name("int1") int int1,
                             @Name("int2") Integer int2, @Name("double1") double double1, @Name("double2") Double double2,
                             @Name("float1") float float1, @Name("float2") Float float2, @Name("bool1") boolean bool1,
                             @Name("bool2") Boolean bool2) {
        this.long1 = long1;
        this.long2 = long2;
        this.int1 = int1;
        this.int2 = int2;
        this.double1 = double1;
        this.double2 = double2;
        this.float1 = float1;
        this.float2 = float2;
        this.bool1 = bool1;
        this.bool2 = bool2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumericPrimitives that = (NumericPrimitives) o;

        if (bool1 != that.bool1) return false;
        if (Double.compare(that.double1, double1) != 0) return false;
        if (Float.compare(that.float1, float1) != 0) return false;
        if (int1 != that.int1) return false;
        if (long1 != that.long1) return false;
        if (bool2 != null ? !bool2.equals(that.bool2) : that.bool2 != null) return false;
        if (double2 != null ? !double2.equals(that.double2) : that.double2 != null) return false;
        if (float2 != null ? !float2.equals(that.float2) : that.float2 != null) return false;
        if (int2 != null ? !int2.equals(that.int2) : that.int2 != null) return false;
        if (long2 != null ? !long2.equals(that.long2) : that.long2 != null) return false;

        return true;
    }


    @Override
    public String toString() {
        return "NumericPrimitives{" +
                "long1=" + long1 +
                ", long2=" + long2 +
                ", int1=" + int1 +
                ", int2=" + int2 +
                ", double1=" + double1 +
                ", double2=" + double2 +
                ", float1=" + float1 +
                ", float2=" + float2 +
                ", bool1=" + bool1 +
                ", bool2=" + bool2 +
                '}';
    }
}
