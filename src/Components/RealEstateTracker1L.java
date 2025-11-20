package Components;

import components.map.Map;
import components.map.Map1L;
import components.set.Set;

public final class RealEstateTracker1L extends RealEstateTrackerSecondary
        implements RealEstateTracker {

    /**
     * Underlying map from id to property record.
     */
    private Map<String, RealEstateTrackerKernel.PropertyRecord> rep;

    /**
     * No argument constructor.
     */
    public RealEstateTracker1L() {
        this.rep = new Map1L<>();
    }

    // ---------- Kernel methods ----------

    @Override
    public RealEstateTracker newInstance() {
        return new RealEstateTracker1L();
    }

    @Override
    public void transferFrom(RealEstateTracker source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source != this";

        RealEstateTracker1L localSource = (RealEstateTracker1L) source;
        this.rep = localSource.rep;
        localSource.rep = new Map1L<>();
    }

    @Override
    public void add(String id, RealEstateTrackerKernel.PropertyRecord record) {
        assert id != null : "Violation of: id is not null";
        assert id.length() > 0 : "Violation of: id is nonempty";
        assert record != null : "Violation of: record is not null";

        if (this.rep.hasKey(id)) {
            this.rep.replaceValue(id, record);
        } else {
            this.rep.add(id, record);
        }
    }

    @Override
    public RealEstateTrackerKernel.PropertyRecord remove(String id) {
        assert id != null : "Violation of: id is not null";
        assert this.rep.hasKey(id) : "Violation of: this.hasKey(id)";

        Map.Pair<String, RealEstateTrackerKernel.PropertyRecord> p = this.rep
                .remove(id);
        return p.value();
    }

    @Override
    public RealEstateTrackerKernel.PropertyRecord value(String id) {
        assert id != null : "Violation of: id is not null";
        assert this.rep.hasKey(id) : "Violation of: this.hasKey(id)";

        return this.rep.value(id);
    }

    @Override
    public int size() {
        return this.rep.size();
    }

    @Override
    public Set<String> keys() {
        return this.rep.keySet();
    }
}