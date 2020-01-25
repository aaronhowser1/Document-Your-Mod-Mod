package com.aaronhowser1.dymm.common.sort;

import javax.annotation.Nonnull;

final class CyclingDependencyException extends RuntimeException {
    CyclingDependencyException(@Nonnull final String message) {
        super(message);
    }
}
