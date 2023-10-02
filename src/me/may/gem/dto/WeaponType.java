package me.may.gem.dto;

/**
 * Create By IDEA
 *
 * @author May_Speed
 * @since 2017-08-28
 */
public class WeaponType {

    private int id;
    private short data;

    public WeaponType(int id, short data) {

        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getData() {
        return data;
    }

    public void setData(short data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeaponType that = (WeaponType) o;

        if (id != that.id) return false;
        return data == that.data;
    }

    @Override
    public String toString() {
        return "WeaponType{" +
                "id=" + id +
                ", data=" + data +
                '}';
    }
}
