import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * MVP for tracking real estate properties, their financials, and portfolio summary.
 */
public final class RealEstateTrackerMVP {
/**
 * Rounding mode used for all monetary calculations.
 */
private static final RoundingMode RM = RoundingMode.HALF_UP;

/**
 * Represents a real estate property with financial details and status.
 */
private static final class Property {
    /**
     * Unique identifier for the property.
     */
    private final int id;
    /**
     * Street address of the property.
     */
    private String address;

    /**
     * City where the property is located.
     */
    String city;

    /**
     * Purchase price of the property.
     */
    BigDecimal purchase;
    /**
     * Rehabilitation cost of the property.
     */
    private BigDecimal rehab;
    /**
     * Current market value of the property.
     */
    BigDecimal currentValue;

    /**
     * Monthly rent amount for the property.
     */
    BigDecimal rentMonthly;
    /**
     * Year-to-date income for the property.
     */
    /**
     * Year-to-date income represented as a BigDecimal for precise financial calculations.
     */
    BigDecimal ytdIncome;
    /**
     * Year-to-date expense represented as a BigDecimal for precise financial
     * calculations.
     */
    BigDecimal ytdExpense;
    /**
     * Status of the property (e.g., Active, Sold, Under Contract).
     */
    String status;
/**
     * Constructs a Property with the given details.
     *
     * @param id unique identifier for the property
     * @param address street address of the property
     * @param city city where the property is located
     * @param purchase purchase price of the property
     * @param rehab rehabilitation cost of the property
     * @param currentValue current market value of the property
     * @param rentMonthly monthly rent amount for the property
     * @param status status of the property (e.g., Active, Sold)
     */
    Property(int id, String address, String city,
             BigDecimal purchase, BigDecimal rehab,
             BigDecimal currentValue, BigDecimal rentMonthly,
             String status) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.purchase = purchase.setScale(2, RM);
        this.rehab = rehab.setScale(2, RM);
        this.currentValue = currentValue.setScale(2, RM);
        this.rentMonthly = rentMonthly.setScale(2, RM);
        this.ytdIncome = BigDecimal.ZERO.setScale(2, RM);
        this.ytdExpense = BigDecimal.ZERO.setScale(2, RM);
        if (status == null || status.isEmpty()) {
            this.status = "Active";
        } else {
            this.status = status;
        }
    }

    /**
     * Calculates the annual Net Operating Income (NOI) for this property.
     *
     * @return the annual NOI as BigDecimal
     */
    BigDecimal annualNOI() {
        BigDecimal annualRent = this.rentMonthly.multiply(BigDecimal.valueOf(12));
        return annualRent.add(this.ytdIncome).subtract(this.ytdExpense).setScale(2, RM);
    }

    /**
     * Calculates the capitalization rate (cap rate) as a percentage for this property.
     *
     * @return the cap rate percentage, or 0 if purchase price is zero
     */
    BigDecimal capRatePercent() {
        if (this.purchase.signum() == 0) {
            return BigDecimal.ZERO.setScale(2, RM);
        }
        BigDecimal ratio = this.annualNOI().divide(this.purchase, 6, RM);
        return ratio.multiply(BigDecimal.valueOf(100)).setScale(2, RM);
    }

    /**
     * Calculates the equity for this property.
     *
     * @return the equity as current value minus purchase and rehab costs
     */
    BigDecimal equity() {
        BigDecimal basis = this.purchase.add(this.rehab);
        return this.currentValue.subtract(basis).setScale(2, RM);
    }

    @Override
    public String toString() {
        return "#" + this.id + " " + this.address + ", " + this.city + " ["
                + this.status + "] " + "price=$"
                + this.purchase.setScale(2, RM).toPlainString() + " value=$"
                + this.currentValue.setScale(2, RM).toPlainString()
                + " rent/mo=$"
                + this.rentMonthly.setScale(2, RM).toPlainString() + " NOI/yr=$"
                + this.annualNOI().toPlainString() + " cap="
                + this.capRatePercent().toPlainString() + "%";
    }
}

/**
 * List of properties being tracked.
 */
private final ArrayList<Property> items = new ArrayList<>();

/**
 * Next unique property id to assign.
 */
private int nextId = 1;

/**
 * Adds a new property to the tracker.
 *
 * @param address the property address
 * @param city the city where the property is located
 * @param purchase the purchase price of the property
 * @param rehab the rehabilitation cost of the property
 * @param initialValue the initial value of the property
 * @param rentMonthly the monthly rent amount
 * @param status the status of the property
 * @return the unique id assigned to the property
 * @throws IllegalArgumentException if any required argument is invalid
 */
