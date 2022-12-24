package api.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class ColorsData {
    private Integer id;
    private String name;
    private Integer year;
    private String color;
    private String pantone_value;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getPantone_value() {
        return pantone_value;
    }
}
