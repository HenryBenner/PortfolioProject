package Components;

import components.set.Set;

/**
 * Abstract class for secondary real estate tracker implementations.
 */
public abstract class RealEstateTrackerSecondary implements RealEstateTracker {
    /**
     * Protected constructor for RealEstateTrackerSecondary.
     */
    protected RealEstateTrackerSecondary() {
    }

    @Override
    public final double totalMonthlyRent() {
        double sum = 0.0;
        Set<String> ids = this.keys();
        for (String id : ids) {
            sum += this.value(id).rentMonthlyUSD();
        }
        return sum;
    }

    @Override
    public final double totalMonthlyExpenses() {
        double sum = 0.0;
        Set<String> ids = this.keys();
        for (String id : ids) {
            sum += this.value(id).expensesMonthlyUSD();
        }
        return sum;
    }

    @Override
    public final double monthlyNOI() {
        return this.totalMonthlyRent() - this.totalMonthlyExpenses();
    }

    @Override
    public final double portfolioValue() {
        double sum = 0.0;
        Set<String> ids = this.keys();
        for (String id : ids) {
            sum += this.value(id).priceUSD();
        }
        return sum;
    }

    @Override
    public final String argMaxMonthlyCashflow() {
        String bestId = null;
        double best = (-1 * Double.MAX_VALUE);
        Set<String> ids = this.keys();
        for (String id : ids) {
            RealEstateTrackerKernel.PropertyRecord r = this.value(id);
            double cf = r.rentMonthlyUSD() - r.expensesMonthlyUSD();
            if (cf > best) {
                best = cf;
                bestId = id;
            }
        }
        return bestId;
    }

    @Override
    public final RealEstateTrackerKernel.PropertyRecord highestMonthlyCashflow() {
        String id = this.argMaxMonthlyCashflow();
        return this.value(id);
    }

    @Override
    public final void updateRent(String id, double newRent) {
        RealEstateTrackerKernel.PropertyRecord r = this.value(id);
        r.setRentMonthlyUSD(newRent);
    }

    @Override
    public final void updateExpenses(String id, double newExpenses) {
        RealEstateTrackerKernel.PropertyRecord r = this.value(id);
        r.setExpensesMonthlyUSD(newExpenses);
    }

    @Override
    public final void setPrice(String id, double newPrice) {
        RealEstateTrackerKernel.PropertyRecord r = this.value(id);
        r.setPriceUSD(newPrice);
    }
}
