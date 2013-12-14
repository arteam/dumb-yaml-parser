package test.data;

import org.dumb.yaml.annotation.DateConverter;
import org.dumb.yaml.annotation.DateConverters;
import org.dumb.yaml.annotation.Name;

import java.util.Date;

/**
 * Date: 11/23/13
 * Time: 5:47 PM
 *
 * @author Artem Prigoda
 */
@DateConverters(names = {"start_date"}, converters = {@DateConverter("yyyy-MM-dd HH:mm")})
public class DateInterval {

    private final Date startDate;
    private final Date endDate;

    public DateInterval(@Name("start_date") @DateConverter("yyyy-MM-dd HH:mm") Date startDate,
                        @Name("end_date") @DateConverter("yyyy-MM-dd HH:mm") Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateInterval that = (DateInterval) o;

        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "DateInterval{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
