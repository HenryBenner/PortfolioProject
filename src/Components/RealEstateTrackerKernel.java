package Components;
import components.set.Set;

/**
 * Kernel interface for a real estate portfolio tracker.
 *
 * Math model: this is a finite map from String id to PropertyRecord
 *
 * Initialization ensures: this is the empty map
 */
public interface RealEstateTrackerKernel {

    /**
     * Property record type placed inside this kernel to avoid extra files.
     * Kernel operations only. Clients can use the default monthlyCashflow.
     */
    public static interface PropertyRecord {

        /**
         * Creator of a new object with the same dynamic type as this.
         *
         * @return new instance
         * @ensures result.address = "" and result.priceUSD = 0 and
         *          result.rentMonthlyUSD = 0 and result.expensesMonthlyUSD = 0
         */
        PropertyRecord newInstance();

        /**
         * Resets this to default values.
         *
         * @updates this
         * @ensures this.address = "" and this.priceUSD = 0 and
         *          this.rentMonthlyUSD = 0 and this.expensesMonthlyUSD = 0
         */
        void clear();

        /**
         * Replaces this with source and clears source.
         *
         * @param source
         *            the record to take from
         * @updates this, source
         * @requires source != this
         * @ensures this.address = #source.address and this.priceUSD =
         *          #source.priceUSD and this.rentMonthlyUSD =
         *          #source.rentMonthlyUSD and this.expensesMonthlyUSD =
         *          #source.expensesMonthlyUSD and source is default as in clear
         */
        void transferFrom(PropertyRecord source);

        /**
         * Address getter.
         *
         * @return address string
         * @ensures result = this.address
         */
        String address();

        /**
         * Address setter.
         *
         * @param address
         *            new address
         * @updates this
         * @requires address != null
         * @ensures this.address = address and this.priceUSD = #this.priceUSD
         *          and this.rentMonthlyUSD = #this.rentMonthlyUSD and
         *          this.expensesMonthlyUSD = #this.expensesMonthlyUSD
         */
        void setAddress(String address);

        /**
         * Price getter.
         *
         * @return price in USD
         * @ensures result = this.priceUSD
         */
        double priceUSD();

        /**
         * Price setter.
         *
         * @param price
         *            new price in USD
         * @updates this
         * @requires price >= 0
         * @ensures this.priceUSD = price and this.address = #this.address and
         *          this.rentMonthlyUSD = #this.rentMonthlyUSD and
         *          this.expensesMonthlyUSD = #this.expensesMonthlyUSD
         */
        void setPriceUSD(double price);

        /**
         * Rent getter.
         *
         * @return monthly rent in USD
         * @ensures result = this.rentMonthlyUSD
         */
        double rentMonthlyUSD();

        /**
         * Rent setter.
         *
         * @param rent
         *            new monthly rent in USD
         * @updates this
         * @requires rent >= 0
         * @ensures this.rentMonthlyUSD = rent and this.address = #this.address
         *          and this.priceUSD = #this.priceUSD and
         *          this.expensesMonthlyUSD = #this.expensesMonthlyUSD
         */
        void setRentMonthlyUSD(double rent);

        /**
         * Expenses getter.
         *
         * @return monthly expenses in USD
         * @ensures result = this.expensesMonthlyUSD
         */
        double expensesMonthlyUSD();

        /**
         * Expenses setter.
         *
         * @param expenses
         *            new monthly expenses in USD
         * @updates this
         * @requires expenses >= 0
         * @ensures this.expensesMonthlyUSD = expenses and this.address =
         *          #this.address and this.priceUSD = #this.priceUSD and
         *          this.rentMonthlyUSD = #this.rentMonthlyUSD
         */
        void setExpensesMonthlyUSD(double expenses);
    }

    /**
     * Creator of a new object with the same dynamic type as this.
     *
     * @return new instance
     * @ensures result.size() = 0
     */
    RealEstateTracker newInstance();

    /**
     * Replaces this with source and clears source.
     *
     * @param source
     *            tracker to transfer from
     * @updates this, source
     * @requires source != this
     * @ensures this.keys() = #source.keys() and for all id in #source.keys():
     *          this.value(id) equals #source.value(id) and source.size() = 0
     */
    void transferFrom(RealEstateTracker source);

    /**
     * Adds or replaces a property under id.
     *
     * @param id
     *            nonempty id
     * @param record
     *            record to store
     * @updates this
     * @requires id != null and id.length() > 0 and record != null
     * @ensures this.keys() = #this.keys() union {id} and this.value(id) =
     *          record and for all k in #this.keys() with k != id: this.value(k)
     *          equals #this.value(k)
     */
    void add(String id, PropertyRecord record);

    /**
     * Removes and returns the record at id.
     *
     * @param id
     *            id to remove
     * @updates this
     * @requires this.hasKey(id)
     * @ensures result equals #this.value(id) and this.keys() = #this.keys()
     *          without {id} and for all k in this.keys(): this.value(k) equals
     *          #this.value(k)
     */
    PropertyRecord remove(String id);

    /**
     * Returns the record at id.
     *
     * @param id
     *            existing id
     * @return record stored at id
     * @requires this.hasKey(id)
     * @ensures result equals this.value(id)
     */
    PropertyRecord value(String id);

    /**
     * Number of properties.
     *
     * @return size
     * @ensures result = |this.keys()|
     */
    int size();

    /**
     * Copy of the id set.
     *
     * @return set of ids
     * @ensures result = this.keys()
     */
    Set<String> keys();
}
