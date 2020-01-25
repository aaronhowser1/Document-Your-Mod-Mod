package com.aaronhowser1.dymm.module.base;

import com.aaronhowser1.dymm.api.documentation.Condition;

public final class BasicCondition implements Condition {
    private final boolean canParse;

    public BasicCondition(final boolean canParse) {
        this.canParse = canParse;
    }

    @Override
    public boolean canParse() {
        return this.canParse;
    }
}
