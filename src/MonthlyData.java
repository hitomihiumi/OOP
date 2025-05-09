class MonthlyData {
    private MonthEnum month;
    private int year;
    private double previous;
    private double current;
    private double used;
    private double price;
    private double cost;


    public MonthlyData(MonthEnum month, int year) {
        this.month = month;
        this.year = year;
    }

    public void setPrevious(double previous) {
        this.previous = previous;
        recalculate();
    }

    public void setCurrent(double current) {
        this.current = current;
        recalculate();
    }

    public void setPrice(double price) {
        this.price = price;
        recalculate();
    }

    private void recalculate() {
        used = current - previous;
        if (used < 0) used = 0;
        double nightUsed = used / 100 * 29.2;
        double dayUsed = used - nightUsed;
        cost = (dayUsed * price) + (nightUsed * price * 0.5);
    }

    public MonthEnum getMonth() { return month; }
    public int getYear() { return year; }
    public double getPrevious() { return previous; }
    public double getCurrent() { return current; }
    public double getUsed() { return used; }
    public double getPrice() { return price; }
    public double getCost() { return cost; }

    public String[] toCSVRow() {
        return new String[]{month.name(), String.valueOf(year), String.valueOf(previous), String.valueOf(current),
                String.valueOf(used), String.valueOf(price), String.valueOf(cost)};
    }

    public static MonthlyData from(String[] parts) {
        MonthlyData d = new MonthlyData(MonthEnum.valueOf(parts[0]), Integer.parseInt(parts[1]));
        d.previous = Double.parseDouble(parts[2]);
        d.current = Double.parseDouble(parts[3]);
        d.used = Double.parseDouble(parts[4]);
        d.price = Double.parseDouble(parts[5]);
        d.cost = Double.parseDouble(parts[6]);
        return d;
    }
}