public int addProperty(String address, String city, BigDecimal purchase,
        BigDecimal rehab, BigDecimal initialValue, BigDecimal rentMonthly,
        String status) {
    if (address == null || address.isEmpty()) {
        throw new IllegalArgumentException("bad address");
    }
    if (city == null || city.isEmpty()) {
        throw new IllegalArgumentException("bad city");
    }
    if (purchase == null || rehab == null || initialValue == null) {
        throw new IllegalArgumentException("null money");
    }
    if (rentMonthly == null) {
        throw new IllegalArgumentException("null rent");
    }
    Property p = new Property(this.nextId++, address, city, purchase, rehab,
            initialValue, rentMonthly, status);
    this.items.add(p);
    return p.id;
}

/**
 * Updates the monthly rent for the property with the given id.
 *
 * @param id the property id
 * @param newRentMonthly the new monthly rent amount
 * @return true if the property was found and updated, false otherwise
 * @throws IllegalArgumentException if newRentMonthly is null or negative
 */
public boolean updateRent(int id, BigDecimal newRentMonthly) {
    if (newRentMonthly == null || newRentMonthly.signum() < 0) {
        throw new IllegalArgumentException("bad rent");
    }
    Property p = find(id);
    if (p == null) {
        return false;
    }
    p.rentMonthly = newRentMonthly.setScale(2, RM);
    return true;
}
/**
 * Updates the current market value for the property with the given id.
 *
 * @param id the property id
 * @param newValue the new current market value
 * @return true if the property was found and updated, false otherwise
 */
public boolean updateCurrentValue(int id, BigDecimal newValue) {
    if (newValue == null || newValue.signum() < 0) {
        throw new IllegalArgumentException("bad value");
    }
    Property p = find(id);
    if (p == null) {
        return false;
    }
    p.currentValue = newValue.setScale(2, RM);
    return true;
}

/**
 * Records income for the property with the given id.
 *
 * @param id the property id
 * @param date the date of the income
 * @param note a note describing the income
 * @param amount the amount of the income
 * @return true if the property was found and updated, false otherwise
 * @throws IllegalArgumentException if amount is null or negative
 */
public boolean recordIncome(int id, String date, String note,
        BigDecimal amount) {
    if (amount == null || amount.signum() < 0) {
        throw new IllegalArgumentException("bad amount");
    }
    Property p = find(id);
    if (p == null) {
        return false;
    }
    p.ytdIncome = p.ytdIncome.add(amount).setScale(2, RM);
    return true;
}

/**
 * Records an expense for the property with the given id.
 *
 * @param id the property id
 * @param date the date of the expense
 * @param note a note describing the expense
 * @param amount the amount of the expense
 * @return true if the property was found and updated, false otherwise
 * @throws IllegalArgumentException if amount is null or negative
 */
public boolean recordExpense(int id, String date, String note,
        BigDecimal amount) {
    if (amount == null || amount.signum() < 0) {
        throw new IllegalArgumentException("bad amount");
    }
    Property p = find(id);
    if (p == null) {
        return false;
    }
    p.ytdExpense = p.ytdExpense.add(amount).setScale(2, RM);
    return true;
}

/**
 * Prints a summary of all properties and portfolio totals to the console.
 */
public void printSummary() {
    System.out.println("Properties: " + this.items.size());
    for (int i = 0; i < this.items.size(); i++) {
        System.out.println(this.items.get(i).toString());
    }
    System.out.println(
            "Portfolio NOI/yr=$" + this.portfolioNOIAnnual().toPlainString());
    System.out.println(
            "Portfolio equity=$" + this.portfolioEquity().toPlainString());
}

private Property find(int id) {
    for (int i = 0; i < this.items.size(); i++) {
        if (this.items.get(i).id == id) {
            return this.items.get(i);
        }
    }
    return null;
}

/**
 * Entry point for the RealEstateTrackerMVP application.
 * Demonstrates adding properties, recording income/expense, updating values,
 * and printing summary.
 *
 * @param args command-line arguments (not used)
 */
public static void main(String[] args) {
    RealEstateTrackerMVP t = new RealEstateTrackerMVP();
    int id1 = t.addProperty(
            "123 Main St", "Anytown", BigDecimal.valueOf(200000),
            BigDecimal.valueOf(15000), BigDecimal.valueOf(220000),
            BigDecimal.valueOf(1500), "Active");
}
}
