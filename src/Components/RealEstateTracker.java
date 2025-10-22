package Components;

/**
 * Secondary interface for a real estate portfolio tracker.
 * Layered methods must use only kernel methods.
 *
 * Abbreviation:
 *  cf(r) = r.rentMonthlyUSD - r.expensesMonthlyUSD
 */
public interface RealEstateTracker extends RealEstateTrackerKernel {

    /**
     * Total monthly rent.
     *
     * @return sum of rent over all ids
     * @ensures result =
     *   Sum over id in this.keys() of this.value(id).rentMonthlyUSD()
     */
    double totalMonthlyRent();

    /**
     * Total monthly expenses.
     *
     * @return sum of expenses over all ids
     * @ensures result =
     *   Sum over id in this.keys() of this.value(id).expensesMonthlyUSD()
     */
    double totalMonthlyExpenses();

    /**
     * Monthly net operating income.
     *
     * @return rent minus expenses
     * @ensures result = totalMonthlyRent(this) - totalMonthlyExpenses(this)
     */
    double monthlyNOI();

    /**
     * Annual net operating income.
     *
     * @return annualized NOI
     * @ensures result = 12 * monthlyNOI(this)
     */
    double annualNOI();

    /**
     * Portfolio market value.
     *
     * @return sum of prices
     * @ensures result =
     *   Sum over id in this.keys() of this.value(id).priceUSD()
     */
    double portfolioValue();

    /**
     * Capitalization rate.
     *
     * @return cap rate as a fraction
     * @requires portfolioValue(this) > 0
     * @ensures result = annualNOI(this) / portfolioValue(this)
     */
    double capRate();

    /**
     * Id with the highest monthly cashflow.
     *
     * @return an id that maximizes cf
     * @requires this.size() > 0
     * @ensures result in this.keys()  and
     *   for all id in this.keys():
     *     cf(this.value(result)) >= cf(this.value(id))
     */
    String argMaxMonthlyCashflow();

    /**
     * Record with the highest monthly cashflow.
     *
     * @return record for arg max
     * @requires this.size() > 0
     * @ensures result equals this.value(argMaxMonthlyCashflow(this))
     */
    RealEstateTrackerKernel.PropertyRecord highestMonthlyCashflow();

    /**
     * Update monthly rent for id.
     *
     * @param id existing id
     * @param newRent nonnegative
     * @updates this
     * @requires this.hasKey(id)  and  newRent >= 0
     * @ensures this.value(id).rentMonthlyUSD() = newRent  and
     *   this.value(id).priceUSD() = #this.value(id).priceUSD()  and
     *   this.value(id).expensesMonthlyUSD() = #this.value(id).expensesMonthlyUSD()  and
     *   for all k in this.keys() with k != id:
     *     this.value(k) equals #this.value(k)
     */
    void updateRent(String id, double newRent);

    /**
     * Update monthly expenses for id.
     *
     * @param id existing id
     * @param newExpenses nonnegative
     * @updates this
     * @requires this.hasKey(id)  and  newExpenses >= 0
     * @ensures this.value(id).expensesMonthlyUSD() = newExpenses  and
     *   this.value(id).priceUSD() = #this.value(id).priceUSD()  and
     *   this.value(id).rentMonthlyUSD() = #this.value(id).rentMonthlyUSD()  and
     *   for all k in this.keys() with k != id:
     *     this.value(k) equals #this.value(k)
     */
    void updateExpenses(String id, double newExpenses);

    /**
     * Update price for id.
     *
     * @param id existing id
     * @param newPrice nonnegative
     * @updates this
     * @requires this.hasKey(id)  and  newPrice >= 0
     * @ensures this.value(id).priceUSD() = newPrice  and
     *   this.value(id).rentMonthlyUSD() = #this.value(id).rentMonthlyUSD()  and
     *   this.value(id).expensesMonthlyUSD() = #this.value(id).expensesMonthlyUSD()  and
     *   for all k in this.keys() with k != id:
     *     this.value(k) equals #this.value(k)
     */
    void setPrice(String id, double newPrice);
}
