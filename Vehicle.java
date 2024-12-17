public abstract class Vehicle {

    private String brand;
    private int kimeters;

    public Vehicle(String brand, int kimeters) {
        this.brand = brand;
        this.kimeters = kimeters;
    }

    public String getBrand() {
        return brand;
    }

    public int getKimeters() {
        return kimeters;
    }

    abstract String doStuff();
}
