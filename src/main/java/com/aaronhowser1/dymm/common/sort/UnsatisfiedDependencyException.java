package com.aaronhowser1.dymm.common.sort;

import javax.annotation.Nonnull;

final class UnsatisfiedDependencyException extends RuntimeException {
    UnsatisfiedDependencyException(@Nonnull final String message) {
        super(message);
    }
}
